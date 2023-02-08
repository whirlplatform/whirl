package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TabPanelBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Представляет панель вкладок-контейнеров.
 */
public abstract class TabPanelBuilderOverlay {

    public static TabPanelBuilder create(TabPanelBuilder instance) {
        instance.create();
        return instance;
    }

    public static void addChild(TabPanelBuilder instance,
                                ComponentBuilder builder) {
        addChild(instance, instance.getChildrenCount(), builder);
    }

    public static void addChild(TabPanelBuilder instance, int index,
                                ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataIndex.getCode(),
            new DataValueImpl(DataType.NUMBER, index));
        instance.addChild(builder);
    }

    public static void clear(TabPanelBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(TabPanelBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public static ComponentBuilder getParent(TabPanelBuilder instance) {
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

    /**
     * Получить список билдеров всех вкладок TabPanel
     *
     * @return
     */
    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public abstract int getChildrenCount();

    public abstract void focus();

    /**
     * Получить билдер текущей открытой вкладки
     *
     * @return
     */
    public abstract ComponentBuilder getActive();

    /**
     * Открыть другую вкладку
     *
     * @param child
     */
    public abstract void setActive(ComponentBuilder child);

}
