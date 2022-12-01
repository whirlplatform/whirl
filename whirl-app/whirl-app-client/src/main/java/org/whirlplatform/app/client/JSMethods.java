package org.whirlplatform.app.client;

public class JSMethods {

    public static native void export() /*-{
        logout = function(compId) {
            @org.whirlplatform.app.client.ClientLoginUtils::logout()();
        };

        $wnd.login = function() {
            @org.whirlplatform.app.client.ClientLoginUtils::login()();
        };
    }-*/;

}
