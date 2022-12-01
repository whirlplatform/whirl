package org.whirlplatform.js.client;

import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HorizontalContainerBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Контейнер, позволяющий располагать элементы один рядом с другим. В отличие от
 * HBoxContainerBuilder, вложенные компоненты стараются занять всю ширину контейнера
 */
public abstract class HorizontalContainerBuilderOverlay {

    public static HorizontalContainerBuilder create(
            HorizontalContainerBuilder instance) {
        instance.create();
        return instance;
    }

    public static void addChild(HorizontalContainerBuilder instance,
                                ComponentBuilder builder, HorizontalLayoutData data) {
        addChildByIndex(instance, instance.getChildrenCount(), builder, data);
    }

    public static void addChildByIndex(HorizontalContainerBuilder instance,
                                       int index, ComponentBuilder builder,
                                       HorizontalLayoutData data) {
        builder.setProperty(PropertyType.LayoutDataIndex.getCode(),
                new DataValueImpl(DataType.NUMBER, index));
        if (data != null) {
            builder.setProperty(PropertyType.LayoutDataWidth.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getWidth()));
            builder.setProperty(PropertyType.LayoutDataHeight.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getHeight()));
            builder.setProperty(PropertyType.LayoutDataMarginTop.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMargins().getTop()));
            builder.setProperty(PropertyType.LayoutDataMarginRight.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMargins().getRight()));
            builder.setProperty(PropertyType.LayoutDataMarginBottom.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMargins().getBottom()));
            builder.setProperty(PropertyType.LayoutDataMarginLeft.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMargins().getLeft()));
        }
        instance.addChild(builder);
    }

    public static void clear(HorizontalContainerBuilder instance) {
        instance.clearContainer();
    }

    public static void remove(HorizontalContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public static ComponentBuilder getParent(HorizontalContainerBuilder instance) {
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

    public abstract int getChildrenCount();

    public abstract void focus();

    public abstract void mask();

    public abstract void mask(String message);

    public abstract void unmask();
}
