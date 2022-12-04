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
     * Получение родительского компонента
     *
     * @param instance - LabelBuilder
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(LabelBuilder instance) {
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
     * Получение кода у текста
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка кода тексту
     *
     * @param name - Strinng, код
     */
    public abstract void setCode(String name);

    /**
     * Получение информации об активности текста
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка активности текста
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение инфформации об скрытости текста
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка скрытости текста
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установить стиль тексту
     *
     * @param styleName - String
     */
    public abstract void setStyleName(String styleName);

    /**
     * Получение текста
     *
     * @return String
     */
    public abstract String getHtml();

    /**
     * Установка текста
     *
     * @param value - Stirng, текст
     */
    public abstract void setHtml(String value);

    /**
     * Установить фокус на надпись
     */
    public abstract void focus();

}
