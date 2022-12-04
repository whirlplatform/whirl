package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.FramedLoginPanelBuilder;


/**
 * Компонент, позволяющий выполнить вход в приложение.
 */
public abstract class FramedLoginPanelBuilderOverlay {

    public static FramedLoginPanelBuilder create(
            FramedLoginPanelBuilder instance) {
        instance.create();
        return instance;
    }

    public static ComponentBuilder getParent(FramedLoginPanelBuilder instance) {
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

    public abstract void focus();
}
