package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.TreeComboBoxBuilder;

public abstract class TreeComboBoxBuilderOverlay {

    public static TreeComboBoxBuilder create(TreeComboBoxBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    public abstract String getDomId();

    public abstract void setCode(String name);

    public abstract String getCode();

    public abstract void setEnabled(boolean enabled);

    public abstract boolean isEnabled();

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    public abstract void setStyleName(String styleName);

    public abstract boolean markInvalid(String message);

    public static ComponentBuilder getParent(TreeComboBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

////    public static RowListValue getDataValue(TreeComboBoxBuilder instance) {
//        return instance.getFieldValue();
//    }
//
////    public static void setDataValue(TreeComboBoxBuilder instance,
//                                    RowListValue value) {
//        instance.setFieldValue(value);
//    }

    public abstract String getText();

    public abstract void clear();

}
