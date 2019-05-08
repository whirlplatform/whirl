package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.LabelBuilder;

/**
 * Текстовая строка вывода, надпись
 */
public abstract class LabelBuilderOverlay {

    /**
     * Инициализация LabelBuilder
     *
     * @param instance - LabelBuilder
     * @return LabelBuilder
     */
    public static LabelBuilder create(LabelBuilder instance) {
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
     * Установка кода тексту
     *
     * @param name - Strinng, код
     */
    public abstract void setCode(String name);

    /**
     * Получение кода у текста
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка активности текста
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности текста
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка скрытости текста
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получение инфформации об скрытости текста
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить стиль тексту
     *
     * @param styleName - String
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установка текста
     *
     * @param value - Stirng, текст
     */
    public abstract void setHtml(String value);

    /**
     * Получение текста
     *
     * @return String
     */
    public abstract String getHtml();

    /**
     * Получение родительского компонента
     *
     * @param instance - LabelBuilder
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(LabelBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на надпись
     */
    public abstract void focus();

}
