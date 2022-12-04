package org.whirlplatform.app.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.widget.core.client.WindowManager;
import java.util.Date;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.base.FramedLoginPanelBuilder;
import org.whirlplatform.component.client.base.LoginPanelBuilder;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class ClientLoginUtils {

    private static Integer SESSION_TIMEOUT = 500;
    private static Timer timer;

    static {
        Element element = DomQuery.selectNode("#session-timeout");
        if (element != null) {
            SESSION_TIMEOUT = Integer.parseInt(element.getInnerText());
        }
    }

    public static void logout() {
        WindowManager.get().hideAll();

        DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                loginRedirect();
            }

            @Override
            public void onFailure(Throwable caught) {
                loginRedirect();
            }
        }).logout(SessionToken.get());
    }

    public static void login() {
        LoginPanelBuilder.login();
    }

    private static void loginRedirect() {
        String logoutPage = null;
        // получить ссылку перехода из ApplicationData
        ApplicationData app = AppBuilder.getCurrApp();
        if (app != null && app.getLogoutPage() != null && !app.getLogoutPage().isEmpty()) {
            logoutPage = app.getLogoutPage();
        }
        // показываем окно стандартного логина
        JSMethods.export();
        showLogin();
    }

    public static void showLogin() {
        if (LoginPanelBuilder.isLoginPanelExists()) {
            Containable parent = AppEntryPoint.getRootContainer();
            ComponentBuilder login = new FramedLoginPanelBuilder();
            login.create();
            parent.addChild(login);
            parent.forceLayout();
        } else {
            Location.reload();
        }
    }

    public static void initTouchTimer() {
        final DataServiceAsync touchService =
                DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                    }
                });

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer() {
            @Override
            public void run() {
                // обрубаем сессию через 90 минут
                Date date = DataServiceAsync.Util.lastCallDate();
                Date now = new Date(System.currentTimeMillis() - SESSION_TIMEOUT * 60 * 1000);
                if (date.before(now)) {
                    DataServiceAsync.Util.getDataService(new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(Void result) {
                        }
                    }).removeToken(SessionToken.get());
                    cancel();
                } else {
                    touch(touchService);
                }
            }
        };
        // говорим что токен жив каждые 30 секунд
        timer.scheduleRepeating(30 * 1000);
        touch(touchService);
    }

    public static void touch(DataServiceAsync service) {
        service.touch(SessionToken.get());
    }

}
