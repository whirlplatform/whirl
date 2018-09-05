package org.whirlplatform.js.client;

import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HorizontalContainerBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Контейнер, позволяющий располагать элементы один рядом с другим.
 * В отличие от HBoxContainerBuilder, вложенные компоненты стараются занять всю ширину контейнера
 */
@Export("HorizontalContainer")
@ExportPackage("Whirl")
public abstract class HorizontalContainerBuilderOverlay implements
        ExportOverlay<HorizontalContainerBuilder> {

    @ExportInstanceMethod
    public static HorizontalContainerBuilder create(
            HorizontalContainerBuilder instance) {
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
    public static void addChild(HorizontalContainerBuilder instance,
                                ComponentBuilder builder, HorizontalLayoutData data) {
        addChildByIndex(instance, instance.getChildrenCount(), builder, data);
    }

    @ExportInstanceMethod
    public static void addChildByIndex(HorizontalContainerBuilder instance,
                                       int index, ComponentBuilder builder, HorizontalLayoutData data) {
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
    public static void clear(HorizontalContainerBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(HorizontalContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(HorizontalContainerBuilder instance) {
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
