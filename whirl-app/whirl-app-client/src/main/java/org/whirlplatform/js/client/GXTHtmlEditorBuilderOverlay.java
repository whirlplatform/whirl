package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.GXTHtmlEditorBuilder;


@Export("HtmlEditor")
@ExportPackage("Whirl")
public abstract class GXTHtmlEditorBuilderOverlay implements
        ExportOverlay<GXTHtmlEditorBuilder> {

    @ExportInstanceMethod
    public static GXTHtmlEditorBuilder create(GXTHtmlEditorBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     */
    @Export
    public abstract String getDomId();

    @Export
    public abstract void setCode(String name);

    @Export
    public abstract String getCode();

    @Export
    public abstract void setEnabled(boolean enabled);

    @Export
    public abstract boolean isEnabled();

    @Export
    public abstract void setHidden(boolean hidden);

    @Export
    public abstract void isHidden();

    @Export
    public abstract void setStyleName(String styleName);

    @ExportInstanceMethod
    public static boolean isEmpty(GXTHtmlEditorBuilder instance) {
        return instance.getValue() == null;
    }

    @Export
    public abstract void setValue(String value);

    @Export
    public abstract String getValue();

//    @ExportInstanceMethod
//    public static DataValue getDataValue(GXTHtmlEditorBuilder instance) {
//        return instance.getFieldValue();
//    }

//    @ExportInstanceMethod
//    public static void setDataValue(GXTHtmlEditorBuilder instance,
//                                    DataValue value) {
//        instance.setFieldValue(value);
//    }

    @ExportInstanceMethod
    public static ComponentBuilder getParent(GXTHtmlEditorBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);
}
