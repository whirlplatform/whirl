package org.whirlplatform.js.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import org.timepedia.exporter.client.*;
import org.whirlplatform.app.client.ClientLoginUtils;
import org.whirlplatform.app.client.LocationManager;
import org.whirlplatform.meta.shared.AppConstant;

/**
 * Служебные методы приложения
 */
@Export("Helper")
@ExportPackage("Whirl")
public class Helper implements Exportable {

    /**
     * Запустить приложение по его коду
     *
     * @param appCode - String, код приложения
     */
    @ExportStaticMethod
    public static void setCurrentApplication(String appCode) {
        LocationManager.get().setRole(appCode);
        LocationManager.get().reload();
    }

    /**
     * Открывает приложение в новом окне с новой сессией текущего пользователя.
     * Авторизация при этом не требуется.
     *
     * @param appCode - String, код приложения
     */
    @ExportStaticMethod
    public static void openApplication(String appCode) {
        UrlBuilder url = Location.createUrlBuilder();
        url.setParameter(AppConstant.NEW_SESSION, String.valueOf(true));
        url.setParameter(AppConstant.ROLE_URL, appCode);
        Window.open(url.buildString(), "_blank", "");
    }

    /**
     * Выход
     */
    @Export
    public static void logout() {
        ClientLoginUtils.logout();
    }

    /**
     * Получение обработчика ошибки
     *
     * @return JavaScriptException
     */
    @NoExport
    public static JavaScriptException getAndLogException(String message) {
        consoleError(message);
        return new JavaScriptException(message);
    }

    /**
     * Создание сообщение об ошибке
     *
     * @param message - String
     */
    private static native void consoleError(String message) /*-{
		if (window.console) {
			window.console.error("JS Error: " + message);
		}
	}-*/;

}
