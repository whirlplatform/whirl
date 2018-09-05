package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.FieldSetBuilder;

@Export("FieldSet")
@ExportPackage("Whirl")
public abstract class FieldSetOverlay implements ExportOverlay<FieldSetBuilder> {

    @ExportInstanceMethod
    public static FieldSetBuilder create(FieldSetBuilder instance) {
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
    public abstract boolean isHidden();

    @Export
    public abstract void setStyleName(String styleName);

    @ExportInstanceMethod
    public static void setChild(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.addChild(builder);
    }

    @ExportInstanceMethod
    public static void clear(FieldSetBuilder instance) {
        instance.clearContainer();
    }

    @ExportInstanceMethod
    public static void remove(FieldSetBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(FieldSetBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

    @Export
    public abstract boolean isExpanded();

    @Export
    public abstract void setExpanded(boolean expand);

}
