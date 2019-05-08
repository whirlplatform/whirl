package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.SimpleHtmlEditorBuilder;

public abstract class SimpleHtmlEditorBuilderOverlay {

    public static SimpleHtmlEditorBuilder create(SimpleHtmlEditorBuilder instance) {
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
     */
    public abstract String getDomId();

    public abstract void setCode(String name);

    public abstract String getCode();

    public abstract void setEnabled(boolean enabled);

    public abstract boolean isEnabled();

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    public abstract void setStyleName(String styleName);

    public static boolean isEmpty(SimpleHtmlEditorBuilder instance) {
        return instance.getValue() == null;
    }

    public abstract void setValue(String value);

    public abstract String getValue();

////    public static DataValue getDataValue(SimpleHtmlEditorBuilder instance) {
//        return instance.getFieldValue();
//    }

////    public static void setDataValue(SimpleHtmlEditorBuilder instance,
//                                    DataValue value) {
//        instance.setFieldValue(value);
//    }

    public static ComponentBuilder getParent(SimpleHtmlEditorBuilder instance) {
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
}
