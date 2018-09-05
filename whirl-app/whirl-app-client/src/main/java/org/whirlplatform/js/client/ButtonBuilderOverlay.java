package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ButtonBuilder;

/**
 * Кнопка
 */
@Export("Button")
@ExportPackage("Whirl")
public abstract class ButtonBuilderOverlay implements
        ExportOverlay<ButtonBuilder> {

    /**
     * Инициализация кнопки
     *
     * @param instance - ButtonBuilder, кнопка
     * @return ButtonBuilder, кнопка
     */
    @ExportInstanceMethod
    public static ButtonBuilder create(ButtonBuilder instance) {
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

    /**
     * Установить код кнопки
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код кнопки
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность кнопки
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию об активности кнопки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытости кнопки
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации об скрытости кнопки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установка стиля кнопки
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Установка надписи на кнопке
     *
     * @param html - String, надпись
     */
    @Export
    public abstract void setHTML(String html);

    /**
     * Получение надписи кнопки
     *
     * @return String
     */
    @Export
    public abstract String getHTML();

    /**
     * Получение родительского компонента кнопки
     *
     * @param instance - ButtonBuilder, кнопка
     * @return ComponentBuilder, компонент
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(ButtonBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на кнопку
     */
    @Export
    public abstract void focus();

//    @ExportInstanceMethod
//    public static HandlerRegistrationWrapper addClickHandler(
//            final ButtonBuilder instance, final Events.EventClosure callback) {
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
