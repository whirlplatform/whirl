package org.whirlplatform.editor.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.whirlplatform.editor.client.meta.*;
import org.whirlplatform.editor.server.i18n.EditorI18NMessage;
import org.whirlplatform.editor.server.packager.Packager;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.SaveData;
import org.whirlplatform.editor.shared.SaveResult;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.editor.shared.util.EditorHelper;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ParameterType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.FileElement.InputStreamProvider;
import org.whirlplatform.meta.shared.editor.db.*;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.login.LoginData;
import org.whirlplatform.server.login.LoginException;
import org.whirlplatform.server.servlet.ExportServlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

@Singleton
@SuppressWarnings("serial")
public class EditorDataServiceImpl extends RemoteServiceServlet implements EditorDataService {

    private Logger _log = LoggerFactory.getLogger(ExportServlet.class);

    private EditorConnector _connector;
    private AccountAuthenticator _authenticator;

    @Inject
    public EditorDataServiceImpl(EditorConnector connector, AccountAuthenticator authenticator) {
        super();
        this._connector = connector;
        this._authenticator = authenticator;
    }

    private AccountAuthenticator authenticator() {
        return _authenticator;
    }

    private EditorConnector connector() {
        return _connector;
    }

    @Override
    public ApplicationElement newApplication(ApplicationBasicInfo appInfo) throws RPCException {
        ApplicationElement newApplication = connector().newApplication(appInfo, getApplicationUser());
        syncServerApplication(newApplication, appInfo.getVersion());
        return newApplication;
    }

    @Override
    public Collection<ApplicationStoreData> loadApplicationList() throws RPCException {
        if (getApplicationUser() == null) {
            throw new RPCException(getMessage().session_expired(), true);
        }
        return connector().getApplicationList(getApplicationUser());
    }

    @Override
    public ApplicationElement loadApplication(ApplicationStoreData applicationData) throws RPCException {
        ApplicationElement result = connector().loadApplication(applicationData, getApplicationUser());
        syncServerApplication(result, applicationData.getVersion());
        return result;
    }

    @Override
    public SaveResult saveApplication(SaveData saveData) throws RPCException {
        if (getUser() == null) {
            throw new RPCException(getMessage().session_expired_on_save(), true);
        }
        ApplicationElement application = saveData.getApplication();
        Version version = saveData.getVersion();
        syncServerApplication(application, version);
        connector().saveApplication(application, saveData.getVersion(), getApplicationUser());
        removeApplicationFilesFromSession(application);
//		syncServerApplication(application, version);
        return new SaveResult(application, version, saveData.getState());
    }

    private void removeApplicationFilesFromSession(ApplicationElement application) {
        // Будут ли удаляться файлы, если не сохранили приложение?
        Map<String, FileItem> files = getSessionApplicationFiles();
        if (files != null) {
            for (FileElement element : application.getJavaScriptFiles()) {
                files.remove(element.getId());
            }
            for (FileElement element : application.getCssFiles()) {
                files.remove(element.getId());
            }
            for (FileElement element : application.getImageFiles()) {
                files.remove(element.getId());
            }
            if (application.getStaticFile() != null) {
                files.remove(application.getStaticFile().getId());
            }
            for (FileElement element : application.getJavaFiles()) {
                files.remove(element.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, FileItem> getSessionApplicationFiles() {
        return (Map<String, FileItem>) getThreadLocalRequest().getSession()
                .getAttribute("whirl-editor-filemap");
    }

    @Override
    public SaveResult saveApplicationAs(SaveData saveData, Version oldVersion) throws RPCException {
        if (getUser() == null) {
            throw new RPCException(getMessage().session_expired_on_save(), true);
        }
        ApplicationElement application = saveData.getApplication();
        Version version = saveData.getVersion();
        application.setVersion(version.toString());
        syncServerApplication(application, version);
        ApplicationUser appUser = getApplicationUser();
        connector().saveApplication(application, saveData.getVersion(), getApplicationUser());
        connector().saveApplicationAs(application, oldVersion, saveData.getVersion(), appUser);
        removeApplicationFilesFromSession(application);
//		syncServerApplication(application, version);
        return new SaveResult(application, version, saveData.getState());
    }

    private void syncSessionApplicationFiles(ApplicationElement application) throws IOException {
        for (FileElement file : application.getJavaScriptFiles()) {
            syncApplicationFiles("javascript", application, file);
        }
        for (FileElement file : application.getCssFiles()) {
            syncApplicationFiles("css", application, file);
        }
        for (FileElement file : application.getJavaFiles()) {
            syncApplicationFiles("java", application, file);
        }
        for (FileElement file : application.getImageFiles()) {
            syncApplicationFiles("image", application, file);
        }
        if (application.getStaticFile() != null) {
            syncApplicationFiles("static", application, application.getStaticFile());
        }
    }

    private long checksum(InputStream stream) {
        CheckedInputStream cis = null;
        long checksum = 0;
        try {
            cis = new CheckedInputStream(stream, new Adler32());
            byte[] tempBuf = new byte[128];
            while (cis.read(tempBuf) >= 0) {
            }
            checksum = cis.getChecksum().getValue();
        } catch (IOException e) {
            checksum = 0;
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException ioe) {
                }
            }
        }
        return checksum;
    }

    private void syncApplicationFiles(String path, ApplicationElement application, FileElement element)
            throws IOException {
        // получаем файл из сессии
        Map<String, FileItem> files = getSessionApplicationFiles();
        if (files != null && files.containsKey(element.getId())) {
            final FileItem item = files.get(element.getId());
            if (item.getSize() != 0) {
                // files.remove(element.getId());
                element.setInputStreamProvider(new InputStreamProvider() {
                    @Override
                    public Object get() throws IOException {
                        return item.getInputStream();
                    }

                    @Override
                    public String path() {
                        return null;
                    }
                });
                element.setChecksum(checksum(item.getInputStream()));
            }
        }
    }

    private void syncFiles(ApplicationElement destApplication, ApplicationElement srcApplication) {
        syncFiles(destApplication.getJavaScriptFiles(), srcApplication.getJavaScriptFiles());
        syncFiles(destApplication.getCssFiles(), srcApplication.getCssFiles());
        syncFiles(destApplication.getJavaFiles(), srcApplication.getJavaFiles());
        syncFiles(destApplication.getImageFiles(), srcApplication.getImageFiles());
    }

    private void syncFiles(Collection<FileElement> destFiles, Collection<FileElement> srcFiles) {
        for (FileElement destFile : destFiles) {
            for (FileElement srcFile : srcFiles) {
                if (destFile.getId().equals(srcFile.getId()) && destFile.getChecksum() == srcFile.getChecksum()) {
                    // TODO destFile.setInputStreamProvider(
                    // srcFile.getInputStreamProvider());
                }
            }
        }
    }

    @Override
    public SaveResult syncServerApplication(ApplicationElement application, Version version) throws RPCException {
        try {
            ApplicationElement oldApplication = (ApplicationElement) getThreadLocalRequest().getSession()
                    .getAttribute("APPLICATION");
            if (oldApplication != null) {
                syncFiles(application, oldApplication);
            }
            syncSessionApplicationFiles(application);
            getThreadLocalRequest().getSession().setAttribute("APPLICATION", application);
            getThreadLocalRequest().getSession().setAttribute("VERSION", version);
            return new SaveResult(application, version, null);
        } catch (IOException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public SaveResult loadServerApplication() {
        ApplicationElement application = (ApplicationElement) getThreadLocalRequest().getSession()
                .getAttribute("APPLICATION");
        Version version = (Version) getThreadLocalRequest().getSession().getAttribute("VERSION");
        return new SaveResult(application, version, null);
    }

    // TODO эту штуку доставать из шаблонов, продумать как
    @Override
    public AbstractElement newElement(AbstractElement parent, AbstractElement element) {
        LocaleElement defaultLocale = loadServerApplication().getApplication().getDefaultLocale();
        if (element instanceof NewComponentElement && parent instanceof ReportElement) {
            return EditorHelper.newComponentElement(ComponentType.FormBuilderType, defaultLocale);
        } else if (element instanceof NewComponentElement) {
            // новый корневой компонент
            return EditorHelper.newComponentElement(((NewComponentElement) element).getType(), defaultLocale);
        } else if (parent instanceof ComponentElement && element instanceof NewEventElement) {
            // добавление нового события компоненту
            ComponentElement component = (ComponentElement) parent;
            EventElement event = new EventElement();
            event.setId(RandomUUID.uuid());
            event.setType(EventType.DatabaseFunction);
            // Новое событие
            event.setName(getMessage().new_element_event() + component.getEvents().size());
            event.setHandlerType("AttachHandler");
            return event;
        } else if (parent instanceof ApplicationElement && element instanceof NewEventElement) {
            // добавление нового события компоненту
            ApplicationElement app = (ApplicationElement) parent;
            EventElement event = new EventElement();
            event.setId(RandomUUID.uuid());
            event.setType(EventType.DatabaseFunction);
            event.setName(getMessage().new_element_event() + app.getFreeEvents().size());
            event.setHandlerType("AttachHandler");
            return event;
        } else if (parent instanceof EventElement
                && element instanceof NewEventElement) {
            EventElement parentEvent = (EventElement) parent;
            EventElement event = new EventElement();
            event.setType(EventType.DatabaseFunction);
            event.setId(RandomUUID.uuid());
            event.setName(getMessage().new_element_event() + parentEvent.getSubEvents().size());
            event.setHandlerType("AttachHandler");
            return event;
        } else if (parent instanceof EventElement && element instanceof NewEventParameterElement) {
            // добавление нового параметра событию
            NewEventParameterElement newParam = (NewEventParameterElement) element;
            EventElement event = (EventElement) parent;
            EventParameterElement parameter = new EventParameterElement(ParameterType.COMPONENT);
            parameter.setId(RandomUUID.uuid());
            parameter.setName(getMessage().new_element_parameter() + event.getParametersCount());
            if (newParam.getComponentId() != null) {
                parameter.setIndex(newParam.getIndex());
                parameter.setComponentId(newParam.getComponentId());
            }
            return parameter;
        } else if (parent instanceof ApplicationElement
                && element instanceof NewDataSourceElement) {
            ApplicationElement application = (ApplicationElement) parent;
            DataSourceElement datasource = new DataSourceElement("metadata", "DATABASE");
            datasource.setId(RandomUUID.uuid());
            datasource.setName(getMessage().new_element_datasource() + application.getDataSources().size());
            return datasource;
        } else if (parent instanceof DataSourceElement
                && element instanceof NewSchemaElement) {
            // созадем новую таблицу
            DataSourceElement datasource = (DataSourceElement) parent;
            SchemaElement schema = new SchemaElement(datasource);
            schema.setId(RandomUUID.uuid());
            schema.setSchemaName("UNI_NEW_" + datasource.getSchemas().size());
            // "Новая схема"
            schema.setName(getMessage().new_element_schema() + schema.getSchemaName());
            return schema;
        } else if (parent instanceof SchemaElement && element instanceof NewTableElement) {
            SchemaElement schema = (SchemaElement) parent;
            PlainTableElement table = new PlainTableElement(schema);
            table.setId(RandomUUID.uuid());
            table.setTableName("TABLE_NEW_" + schema.getTables().size());
            // "Новая таблица"
            table.setName(getMessage().new_element_table() + table.getTableName());
            TableColumnElement idColumn = new TableColumnElement();
            idColumn.setId(RandomUUID.uuid());
            idColumn.setIndex(0);
            idColumn.setType(DataType.NUMBER);
            // "Первичный ключ"
            idColumn.setName(getMessage().primary_key());
            // "Первичный ключ"
            idColumn.setTitle(new PropertyValue(DataType.STRING, defaultLocale, getMessage().primary_key()));
            idColumn.setColumnName("DFOBJ");
            idColumn.setHeight(15);
            idColumn.setNotNull(false);
            idColumn.setHidden(true);
            table.setIdColumn(idColumn);
            table.addColumn(idColumn);

            TableColumnElement deleteColumn = new TableColumnElement();
            deleteColumn.setId(RandomUUID.uuid());
            deleteColumn.setIndex(1);
            deleteColumn.setType(DataType.BOOLEAN);
            deleteColumn.setName(getMessage().deleted());
            deleteColumn.setTitle(new PropertyValue(DataType.STRING, defaultLocale, getMessage().deleted()));
            deleteColumn.setColumnName("DFDELETE");
            deleteColumn.setHeight(15);
            deleteColumn.setHidden(true);
            table.setDeleteColumn(deleteColumn);
            table.addColumn(deleteColumn);

            // представления
            ViewElement view = new ViewElement();
            view.setId(RandomUUID.uuid());
            view.setName(getMessage().new_element_view_v());
            view.setViewName("V_" + table.getTableName());
            table.setView(view);

            return table;
        } else if (parent instanceof SchemaElement && element instanceof NewDynamicTableElement) {
            SchemaElement schema = (SchemaElement) parent;

            DynamicTableElement table = new DynamicTableElement(schema);
            table.setId(RandomUUID.uuid());
            table.setName(getMessage().new_element_dynamic_table() + "DYNAMIC_TABLE_NEW_" + schema.getTables().size());
            table.setMetadataFunction("get_metadata");
            table.setDataFunction("get_data(:data_config, :data_count)");
            table.setInsertFunction("insert(:insert_config)");
            table.setUpdateFunction("update(:update_config)");
            table.setDeleteFunction("delete(:delete_config)");
            return table;
        } else if (parent instanceof AbstractTableElement && element instanceof NewTableColumnElement) {
            TableColumnElement column = new TableColumnElement();
            column.setId(RandomUUID.uuid());
            column.setTitle(new PropertyValue(DataType.STRING, defaultLocale, getMessage().new_element_column()));
            column.setColumnName("DFCOLUMN_NEW");
            column.setType(DataType.STRING);
            column.setSize(255);
            column.setWidth(30);
            return column;
        } else if (element instanceof NewGroupElement) {
            GroupElement group = new GroupElement();
            group.setId(RandomUUID.uuid());
            group.setName(getMessage().new_element_group());
            group.setGroupName("new_group");
            return group;
        } else if (parent instanceof PlainTableElement && element instanceof NewTableElement) {
            PlainTableElement pTable = (PlainTableElement) parent;
            PlainTableElement table = new PlainTableElement(pTable.getSchema());
            table.setId(RandomUUID.uuid());
            table.setTableName(pTable.getTableName());
            // Клон таблицы
            StringBuffer sb = new StringBuffer(pTable.getName());
            sb.append(" - ");
            sb.append(getMessage().new_element_table_clone());
            sb.append(pTable.getClones().size() + 1);
            table.setName(sb.toString());
            TableColumnElement idColumn = new TableColumnElement();
            idColumn.setId(RandomUUID.uuid());
            idColumn.setType(DataType.NUMBER);
            idColumn.setName(getMessage().primary_key());
            idColumn.setTitle(new PropertyValue(DataType.STRING, defaultLocale, getMessage().primary_key()));
            idColumn.setColumnName("DFOBJ");
            idColumn.setHeight(15);
            idColumn.setNotNull(false);
            idColumn.setHidden(true);
            table.setIdColumn(idColumn);
            table.addColumn(idColumn);

            TableColumnElement deleteColumn = new TableColumnElement();
            deleteColumn.setId(RandomUUID.uuid());
            deleteColumn.setType(DataType.BOOLEAN);
            deleteColumn.setName(getMessage().deleted());
            deleteColumn.setTitle(new PropertyValue(DataType.STRING, defaultLocale, getMessage().deleted()));
            deleteColumn.setColumnName("DFDELETE");
            deleteColumn.setHeight(15);
            deleteColumn.setHidden(true);
            table.setDeleteColumn(deleteColumn);
            table.addColumn(deleteColumn);

            // представления
            // ViewElement view = new ViewElement();
            // view.setId(RandomUUID.uuid());
            // view.setName("Представление L");
            // view.setViewName(pTable.getView());
            // table.setView(view);
            if (pTable.getView() != null) {
                ViewElement view = pTable.getView().clone();
                view.setId(RandomUUID.uuid());
                table.setView(view);
            }

            return table;
        } else if (parent instanceof ComponentElement && element instanceof NewContextMenuItemElement) {
            ComponentElement component = (ComponentElement) parent;
            ContextMenuItemElement item = new ContextMenuItemElement();
            item.setId(RandomUUID.uuid());
            item.setName(getMessage().new_element_context_menu() + component.getContextMenuItems().size());
            return item;
        } else if (parent instanceof ContextMenuItemElement && element instanceof NewEventElement) {
            ContextMenuItemElement item = (ContextMenuItemElement) parent;
            EventElement event = new EventElement();
            event.setId(RandomUUID.uuid());
            event.setType(EventType.DatabaseFunction);
            event.setName(getMessage().new_element_event() + item.getEvents().size());
            event.setHandlerType("AttachHandler");
            return event;
        } else if (parent instanceof ContextMenuItemElement && element instanceof NewContextMenuItemElement) {
            ContextMenuItemElement item = (ContextMenuItemElement) parent;
            ContextMenuItemElement child = new ContextMenuItemElement();
            child.setId(RandomUUID.uuid());
            child.setName(getMessage().new_element_context_menu() + item.getChildren().size());
            return child;
        }
        return null;
    }

    public Collection<RowModelData> getTableImportList(DataSourceElement datasource, SchemaElement schema)
            throws RPCException {
        return connector().getTableImportList(datasource, schema, getApplicationUser());
    }

    public Collection<PlainTableElement> importTables(DataSourceElement datasource, SchemaElement schema,
                                                      Collection<RowModelData> models) throws RPCException {
        return connector().importTables(datasource, schema, models, getApplicationUser(),
                loadServerApplication().getApplication());
    }

    public ClientUser login(String login, String password) throws RPCException {
        ApplicationUser user = login(login, password, getThreadLocalRequest().getRemoteAddr());
        getThreadLocalRequest().getSession().setAttribute("user", user);
        return user.toClientUser();
    }

    private ApplicationUser login(String login, String password, String ip) throws RPCException {
        LoginData loginData = new LoginData(login, password);
        loginData.setIp(ip);
        try {
            ApplicationUser user = authenticator().login(loginData);
            boolean allowed = connector().isEditorAllowed("whirl-editor-access-group", user.getId());
            if (!allowed) {
                throw new RPCException(getMessage().alert_editor_not_allowed());
            }
            return user;
        } catch (LoginException e) {
            throw new RPCException(e.getMessage());
        }
    }

    private ApplicationUser getApplicationUser() {
        return (ApplicationUser) getThreadLocalRequest().getSession().getAttribute("user");
    }

    public ClientUser getUser() {
        ApplicationUser user = getApplicationUser();
        if (user == null) {
            return null;
        } else {
            return user.toClientUser();
        }
    }

    @Override
    public void createPackage(ApplicationStoreData data) {
        getThreadLocalRequest().getSession().setAttribute(Packager.SESSION_KEY, data);
    }

    @Override
    public ApplicationsDiff diff(ApplicationStoreData left, ApplicationStoreData right) throws RPCException {
        return connector().diff(left, right);
    }

    @Override
    public ApplicationsDiff diff(ApplicationElement left, ApplicationElement right) throws RPCException {
        return connector().diff(left, right);
    }

    @Override
    public ApplicationElement merge(ApplicationsDiff diff) throws RPCException {
        return connector().merge(diff);
    }

    private EditorMessage getMessage() {
        return EditorI18NMessage.getMessage(EditorI18NMessage.getRequestLocale());
    }

    @Override
    public String saveTemplate(BaseTemplate template) throws RPCException {
        return connector().saveTemplate(template);
    }

    @Override
    public List<BaseTemplate> loadEventTemplates() throws RPCException {
        return connector().loadEventTemplates();
    }

    @Override
    public List<BaseTemplate> loadComponentTemplates() throws RPCException {
        return connector().loadComponentTemplates();
    }

    @Override
    public void deleteTemplate(BaseTemplate template) throws RPCException {
        connector().deleteTemplate(template);
    }

    @Override
    public List<String> getIcons() throws RPCException {
        String path = "META-INF/resources/webjars/famfamfam-silk";  //"resources.webjars.famfamfam-silk.1.3.icons";
        Reflections reflections = new Reflections(path, new ResourcesScanner() );
        Set<String > result = reflections.getResources(Pattern.compile(".*\\.png"));
//        return result;
        List<String> res = new ArrayList<>(Arrays.asList("META-INF/resources/webjars/famfamfam-silk/1.3/icons/font_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/lorry_link.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/map_go.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/layout_link.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/user_female.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/text_heading_1.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/page_white_tux.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/sound.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/brick_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/table_relationship.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/report_picture.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/link_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/building_edit.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/dvd.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/application_form_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/shape_move_back.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/control_equalizer.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/drink_empty.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/database_add.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/sport_shuttlecock.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/cart_put.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/world_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/cd_add.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/cd_delete.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/dvd_edit.png",
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/tag_blue.png" ,
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/award_star_delete.png" ,
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/text_italic.png" ,
                "META-INF/resources/webjars/famfamfam-silk/1.3/icons/page_go.png"));
        return res;


    }
}
