
package org.whirlplatform.rpc.server;

import com.google.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.apache.commons.fileupload.FileItem;
import org.fusesource.restygwt.client.DirectRestService;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TableConfig;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.EventParameter;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ParameterType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.DataService;
import org.whirlplatform.rpc.shared.ListHolder;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.compiler.JavaExecutor;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.driver.multibase.Encryptor;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.JavaFunctionMessage;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.login.LoginData;
import org.whirlplatform.server.login.LoginException;
import org.whirlplatform.server.monitor.RunningEvent;
import org.whirlplatform.server.servlet.FileServlet.FileUpload;
import org.whirlplatform.server.session.SessionManager;

/**
 *
 */
@Singleton
@Path("/")
public class DataServiceImpl implements DataService, DirectRestService {

    private static final Logger _log = LoggerFactory.getLogger(DataServiceImpl.class);
    private static final String SESSION_FILE_MAP = "SESSION_FILE_MAP";
    private static final String NON_SERIALAZABLE_PARAMS = "NON_SERIALAZABLE_PARAMS";
    private static final String DEFINED_NEXT_EVENT = "DEFINED_NEXT_EVENT";
    private Connector _connector;
    private ConnectionProvider connectionProvider;
    private AccountAuthenticator _authenticator;
    /**
     * По идее jersey оборачивает объект в прокси, и в каждом потоке будет свой HttpServletRequest
     */
    @Context
    private HttpServletRequest request;

    @Context
    private ServletContext servletContext;

    @Inject
    public DataServiceImpl(Connector connector, ConnectionProvider connectionProvider,
                           AccountAuthenticator authenticator) {
        super();
        this._connector = connector;
        this.connectionProvider = connectionProvider;
        this._authenticator = authenticator;
    }

    private Connector connector() {
        return _connector;
    }

    private AccountAuthenticator authenticator() {
        return _authenticator;
    }

    private ApplicationUser getApplicationUser(SessionToken token) {
        ApplicationUser user = SessionManager.get(request.getSession()).getUser(token);
        if (user.getEncryptor() == null) {
            user.setEncryptor(Encryptor.get(servletContext));
        }
        return SessionManager.get(request.getSession()).getUser(token);
    }

    private Locale getLocaleByString(String string) {
        if (string != null) {
            int last = string.length();
            boolean hasCountry = string.contains("_");
            if (hasCountry) {
                last = string.indexOf("_");
            }
            String lang = string.substring(0, last);
            if (hasCountry) {
                String country = string.substring(last, string.length() - 1);
                return new Locale(lang, country);
            } else {
                return new Locale(lang);
            }
        }

        return request.getLocale();
    }

    public ClientUser getUser(SessionToken token, boolean newSession, String timeZoneKey) {
        token.setSessionId(request.getSession().getId());

        ApplicationUser user = getApplicationUser(token);

        user.setIp(request.getRemoteAddr());
        if (newSession) {
            user = user.copy();
            token.setTokenId(String.valueOf(Math.abs((new Random()).nextInt())));
            SessionManager.get(request.getSession()).registerToken(token);
        }

        if (timeZoneKey != null) {
            user.setTimeZone(TimeZone.getTimeZone(timeZoneKey));
        } else {
            user.setTimeZone(TimeZone.getDefault());
        }
        _log.info(user.toString());

        SessionManager.get(request.getSession()).registerUser(token, user);
        SessionManager.get(request.getSession()).touch(token);

        ClientUser clientUser = user.toClientUser();
        clientUser.setSessionToken(token);
        return clientUser;
    }

    @Override
    public ClientUser login(SessionToken token, String login, String password) {
        try {
            token.setSessionId(request.getSession().getId());

            LoginData loginData = new LoginData(login, password);
            loginData.setIp(request.getRemoteAddr());
            ApplicationUser user = authenticator().login(loginData);

            user.setLocale(getLocaleByString(null));

            SessionManager manager = SessionManager.get(request.getSession());
            manager.unregisterToken(token);
            manager.unregisterUser(token);
            manager.registerToken(token);
            manager.registerUser(token, user);

            // SessionManager.get(getServletContext()).touch(token);
            SessionManager.get(request.getSession()).touch(token);

            user.setIp(request.getRemoteAddr());

            ClientUser clientUser = user.toClientUser();
            clientUser.setSessionToken(token);
            return clientUser;
        } catch (LoginException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        } catch (CustomException e) {
            _log.error(e);
            throw e;
        }

    }

    /**
     * Выход из приложения
     */
    public Boolean logout(SessionToken token) {
        token.setSessionId(request.getSession().getId());
        try {
            SessionManager.get(
                    // getThreadLocalRequest().getSession().getServletContext())
                    request.getSession()).unregisterUser(token);
            return true;
        } catch (Exception e) {
            _log.error(e);
            throw new CustomException(
                    I18NMessage.getMessage(getLocaleByString(null)).error() + ": logout()");
        }
    }

    @Override
    public ApplicationData getApplication(SessionToken token, String applicationCode,
                                          Version version, String locale) {
        // ApplicationUser user =
        // SessionManager.get(getServletContext()).getUser(token);
        ApplicationUser user = SessionManager.get(request.getSession()).getUser(token);

        // SessionManager.get(getServletContext()).touch(token);
        SessionManager.get(request.getSession()).touch(token);
        user.setLocale(getLocaleByString(locale));

        ApplicationData app = connector().getApplication(applicationCode, version, user);
        return app;
    }

    @Override
    public ComponentModel getComponents(SessionToken token, String componentId) {
        ApplicationUser user = getApplicationUser(token);
        return connector().getComponents(componentId, Collections.emptyList(), user);
    }

    @Override
    public TableConfig getTableConfig(SessionToken token, String classId, String whereSql,
                                      Map<String, DataValue> params) {
        ApplicationUser user = getApplicationUser(token);
        TableConfig config = new TableConfig();

        ClassMetadata metadata = connector().getClassMetadata(classId, params, user);
        config.setMetadata(metadata);
        return config;
    }

    @Override
    public LoadData<ListModelData> getListClassData(SessionToken token, ClassMetadata metadata,
                                                    ClassLoadConfig loadConfig) {
        ApplicationUser user = getApplicationUser(token);
        return connector().getListClassData(metadata, loadConfig, user);
    }

    @Override
    public LoadData<RowModelData> getTableClassData(SessionToken token, ClassMetadata metadata,
                                                    ClassLoadConfig loadConfig) {
        ApplicationUser user = getApplicationUser(token);
        return connector().getTableClassData(metadata, loadConfig, user);
    }

    @Override
    public List<RowModelData> getTreeClassData(SessionToken token, ClassMetadata metadata,
                                               TreeClassLoadConfig loadConfig) {
        ApplicationUser user = getApplicationUser(token);
        return connector().getTreeClassData(metadata, loadConfig, user);
    }

    @Override
    public RowModelData insert(SessionToken token, ClassMetadata metadata,
                               DataModifyConfig config) {
        Set<FileItem> files;

        files = setFiles(metadata, config.getModels().get(0));
        RowModelData result = connector().insert(metadata, config, getApplicationUser(token));

        for (FileItem file : files) {
            file.delete();
        }
        return result;
    }

    @Override
    public RowModelData update(SessionToken token, ClassMetadata metadata,
                               DataModifyConfig config) {
        Set<FileItem> files;

        files = setFiles(metadata, config.getModels().get(0));
        RowModelData result = connector().update(metadata, config, getApplicationUser(token));

        for (FileItem file : files) {
            file.delete();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Set<FileItem> setFiles(ClassMetadata metadata, RowModelData model) {
        HttpSession session = request.getSession();

        Map<String, FileUpload> map =
                (Map<String, FileUpload>) session.getAttribute("SESSION_FILE_MAP");
        Set<FileItem> files = new HashSet<FileItem>();

        if (map == null) {
            return files;
        }

        for (FieldMetadata f : metadata.getFields()) {
            if (f.getType() == DataType.FILE) {
                FileValue value = model.get(f.getName());
                FileUpload upload = map.get(value.getTempId());
                if (upload != null) {
                    try {
                        files.add(upload.getFile());
                        value.setInputStream(upload.getFile().getInputStream());
                        value.setSize(upload.getFile().getSize());
                    } catch (IOException e) {
                        throw new CustomException(e.getMessage());
                    }
                }
                map.remove(value.getTempId());
            }
        }
        return files;
    }

    @Override
    public void delete(SessionToken token, ClassMetadata metadata, DataModifyConfig config) {
        connector().delete(metadata, config, getApplicationUser(token));
    }

    @Override
    public FormModel getForm(SessionToken token, String formId, ListHolder<DataValue> parameters) {
        FormModel form =
                connector().getForm(formId, parameters.getList(), getApplicationUser(token));
        return form;
    }

    @Override
    public EventMetadata getNextEvent(SessionToken token, EventMetadata event,
                                      String nextEventCode) {
        return connector().getNextEvent(event, nextEventCode, getApplicationUser(token));
    }

    private List<DataValue> extractValues(EventResult result) {
        List<DataValue> list = new ArrayList<DataValue>();
        for (EventParameter p : result.getParametersMap().values()) {
            list.add(p.getData());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EventResult executeServer(SessionToken token, EventMetadata event,
                                     ListHolder<DataValue> parameters) {
        HttpSession session = request.getSession();
        Set<FileItem> files = new HashSet<FileItem>();
        Map<String, FileUpload> map = (Map<String, FileUpload>) session.getAttribute(SESSION_FILE_MAP);
        List<DataValue> paramList = parameters.getList();

        if (event.getId().equals(session.getAttribute(DEFINED_NEXT_EVENT))) {
            Map<String, DataValue> nonSerializableParams = (Map<String, DataValue>) session
                    .getAttribute(NON_SERIALAZABLE_PARAMS);
            for (DataValue paramFromList : paramList) {
                FileValue fileValue;
                if (paramFromList.getType() == DataType.FILE
                        && (fileValue = paramFromList.getFileValue()) != null) {
                    DataValue paramFromSession = nonSerializableParams.get(fileValue.getTempId());
                    if (paramFromSession != null) {
                        paramList.set(paramList.indexOf(paramFromList), paramFromSession);
                    }
                }
            }
        }
        session.removeAttribute(DEFINED_NEXT_EVENT);
        session.removeAttribute(NON_SERIALAZABLE_PARAMS);
        if (map != null) {
            for (DataValue v : paramList) {
                if (v != null && v.getType() == DataType.FILE) {
                    FileValue value = v.getFileValue();
                    FileUpload upload = map.get(value.getTempId());
                    if (upload != null) {
                        try {
                            files.add(upload.getFile());
                            value.setInputStream(upload.getFile().getInputStream());
                            value.setSaveName(upload.isSaveName());
                        } catch (IOException e) {
                            throw new CustomException(e.getMessage());
                        }
                    }
                    map.remove(value.getTempId());
                }

            }
        }
        EventResult result;
        if (event.getType() == EventType.DatabaseFunction) {
            result = connector().executeDBFunction(event, paramList, getApplicationUser(token));
        } else if (event.getType() == EventType.Java) {
            result = executeJava(event, paramList, getApplicationUser(token));
        } else if (event.getType() == EventType.DatabaseSQL) {
            result = connector().executeSQL(event, paramList, getApplicationUser(token));
        } else {
            throw new UnsupportedOperationException("Event type: " + event.getType());
        }
        for (FileItem file : files) {
            file.delete();
        }

        // Дочернее событие добавляется в EventHelper.onResult
        // выполняем подсобытие если оно серверное и не содержит параметров
        // которые надо забирать с клиента
        // TODO на данный момент если событие содержит выходные параметры как
        // файл, то файлы не возвращаются обратно на сервер
        if (result.getNextEventCode() != null) {
            EventMetadata nextEvent = connector().getNextEvent(event, result.getNextEventCode(),
                    getApplicationUser(token));
            result.setNextEvent(nextEvent);
            if (nextEvent != null) {
                // проверяем, что есть праметры которые надо достать с клиента
                boolean hasClientParameter = false;
                ArrayList<EventParameter> allParams =
                        new ArrayList<>(result.getParametersMap().values());
                allParams.addAll(nextEvent.getParametersList());

                for (EventParameter p : allParams) {
                    ParameterType t = p.getType();
                    hasClientParameter =
                            t == ParameterType.COMPONENT || t == ParameterType.COMPONENTCODE
                                    || t == ParameterType.STORAGE;
                    if (hasClientParameter) {
                        break;
                    }
                }
                boolean isNextServerEvent = nextEvent.getType() == EventType.DatabaseFunction
                        || nextEvent.getType() == EventType.Java;
                // если событие серверное и нет параметров с клиента, то можем
                // выполнить сразу
                if (nextEvent != null && isNextServerEvent && !hasClientParameter) {
                    result = executeServer(token, nextEvent,
                            new ListHolder<DataValue>(extractValues(result)));
                    // если след.событие серверное, но есть параметры с клиента.
                } else if (nextEvent != null && isNextServerEvent && hasClientParameter) {
                    Map<String, DataValue> nonSerializableParams = new HashMap<>();
                    for (EventParameter eventParameter : result.getParametersMap().values()) {
                        DataValue dataValue = eventParameter.getData();
                        if (dataValue != null && dataValue.getType() == DataType.FILE) {
                            FileValue fileValue = dataValue.getFileValue();
                            if (fileValue != null) {
                                nonSerializableParams.put(fileValue.getTempId(), dataValue);
                            }
                        }
                    }
                    // если есть несериализуемые параметры, сохранить их в
                    // сессии.
                    if (!nonSerializableParams.isEmpty()) {
                        session.setAttribute(DEFINED_NEXT_EVENT, nextEvent.getId());
                        session.setAttribute(NON_SERIALAZABLE_PARAMS, nonSerializableParams);
                    }

                }
            }
        }
        return result;
    }

    private EventResult executeJava(EventMetadata metadata, List<DataValue> parameters,
                                    ApplicationUser user) {
        // TODO хрень какая-то. нужно продумать переиспользуемые методы для
        // событий.
        // EventMetadata meta = connector()
        // .getEvent(metadata.getId(), false, user);
        // metadata.setSource(meta.getSource());
        JavaFunctionMessage msg = new JavaFunctionMessage(user, metadata, parameters);
        RunningEvent ev = new RunningEvent(RunningEvent.Type.JAVAEVENT, metadata.getCode(), "",
                user.getLogin()) {
            @Override
            public void onStop() {
                // Не понятно как остановить выполнение
            }
        };

        try (Profile p = new ProfileImpl(msg, ev)) {
            JavaExecutor exec =
                    new JavaExecutor(connector(), connectionProvider, user, metadata.getSource(),
                            servletContext);
            EventResult result = exec.execute(parameters);
            if (result != null) {
                if ("ERROR".equals(result.getMessageType())) {
                    msg.setError(result.getMessage());
                }
            }
            return result;
        }
    }

    // TODO: Доделать загрузку из сессии
    @Override
    public List<FieldMetadata> getReportFields(SessionToken token, String classId) {
        return connector().getReportFields(classId, getApplicationUser(token));
    }

    @Override
    public void saveReportValues(SessionToken token, String componentId,
                                 Map<String, DataValue> values) {
        request.getSession().setAttribute("report_id_" + componentId, values);
    }

    public EventMetadata getEvent(SessionToken token, String eventCode) {
        ApplicationUser user = getApplicationUser(token);
        return connector().getEvent(eventCode, true, user);
    }

    @Override
    public Boolean touch(SessionToken token) {
        // SessionManager sessionManager =
        // SessionManager.get(getServletContext());
        SessionManager sessionManager = SessionManager.get(request.getSession());
        sessionManager.touch(token);

        // нужно достать что-то из сессии иначе она убивается
        request.getSession().getLastAccessedTime();

        ApplicationUser user = sessionManager.getUser(token);
        return true;
    }

    @Override
    public void removeToken(SessionToken token) {
        SessionManager.get(
                // getThreadLocalRequest().getSession().getServletContext())
                request.getSession()).markForUnregister(token);
    }

    public Boolean checkCaptchaCode(SessionToken token, String captchaCode,
                                    String componentId) { // ajax-проверка
        String key = AppConstant.CAPTCHA_SESSION_KEY + componentId;
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(key);
        session.removeAttribute(key);
        return (code != null && code.equals(captchaCode));
    }

    @Override
    public EventMetadata getFreeEvent(SessionToken token, String eventCode) {
        return connector().getFreeEvent(eventCode, getApplicationUser(token));
    }

    @Override
    public String saveLoadConfig(SessionToken token, ClassLoadConfig loadConfig) {
        HttpSession session = request.getSession();
        String result = "loadConfig_" + new Random().nextInt();
        session.setAttribute(result, loadConfig);
        return result;
    }

}
