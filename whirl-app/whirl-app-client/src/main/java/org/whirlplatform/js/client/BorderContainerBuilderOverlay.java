package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.BorderContainerBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;


/**
 * Контейнер может содержать вложенные контейнеры, выровненные по его границам
 * (северный-NORTH, южный-SOUTH, западный-WEST, восточный-EAST)
 *
 * @author slusarenko_ig
 */
@Export("BorderContainer")
@ExportPackage("Whirl")
public abstract class BorderContainerBuilderOverlay implements
        ExportOverlay<BorderContainerBuilder> {

    @ExportInstanceMethod
    public static BorderContainerBuilder create(BorderContainerBuilder instance) {
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


    /**
     * Управляет доступностью контейнера и вложенных компонентов для действий пользователя.
     * В случае enabled = false компоненты остаются видимыми, но недоступными для действий пользователя
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    @Export
    public abstract boolean isEnabled();

    /**
     * Метод позволяет управлять видимостью компонента
     * Скрытые компоненты не занимают места в DOM-дереве браузера
     *
     * @param hidden Скрыть компонент (hidden = true), либо показать(hidden = false)
     */
    @Export
    public abstract void setHidden(boolean hidden);

    @Export
    public abstract boolean isHidden();

    /**
     * Устанавливает значение DOM-атрибута компонента class = "styleName".
     * Пример: setStyleName("wide-container-class no-bordered") -> <div ... class="wide-container-class no-bordered" ... >...</div>
     *
     * @param styleName
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Устанавливает компонент(builder) в западную(слева) часть контейнера
     *
     * @param builder билдер компонента, который помещаем в западную часть BorderContainer
     */
    @ExportInstanceMethod
    public static void setChildWest(BorderContainerBuilder instance,
                                    ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataLocation.getCode(), new DataValueImpl(DataType.STRING, "West"));
        instance.addChild(builder);
    }


    /**
     * Устанавливает компонент(builder) в восточную(справа) часть контейнера
     *
     * @param builder билдер компонента, который помещаем в восточную часть BorderContainer
     */
    @ExportInstanceMethod
    public static void setChildEast(BorderContainerBuilder instance,
                                    ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataLocation.getCode(), new DataValueImpl(DataType.STRING, "East"));
        instance.addChild(builder);
    }

    /**
     * Устанавливает компонент(builder) в северную(верхнюю) часть контейнера
     *
     * @param builder билдер компонента, который помещаем в северную часть BorderContainer
     */
    @ExportInstanceMethod
    public static void setChildNorth(BorderContainerBuilder instance,
                                     ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataLocation.getCode(), new DataValueImpl(DataType.STRING, "North"));
        instance.addChild(builder);
    }

    /**
     * Устанавливает компонент(builder) в южную(нижнюю) часть контейнера
     *
     * @param builder билдер компонента, который помещаем в южную часть BorderContainer
     */
    @ExportInstanceMethod
    public static void setChildSouth(BorderContainerBuilder instance,
                                     ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataLocation.getCode(), new DataValueImpl(DataType.STRING, "South"));
        instance.addChild(builder);
    }

    /**
     * Удалить вложенный компонент(builder) из контейнера
     *
     * @param builder удаляемый компонент
     */
    @ExportInstanceMethod
    public static void remove(BorderContainerBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    /**
     * Удаляет все вложенные компоненты из контейнера.
     */
    @ExportInstanceMethod
    public static void clear(BorderContainerBuilder instance) {
        instance.clearContainer();
    }

    @Export
    public abstract ComponentBuilder[] getChildren();


    /**
     * Выполняет принудительную перерисовку контейнера и вложенных компонентов.
     */
    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(BorderContainerBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

}
