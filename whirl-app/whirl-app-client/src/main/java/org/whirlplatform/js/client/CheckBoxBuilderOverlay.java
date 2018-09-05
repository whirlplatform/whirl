package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.CheckBoxBuilder;

/**
 * Чек-бокс
 */
@Export("CheckBox")
@ExportPackage("Whirl")
public abstract class CheckBoxBuilderOverlay implements
        ExportOverlay<CheckBoxBuilder> {

    /**
     * Инициализация CheckBox
     *
     * @param instance - CheckBoxBuilder
     * @return CheckBoxBuilder
     */
    @ExportInstanceMethod
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
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установка кода CheckBox
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получение кода CheckBox
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установка активности CheckBox
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности CheckBox
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытности CheckBox
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации о скрытности CheckBox
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установка стиля CheckBox
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Установить надпись для CheckBox, которая будет отображена рядом с ним
     *
     * @param label - String, отображаемый текст
     */
    @Export
    public abstract void setBoxLabel(String label);

    /**
     * Получить значение подписи CheckBox
     *
     * @return String
     */
    @Export
    public abstract String getBoxLabel();

    /**
     * Проверка на null значения CheckBox
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(CheckBoxBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения для CheckBox
     *
     * @param instance - RadioBuilder
     * @param value    - boolean
     */
    @ExportInstanceMethod
    public static void setValue(CheckBoxBuilder instance, boolean value) {
        instance.setValue(value);
    }

    /**
     * Получение значения CheckBox
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean getValue(CheckBoxBuilder instance) {
        return instance.getValue().booleanValue();
    }

    /**
     * Установка свойства "Обязателен для заполнения" для CheckBox
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у CheckBox
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Очистка значения CheckBox
     */
    @Export
    public abstract void clear();

    /**
     * Получение родительского компонента CheckBox
     *
     * @param instance - CheckBoxBuilder
     * @return ComponentBuilder, компонент
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(CheckBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на CheckBox
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
    public abstract void markInvalid(String msg);

    @Export
    public abstract void clearInvalid();
}
