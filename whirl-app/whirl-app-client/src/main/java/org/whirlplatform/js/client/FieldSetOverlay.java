package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.FieldSetBuilder;

public abstract class FieldSetOverlay {

    public static FieldSetBuilder create(FieldSetBuilder instance) {
        instance.create();
        return instance;
    }

    public static void setChild(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.addChild(builder);
    }

    public static void clear(FieldSetBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public static ComponentBuilder getParent(FieldSetBuilder instance) {
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

    public abstract boolean isHidden();

    public abstract void setHidden(boolean hidden);

    public abstract void setStyleName(String styleName);

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public abstract int getChildrenCount();

    public abstract void focus();

    public abstract boolean isExpanded();

    public abstract void setExpanded(boolean expand);

}
