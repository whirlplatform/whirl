package org.whirlplatform.server.driver.multibase;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.data.DataType;

/**
 * Конвертер
 */
@Deprecated
public class Getter {

    private static String TRUE = "T";

    public static Boolean fromDBBoolean(String value) {
        return TRUE.equals(value);
    }

    public static Double fromDBNumber(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Double.parseDouble(value.replace(",", "."));
    }

    public static Date fromDBDate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Date result = null;
        if (GWT.isClient()) {
            result = AppConstant.getDateFormatLong().parse(value);
        } else {
            SimpleDateFormat format = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONG);
            try {
                result = format.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object fromDBString(String value, DataType type) {
        if (DataType.DATE == type) {
            return fromDBDate(value);
        } else if (DataType.BOOLEAN == type) {
            return fromDBBoolean(value);
        } else if (DataType.NUMBER == type) {
            return fromDBNumber(value);
        }
        return value;
    }

    /**
     * Преобразует значение в сторку для передачи в БД.
     *
     * <p>
     * <b>Внимание!</b> Этот метод не должен быть использован для преобразования
     * чисел, так как округлаяет значение и и спользуется для преобразования DFOBJ.
     * </p>
     *
     * @param value
     * @return
     */
    public static String toDBString(Object value) {
        if (value == null) {
            return "";
        }
        String strVal;
        if (value instanceof String) {
            strVal = (String) value;
        } else if (value instanceof Double) {
            strVal = toDBString((Double) value);
        } else if (value instanceof Boolean) {
            strVal = toDBString((Boolean) value);
        } else if (value instanceof Date) {
            strVal = toDBString((Date) value);
        } else {
            strVal = value.toString();
        }
        return strVal;
    }

    /**
     * Преобразует значение в сторку для передачи в БД.
     *
     * <p>
     * <b>Внимание!</b> Этот метод не должен быть использован для преобразования
     * чисел, так как округлаяет значение и и спользуется для преобразования DFOBJ.
     * </p>
     *
     * @param value
     * @return
     */
    public static String toDBString(Double value) {
        // TODO этот метод должен пеобразовывать именно числа
        if (value == null) {
            return null;
        }
        Double v = value;
        long val1 = Math.round(v * 10);
        long val2 = Math.round(v) * 10;
        if (val1 != val2)
            return value.toString();
        else
            return String.valueOf(v.longValue());
    }

    public static String toDBString(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? "T" : "F";
    }

    public static String toDBString(Date value) {
        if (value == null) {
            return null;
        }
        if (GWT.isClient()) {
            return AppConstant.getDateFormatLong().format(value);
        } else {
            SimpleDateFormat format = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONG);
            return format.format(value);
        }
    }

    public static String objToDBString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }
        Double v = (Double) value;
        long val1 = Math.round(v * 10);
        long val2 = Math.round(v) * 10;
        if (val1 != val2)
            return value.toString();
        else
            return String.valueOf(v.longValue());
    }

    public static boolean safeBoolean(Boolean value) {
        if (value == null) {
            return false;
        }
        return value;
    }

    public static String formatDate(Date value, String format) {
        if (value == null)
            return "";
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(format);
        String date = dateFormat.format(value);
        return date;
    }

    public static String listToString(List<String> list) {
        String delimeter = ";";
        String result = "";
        for (String x : list) {
            result = result + x + delimeter;
        }
        return result;
    }

    public static Map<String, String> fromURLEncoded(String value) {
        Map<String, String> result = new HashMap<>();
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

    public static double parseSizeString(String size) {
        if (size == null) {
            return Double.NaN;
        }
        double result = Double.NaN;
        boolean percent = false;
        try {
            result = Double.parseDouble(size);
        } catch (NumberFormatException e) {
            if (size != null) {
                if (size.contains("px")) {
                    size = size.replace("px", "");
                }
                if (size.contains("%")) {
                    size = size.replace("%", "");
                    percent = true;
                }
                try {
                    result = Double.parseDouble(size);
                    if (percent) {
                        result = result / 100;
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }
        return result;
    }

}
