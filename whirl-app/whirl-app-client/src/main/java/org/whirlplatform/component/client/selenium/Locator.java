package org.whirlplatform.component.client.selenium;

import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.sencha.gxt.core.client.util.Util;
import org.whirlplatform.meta.shared.component.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Формат локатора имеет следующий вид:<br>
 * <code>
 * ComponentType(id=1&code=code1)[Part(paramName1=paramValue1)[PartNested]]
 * </code>
 * <br>
 * Типы указываются в CamelCase, параметры в lowerCamelCase.
 *
 */
public class Locator {

    private String type;

    private Map<String, String> params = new HashMap<>();

    private Locator part;

    public Locator(String type) {
        if (Util.isEmptyString(type)) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public Locator(ComponentType componentType) {
        this(componentType.getType());
    }

    public String getType() {
        return type;
    }

    public void setParameter(String parameter, String value) {
        if (Util.isEmptyString(parameter)) {
            throw new IllegalArgumentException();
        }
        params.put(parameter, value);
    }

    public boolean hasParameter(String parameter) {
        return params.containsKey(parameter);
    }

    public String getParameter(String parameter) {
        return params.get(parameter);
    }

    public void setPart(Locator part) {
        this.part = part;
    }

    public Locator getPart() {
        return part;
    }

    /**
     * Метод используется при кодировании значений локатора
     *
     * @param value
     * @return
     */
    public static String escape(String value) {
        try {
            return URL.encodeQueryString(value);
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Метод используется при декодировании значений локатора
     *
     * @param value
     * @return
     */
    public static String unescape(String value) {
        try {
            return URL.decodeQueryString(value);
        } catch (Exception e) {
            return value;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(type);
        if (!params.isEmpty()) {
            result.append("(");
            boolean first = true;
            for (String key : params.keySet()) {
                if (!first) {
                    result.append("&");
                }
                result.append(key).append('=').append(escape(params.get(key)));
                first = false;
            }
            result.append(")");
        }
        if (part != null) {
            result.append("[").append(part.toString()).append("]");
        }
        return result.toString();
    }

    public static Locator parse(String locatorString) {
        if (Util.isEmptyString(locatorString)) {
            throw new IllegalArgumentException("Locator string can not be empty");
        }
        // проверить валидность локатора
        String type = findByRegex(locatorString, "^\\w+");
        if (Util.isEmptyString(type)) {
            throw new IllegalArgumentException("Type should be provided");
        }

        Locator result = new Locator(type);

        int firstOpenBracket = locatorString.indexOf("[");
        List<String> paramsString = getBalancedSubstrings(
                firstOpenBracket == -1 ? locatorString : locatorString.substring(0, firstOpenBracket), '(', ')', false);
        if (paramsString.size() == 1) {
            result.params = parseParams(paramsString.get(0));
        }

        List<String> partStrings = getBalancedSubstrings(locatorString, '[', ']', false);
        if (partStrings.size() == 1) {
            result.part = parse(partStrings.get(0));
        }
        return result;
    }

    private static String findByRegex(String string, String regex) {
        RegExp regexp = RegExp.compile(regex);
        MatchResult match = regexp.exec(string);
        String result = match.getGroup(0);
        if (!Util.isEmptyString(result)) {
            return result;
        }
        return null;
    }

    // не будем долго думать как распарсить сбалансированно из скобок - вытащим
    // из интернета
    // https://stackoverflow.com/questions/33188095/match-contents-within-square-brackets-including-nested-square-brackets
    private static List<String> getBalancedSubstrings(String s, Character markStart, Character markEnd,
                                                      boolean includeMarkers) {
        List<String> subTreeList = new ArrayList<String>();
        int level = 0;
        int lastOpenBracket = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == markStart) {
                level++;
                if (level == 1) {
                    lastOpenBracket = (includeMarkers ? i : i + 1);
                }
            } else if (c == markEnd) {
                if (level == 1) {
                    subTreeList.add(s.substring(lastOpenBracket, (includeMarkers ? i + 1 : i)));
                }
                level--;
            }
        }
        return subTreeList;
    }

    /**
     * Преобразует строку формата name=value&name=value в HashMap
     *
     * @param paramsString
     * @return
     */
    private static Map<String, String> parseParams(String paramsString) {
        String[] params = paramsString.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] parts = param.split("=");
            String name = parts.length > 0 ? parts[0] : null;
            String value = parts.length > 1 ? parts[1] : null;
            if (name != null) {
                map.put(name, unescape(value));
            }
        }
        return map;
    }

    /**
     * Сравнивает type с типом локатора
     *
     * @param type
     * @return true, если типы совпадают (case ignored)
     */
    public boolean typeEquals(final String type) {
        if (Util.isEmptyString(getType()) || Util.isEmptyString(type)) {
            return false;
        }
        return getType().equalsIgnoreCase(type);
    }
}