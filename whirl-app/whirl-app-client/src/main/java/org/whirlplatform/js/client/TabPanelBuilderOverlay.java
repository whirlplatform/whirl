package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TabPanelBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Представляет панель вкладок-контейнеров.
 */
@Export("TabPanel")
@ExportPackage("Whirl")
public abstract class TabPanelBuilderOverlay implements
        ExportOverlay<TabPanelBuilder> {

    @ExportInstanceMethod
    public static TabPanelBuilder create(TabPanelBuilder instance) {
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
    public static void addChild(TabPanelBuilder instance,
                                ComponentBuilder builder) {
        addChild(instance, instance.getChildrenCount(), builder);
    }

    @ExportInstanceMethod
    public static void addChild(TabPanelBuilder instance, int index,
                                ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataIndex.getCode(),
                new DataValueImpl(DataType.NUMBER, index));
        instance.addChild(builder);
    }

    @ExportInstanceMethod
    public static void clear(TabPanelBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(TabPanelBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    /**
     * Получить список билдеров всех вкладок TabPanel
     *
     * @return
     */
    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(TabPanelBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

    /**
     * Получить билдер текущей открытой вкладки
     *
     * @return
     */
    @Export
    public abstract ComponentBuilder getActive();

    /**
     * Открыть другую вкладку
     *
     * @param child
     */
    @Export
    public abstract void setActive(ComponentBuilder child);

}
