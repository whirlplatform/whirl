package org.whirlplatform.component.client.utils;

public class DataUtils {

    public static String notNull(String string, String def) {
        return string == null ? def : string;
    }
}
