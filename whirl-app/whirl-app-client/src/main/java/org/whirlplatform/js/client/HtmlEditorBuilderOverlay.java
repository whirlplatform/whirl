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

    public static boolean isEmpty(HtmlEditorBuilder instance) {
        return instance.getValue() == null;
    }

    public static DataValue getDataValue(HtmlEditorBuilder instance) {
        return instance.getFieldValue();
    }

    public static void setDataValue(HtmlEditorBuilder instance, DataValue value) {
        instance.setFieldValue(value);
    }

    public static ComponentBuilder getParent(HtmlEditorBuilder instance) {
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

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract String getText();

    public abstract void clear();

    public abstract void focus();

}
