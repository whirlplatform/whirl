package org.whirlplatform.server.driver.multibase.fetch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class DataFetcherUtil {

    // Аналог метода из Getter, но он не работает на сервере
    public static Map<String, String> fromURLEncoded(String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (value != null) {
            String[] parameters = value.split("&");
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].isEmpty()) {
                    String[] pair = parameters[i].split("=");
                    if (pair.length != 1) {
                        if (pair[1] == null) {
                            result.put(pair[0], null);
                        } else {
                            try {
                                result.put(pair[0],
                                    URLDecoder.decode(pair[1], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                result.put(pair[0], null);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

}
