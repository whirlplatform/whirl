package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.ClientRestException;
import org.whirlplatform.rpc.shared.ExceptionData;
import org.whirlplatform.rpc.shared.SessionToken;

/**
 * Компонент Форма логина. Берет контейнер с формой из ClientLoginUtil.
 */
public class LoginPanelBuilder extends ComponentBuilder {

    private static Element loginDiv;
    private static InputElement loginField;
    private static InputElement passwordField;
    private static InputElement submitButton;
    private static ScheduledCommand buildApplicationCommand = new ScheduledCommand() {
        @Override
        public void execute() {
        }
    };
    private static ScheduledCommand loginSuccessCommand = new ScheduledCommand() {

        @Override
        public void execute() {
        }

    };

    static {
        loginDiv = DOM.getElementById("div_login_form");

        if (loginDiv == null) {
            initLoginPanelMock();
        }

        Element loginEl = DOM.getElementById("login-field");
        if (loginEl != null) {
            loginField = loginEl.cast();
        }
        Element passwordEl = DOM.getElementById("pwd-field");
        if (passwordEl != null) {
            passwordField = passwordEl.cast();
        }
        Element submitEl = DOM.getElementById("submit-btn");
        if (submitEl != null) {
            submitButton = submitEl.cast();
        }
    }

    private HTML html;
    private WidgetComponent wrapper;

    public LoginPanelBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public LoginPanelBuilder() {
        this(Collections.emptyMap());
    }

    // предполагается срабатывание только в редакторе форм
    // это достигается путём отсутствия тэга c id=div_login_form в editor.jsp
    private static void initLoginPanelMock() {
        loginDiv = DOM.createDiv().cast(); // for avoid error "A widget that has
        // an existing parent widget may not
        // be added to the detach list"
        loginDiv.getStyle().setBorderWidth(1, Unit.PX);
        loginDiv.getStyle().setBorderStyle(BorderStyle.DASHED);
        loginDiv.setInnerText("Login Panel");
        loginDiv.setAttribute("id", "div_login_form");
        RootPanel.getBodyElement().appendChild(loginDiv); // чуть позже
        // HTML.wrap
        // переместит этот
        // элемент внутрь
        // виджета, и затем
        // уберёт из дом
        // модели.
        // так что нужно будет пересоздавать этот элемент в редакторе форм.
    }

    public static void login() {
        login(loginField.getValue(), passwordField.getValue());
    }

    private static void login(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            InfoHelper.error("login", AppMessage.Util.MESSAGE.alert(),
                    AppMessage.Util.MESSAGE.login_header());
            return;
        }

        submitButton.setDisabled(true);

        AsyncCallback<ClientUser> callback = new AsyncCallback<ClientUser>() {
            @Override
            public void onSuccess(ClientUser user) {
                ClientUser.setCurrentUser(user);
                SessionToken.set(user.getSessionToken());

                submitButton.setDisabled(false);

                loginSuccessCommand.execute();

                Scheduler.get().scheduleDeferred(buildApplicationCommand);
            }

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof ClientRestException
                        && ((ClientRestException) caught).getData() != null
                        && ((ClientRestException) caught).getData().getType()
                        == ExceptionData.ExceptionType.PASSWORDCHANGE) {
                    ExceptionData pce = ((ClientRestException) caught).getData();
                    String pwdServiceUrl = pce.getPasswordChangeServiceUrl();
                    String currentUrl = URL.encodeComponent(Window.Location.getHref());
                    pwdServiceUrl = pwdServiceUrl.replace("{{redirect_uri}}", currentUrl);
                    Location.replace(pwdServiceUrl);

                } else {
                    submitButton.setDisabled(false);
                    InfoHelper.throwInfo("login", caught);
                }
            }
        };
        DataServiceAsync.Util.getDataService(callback).login(SessionToken.get(), login, password);
    }

    public static void setBuildApplicationCommand(ScheduledCommand command) {
        buildApplicationCommand = command;
    }

    public static void setLoginSuccessCommand(ScheduledCommand command) {
        loginSuccessCommand = command;
    }

    public static boolean isLoginPanelExists() {
        return Document.get().getBody().isOrHasChild(loginDiv);
    }

    @JsIgnore
    public static Locator locatorByElement(Element element) {
        if (loginDiv == null || !loginDiv.<XElement>cast().isVisible()) {
            return null;
        }
        Locator result = new Locator(ComponentType.LoginPanelType);
        if (loginField.isOrHasChild(element)) {
            result.setPart(new Locator(LoginPanelBuilder.LocatorParams.TYPE_LOGIN_FIELD));
            return result;
        } else if (passwordField.isOrHasChild(element)) {
            result.setPart(new Locator(LoginPanelBuilder.LocatorParams.TYPE_PASSWORD_FIELD));
            return result;
        } else if (submitButton.isOrHasChild(element)) {
            result.setPart(new Locator(LoginPanelBuilder.LocatorParams.TYPE_SUBMIT_BUTTON));
            return result;
        }
        return null;
    }

    @JsIgnore
    public static Element elementByLocator(Locator locator) {
        if (loginDiv == null || !loginDiv.<XElement>cast().isVisible()) {
            return null;
        }
        if (ComponentType.LoginPanelType.getType().equals(locator.getType())) {
            Locator part = locator.getPart();
            if (part != null) {
                if (part.getType().equals(LoginPanelBuilder.LocatorParams.TYPE_LOGIN_FIELD)) {
                    return loginField;
                } else if (part.getType()
                        .equals(LoginPanelBuilder.LocatorParams.TYPE_PASSWORD_FIELD)) {
                    return passwordField;
                } else if (part.getType()
                        .equals(LoginPanelBuilder.LocatorParams.TYPE_SUBMIT_BUTTON)) {
                    return submitButton;
                }
            }
        }
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.LoginPanelType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        loginDiv = DOM.getElementById("div_login_form");

        if (loginDiv == null) { // в редакторе форм будет именно так. поэтому
            // предусматриваем заглушку
            initLoginPanelMock();
        }
        html = HTML.wrap(loginDiv);
        loginDiv.getStyle().setDisplay(Display.BLOCK);
        wrapper = new WidgetComponent(html);
        return wrapper;
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) html;
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        return locatorByElement(element);
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        return elementByLocator(locator);
    }

    /**
     * Возвращает код компонента.
     *
     * @return код компонента
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true если компонент скрыт
     */
    @Override
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private static class LocatorParams {

        private static String TYPE_LOGIN_FIELD = "LoginField";
        private static String TYPE_PASSWORD_FIELD = "PasswordField";
        private static String TYPE_SUBMIT_BUTTON = "SubmitButton";

    }

}
