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
     * Установить код на текстовую область
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить код текстовой области
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить активность текстовой области
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности текстовой области
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить скрытость текстовой области
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости текстовой области
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить стиль на текстовую область
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

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
     * Установка значения текстовой области
     *
     * @param value - String
     */
    public abstract void setValue(String value);

    /**
     * Получение значения текстовой области
     *
     * @return String
     */
    public abstract String getValue();

////    public static DataValue getDataValue(TextAreaBuilder instance) {
//        return instance.getFieldValue();
//    }
//
////    public static void setDataValue(TextAreaBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    public abstract String getText();

    /**
     * Установка свойства "Обязателен для заполнения" для текстовой области
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у текстовой
     * области
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Очистка значения текстовой области
     */
    public abstract void clear();

    /**
     * Получение родителя текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(TextAreaBuilder instance) {
        return instance.getParentBuilder();
    }

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

    public static void setToolTip(TextAreaBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
