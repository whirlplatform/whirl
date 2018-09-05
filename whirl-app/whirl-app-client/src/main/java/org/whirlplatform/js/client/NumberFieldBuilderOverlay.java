package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.NumberFieldBuilder;

/**
 * Числовое поле ввода
 */
@Export("NumberField")
@ExportPackage("Whirl")
public abstract class NumberFieldBuilderOverlay implements
        ExportOverlay<NumberFieldBuilder> {

    /**
     * Инициализация числового поля
     *
     * @param instance - NumberFieldBuilder
     * @return NumberFieldBuilder
     */
    @ExportInstanceMethod
    @Deprecated
    public static NumberFieldBuilder create(NumberFieldBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установить код на числовое поле
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код числового поля
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность числового поля
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности числового поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость числового поля
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости числового поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на числовое поле
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Проверка на null значения числового поля
     *
     * @param instance - NumberFieldBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(NumberFieldBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения числового поля
     *
     * @param instance - NumberFieldBuilder
     * @param value    - double
     */
    @ExportInstanceMethod
    public static void setValue(NumberFieldBuilder instance, double value) {
        instance.setValue(value);
    }

    /**
     * Получение значение числового поля
     *
     * @param instance - NumberFieldBuilder
     * @return double
     */
    @ExportInstanceMethod
    public static double getValue(NumberFieldBuilder instance) {
        if (instance.getValue() == null) {
            return 0;
        }
        return instance.getValue();
    }

//    @ExportInstanceMethod
//    public static DataValue getDataValue(NumberFieldBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(NumberFieldBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract String getText();

    /**
     * Очистка значения числового поля
     */
    @Export
    public abstract void clear();

    /**
     * Установка свойства "Обязателен для заполнения" для числового поля
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у числового
     * поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Установить формат числового поля
     *
     * @param format - String
     */
    @Export
    public abstract void setFormat(String format);

    /**
     * Установить маску числового поля
     *
     * @param mask - String
     */
    @Export
    public abstract void setMask(String mask);

    /**
     * Установить сообщение не валидности числового поля
     *
     * @param message - String
     */
    @Export
    public abstract void markInvalid(String message);

    /**
     * Очистка валидности числового поля
     */
    @Export
    public abstract void clearInvalid();

    /**
     * Получение родителя числового поля
     *
     * @param instance - NumberFieldBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(NumberFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на числовое поле
     */
    @Export
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);

    @ExportInstanceMethod
    public static void setToolTip(NumberFieldBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
