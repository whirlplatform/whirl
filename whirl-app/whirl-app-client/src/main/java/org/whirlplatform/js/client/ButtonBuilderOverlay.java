package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ButtonBuilder;

/**
 * Кнопка
 */
public abstract class ButtonBuilderOverlay {

    /**
     * Инициализация кнопки
     *
     * @param instance - ButtonBuilder, кнопка
     * @return ButtonBuilder, кнопка
     */
    public static ButtonBuilder create(ButtonBuilder instance) {
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

    /**
     * Установить код кнопки
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить код кнопки
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить активность кнопки
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию об активности кнопки
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка скрытости кнопки
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации об скрытости кнопки
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка стиля кнопки
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установка надписи на кнопке
     *
     * @param html - String, надпись
     */
    public abstract void setHTML(String html);

    /**
     * Получение надписи кнопки
     *
     * @return String
     */
    public abstract String getHTML();

    /**
     * Получение родительского компонента кнопки
     *
     * @param instance - ButtonBuilder, кнопка
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(ButtonBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на кнопку
     */
    public abstract void focus();

////    public static HandlerRegistrationWrapper addClickHandler(
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
