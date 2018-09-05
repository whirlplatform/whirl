package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TextFieldBuilder;

/**
 * Поле ввода - текстовое
 */
@Export("TextField")
@ExportPackage("Whirl")
public abstract class TextFieldBuilderOverlay implements
        ExportOverlay<TextFieldBuilder> {

    /**
     * Инициализация текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return TextFieldBuilder
     */
    @ExportInstanceMethod
    @Deprecated
    public static TextFieldBuilder create(TextFieldBuilder instance) {
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
     * Установить код на текстовое поле
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код текстового поля
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность текстового поля
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности текстового поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость текстового поля
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости текстового поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на текстовое поле
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Проверка на null значения текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(TextFieldBuilder instance) {
        return instance.getValue() == null || instance.getValue().isEmpty();
    }

    /**
     * Установка значения текстового поля
     *
     * @param value - String
     */
    @Export
    public abstract void setValue(String value);

    /**
     * Получить значение текстового поля
     *
     * @return String
     */
    @Export
    public abstract String getValue();

//    @ExportInstanceMethod
//    public static DataValue getDataValue(TextFieldBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(TextFieldBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract String getText();

    /**
     * Установка свойства "Обязателен для заполнения" для текстового поля
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у текстового
     * поля
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Очистка значения текстового поля
     */
    @Export
    public abstract void clear();

    /**
     * Получение родителя текстового поля
     *
     * @param instance - TextFieldBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(TextFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на текстовое поле
     */
    @Export
    public abstract void focus();

    /**
     * Пометить поле как невалидное
     *
     * @param message - текст сообщения
     */
    @Export
    public abstract void markInvalid(String message);

    /**
     * Убрать невалидность поля
     */
    @Export
    public abstract void clearInvalid();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);

    @ExportInstanceMethod
    public static void setToolTip(TextFieldBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
