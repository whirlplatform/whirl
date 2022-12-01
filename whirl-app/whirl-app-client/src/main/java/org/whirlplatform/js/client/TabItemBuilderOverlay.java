package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TabItemBuilder;

/**
 * Представляет вкладку панели вкладок {@link TabPanelBuilderOverlay TabPanelBuilder}
 */
public abstract class TabItemBuilderOverlay {

    public static TabItemBuilder create(TabItemBuilder instance) {
        instance.create();
        return instance;
    }

    public static void setChild(TabItemBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    public static void clear(TabItemBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(TabItemBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public static ComponentBuilder getParent(TabItemBuilder instance) {
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

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public abstract void setTitle(String title);

    public abstract int getChildrenCount();

    /**
     * Возвращает  true, если вкладка в данный момент активна, false - если неактивна
     *
     * @return
     */
    public abstract boolean isActive();

    public abstract void focus();

}
