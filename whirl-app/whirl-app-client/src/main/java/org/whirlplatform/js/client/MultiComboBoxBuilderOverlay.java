package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.MultiComboBoxBuilder;

public abstract class MultiComboBoxBuilderOverlay {

    @Deprecated
    public static MultiComboBoxBuilder create(MultiComboBoxBuilder instance) {
        instance.create();
        return instance;
    }

    public static ComponentBuilder getParent(MultiComboBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Возвращает идентификатор элемента в DOM документа.
     */
    public abstract String getDomId();

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    public abstract String getCode();

    public abstract void setCode(String name);

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    public abstract void setStyleName(String styleName);

    public abstract void clear();

    public abstract boolean isRequired();

    public abstract void setRequired(boolean required);

    public abstract void markInvalid(String message);

    public abstract void clearInvalid();

    public abstract void focus();

    //    @Deprecated
    //    public abstract void setFieldValue(RowListValue value);

    //    @Deprecated
    //    public abstract RowListValue getFieldValue();
    //
    //    public static RowListValue getDataValue(MultiComboBoxBuilder instance) {
    //        return instance.getFieldValue();
    //    }
    //
    //    public static void setDataValue(MultiComboBoxBuilder instance,
    //                                    RowListValue value) {
    //        instance.setFieldValue(value);
    //    }

    public abstract String getText();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);
}
