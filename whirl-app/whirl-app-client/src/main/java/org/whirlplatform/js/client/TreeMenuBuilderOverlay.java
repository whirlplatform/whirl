package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.tree.TreeMenuBuilder;

public abstract class TreeMenuBuilderOverlay {

    public static TreeMenuBuilder create(TreeMenuBuilder instance) {
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

    public static ComponentBuilder getParent(TreeMenuBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract void focus();

}
