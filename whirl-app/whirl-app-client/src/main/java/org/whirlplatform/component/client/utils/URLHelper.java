package org.whirlplatform.component.client.utils;

import com.google.gwt.http.client.URL;
import com.sencha.gxt.core.shared.FastMap;
import java.util.Map;

public class URLHelper {

    public static Map<String, String> fromURLEncoded(String value) {
        Map<String, String> result = new FastMap<String>();
        if (value != null) {
            String[] parameters = value.split("&");
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].isEmpty()) {
                    String[] pair = parameters[i].split("=");
                    if (pair.length != 1) {
                        if (pair[1] == null) {
                            result.put(pair[0], null);
                        } else {
                            result.put(pair[0], URL.decodeQueryString(pair[1]));
                        }

                    }
                }
            }
        }
        return result;
    }

}
