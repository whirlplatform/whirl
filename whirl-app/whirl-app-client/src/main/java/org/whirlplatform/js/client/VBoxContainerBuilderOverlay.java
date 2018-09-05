package org.whirlplatform.js.client;

import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.VBoxContainerBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Контейнер, позволяющий располагать элементы один под другим. В отличие от
 * VerticalContainerBuilder, вложенные компоненты занимают по высоте минимум
 * места.
 */
@Export("VBoxContainer")
@ExportPackage("Whirl")
public abstract class VBoxContainerBuilderOverlay implements ExportOverlay<VBoxContainerBuilder> {

    @ExportInstanceMethod
    public static VBoxContainerBuilder create(VBoxContainerBuilder instance) {
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
    public static void addChild(VBoxContainerBuilder instance, ComponentBuilder builder, BoxLayoutData data) {
        addChildByIndex(instance, instance.getChildrenCount(), builder, data);
    }

    @ExportInstanceMethod
    public static void addChildByIndex(VBoxContainerBuilder instance, int index, ComponentBuilder builder,
                                       BoxLayoutData data) {
        builder.setProperty(PropertyType.LayoutDataIndex.getCode(), new DataValueImpl(DataType.NUMBER, index));

        if (data != null) {
            builder.setProperty(PropertyType.LayoutDataMinSize.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMinSize()));
            builder.setProperty(PropertyType.LayoutDataMaxSize.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getMaxSize()));
            builder.setProperty(PropertyType.LayoutDataFlex.getCode(),
                    new DataValueImpl(DataType.NUMBER, data.getFlex()));
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
    public static void clear(VBoxContainerBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(VBoxContainerBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(VBoxContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

}
