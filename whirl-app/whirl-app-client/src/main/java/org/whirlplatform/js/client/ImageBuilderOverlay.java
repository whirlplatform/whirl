package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ImageBuilder;

/**
 * Простая картинка
 */
public abstract class ImageBuilderOverlay {

    /**
     * Инициализация картинки
     *
     * @param instance - ImageBuilder, картинка
     * @return ImageBuilder, картинка
     */
    public static ImageBuilder create(ImageBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Полчение родительского компонента картинки
     *
     * @param instance - ImageBuilder, картинка
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(ImageBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Получение кода картинки
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка кода картинки
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получение информации об активности картинки
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка активности картинки
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации о скрытности картинки
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка скрытности картинки
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установка стиля картинки
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установить фокус на картинку
     */
    public abstract void focus();

    //    public static HandlerRegistrationWrapper addClickHandler(
    //            final ImageBuilder instance, final Events.EventClosure callback) {
    //        return new HandlerRegistrationWrapper(
    //                instance.addClickHandler(new ClickEvent.ClickHandler() {
    //                    @Override
    //                    public void onClick(ClickEvent event) {
    //                        EventCallbackResult result = new EventCallbackResult();
    //                        result.setSource(instance);
    //                        callback.success(result);
    //                    }
    //                }));
    //    }

}
