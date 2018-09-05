package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HtmlEditorBuilder;
import org.whirlplatform.meta.shared.data.DataValue;


/**
 * Редактор HTML
 */
//@Export("HtmlEditor")
//@ExportPackage("Whirl")
public abstract class HtmlEditorBuilderOverlay implements
        ExportOverlay<HtmlEditorBuilder> {

    @ExportInstanceMethod
    public static HtmlEditorBuilder create(HtmlEditorBuilder instance) {
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
     *
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
    public static boolean isEmpty(HtmlEditorBuilder instance) {
        return instance.getValue() == null;
    }

    @Export
    public abstract void setValue(String value);

    @Export
    public abstract String getValue();

    @Export
    public abstract String getText();

    @ExportInstanceMethod
    public static DataValue getDataValue(HtmlEditorBuilder instance) {
        return instance.getFieldValue();
    }

    @ExportInstanceMethod
    public static void setDataValue(HtmlEditorBuilder instance, DataValue value) {
        instance.setFieldValue(value);
    }

    @Export
    public abstract void clear();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(HtmlEditorBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract void focus();

}
