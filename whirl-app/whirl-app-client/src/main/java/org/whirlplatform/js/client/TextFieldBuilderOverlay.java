package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TextFieldBuilder;

/**
 * Поле ввода - текстовое
 */
public abstract class TextFieldBuilderOverlay {

    /**
     * Инициализация текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return TextFieldBuilder
     */
    @Deprecated
    public static TextFieldBuilder create(TextFieldBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Проверка на null значения текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return boolean
     */
    public static boolean isEmpty(TextFieldBuilder instance) {
        return instance.getValue() == null || instance.getValue().isEmpty();
    }

    /**
     * Получение родителя текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(TextFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    public static void setToolTip(TextFieldBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
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
     * Получить код текстового поля
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить код на текстовое поле
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить информачию об активности текстового поля
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить активность текстового поля
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию о скрытости текстового поля
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить скрытость текстового поля
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установить стиль на текстовое поле
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    //    public static DataValue getDataValue(TextFieldBuilder instance) {
    //        return instance.getFieldValue();
    //    }
    //
    //    public static void setDataValue(TextFieldBuilder instance, DataValue value) {
    //        instance.setFieldValue(value);
    //    }

    /**
     * Получить значение текстового поля
     *
     * @return String
     */
    public abstract String getValue();

    /**
     * Установка значения текстового поля
     *
     * @param value - String
     */
    public abstract void setValue(String value);

    public abstract String getText();

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у текстового поля
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Установка свойства "Обязателен для заполнения" для текстового поля
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Очистка значения текстового поля
     */
    public abstract void clear();

    /**
     * Установить фокус на текстовое поле
     */
    public abstract void focus();

    /**
     * Пометить поле как невалидное
     *
     * @param message - текст сообщения
     */
    public abstract void markInvalid(String message);

    /**
     * Убрать невалидность поля
     */
    public abstract void clearInvalid();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);
}
