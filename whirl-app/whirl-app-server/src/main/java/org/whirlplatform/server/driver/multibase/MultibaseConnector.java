package org.whirlplatform.server.driver.multibase;

import com.google.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.CellRowCol;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DatabaseTableElement;
import org.whirlplatform.meta.shared.form.FormCellModel;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.meta.shared.form.FormRowModel;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.ExceptionData.ExceptionType;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.AbstractConnector;
import org.whirlplatform.server.expimp.CSVExporter;
import org.whirlplatform.server.expimp.XLSExporter;
import org.whirlplatform.server.form.CellElementWrapper;
import org.whirlplatform.server.form.ClientFormWriter;
import org.whirlplatform.server.form.ColumnElementWrapper;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.RowElementWrapper;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.MetadataProvider;
import org.whirlplatform.server.metadata.container.ContainerException;
import org.whirlplatform.server.metadata.container.MetadataContainer;
import org.whirlplatform.server.metadata.store.file.FileSystemMetadataStore;
import org.whirlplatform.server.monitor.mbeans.Applications;
import org.whirlplatform.server.utils.ApplicationReference;

@Singleton
public class MultibaseConnector extends AbstractConnector {

    private static Logger _log = LoggerFactory.getLogger(MultibaseConnector.class);

    private MetadataContainer metadataContainer;

    private MetadataProvider metadataProvider;

    @Inject
    public MultibaseConnector(MetadataContainer metadataContainer,
                              MetadataProvider metadataProvider,
                              ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.metadataContainer = metadataContainer;
        this.metadataProvider = metadataProvider;
    }

    private ConnectionWrapper aliasConnection(DatabaseTableElement table, ApplicationUser user) {
        return aliasConnection(table.getSchema().getDataSource().getAlias(), user);
    }

    @Override
    public ApplicationData getApplication(String applicationCode, Version version) {
        try {
            ApplicationElement application =
                metadataContainer.getApplication(applicationCode, version).get()
                    .getApplication();
            ApplicationData data = new ApplicationData();
            data.setName(application.getName());
            data.setApplicationCode(application.getCode());
            data.setGuest(application.isGuest());
            data.setRootComponentId(application.getRootComponent().getId());
            data.setBlocked(!application.isEnabled());
            data.setHeaderHtml(application.getHtmlHeader());

            for (FileElement file : application.getJavaScriptFiles()) {
                data.addScript(file.getFileName());
            }
            for (FileElement file : application.getCssFiles()) {
                data.addCss(file.getFileName());
            }

            return data;
        } catch (ContainerException e) {
            String message = "Application load problem: " + applicationCode;
            _log.error(message, e);
            throw new CustomException(message);
        }

    }

    @Override
    public ApplicationData getApplication(String applicationCode, Version version,
                                          ApplicationUser user) {
        try {
            if (applicationCode == null) {
                // TODO message about empty application code
                CustomException e = new CustomException(ExceptionType.WRONGAPP,
                        I18NMessage.getSpecifiedMessage("forbiddenApp", user.getLocale()));

                FileSystem fs = FileSystems.getDefault();

                FileSystemMetadataStore store = new FileSystemMetadataStore(new JndiConfiguration(), fs);

                e.setAllowedApps(store.getAllowedApplications());
                throw e;
            }

            ApplicationData data = getApplication(applicationCode, version);
            if (user.isGuest() && !data.isGuest()) {
                return null;
            }

            AtomicReference<ApplicationReference> appRef =
                metadataContainer.getApplication(applicationCode, version);

            checkApplicationAllowed(appRef.get().getApplication(), user);

            user.setApplication(appRef);

            // Добавление событий
            ApplicationElement application = user.getApplication();
            for (EventElement element : application.getFreeEvents()) {
                if (isEventAvailable(element, Collections.emptyList(), user)) {
                    data.addEvent(element.getHandlerType(),
                        EventElement.eventElementToMetadata(element,
                            applicationLocale(application, user.getLocaleElement())));
                }
            }
            return data;
        } catch (ContainerException e) {
            String message = "Application load problem: " + applicationCode;
            _log.error(message, e);
            throw new CustomException(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkApplicationAllowed(ApplicationElement application, ApplicationUser user) {
        List<String> allowedApps = new ArrayList<String>(Applications.getAllowedApps());
        List<String> forbiddenApps = new ArrayList<String>(Applications.getBlockedApps());
        // Проверка доступности приложения на сервере

        if (!forbiddenApps.isEmpty() && !allowedApps.isEmpty()) {
            allowedApps.removeAll(forbiddenApps);
        }

        // Если есть разрешенные, разрешает только их
        if (!allowedApps.isEmpty()) {
            if (!allowedApps.contains(application.getCode())) {
                throw new CustomException(ExceptionType.WRONGAPP,
                    I18NMessage.getSpecifiedMessage("forbiddenApp", user.getLocale()));
            }
            // Если разрешенных нет, запрещаем запрещенные
        } else {
            if (forbiddenApps.contains(application.getCode())) {
                throw new CustomException(ExceptionType.WRONGAPP,
                    I18NMessage.getSpecifiedMessage("forbiddenApp", user.getLocale()));
            }
        }

        if (application.isGuest())  {
            return;
        }

        for (String code : metadataProvider.getUserApplications(user)) {
            if (application.getCode().equals(code)) {
                return;
            }
        }

        if (application.hasGroups()) {
            for (String group : user.getGroups()) {
                if (application.hasGroup(group)) {
                    return;
                }
            }
        }
        throw new CustomException(
            I18NMessage.getMessage(I18NMessage.getRequestLocale()).errorAppAccess());
    }

    @Override
    public ComponentModel getComponents(String componentId, List<DataValue> params,
                                        ApplicationUser user) {
        ComponentModel model = null;
        ApplicationElement app = user.getApplication();

        List<DataValue> extraParams = appendInitialParams(user, params);

        for (ComponentElement c : app.getAvailableComponents()) {
            if (c.getId().equals(componentId)) {
                model = componentElementsToModels(c, extraParams,
                    applicationLocale(app, user.getLocaleElement()), user);
                break;
            }
        }
        encode(model, user);
        return model;
    }

    private LocaleElement applicationLocale(ApplicationElement application, LocaleElement locale) {
        LocaleElement eq = null;
        LocaleElement langEq = null;
        if (application.getDefaultLocale().equals(locale)) {
            return eq;
        }
        for (LocaleElement l : application.getLocales()) {
            if (l.equals(locale)) {
                eq = l;
            }
            if (l.getLanguage().equalsIgnoreCase(locale.getLanguage())) {
                langEq = l;
            }
        }
        return eq != null ? eq : (langEq != null ? langEq : locale);
    }

    @Override
    public ClassMetadata getClassMetadata(String classId, List<DataValue> params,
                                          ApplicationUser user) {
        AbstractTableElement table = findTableElement(classId, user);
        assertTrue(table != null, "Table definition not found: ID = " + classId + "");

        List<DataValue> extraParams = appendInitialParams(user, params);

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            return conn.getDataSourceDriver().createMetadataFetcher(table)
                .getClassMetadata(table, extraParams);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public LoadData<ListModelData> getListClassData(String dataSourceId,
                                                    ClassLoadConfig loadConfig,
                                                    ApplicationUser user) {
        ClassLoadConfig decodedConfig = decodeAndAppendInitParams(loadConfig, user);

        ClassMetadata metadata = getClassMetadata(dataSourceId, new ArrayList<>(decodedConfig.getParameters().values()),
            user);
        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        // TODO надо запрашивать здесь getClassMetadata
        if (!metadata.isViewable()) {
            return new LoadData<>();
        }

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {

            return conn.getDataSourceDriver().createListFetcher(table).getListData(metadata, table,
                decodedConfig);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public LoadData<RowModelData> getTableClassData(ClassMetadata metadata,
                                                    ClassLoadConfig loadConfig,
                                                    ApplicationUser user) {

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        // TODO надо запрашивать здесь getClassMetadata
        if (!metadata.isViewable()) {
            return new LoadData<>();
        }

        ClassLoadConfig decodedConfig = decodeAndAppendInitParams(loadConfig, user);

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            return conn.getDataSourceDriver().createTableFetcher(table)
                .getTableData(metadata, table, decodedConfig);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<TreeModelData> getTreeClassData(String dataSourceId,
                                                ClassLoadConfig loadConfig,
                                                ApplicationUser user) {

        ClassLoadConfig decodedConfig = decodeAndAppendInitParams(loadConfig, user);

        ClassMetadata metadata = getClassMetadata(dataSourceId, new ArrayList<>(decodedConfig.getParameters().values()),
            user);
        if (metadata.getClassId() == null) {
            // чтобы при использовании HorizontalMenu и MenuTreePanel без
            // DataSource, не было ошибки об отсутствии таблицы в справочнике
            return new ArrayList<>();
        }

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        //TODO надо запрашивать здесь getClassMetadata
        if (!metadata.isViewable()) {
            return new ArrayList<>();
        }

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            return conn.getDataSourceDriver()
                    .createTreeFetcher(table)
                    .getTreeData(metadata, table, decodedConfig);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public RowModelData insert(ClassMetadata metadata, DataModifyConfig config,
                               ApplicationUser user) {

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            return conn.getDataSourceDriver().createDataChanger(table)
                .insert(metadata, config, table);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public RowModelData update(ClassMetadata metadata, DataModifyConfig config,
                               ApplicationUser user) {
        RowModelData model = config.getModels().get(0);
        // не редактируемые записи не трогаем.
        if (model == null || !model.isEditable()) {
            return null;
        }

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            return conn.getDataSourceDriver().createDataChanger(table)
                .update(metadata, config, table);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void delete(ClassMetadata metadata, DataModifyConfig config, ApplicationUser user) {
        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            conn.getDataSourceDriver().createDataChanger(table).delete(metadata, config, table);
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    public FormModel getForm(String formId, List<DataValue> params, ApplicationUser user)
        throws CustomException {

        List<DataValue> extraParams = appendInitialParams(user, params);

        try (ClientFormWriter writer = new ClientFormWriter(connectionProvider,
            getFormRepresent(formId, extraParams, user), extraParams, user)) {
            writer.write(null);
            FormModel model = writer.getFormModel();
            // Проходим по всем ячейкам, шифруем whereSql в компонентах
            for (FormRowModel r : model.getRows()) {
                for (FormCellModel c : r.getCells()) {
                    encode(c.getComponent(), user);
                }
            }
            return model;
        } catch (IOException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        } catch (ConnectException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public FormElementWrapper getFormRepresent(String formId, List<DataValue> params,
                                               ApplicationUser user) {
        FormElement element = null;
        for (ComponentElement comp : user.getApplication().getAvailableComponents()) {
            if (formId.equals(comp.getId())) {
                element = (FormElement) comp;
                break;
            }
        }
        if (element == null) {
            return null;
        }

        FormElementWrapper form = new FormElementWrapper(element,
            applicationLocale(user.getApplication(), user.getLocaleElement()));

        for (Entry<CellRowCol, CellElement> entry : element.getCells().entrySet()) {
            int row = entry.getKey().getRow();
            int col = entry.getKey().getCol();
            CellElement cellElement = entry.getValue();

            CellElementWrapper cell = form.getCell(row, col);
            cell.setElement(cellElement);
        }

        List<DataValue> extraParams = appendInitialParams(user, params);

        for (ComponentElement child : element.getChildren()) {
            ComponentModel model = componentElementsToModels(child, extraParams,
                applicationLocale(user.getApplication(), user.getLocaleElement()), user);
            Integer row = (model.getValue(PropertyType.LayoutDataFormRow.getCode()) == null ? 0
                : model.getValue(PropertyType.LayoutDataFormRow.getCode()).getDouble()
                .intValue());
            Integer column =
                (model.getValue(PropertyType.LayoutDataFormColumn.getCode()) == null ? 0
                    :
                    model.getValue(PropertyType.LayoutDataFormColumn.getCode()).getDouble()
                        .intValue());
            CellElementWrapper cell = form.getCell(row, column);
            cell.setComponentElement(child);
            cell.setComponent(model);
        }

        List<RowElement> rowsHeight = element.getRowsHeight();
        for (RowElement m : rowsHeight) {
            int row = m.getRow();
            RowElementWrapper formRow = form.getRow(row);
            formRow.setElement(m);
        }

        List<ColumnElement> colsWidth = element.getColumnsWidth();
        for (ColumnElement m : colsWidth) {
            int col = m.getColumn();
            ColumnElementWrapper formCol = form.getColumn(col);
            formCol.setElement(m);
        }

        return form;
    }

    @Override
    public void exportXLS(ClassMetadata metadata, boolean columnHeader, boolean xlsx,
                          ClassLoadConfig loadConfig,
                          OutputStream output, ApplicationUser user) {
        DBReader reader = null;

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        try (ConnectionWrapper conn = aliasConnection(
            ((DatabaseTableElement) table).getSchema().getDataSource().getAlias(), user)) {
            reader = conn.getDataSourceDriver().createDataFetcher(table)
                .getTableReader(metadata, table,
                    decodeAndAppendInitParams(loadConfig, user));

            XLSExporter exporter = new XLSExporter(metadata, reader);
            exporter.setColumnHeader(columnHeader);
            exporter.setXslx(xlsx);
            exporter.write(output);
            output.flush();
        } catch (SQLException e) {
            _log.error("Error while loading table data: " + metadata.getTitle(), e);
            throw new CustomException("Error while loading table data: " + metadata.getTitle());
        } catch (IOException e) {
            _log.error("Error while exporting table: " + metadata.getTitle() + " to xls", e);
            throw new CustomException(
                "Error while exporting table: " + metadata.getTitle() + " to xls");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public void exportCSV(ClassMetadata metadata, boolean columnHeader, ClassLoadConfig loadConfig,
                          OutputStream output,
                          ApplicationUser user) {

        AbstractTableElement table = findTableElement(metadata.getClassId(), user);
        assertTrue(table != null, "Table definition not found: " + metadata.getTitle());

        DBReader reader = null;
        try (ConnectionWrapper conn = aliasConnection((DatabaseTableElement) table, user)) {
            reader = conn.getDataSourceDriver().createDataFetcher(table)
                .getTableReader(metadata, table,
                    decodeAndAppendInitParams(loadConfig, user));

            CSVExporter exporter = new CSVExporter(metadata, reader);
            exporter.setColumnHeader(columnHeader);
            exporter.write(output);
            output.flush();
        } catch (IOException e) {
            _log.error("Error while exporting table: " + metadata.getTitle() + " to xls", e);
            throw new CustomException(
                "Error while exporting table: " + metadata.getTitle() + " to xls");
        } catch (SQLException e) {
            _log.error("Error while loading table data: " + metadata.getTitle(), e);
            throw new CustomException("Error while loading table data: " + metadata.getTitle());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public List<FieldMetadata> getReportFields(String classId, ApplicationUser user) {
        List<FieldMetadata> fields = null;
        for (ComponentElement comp : user.getApplication().getAvailableComponents()) {
            if (classId.equals(comp.getId())) {
                ReportElement report = (ReportElement) comp;
                fields = report.getFields();
                break;
            }
        }
        return fields;
    }

    @Override
    public Map<PropertyType, PropertyValue> getReportProperties(String codeOrId, boolean isCode,
                                                                ApplicationUser user) {
        for (ComponentElement comp : user.getApplication().getAvailableComponents()) {
            if (isCode
                ? codeOrId.equals(comp.getProperty(PropertyType.Code)
                .getValue(applicationLocale(user.getApplication(), user.getLocaleElement()))
                .getString())
                : codeOrId.equals(comp.getId())) {
                ReportElement report = (ReportElement) comp;
                Map<PropertyType, PropertyValue> props = new HashMap<PropertyType, PropertyValue>(
                    report.getProperties());
                for (ComponentElement inner : report.getChildren()) {
                    if (ComponentType.FormBuilderType.equals(inner.getType())) {
                        props.put(PropertyType.FormId, new PropertyValue(DataType.STRING,
                            user.getApplication().getDefaultLocale(), inner.getId()));
                    }
                }
                return props;
            }
        }
        return null;
    }

    @Override
    public EventMetadata getNextEvent(EventMetadata event, String nextEventCode,
                                      ApplicationUser user) {
        EventMetadata result = null;
        ApplicationElement appElem = user.getApplication();
        EventElement foundEvent = appElem.findNextEventElement(event.getId(), nextEventCode);
        if (foundEvent != null) {
            LocaleElement locale = applicationLocale(appElem, user.getLocaleElement());
            result = EventElement.eventElementToMetadata(foundEvent, locale);
        }
        return (result != null) ? result : getFreeEvent(nextEventCode, user);
    }

    // Получить свободное событие приложения по коду
    public EventMetadata getFreeEvent(final String eventCode, ApplicationUser user) {
        ApplicationElement appElem = user.getApplication();
        EventElement foundEvent = appElem.findFreeEventElementByIdOrCode(eventCode, true);
        if (foundEvent == null || "CreateEvent".equals(foundEvent.getHandlerType())
            || "AttachEvent".equals(foundEvent.getHandlerType())) {
            return null;
        }
        LocaleElement locale = applicationLocale(user.getApplication(), user.getLocaleElement());
        EventMetadata result = EventElement.eventElementToMetadata(foundEvent, locale);
        return result;
    }

    // Рекурсивно достает событие. Если parentId == null, вернет любое событие,
    // с подходящим кодом
    // Если коды событий гарантировано уникальны, можно упростить
    // public static EventElement getEventFromList(Collection<EventElement>
    // events, String codeOrId, String parentId,
    // boolean code) {
    // if (events == null) {
    // return null;
    // }
    // for (EventElement event : events) {
    // // совпадает код или идентификатор
    // if ((code && codeOrId.equals(event.getCode()) ||
    // codeOrId.equals(event.getId()))
    // // и если указан идентификатор родителя то проверяем, что событее
    // подчинено ему
    // && (parentId == null || parentId.equals(event.getParentEventId()))
    // ) {
    // return event;
    // }
    // EventElement subEvent = getEventFromList(event.getSubEvents(), codeOrId,
    // parentId, code);
    // if (subEvent != null) {
    // return subEvent;
    // }
    // }
    // return null;
    // }

    @Override
    public EventResult executeDBFunction(EventMetadata event, List<DataValue> params,
                                         ApplicationUser user) {
        return executeDB(event, params, user,
            (eventConnection, holder) ->
                eventConnection.getDataSourceDriver().createEventExecutor()
                    .executeFunction(holder.getKey(), holder.getValue()));
    }

    @Override
    public EventResult executeSQL(EventMetadata event, List<DataValue> params,
                                  ApplicationUser user) {
        List<DataValue> extraParams = appendInitialParams(user, params);
        return executeDB(event, extraParams, user,
            (eventConnection, holder) ->
                eventConnection.getDataSourceDriver().createEventExecutor()
                    .executeQuery(holder.getKey(), holder.getValue()));
    }

    private EventResult executeDB(EventMetadata event, List<DataValue> params, ApplicationUser user,
                                  BiFunction<ConnectionWrapper, Entry<EventElement, List<DataValue>>,
                                      EventResult> func) {
        EventElement eventElement = user.getApplication().findEventElementById(event.getId());
        if (eventElement != null) {
            List<DataValue> splittedParameters = splitParameters(params);
            String alias =
                eventElement.getDataSource() != null ? eventElement.getDataSource().getAlias()
                    : SrvConstant.DEFAULT_CONNECTION;
            try (ConnectionWrapper eventConnection = aliasConnection(alias, user)) {
                return func.apply(eventConnection,
                    new AbstractMap.SimpleEntry<>(eventElement, splittedParameters));
            } catch (SQLException e) {
                _log.error("Error while loading event data: " + eventElement.getName(), e);
                throw new CustomException(
                    "Error while loading event data: " + eventElement.getName());
            }
        } else {
            throw new CustomException("Event not found.");
        }
    }

    private List<DataValue> splitParameters(final List<DataValue> params) {
        List<DataValue> splittedParameters = new ArrayList<DataValue>();
        for (DataValue v : params) {
            if (v.getType() == DataType.FILE) {
                FileValue f = v.getFileValue();
                if (f.isSaveName()) {
                    splittedParameters.add(new DataValueImpl(DataType.STRING, f.getName()));
                }
            }
            splittedParameters.add(v);
        }
        return splittedParameters;
    }

    @Override
    public EventMetadata getEvent(String eventCodeOrId, boolean byCode, ApplicationUser user) {
        ApplicationElement appElem = user.getApplication();
        EventElement foundEvent = appElem.findFreeEventElementByIdOrCode(eventCodeOrId, byCode);
        EventMetadata result = null;
        if (foundEvent != null) {
            LocaleElement locale = applicationLocale(appElem, user.getLocaleElement());
            result = EventElement.eventElementToMetadata(foundEvent, locale);
        }
        return result;
    }

    @Override
    public FileValue downloadFileFromTable(String tableId, String column, String rowId,
                                           ApplicationUser user) {
        AbstractTableElement table = findTableElement(tableId, user);
        assertTrue(table != null, "DataSource not found");
        try (ConnectionWrapper conn = aliasConnection((DatabaseTableElement) table, user)) {
            return conn.getDataSourceDriver().createFileFetcher(table)
                .downloadFileFromTable(table, column, rowId);
        } catch (SQLException e) {
            _log.error("Error while loading file", e);
            throw new CustomException("Error while loading file");
        }
    }

    // private String getNextId(ConnectionWrapper connection) throws
    // RPCException {
    // MetadataDatabase db = openMetadataDatabase();
    // Object result = db.getNextSequenceValue("SOBJ", connection);
    // return String.valueOf(result);
    // }
    //
    // public String getNextId() throws RPCException {
    // try (ConnectionWrapper conn = metadataConnection()) {
    // String result = getNextId(conn);
    // if (result == null) {
    // java.util.Random rnd = new java.util.Random();
    // result = String.valueOf(rnd.nextLong());
    // }
    // return String.valueOf(result);
    // } catch (NotImplementedException e) {
    // _log.warn("Sequence support not implemented", e);
    // java.util.Random rnd = new java.util.Random();
    // return String.valueOf(rnd.nextLong());
    // } catch (SQLException e) {
    // _log.error("Sequence generation error", e);
    // throw new RPCException("Sequence generation error");
    // }
    // }

    // Возможно Connection и DBDatabase получать параметрами?
    // private String getListLabel(String classId, String recordId,
    // ApplicationUser user) throws SQLException, RPCException {
    // String result = null;
    //
    // if (recordId == null) {
    // return null;
    // }
    // AbstractTableElement table = findTableElement(classId,
    // user.getApplication());
    // if (table == null) {
    // _log.error("Table definition not found: id = " + classId);
    // // throw new RPCException("Table definition not found:");
    // return null;
    // }
    //
    // ConnectionWrapper conn = null;
    // try {
    // if (table instanceof DatabaseTableElement) {
    // conn = aliasConnection(((DatabaseTableElement) table)
    // .getSchema().getDataSource().getAlias(), user);
    // }
    // return FetcherFactory.getDataFetcher(table, this, conn)
    // .getListLabel(table, recordId, user);
    // } finally {
    // DBConnection.closeResources(conn);
    // }
    // }

    private ClassLoadConfig decode(ClassLoadConfig config, ApplicationUser user) {
        if (config.getWhereSql() != null) {
            config.setWhereSql(user.getEncryptor().decrypt(config.getWhereSql()));
        }
        if (config.getLabelExpression() != null) {
            config.setLabelExpression(user.getEncryptor().decrypt(config.getLabelExpression()));
        }
        return config;
    }

    private ClassLoadConfig decodeAndAppendInitParams(ClassLoadConfig loadConfig, ApplicationUser user) {
        ClassLoadConfig decodedConfig = decode(loadConfig, user);
        decodedConfig.setParameters(appendInitialParams(user, new ArrayList<>(decodedConfig.getParameters().values()))
            .stream().collect(Collectors.toMap(DataValue::getCode, Function.identity())));
        return decodedConfig;
    }

    private void throwException(final String message) throws CustomException {
        _log.error(message);
        throw new CustomException(message);
    }

    private void assertTrue(boolean condition, final String message) throws CustomException {
        if (!condition) {
            throwException(message);
        }
    }

}
