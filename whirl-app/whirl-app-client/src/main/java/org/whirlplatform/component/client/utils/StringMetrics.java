package org.whirlplatform.component.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.util.TextMetrics;

public class StringMetrics {

    /**
     * Возвращает ширину текста по параметрам CSS.
     *
     * @param text   - текст
     * @param size   - размер текста в формате CSS
     * @param weight - вес шрифта
     * @param family - тип шрифта
     */
    public static int getWidth(String text, String size, String weight,
                               String family) {
        Element el = DOM.createDiv();
        if (size != null) {
            el.getStyle().setProperty("fontSize", size);
        }
        if (weight != null) {
            el.getStyle().setProperty("fontWeight", weight);
        }
        if (family != null) {
            el.getStyle().setProperty("fontFamily", family);
        }
        TextMetrics metric = TextMetrics.get();
        metric.bind(el);
        return metric.getWidth(text);
    }

    /**
     * Возвращает ширину текста по параметрам CSS.<br>
     * <br>
     * По-умолчанию:<br>
     * <code>font-weight: normal;<br>
     * font-family: arial,tahoma,helvetica,sans-serif;<br>
     * </code>
     *
     * @param text - текст
     * @param size - размер текста в формате CSS
     */
    public static int getWidth(String text, String size) {
        return getWidth(text, size, "normal",
            "arial,tahoma,helvetica,sans-serif");
    }

    /**
     * Возвращает ширину текста по параметрам CSS.<br>
     * <br>
     * По-умолчанию:<br>
     * <code>
     * font-size: 12px;<br> font-weight: normal;<br> font-family:
     * arial,tahoma,helvetica,sans-serif;<br>
     * </code>
     *
     * @param text - текст
     */
    public static int getWidth(String text) {
        return getWidth(text, "12px");
    }

    public static int getWidth(int lenght, String size) {
        String text = "";
        for (int i = 1; i <= lenght; i++) {
            text = text + "X";
        }
        return getWidth(text, size);
    }
}
