package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ContentPanelBuilder;


@Export("ContentPanel")
@ExportPackage("Whirl")
public abstract class ContentPanelBuilderOverlay implements
        ExportOverlay<ContentPanelBuilder> {

    @ExportInstanceMethod
    public static ContentPanelBuilder create(ContentPanelBuilder instance) {
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

    /**
     * Устанавливает значение DOM-атрибута компонента class = "styleName".
     * Пример: setStyleName("wide-container-class no-bordered") -> <div ... class="wide-container-class no-bordered" ... >...</div>
     *
     * @param styleName
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Помещает дочерний компонент в контейнер. Вновь добавленный элемент будет заменять существующий, если такой уже есть.
     *
     * @param builder билдер компонента
     */
    @ExportInstanceMethod
    public static void setChild(ContentPanelBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Удаляет компонент, находящийся в контейнере
     */
    @ExportInstanceMethod
    public static void clear(ContentPanelBuilder instance) {
        instance.clearContainer();
    }

    /**
     * Удаляет компонент контейнера с заданным builder
     *
     * @param builder
     */
    @ExportInstanceMethod
    public static void remove(ContentPanelBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(ContentPanelBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * ContentPanelBuilder может иметь не более одного вложенного компонента. Так что эта функция вернёт 0 или 1.
     *
     * @return
     */
    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

}
