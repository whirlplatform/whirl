package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.CenterContainerBuilder;

public abstract class CenterContainerBuilderOverlay {

    public static CenterContainerBuilder create(CenterContainerBuilder instance) {
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

    public static void setChild(CenterContainerBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Метод удаляет все вложенные компоненты контейнера
     */
    public static void clear(CenterContainerBuilder instance) {
        instance.clearContainer();
    }


    /**
     * Удалить определённый вложенный компонент
     *
     * @param builder Билдер компонента, подлежащего удалению из контейнера
     */
    public static void remove(CenterContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public abstract ComponentBuilder[] getChildren();

    /**
     * Перерисовать контейнер и вложенные компоненты.
     */
    public abstract void forceLayout();

    public static ComponentBuilder getParent(CenterContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract void focus();
}
