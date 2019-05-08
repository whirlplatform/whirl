package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.RadioBuilder;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Радиокнопка
 */
public abstract class RadioBuilderOverlay {

    /**
     * Инициализация радиокнопки
     *
     * @param instance - RadioBuilder
     * @return RadioBuilder
     */
    @Deprecated
    public static RadioBuilder create(RadioBuilder instance) {
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
     * Установить код на радиокнопку
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить код радиокнопки
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить активность радиокноки
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности радиокнопки
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить скрытость радиокнопки
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости радиокнопки
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить стиль на радиокнопку
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Установить подпись для радиокнопки
     *
     * @param label - String
     */
    public abstract void setBoxLabel(String label);

    /**
     * Получить значение подписи радиокнопки
     *
     * @return String
     */
    public abstract String getBoxLabel();

    /**
     * Установить имя радиогруппы
     *
     * @param groupName - String
     */
    public abstract void setGroupName(String groupName);

    /**
     * Получить имя радиогруппы
     *
     * @return String
     */
    public abstract String getGroupName();

    /**
     * Проверка на null значения радиокнопки
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    public static boolean isEmpty(RadioBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения для радиокнопки
     *
     * @param instance - RadioBuilder
     * @param value    - boolean
     */
    public static void setValue(RadioBuilder instance, boolean value) {
        instance.setValue(value);
    }

    /**
     * Получение значения радиокнопки
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    public static boolean getValue(RadioBuilder instance) {
        if (instance.getValue() == null) {
            return false;
        }
        return instance.getValue().booleanValue();
    }

    public static DataValue getDataValue(RadioBuilder instance) {
        return instance.getFieldValue();
    }

    public static void setDataValue(RadioBuilder instance, DataValue value) {
        instance.setFieldValue(value);
    }

    /**
     * Установка свойства "Обязателен для заполнения" для радиокнопки
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у радиокнопки
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Очистка значения радиокнопки
     */
    public abstract void clear();

    /**
     * Получение родителя радиокнопки
     *
     * @param instance - RadioBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(RadioBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на радиокнопку
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
