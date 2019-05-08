package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HtmlEditorBuilder;
import org.whirlplatform.meta.shared.data.DataValue;


/**
 * Редактор HTML
 */
public abstract class HtmlEditorBuilderOverlay {

    public static HtmlEditorBuilder create(HtmlEditorBuilder instance) {
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

    public static boolean isEmpty(HtmlEditorBuilder instance) {
        return instance.getValue() == null;
    }

    public abstract void setValue(String value);

    public abstract String getValue();

    public abstract String getText();

    public static DataValue getDataValue(HtmlEditorBuilder instance) {
        return instance.getFieldValue();
    }

    public static void setDataValue(HtmlEditorBuilder instance, DataValue value) {
        instance.setFieldValue(value);
    }

    public abstract void clear();

    public static ComponentBuilder getParent(HtmlEditorBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract void focus();

}
