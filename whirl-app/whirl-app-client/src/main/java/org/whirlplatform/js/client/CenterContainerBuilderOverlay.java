package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.CenterContainerBuilder;

@Export("CenterContainer")
@ExportPackage("Whirl")
public abstract class CenterContainerBuilderOverlay implements
        ExportOverlay<CenterContainerBuilder> {

    @ExportInstanceMethod
    public static CenterContainerBuilder create(CenterContainerBuilder instance) {
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
    public static void setChild(CenterContainerBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Метод удаляет все вложенные компоненты контейнера
     */
    @ExportInstanceMethod
    public static void clear(CenterContainerBuilder instance) {
        instance.clearContainer();
    }


    /**
     * Удалить определённый вложенный компонент
     *
     * @param builder Билдер компонента, подлежащего удалению из контейнера
     */
    @ExportInstanceMethod
    public static void remove(CenterContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    /**
     * Перерисовать контейнер и вложенные компоненты.
     */
    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(CenterContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract void focus();
}
