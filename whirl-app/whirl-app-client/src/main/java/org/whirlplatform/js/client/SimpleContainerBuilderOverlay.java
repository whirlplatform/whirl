package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.SimpleContainerBuilder;

/**
 * Контейнер, содержащий не более одного дочернего компонента.
 */
public abstract class SimpleContainerBuilderOverlay {

    public static SimpleContainerBuilder create(SimpleContainerBuilder instance) {
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

    public static void setChild(SimpleContainerBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    public static void clear(SimpleContainerBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(SimpleContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public static ComponentBuilder getParent(SimpleContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract int getChildrenCount();

    public abstract void focus();

}
