package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TabItemBuilder;

/**
 * Представляет вкладку панели вкладок {@link TabPanelBuilderOverlay TabPanelBuilder}
 */
@Export("TabItem")
@ExportPackage("Whirl")
public abstract class TabItemBuilderOverlay implements
        ExportOverlay<TabItemBuilder> {

    @ExportInstanceMethod
    public static TabItemBuilder create(TabItemBuilder instance) {
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
    public static void setChild(TabItemBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    @ExportInstanceMethod
    public static void clear(TabItemBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(TabItemBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @Export
    public abstract void setTitle(String title);

    @ExportInstanceMethod
    public static ComponentBuilder getParent(TabItemBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    /**
     * Возвращает  true, если вкладка в данный момент активна, false - если неактивна
     *
     * @return
     */
    @Export
    public abstract boolean isActive();

    @Export
    public abstract void focus();

}
