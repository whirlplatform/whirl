package org.whirlplatform.editor.server;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.empire.commons.ObjectUtils;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;
import org.apache.empire.db.DBView;
import org.apache.empire.db.DBView.DBViewColumn;
import org.apache.empire.exceptions.InvalidArgumentException;
import org.whirlplatform.editor.server.db.retrive.DBDatabaseRetriver;
import org.whirlplatform.editor.server.db.retrive.DBRetriverConfig;
import org.whirlplatform.editor.server.i18n.EditorI18NMessage;
import org.whirlplatform.editor.server.templates.TemplateStore;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.merge.ChangeType;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.editor.shared.merge.Differ;
import org.whirlplatform.editor.shared.merge.MergeException;
import org.whirlplatform.editor.shared.merge.Merger;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.editor.shared.util.EditorHelper;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.version.VersionUtil;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

public class MultibaseEditorConnector implements EditorConnector {
    private static Logger _log = LoggerFactory.getLogger(MultibaseEditorConnector.class);

    private MetadataStore metadataStore;
    private EditorDatabaseConnector editorDbConnector;
    private Differ differ;
    private Merger merger;
    private TemplateStore templateStore;

    @Inject
    public MultibaseEditorConnector(EditorDatabaseConnector editorDbConnector,
                                    MetadataStore metadataStore,
                                    Differ differ, Merger merger, TemplateStore templateStore) {
        this.editorDbConnector = editorDbConnector;
        this.metadataStore = metadataStore;
        this.differ = differ;
        this.merger = merger;
        this.templateStore = templateStore;
    }

    @Override
    public ApplicationElement newApplication(ApplicationBasicInfo appInfo, ApplicationUser user)
        throws RPCException {
        if (appInfo.getCode() == null) {
            throw new RPCException("Code cannot be null");
        }
        try {
            for (ApplicationStoreData d : metadataStore.all()) {
                if (appInfo.getCode().equals(d.getCode())) {
                    if (VersionUtil.stringEquals(d.getVersion(), appInfo.getVersion())) {
                        StringBuffer sb = new StringBuffer("The application");
                        sb.append(" code: '").append(appInfo.getCode()).append("'");
                        sb.append(" version: '").append(appInfo.getVersion().toString())
                            .append("'");
                        sb.append(" already exists!");
                        throw new RPCException(sb.toString());
                    }
                }
            }
        } catch (MetadataStoreException e) {
            _log.warn(e.getMessage(), e);
            throw new RPCException(e.getMessage());
        }
        LocaleElement appLocale = appInfo.getLocale();
        ApplicationElement application = new ApplicationElement();
        application.setId(RandomUUID.uuid());
        application.setName(appInfo.getName());
        application.setCode(appInfo.getCode());
        application.setTitle(new PropertyValue(DataType.STRING, appLocale, appInfo.getTitle()));
        application.setEnabled(true);
        application.setRootComponent(
            EditorHelper.newComponentElement(ComponentType.BorderContainerType, appLocale));
        application.setDefaultLocale(appLocale);
        application.setVersion(appInfo.getVersion().toString());
        return application;
    }

    public Collection<ApplicationStoreData> getApplicationList(ApplicationUser user)
        throws RPCException {
        try {
            return metadataStore.all();
        } catch (MetadataStoreException e) {
            throw new RPCException(e.getMessage());
        }

    }

    public ApplicationElement loadApplication(ApplicationStoreData applicationData,
                                              ApplicationUser user)
        throws RPCException {
        try {
            return metadataStore.loadApplication(applicationData.getCode(),
                applicationData.getVersion());
        } catch (MetadataStoreException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public String getNextId() {
        return RandomUUID.uuid();
    }

    @Override
    public Collection<RowModelData> getTableImportList(DataSourceElement datasource,
                                                       SchemaElement schema,
                                                       ApplicationUser user) throws RPCException {
        HashSet<RowModelData> result = new HashSet<RowModelData>();

        // берем талицы бд которые еще не в словаре
        for (RowModelData m : getTableImportListDatabase(datasource, schema, user)) {
            boolean exclude = false;
            String tableName = m.get("tableName");
            for (RowModelData r : result) {
                String tableNameWhirl = r.get("tableName");
                String viewNameWhirl = r.get("viewName");
                String listNameWhirl = r.get("listName");
                if (tableName.equalsIgnoreCase(tableNameWhirl)
                    || tableName.equalsIgnoreCase(viewNameWhirl)
                    || tableName.equalsIgnoreCase(listNameWhirl)) {
                    exclude = true;
                    break;
                }
            }
            if (!exclude) {
                result.add(m);
            }
        }

        return result;
    }

    private Collection<RowModelData> getTableImportListDatabase(DataSourceElement datasource,
                                                                final SchemaElement schema,
                                                                ApplicationUser user)
        throws RPCException {
        try (ConnectionWrapper conn = editorDbConnector.aliasConnection(datasource.getAlias(),
            user)) {
            HashSet<RowModelData> result = new HashSet<RowModelData>();

            DBRetriverConfig config = new DBRetriverConfig();
            config.setDbSchema(schema.getSchemaName());
            DBDatabaseRetriver retriver = new DBDatabaseRetriver(config, conn);
            DBDatabase db = retriver.loadDbModel();
            db.open(conn.getDatabaseDriver(), conn);

            final String defaultTableTitle = getMessage().table_db_table();
            for (DBTable t : db.getTables()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(getNextId());
                model.set("source", "database");
                model.set("schema", db.getSchema());
                model.set("title", defaultTableTitle);
                model.set("tableName", t.getName());
                model.set("objectType", "table");
                result.add(model);
            }
            for (DBView v : db.getViews()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(getNextId());
                model.set("source", "database");
                model.set("schema", db.getSchema());
                model.set("title", defaultTableTitle);
                model.set("tableName", v.getName());
                model.set("objectType", "view");
                result.add(model);
            }

            db.close(conn);
            return result;
        } catch (SQLException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public Collection<PlainTableElement> importTables(DataSourceElement datasource,
                                                      SchemaElement schema,
                                                      Collection<RowModelData> models,
                                                      ApplicationUser user,
                                                      ApplicationElement application)
        throws RPCException {
        Set<PlainTableElement> result = new HashSet<PlainTableElement>();
        for (RowModelData m : models) {
            importTableDatabase(result, datasource, schema, m, user, application);
        }
        return result;
    }

    private void importTableDatabase(Collection<PlainTableElement> result,
                                     DataSourceElement datasource,
                                     final SchemaElement schema, final RowModelData model,
                                     ApplicationUser user, ApplicationElement application)
        throws RPCException {

        try (ConnectionWrapper conn = editorDbConnector.aliasConnection(datasource.getAlias(),
            user)) {

            String tableName = model.get("tableName");

            DBRetriverConfig config = new DBRetriverConfig();
            config.setDbSchema(schema.getSchemaName());
            config.setDbTablePattern(tableName);
            DBDatabaseRetriver retriver = new DBDatabaseRetriver(config, conn);
            DBDatabase db = retriver.loadDbModel();
            db.open(conn.getDatabaseDriver(), conn);

            DBTable tableInfo = db.getTable(tableName);
            if (tableInfo != null) {
                PlainTableElement table = new PlainTableElement();
                table.setId(getNextId());
                table.setName(tableInfo.getName());
                PropertyValue title =
                    new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                        tableInfo.getComment());
                table.setTitle(title);
                table.setTableName(tableInfo.getName());

                for (DBColumn c : tableInfo.getColumns()) {
                    DBTableColumn columnInfo = (DBTableColumn) c;
                    TableColumnElement column = parseColumn(tableInfo, columnInfo, application);
                    table.addColumn(column);
                    if (tableInfo.getKeyColumns() != null
                        && columnInfo.getName().equalsIgnoreCase(tableInfo.getKeyColumns()[0].getName())) {
                        table.setIdColumn(column);
                    }
                }
                result.add(table);
            }

            DBView viewInfo = db.getView(tableName);
            if (viewInfo != null) {
                PlainTableElement table = new PlainTableElement();
                table.setId(getNextId());
                table.setName(viewInfo.getName());
                table.setTitle(
                    new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                        viewInfo.getComment()));
                table.setTableName(viewInfo.getName());

                if (viewInfo.getKeyColumns() != null) {
                    DBViewColumn columnInfo = (DBViewColumn) viewInfo.getKeyColumns()[0];
                    TableColumnElement column = parseColumn(viewInfo, columnInfo, application);
                    table.setIdColumn(column);
                }

                for (DBColumn c : viewInfo.getColumns()) {
                    DBViewColumn columnInfo = (DBViewColumn) c;
                    TableColumnElement column = parseColumn(viewInfo, columnInfo, application);
                    table.addColumn(column);
                }
                result.add(table);
            }
            db.close(conn);
        } catch (SQLException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    private TableColumnElement parseColumn(DBTable tableInfo, DBTableColumn columnInfo,
                                           ApplicationElement application) {
        TableColumnElement column = new TableColumnElement();
        column.setId(getNextId());
        column.setName(columnInfo.getName());
        if (columnInfo.getComment() != null) {
            column.setTitle(
                new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                    columnInfo.getComment()));
        } else {
            column.setTitle(new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                columnInfo.getName()));
        }
        column.setColumnName(columnInfo.getName());
        column.setIndex(tableInfo.getColumnIndex(columnInfo));
        column.setType(parseColumnType(columnInfo.getDataType()));
        if (columnInfo.getDataType() != org.apache.empire.data.DataType.AUTOINC) {
            try {
                if (!ObjectUtils.isEmpty(columnInfo.getDefaultValue())) {
                    column.setDefaultValue(tableInfo.getDatabase().getDriver()
                        .getValueString(columnInfo.getDefaultValue(),
                            columnInfo.getDataType()));
                }
            } catch (InvalidArgumentException e) {
                _log.warn(e);
            }
        }
        column.setNotNull(columnInfo.isRequired());
        if (column.getType() != DataType.NUMBER) {
            column.setSize((int) columnInfo.getSize());
        }
        return column;
    }

    private TableColumnElement parseColumn(DBView viewInfo, DBViewColumn columnInfo,
                                           ApplicationElement application) {
        TableColumnElement column = new TableColumnElement();
        column.setId(getNextId());
        column.setName(columnInfo.getName());
        if (columnInfo.getComment() != null) {
            column.setTitle(
                new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                    columnInfo.getComment()));
        } else {
            column.setTitle(new PropertyValue(DataType.STRING, application.getDefaultLocale(),
                columnInfo.getName()));
        }
        column.setColumnName(columnInfo.getName());
        column.setIndex(viewInfo.getColumnIndex(columnInfo));
        column.setType(parseColumnType(columnInfo.getDataType()));
        column.setNotNull(columnInfo.isRequired());
        if (column.getType() != DataType.NUMBER) {
            column.setSize((int) columnInfo.getSize());
        }
        return column;
    }

    private DataType parseColumnType(org.apache.empire.data.DataType type) {
        switch (type) {
            case AUTOINC:
            case DECIMAL:
            case FLOAT:
            case INTEGER:
                return DataType.NUMBER;
            case CHAR:
            case TEXT:
            case CLOB:
                return DataType.STRING;
            case DATE:
            case DATETIME:
                return DataType.DATE;
            case BOOL:
                return DataType.BOOLEAN;
            case BLOB:
                return DataType.FILE;
            case UNIQUEID:
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        return DataType.STRING;
    }

    public boolean isEditorAllowed(String appCode, String userId) throws RPCException {
        boolean result;
        try (ConnectionWrapper conn = editorDbConnector.metadataConnection()) {
            MetadataDatabase db = editorDbConnector.openMetadataDatabase();

            DBCommand command = db.createCommand();
            command.select(db.WHIRL_USER_GROUPS.GROUP_CODE);
            command.where(db.WHIRL_USER_GROUPS.R_WHIRL_USERS.is(userId)
                .and(db.WHIRL_USER_GROUPS.GROUP_CODE.is(appCode))
                .and(db.WHIRL_USER_GROUPS.DELETED.cmp(DBCmpType.NULL, null)));

            DBReader reader = new DBReader();
            reader.open(command, conn);

            result = reader.moveNext();
            reader.close();
            return result;
        } catch (SQLException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    public org.apache.empire.data.DataType convertColumnType(DataType type) {
        switch (type) {
            case BOOLEAN:
                return org.apache.empire.data.DataType.BOOL;
            case DATE:
                return org.apache.empire.data.DataType.DATETIME;
            case FILE:
                return org.apache.empire.data.DataType.BLOB;
            case LIST:
                return org.apache.empire.data.DataType.DECIMAL;
            case NUMBER:
                return org.apache.empire.data.DataType.DECIMAL;
            case STRING:
                return org.apache.empire.data.DataType.TEXT;
            default:
                return org.apache.empire.data.DataType.UNKNOWN;
        }
    }

    private void clearApplicationsCache() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            mBeanServer.queryNames(new ObjectName("Whirl:type=*,bean=Main"), null).forEach(
                objectName -> {
                    try {
                        _log.info("Clear metadata store cache: " + objectName);
                        mBeanServer.invoke(objectName, "clearMetadataStoreCache", null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
        } catch (MalformedObjectNameException e) {
            _log.warn("Clear metadata store cache error", e);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void saveApplication(ApplicationElement application, Version version,
                                ApplicationUser user)
        throws RPCException {
        try {
            ApplicationIdHelper.initIdsOf(application);
            metadataStore.saveApplication(application, version, user);
            clearApplicationsCache();
        } catch (MetadataStoreException e) {
            String message = "Application save error: "
                + (application != null ? application.getCode() : "null");
            _log.error(message, e);
            throw new RPCException(message);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void saveApplicationAs(ApplicationElement application, Version oldVersion,
                                  Version newVersion,
                                  ApplicationUser user) throws RPCException {
        try {
            ApplicationIdHelper.initIdsOf(application);
            metadataStore.saveApplicationAs(application, oldVersion, newVersion, user);
        } catch (MetadataStoreException e) {
            String message = "Application save error: "
                + (application != null ? application.getCode() : "null");
            _log.error(message, e);
            throw new RPCException(message);
        }
    }

    @Override
    public ApplicationsDiff diff(ApplicationStoreData left, ApplicationStoreData right)
        throws RPCException {
        try {
            ApplicationElement appLeft =
                metadataStore.loadApplication(left.getCode(), left.getVersion());
            ApplicationElement appRight =
                metadataStore.loadApplication(right.getCode(), right.getVersion());
            return differ.diff(appLeft, appRight);
        } catch (MetadataStoreException e) {
            _log.error(e.getMessage(), e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public ApplicationsDiff diff(ApplicationElement left, ApplicationElement right) {
        return differ.diff(left, right);
    }

    @Override
    public ApplicationElement merge(ApplicationsDiff diff) throws RPCException {
        ApplicationElement appLeft = diff.getLeft();
        try {
            // ApplicationStoreData left = diff.getLeftStoreData();
            // ApplicationElement appLeft =
            // metadataStore.loadApplication(left.getCode(), left.getVersion());
            merger.merge(appLeft, diff.getChanges());
            metadataStore.copyFileElements(diff.getRightStoreData(), diff.getLeftStoreData(),
                extractFileElementsToCopy(diff));
        } catch (MergeException e) {
            _log.error(e.getMessage(), e);
            throw new RPCException(e.getMessage());
        } catch (MetadataStoreException e) {
            _log.error(e.getMessage(), e);
        }
        return appLeft;
    }

    private List<FileElement> extractFileElementsToCopy(ApplicationsDiff diff) {
        List<FileElement> result = new ArrayList<>();
        for (ChangeUnit unit : diff.getChanges()) {
            if ((unit.getType() == ChangeType.Add)
                && (unit.getRightValue() instanceof FileElement)) {
                result.add((FileElement) unit.getRightValue());
            }
        }
        return result;
    }

    /**
     * Лаконичнее сообщения доставать
     */
    private EditorMessage getMessage() {
        return EditorI18NMessage.getMessage(EditorI18NMessage.getRequestLocale());
    }

    @Override
    public String saveTemplate(BaseTemplate template) throws RPCException {
        return templateStore.saveTemplate(template);
    }

    @Override
    public List<BaseTemplate> loadEventTemplates() throws RPCException {
        return templateStore.loadEventTemplates();
    }

    @Override
    public List<BaseTemplate> loadComponentTemplates() throws RPCException {
        return templateStore.loadComponentTemplates();
    }

    @Override
    public void deleteTemplate(BaseTemplate template) throws RPCException {
        templateStore.deleteTemplate(template);
    }
}
