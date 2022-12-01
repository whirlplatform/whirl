package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TextAreaBuilder;

/**
 * Текстовая область ввода
 */
public abstract class TextAreaBuilderOverlay {

    /**
     * Инициализация текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return TextAreaBuilder
     */
    @Deprecated
    public static TextAreaBuilder create(TextAreaBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Проверка на null значение текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return boolean
     */
    public static boolean isEmpty(TextAreaBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Получение родителя текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(TextAreaBuilder instance) {
        return instance.getParentBuilder();
    }

    public static void setToolTip(TextAreaBuilder instance, String toolTip) {
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
     * Получить код текстовой области
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить код на текстовую область
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить информачию об активности текстовой области
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить активность текстовой области
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию о скрытости текстовой области
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить скрытость текстовой области
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Установить стиль на текстовую область
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

////    public static DataValue getDataValue(TextAreaBuilder instance) {
//        return instance.getFieldValue();
//    }
//
////    public static void setDataValue(TextAreaBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Получение значения текстовой области
     *
     * @return String
     */
    public abstract String getValue();

    /**
     * Установка значения текстовой области
     *
     * @param value - String
     */
    public abstract void setValue(String value);

    public abstract String getText();

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у текстовой области
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Установка свойства "Обязателен для заполнения" для текстовой области
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Очистка значения текстовой области
     */
    public abstract void clear();

    /**
     * Установить фокус на текстовую область
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

    public abstract void markInvalid(String message);

    public abstract void clearInvalid();
}
