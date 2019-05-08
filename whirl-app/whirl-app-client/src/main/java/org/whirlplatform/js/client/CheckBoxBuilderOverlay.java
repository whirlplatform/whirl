package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.CheckBoxBuilder;

/**
 * Чек-бокс
 */
public abstract class CheckBoxBuilderOverlay {

    /**
     * Инициализация CheckBox
     *
     * @param instance - CheckBoxBuilder
     * @return CheckBoxBuilder
     */
    @Deprecated
    public static CheckBoxBuilder create(CheckBoxBuilder instance) {
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
     * Установка кода CheckBox
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получение кода CheckBox
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка активности CheckBox
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности CheckBox
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка скрытности CheckBox
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации о скрытности CheckBox
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка стиля CheckBox
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установить надпись для CheckBox, которая будет отображена рядом с ним
     *
     * @param label - String, отображаемый текст
     */
    public abstract void setBoxLabel(String label);

    /**
     * Получить значение подписи CheckBox
     *
     * @return String
     */
    public abstract String getBoxLabel();

    /**
     * Проверка на null значения CheckBox
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    public static boolean isEmpty(CheckBoxBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения для CheckBox
     *
     * @param instance - RadioBuilder
     * @param value    - boolean
     */
    public static void setValue(CheckBoxBuilder instance, boolean value) {
        instance.setValue(value);
    }

    /**
     * Получение значения CheckBox
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    public static boolean getValue(CheckBoxBuilder instance) {
        return instance.getValue().booleanValue();
    }

    /**
     * Установка свойства "Обязателен для заполнения" для CheckBox
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у CheckBox
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Очистка значения CheckBox
     */
    public abstract void clear();

    /**
     * Получение родительского компонента CheckBox
     *
     * @param instance - CheckBoxBuilder
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(CheckBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на CheckBox
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

    public abstract void markInvalid(String msg);

    public abstract void clearInvalid();
}
