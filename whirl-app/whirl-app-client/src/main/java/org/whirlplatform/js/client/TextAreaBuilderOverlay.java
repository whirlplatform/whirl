package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.TextAreaBuilder;

/**
 * Текстовая область ввода
 */
@Export("TextArea")
@ExportPackage("Whirl")
public abstract class TextAreaBuilderOverlay implements
        ExportOverlay<TextAreaBuilder> {

    /**
     * Инициализация текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return TextAreaBuilder
     */
    @ExportInstanceMethod
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
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установить код на текстовую область
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код текстовой области
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность текстовой области
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности текстовой области
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость текстовой области
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости текстовой области
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на текстовую область
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Проверка на null значение текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(TextAreaBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения текстовой области
     *
     * @param value - String
     */
    @Export
    public abstract void setValue(String value);

    /**
     * Получение значения текстовой области
     *
     * @return String
     */
    @Export
    public abstract String getValue();

//    @ExportInstanceMethod
//    public static DataValue getDataValue(TextAreaBuilder instance) {
//        return instance.getFieldValue();
//    }
//
//    @ExportInstanceMethod
//    public static void setDataValue(TextAreaBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    @Export
    public abstract String getText();

    /**
     * Установка свойства "Обязателен для заполнения" для текстовой области
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у текстовой
     * области
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Очистка значения текстовой области
     */
    @Export
    public abstract void clear();

    /**
     * Получение родителя текстовой области
     *
     * @param instance - TextAreaBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(TextAreaBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на текстовую область
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

    @Export
    public abstract void markInvalid(String message);

    @Export
    public abstract void clearInvalid();

    @ExportInstanceMethod
    public static void setToolTip(TextAreaBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
