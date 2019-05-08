package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.FieldSetBuilder;

public abstract class FieldSetOverlay {

    public static FieldSetBuilder create(FieldSetBuilder instance) {
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

    public abstract boolean isHidden();

    public abstract void setStyleName(String styleName);

    public static void setChild(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.addChild(builder);
    }

    public static void clear(FieldSetBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public static ComponentBuilder getParent(FieldSetBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract int getChildrenCount();

    public abstract void focus();

    public abstract boolean isExpanded();

    public abstract void setExpanded(boolean expand);

}
