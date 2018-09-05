package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.FramedLoginPanelBuilder;


/**
 * Компонент, позволяющий выполнить вход в приложение.
 */
//@Export("FramedLoginPanel")
//@ExportPackage("Whirl")
public abstract class FramedLoginPanelBuilderOverlay implements
        ExportOverlay<FramedLoginPanelBuilder> {

    @ExportInstanceMethod
    public static FramedLoginPanelBuilder create(
            FramedLoginPanelBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    @Export
    public abstract void setCode(String name);

    @Export
    public abstract String getCode();

    @Export
    public abstract void setEnabled(boolean enabled);

    @Export
    public abstract boolean isEnabled();

    @Export
    public abstract void setHidden(boolean hidden);

    @Export
    public abstract void isHidden();

    @Export
    public abstract void setStyleName(String styleName);

    @ExportInstanceMethod
    public static ComponentBuilder getParent(FramedLoginPanelBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract void focus();
}
