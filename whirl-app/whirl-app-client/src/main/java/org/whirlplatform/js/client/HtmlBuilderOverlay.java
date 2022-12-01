package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HtmlBuilder;

/**
 * Компонент, отображающий разметку HTML
 */
public abstract class HtmlBuilderOverlay {

    /**
     * Инициализация HtmlBuilder
     *
     * @param instance - HtmlBuilder
     * @return HtmlBuilder
     */
    public static HtmlBuilder create(HtmlBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Полчение родительского компонента HtmlBuilder
     *
     * @param instance - HtmlBuilder
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(HtmlBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Возвращает идентификатор элемента в DOM документа.
     */
    public abstract String getDomId();

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    /**
     * Получение кода HtmlBuilder
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка кода на HtmlBuilder
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получение информации об активности HtmlBuilder
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка активности HtmlBuilder
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации о скрытности HtmlBuilder
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка скрытности HtmlBuilder
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установка стиля HtmlBuilder
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Получение контента из HtmlBuilder
     *
     * @return String
     */
    public abstract String getHTML();

    /**
     * Установка необходимого контента в HtmlBuilder
     *
     * @param html - String, контент
     */
    public abstract void setHTML(String html);

    /**
     * Установить фокус на HtmlBuilder
     */
    public abstract void focus();

}
