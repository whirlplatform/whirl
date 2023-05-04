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
import jsinterop.annotations.JsType;
import org.whirlplatform.app.client.ClientLoginUtils;
import org.whirlplatform.app.client.LocationManager;
import org.whirlplatform.meta.shared.AppConstant;

/**
 * Application level helper methods.
 */
@JsType(namespace = "Whirl")
public class Application {

    /**
     * Changes opened application in the current window.
     *
     * @param appCode application code
     */
    public static void setCurrentApplication(String appCode) {
        LocationManager.get().setRole(appCode);
        LocationManager.get().reload();
    }

    /**
     * Opens application in new browser window with the new session of current user.
     *
     * @param appCode application code
     */
    public static void openApplication(String appCode) {
        Window.open(createUrlBuilder(appCode, null), "_blank", "");
    }

    private static String createUrlBuilder(String appCode, JSONObject queryParams) {
        UrlBuilder url = Location.createUrlBuilder();
        url.setParameter(AppConstant.NEW_SESSION, String.valueOf(true));
        url.setParameter(AppConstant.APPLICATION_URL, appCode);

        if (queryParams != null) {
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
        return url.buildString();
    }

    /**
     * Opens application in new browser window with the new session of current user.
     *
     * @param appCode application code
     * @param jsValue additional parameters
     */
    public static void openApplicationWithParams(String appCode, JavaScriptObject jsValue) {
        JSONObject queryParams = new JSONObject(jsValue);
        Window.open(createUrlBuilder(appCode, queryParams), "_blank", "");
    }

    /**
     * Logout.
     */
    public static void logout() {
        ClientLoginUtils.logout();
    }

}
