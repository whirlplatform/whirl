package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.MultiComboBoxBuilder;

@Export("MultiComboBox")
@ExportPackage("Whirl")
public abstract class MultiComboBoxBuilderOverlay implements
        ExportOverlay<MultiComboBoxBuilder> {

    @ExportInstanceMethod
    @Deprecated
    public static MultiComboBoxBuilder create(MultiComboBoxBuilder instance) {
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

    @Export
    public abstract void clear();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(MultiComboBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract void setRequired(boolean required);

    @Export
    public abstract boolean isRequired();

    @Export
    public abstract void markInvalid(String message);

    @Export
    public abstract void clearInvalid();

    @Export
    public abstract void focus();

//    @Export
//    @Deprecated
//    public abstract void setFieldValue(RowListValue value);

//    @Export
//    @Deprecated
//    public abstract RowListValue getFieldValue();
//
//    @ExportInstanceMethod
//    public static RowListValue getDataValue(MultiComboBoxBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(MultiComboBoxBuilder instance,
//                                    RowListValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract String getText();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);
}
