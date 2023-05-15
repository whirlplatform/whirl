package org.whirlplatform.app.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.seanchenxi.gwt.storage.client.StorageExt;
import com.seanchenxi.gwt.storage.client.StorageKey;
import com.seanchenxi.gwt.storage.client.StorageQuotaExceededException;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import java.util.Map.Entry;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.base.LoginPanelBuilder;
import org.whirlplatform.component.client.event.CreateEvent;
import org.whirlplatform.component.client.event.EventHelper;
import org.whirlplatform.component.client.event.EventManager;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.event.client.EventManagerImpl;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.ClientRestException;
import org.whirlplatform.rpc.shared.ExceptionData;
import org.whirlplatform.rpc.shared.SessionToken;

/**
 * Стартовый модуль универсального клиента
 */
public class AppEntryPoint implements EntryPoint {

    private static final ScheduledCommand loginSuccessCommand = new ScheduledCommand() {

        @Override
        public void execute() {
            // TODO
        }

    };
    /**
     * Родительский контейнер
     */
    private static ViewportContainer rootContainer;
    /**
     * Команда испольующаяся для старта постороения компонентов приложения.
     */
    private static final ScheduledCommand buildApplicationCommand = new ScheduledCommand() {

        @Override
        public void execute() {
            // убираем логин на странице
            AppEntryPoint.getRootContainer().clearContainer();
            AppEntryPoint.startBuildApp();
        }

    };

    static {
        GWT.create(Whirl.class);
    }

    public static Containable getRootContainer() {
        return rootContainer;
    }

    private static String getApplicationCode() {
        return Location.getParameter(AppConstant.APPLICATION_URL);
    }

    private static void hideLoading() {
        Element loading = DOM.getElementById("loading");
        loading.getStyle().setVisibility(Visibility.HIDDEN);
    }

    /**
     * Получить приложение. <br> 1. Получить пользователя из сесcии.<br> 2. Выбрать приложение
     * (текущее или гостевое в зависимости от значения guestURL).<br> 3. Получить корневой компонент
     * приложения, начать загрузку
     */
    public static void startBuildApp() {
        SessionToken token = checkToken();
        if (token == null) {
            return;
        }

        GWT.<DefaultStyle>create(DefaultStyle.class).getCss().ensureInjected();

        final boolean newSession = LocationManager.get().isNewSession();
        // получить пользователя из сессии
        DataServiceAsync.Util.getDataService(new AsyncCallback<ClientUser>() {

            @Override
            public void onSuccess(ClientUser user) {
                ClientUser.setCurrentUser(user);

                SessionToken token = user.getSessionToken();
                try {
                    saveSessionTokenId(token.getTokenId());
                    if (checkToken() != null) {
                        SessionToken.set(token);

                        getApplicationData();
                    }
                } catch (SerializationException e) {
                    InfoHelper.throwInfo("get-user", e);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                hideLoading();
                ClientLoginUtils.logout();
            }
        }).getUser(token, newSession, null);
    }

    /**
     * Загружает данные о приложении и запускает построение приложения.
     */
    private static void getApplicationData() {
        // checkEvent(ClientUser.getCurrentUser());

        DataServiceAsync.Util.getDataService(new AsyncCallback<ApplicationData>() {

            @Override
            public void onSuccess(ApplicationData application) {
                hideLoading();

                if (!checkApplication(application)) {
                    return;
                }

                // сохранить данные о приложении на клиенте
                AppBuilder.setCurrentApp(application);
                // начать построение приложения
                rootContainer.clearContainer();
                if (application.getStartMessage() != null) {
                    InfoHelper.warning("start-message", AppMessage.Util.MESSAGE.alert(),
                        application.getStartMessage());
                }
                if (!application.isBlocked()) {
                    buildApplication(application);
                } else {
                    ClientLoginUtils.logout();
                    InfoHelper.error("application-block", AppMessage.Util.MESSAGE.alert(),
                        application.getStartMessage());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                hideLoading();

                if (caught instanceof ClientRestException
                    && ((ClientRestException) caught).getData() != null
                    && ((ClientRestException) caught).getData().getType()
                    == ExceptionData.ExceptionType.WRONGAPP) {
                    wrongApplicationMessage(((ClientRestException) caught).getData());
                } else {
                    InfoHelper.throwInfo("get-application", caught);
                }
            }
        }).getApplication(SessionToken.get(), getApplicationCode(), getApplicationVersion(),
            LocaleInfo.getCurrentLocale().getLocaleName());
    }

    /**
     * Проверяет токен пользователя.
     *
     * @return {@link SessionToken}
     * @throws SerializationException
     */
    private static SessionToken checkToken() {
        try {
            String tokenId = loadSessionTokenId();
            if (Util.isEmptyString(tokenId)) {
                tokenId = String.valueOf(Math.abs(Random.nextInt()));
                saveSessionTokenId(tokenId);
            }
            if (!tokenId.equals(LocationManager.get().getToken())) {
                LocationManager.get().setToken(tokenId);
                LocationManager.get().reload();
                return null;
            }

            SessionToken token = SessionToken.get();
            token.setTokenId(tokenId);
            return token;
        } catch (SerializationException e) {
            InfoHelper.throwInfo("token-serialization", e);
            return null;
        }
    }

    private static String loadSessionTokenId() throws SerializationException {
        StorageExt storage = StorageExt.getSessionStorage();
        StorageKey<String> key = new StorageKey<>(AppConstant.TOKEN, String.class);
        return storage.get(key);
    }

    private static void saveSessionTokenId(String tokenId)
        throws StorageQuotaExceededException, SerializationException {
        StorageExt storage = StorageExt.getSessionStorage();
        StorageKey<String> key = new StorageKey<>(AppConstant.TOKEN, String.class);
        storage.put(key, tokenId);
    }

    /**
     * Проверяет возможность загрузки приложения
     *
     * @param application {@link ApplicationData}
     * @return <code>true</code> - продолжать загрузку приложения,
     * <code>false</code> - не продолжать
     */
    private static boolean checkApplication(ApplicationData application) {
        if (application == null && ClientUser.getCurrentUser().isGuest()) {
            // если нет приложения и пользователь гостевой, то просим
            // авторизоваться
            ClientLoginUtils.logout();
            return false;
        } else if (application == null) {
            // Если приложение не загружено, вывести сообщение
            InfoHelper.error("application-load", AppMessage.Util.MESSAGE.alert(),
                AppMessage.Util.MESSAGE.errorAppData());
            return false;
        }

        return checkRole(application);
    }

    /**
     * Проверяет соотвествие кода полученного приложения коду в адресной строке браузера. При не
     * совпадении перезагружает страницу с подставлением в адресную страку полученного кода.
     *
     * @param application {@link ApplicationData}
     * @return <code>true</code> - продолжать загрузку приложения,
     * <code>false</code> - не продолжать
     */
    private static boolean checkRole(ApplicationData application) {
        String role = getApplicationCode();
        if (!application.getApplicationCode().equals(role)) {
            LocationManager.get().setRole(application.getApplicationCode());
            LocationManager.get().reload();
            return false;
        }
        return true;
    }

    private static Version getApplicationVersion() {
        String branch = Location.getParameter(AppConstant.BRANCH_URL);
        String version = Location.getParameter(AppConstant.VERSION_URL);
        return Version.parseBranchVersionOrNull(branch, version);
    }

    /**
     * Построить приложение
     *
     * @param data {@link ApplicationData}
     */
    private static void buildApplication(final ApplicationData data) {
        Window.setTitle(data.getName());

        initPreferences(data);

        fireCreateEvents(data);
        // TODO executeURLEvent();

        AsyncCallback<ComponentModel> callback = new AsyncCallback<ComponentModel>() {

            public void onSuccess(ComponentModel componentTree) {
                AppBuilder.buildApplication(componentTree);
            }

            public void onFailure(Throwable ex) {
                InfoHelper.throwInfo("get-components", ex);
            }
        };

        DataServiceAsync.Util.getDataService(callback)
            .getComponents(SessionToken.get(), data.getRootComponentId());
    }

    private static void fireCreateEvents(ApplicationData data) {
        for (EventMetadata e : data.getCreateEvents()) {
            EventHelper h = EventManager.Util.get().wrapEvent(e);
            h.onCreate(new CreateEvent());
        }
    }

    // /** Проверка гостевого URL на случай навигации */
    // private static void checkEvent(ClientUser user) {
    // Map<String, String> parameters = FragmentManager.get().getFragments();
    // if (parameters.containsKey("event")) {
    // user.setEvent(parameters.get("event"));
    //
    // LinkedHashMap<String, NativeParameter> eventParams = new
    // LinkedHashMap<String, NativeParameter>();
    // for (int i = 1; parameters.containsKey("p" + i); i++) {
    // eventParams.put("p" + i, new StringParameter(parameters.get("p" + i)));
    // }
    // user.setEventParams(eventParams);
    // }
    // }

    private static void initPreferences(ApplicationData data) {
        ApplicationPreferences.clear();
        ApplicationPreferences.putAll(data.getPreferences());
    }

    private static void wrongApplicationMessage(final ExceptionData e) {
        final com.sencha.gxt.widget.core.client.Window w =
            new com.sencha.gxt.widget.core.client.Window();
        w.setHeading(AppMessage.Util.MESSAGE.alert());
        VerticalLayoutContainer panel = new VerticalLayoutContainer();
        panel.getElement().setAttribute("style", "background-color: white;");
        HTML message = new HTML(AppMessage.Util.MESSAGE.errorWrongApplication());
        message.getElement().getStyle().setFontSize(14, Unit.PX);
        message.getElement().setAttribute("style", "font: 14px 'Times New Roman', Times, serif");
        panel.add(message);

        if (!e.getAllowedApps().isEmpty()) {
            HTML messageList = new HTML(AppMessage.Util.MESSAGE.errorWrongApplicationList());
            messageList.getElement().getStyle().setFontSize(14, Unit.PX);
            messageList.getElement()
                .setAttribute("style", "font: 14px 'Times New Roman', Times, serif");
            panel.add(message);
        }

        HTML listApplicationsMessage = new HTML("<br/>List of allowed applications:");
        panel.add(listApplicationsMessage);

        VerticalLayoutData linksData = new VerticalLayoutData();
        linksData.setMargins(new Margins(5));
        int i = 0;
        for (final Entry<String, String> entry : e.getAllowedApps().entrySet()) {
            i++;
            Anchor anchor = new Anchor(i + ". " + entry.getValue());
            anchor.getElement().getStyle().setFontSize(12, Unit.PX);
            anchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent arg0) {
                    LocationManager.get().setRole(entry.getKey());
                    LocationManager.get().reload();
                }
            });
            SimpleContainer anchorContainer = new SimpleContainer();
            anchorContainer.add(anchor);
            panel.add(anchorContainer, linksData);
        }
        HorizontalLayoutContainer root = new HorizontalLayoutContainer();
        root.getElement().setAttribute("style", "background-color: white;");
        w.setClosable(false);

        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            HTML tab = new HTML(e.getMessage());
            tab.getElement().getStyle().setFontSize(14, Unit.PX);
            tab.getElement().setAttribute("style",
                "font: 14px 'Times New Roman', Times, serif; background-color: white;");
            VerticalLayoutContainer table = new VerticalLayoutContainer();
            panel.setBorders(true);
            table.setBorders(true);
            table.setScrollMode(ScrollMode.AUTOY);
            panel.setScrollMode(ScrollMode.AUTOY);
            table.add(tab);

            root.add(panel, new HorizontalLayoutData(.4, 1, new Margins(5)));
            root.add(table, new HorizontalLayoutData(.6, 1, new Margins(5)));
            w.setHeight(500);
            w.setWidth(800);
        } else {
            panel.setBorders(false);
            panel.setScrollMode(ScrollMode.AUTOY);
            root.add(panel, new HorizontalLayoutData(1, 1, new Margins(5)));
            w.setWidth(400);
            w.setHeight(300);
        }

        w.add(root);
        w.show();
    }

    public void onModuleLoad() {
        Viewport viewport = new Viewport();
        rootContainer = new ViewportContainer(viewport);

        LoginPanelBuilder.setBuildApplicationCommand(buildApplicationCommand);
        LoginPanelBuilder.setLoginSuccessCommand(loginSuccessCommand);

        EventManager.Util.set(new EventManagerImpl());

        startBuildApp();
        RootPanel.get().add(viewport);
    }

    public interface DefaultStyle extends ClientBundle {
        @NotStrict
        @Source("DefaultStyle.css")
        CssResource getCss();
    }

}
