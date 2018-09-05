package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.tree.TreeBuilder;

@Export("Tree")
@ExportPackage("Whirl")
public abstract class TreeBuilderOverlay implements ExportOverlay<TreeBuilder> {

    @ExportInstanceMethod
    @Deprecated
    public static TreeBuilder create(TreeBuilder instance) {
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

//    @Export
//    @Deprecated
//    public abstract RowListValue getFieldValue();
//
//    @Export
//    @Deprecated
//    public abstract void setFieldValue(RowListValue value);
//
//    @ExportInstanceMethod
//    public static RowListValue getDataValue(TreeBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(TreeBuilder instance, RowListValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract void load();

//    @Deprecated
//    @ExportInstanceMethod
//    public static void loadParameters(TreeBuilder instance,
//                                      DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }
//
//    @ExportInstanceMethod
//    public static void loadWithParameters(TreeBuilder instance,
//                                          DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }

    @ExportInstanceMethod
    public static ComponentBuilder getParent(TreeBuilder instance) {
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

//    /**
//     * Очищает фильтр поиска по тексту.
//     */
//    @Export
//    public abstract void clearLabelFilter();

}
