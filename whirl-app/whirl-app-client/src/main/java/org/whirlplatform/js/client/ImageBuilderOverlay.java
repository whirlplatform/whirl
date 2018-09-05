package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ImageBuilder;

/**
 * Простая картинка
 */
@Export("Image")
@ExportPackage("Whirl")
public abstract class ImageBuilderOverlay implements
        ExportOverlay<ImageBuilder> {

    /**
     * Инициализация картинки
     *
     * @param instance - ImageBuilder, картинка
     * @return ImageBuilder, картинка
     */
    @ExportInstanceMethod
    public static ImageBuilder create(ImageBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Установка кода картинки
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получение кода картинки
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установка активности картинки
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности картинки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытности картинки
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации о скрытности картинки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установка стиля картинки
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Полчение родительского компонента картинки
     *
     * @param instance - ImageBuilder, картинка
     * @return ComponentBuilder, компонент
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(ImageBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на картинку
     */
    @Export
    public abstract void focus();

//    @ExportInstanceMethod
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
