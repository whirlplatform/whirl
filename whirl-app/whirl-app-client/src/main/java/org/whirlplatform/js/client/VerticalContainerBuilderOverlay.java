package org.whirlplatform.js.client;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.VerticalContainerBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Контейнер, позволяющий располагать элементы один под другим.
 * В отличие от VBoxContainerBuilder, вложенные компоненты стараются занять максимум места по высоте
 */
@Export("VerticalContainer")
@ExportPackage("Whirl")
public abstract class VerticalContainerBuilderOverlay implements
        ExportOverlay<VerticalContainerBuilder> {

    @ExportInstanceMethod
    public static VerticalContainerBuilder create(
            VerticalContainerBuilder instance) {
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
    public static void addChild(VerticalContainerBuilder instance,
                                ComponentBuilder builder, VerticalLayoutData data) {
        addChildByIndex(instance, instance.getChildrenCount(), builder, data);
    }

    @ExportInstanceMethod
    public static void addChildByIndex(VerticalContainerBuilder instance,
                                       int index, ComponentBuilder builder, VerticalLayoutData data) {
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

    @ExportInstanceMethod
    public static void clear(VerticalContainerBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(VerticalContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(VerticalContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

    @Export
    public abstract void mask();

    @Export
    public abstract void mask(String message);

    @Export
    public abstract void unmask();
}
