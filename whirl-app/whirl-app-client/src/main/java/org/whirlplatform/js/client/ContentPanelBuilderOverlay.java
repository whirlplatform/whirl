package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ContentPanelBuilder;

public abstract class ContentPanelBuilderOverlay {

    public static ContentPanelBuilder create(ContentPanelBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    public abstract String getDomId();

    public abstract void setCode(String name);

    public abstract String getCode();

    public abstract void setEnabled(boolean enabled);

    public abstract boolean isEnabled();

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    /**
     * Устанавливает значение DOM-атрибута компонента class = "styleName".
     * Пример: setStyleName("wide-container-class no-bordered") -> <div ... class="wide-container-class no-bordered" ... >...</div>
     *
     * @param styleName
     */
    public abstract void setStyleName(String styleName);

    /**
     * Помещает дочерний компонент в контейнер. Вновь добавленный элемент будет заменять существующий, если такой уже есть.
     *
     * @param builder билдер компонента
     */
    public static void setChild(ContentPanelBuilder instance,
                                ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Удаляет компонент, находящийся в контейнере
     */
    public static void clear(ContentPanelBuilder instance) {
        instance.clearContainer();
    }

    /**
     * Удаляет компонент контейнера с заданным builder
     *
     * @param builder
     */
    public static void remove(ContentPanelBuilder instance,
                              ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public static ComponentBuilder getParent(ContentPanelBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * ContentPanelBuilder может иметь не более одного вложенного компонента. Так что эта функция вернёт 0 или 1.
     *
     * @return
     */
    public abstract int getChildrenCount();

    public abstract void focus();

}
