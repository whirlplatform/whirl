package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.RadioGroupBuilder;

/**
 * Радиогруппа
 */
public abstract class RadioGroupBuilderOverlay {

    /**
     * Инициализация радиогруппы
     *
     * @param instance - RadioGroupBuilder
     * @return RadioGroupBuilder
     */
    public static RadioGroupBuilder create(RadioGroupBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Получение родителя радиогруппы
     *
     * @param instance - RadioGroupBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(RadioGroupBuilder instance) {
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
     * Получить код радиогруппы
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить код на радиогруппу
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить информачию об активности радиогруппы
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить активность радиогруппы
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию о скрытости радиогруппы
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить скрытость радиогруппы
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установить стиль на радиогруппу
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установить фокус на радиогруппу
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

////    public abstract RowListValue getFieldValue();
}
