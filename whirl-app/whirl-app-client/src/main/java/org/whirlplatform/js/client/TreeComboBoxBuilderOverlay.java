package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.TreeComboBoxBuilder;

@Export("TreeComboBox")
@ExportPackage("Whirl")
public abstract class TreeComboBoxBuilderOverlay implements
        ExportOverlay<TreeComboBoxBuilder> {

    @ExportInstanceMethod
    public static TreeComboBoxBuilder create(TreeComboBoxBuilder instance) {
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
    public abstract boolean markInvalid(String message);

    @ExportInstanceMethod
    public static ComponentBuilder getParent(TreeComboBoxBuilder instance) {
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

//    @ExportInstanceMethod
//    public static RowListValue getDataValue(TreeComboBoxBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(TreeComboBoxBuilder instance,
//                                    RowListValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract String getText();

    @Export
    public abstract void clear();

}
