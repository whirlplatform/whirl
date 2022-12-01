package org.whirlplatform.app.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ApplicationPreferences {

    private static Map<String, String> preferences = new HashMap<>();
    private static List<String> applicationStyles = new ArrayList<String>();
    private static List<String> applicationScripts = new ArrayList<String>();
    private static List<String> applicationCSS = new ArrayList<String>();

    public static void clear() {
        preferences = new HashMap<>();
        applicationStyles.clear();
        applicationScripts.clear();
        applicationCSS.clear();
    }

    public static void putAll(Map<String, List<String>> map) {
        for (Entry<String, List<String>> e : map.entrySet()) {
            for (String v : e.getValue()) {
                put(e.getKey(), v);
            }
        }
    }

    public static void put(String key, String value) {
        preferences.put(key, value);
    }

    public static String get(Preference preference) {
        return preferences.get(preference.name());
    }

    public static void addApplicationStyle(String style) {
        applicationStyles.add(style);
    }

    public static List<String> getApplicationStyles() {
        return applicationStyles;
    }

    public static void addApplicationScript(String script) {
        applicationStyles.add(script);
    }

    public static List<String> getApplicationScripts() {
        return applicationScripts;
    }

    public static void addApplicationCSS(String css) {
        applicationStyles.add(css);
    }

    public static List<String> getApplicationCSS() {
        return applicationCSS;
    }

    public enum Preference {
        TABLE_SELECT_DELAY, TABLE_REFRESH_DELAY
    }

}
