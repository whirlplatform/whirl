package org.whirlplatform.app.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.app.client.ClientLoginUtils;
import org.whirlplatform.app.client.LocationManager;
import org.whirlplatform.meta.shared.AppConstant;

/**
 * Вспомогательные методы на уровне приложения.
 */
@JsType(namespace = "Whirl")
public class Application {

    /**
     * Изменяет открытое приложение в текущем окне.
     *
     * @param appCode код приложения
     */
    public static void changeApplication(String appCode) {
        LocationManager.get().setRole(appCode);
        LocationManager.get().reload();
    }

    /**
     * Открывает приложение в новом окне браузера с новой сессией текущего пользователя.
     *
     * @param appCode код приложения
     * @param urlParams дополнительные параметры
     */
    public static void openApplication(String appCode, @JsOptional JavaScriptObject urlParams) {
        UrlBuilder url = Location.createUrlBuilder();
        url.setParameter(AppConstant.NEW_SESSION, String.valueOf(true));
        url.setParameter(AppConstant.APPLICATION_URL, appCode);

        if (urlParams != null) {
            JSONObject queryParams = new JSONObject(urlParams);
            for (String key : queryParams.keySet()) {
                Object value = queryParams.get(key);
                if (value instanceof JSONString) {
                    url.setParameter(key, queryParams.get(key).isString().stringValue());
                } else if (value instanceof JSONNumber) {
                    url.setParameter(key, queryParams.get(key).isNumber().toString());
                } else if (value instanceof JSONBoolean) {
                    url.setParameter(key, queryParams.get(key).isBoolean().toString());
                } else if (value instanceof JSONNull) {
                    url.setParameter(key, queryParams.get(key).isNull().toString());
                }
            }
        }
        Window.open(url.buildString(), "_blank", "");
    }

    /**
     * Выход из системы.
     */
    public static void logout() {
        ClientLoginUtils.logout();
    }

}
