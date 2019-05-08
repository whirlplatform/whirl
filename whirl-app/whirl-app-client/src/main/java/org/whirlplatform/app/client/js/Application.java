package org.whirlplatform.app.client.js;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import jsinterop.annotations.JsType;
import org.whirlplatform.app.client.ClientLoginUtils;
import org.whirlplatform.app.client.LocationManager;
import org.whirlplatform.meta.shared.AppConstant;

/**
 * Application level helper methods.
 */
@JsType
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
        UrlBuilder url = Location.createUrlBuilder();
        url.setParameter(AppConstant.NEW_SESSION, String.valueOf(true));
        url.setParameter(AppConstant.APPLICATION_URL, appCode);
        Window.open(url.buildString(), "_blank", "");
    }

    /**
     * Logout.
     */
    public static void logout() {
        ClientLoginUtils.logout();
    }

}
