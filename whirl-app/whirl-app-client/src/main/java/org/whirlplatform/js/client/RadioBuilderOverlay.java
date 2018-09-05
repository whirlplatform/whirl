package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.RadioBuilder;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Радиокнопка
 */
@Export("Radio")
@ExportPackage("Whirl")
public abstract class RadioBuilderOverlay implements
        ExportOverlay<RadioBuilder> {

    /**
     * Инициализация радиокнопки
     *
     * @param instance - RadioBuilder
     * @return RadioBuilder
     */
    @ExportInstanceMethod
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
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установить код на радиокнопку
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код радиокнопки
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность радиокноки
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности радиокнопки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость радиокнопки
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости радиокнопки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на радиокнопку
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Установить подпись для радиокнопки
     *
     * @param label - String
     */
    @Export
    public abstract void setBoxLabel(String label);

    /**
     * Получить значение подписи радиокнопки
     *
     * @return String
     */
    @Export
    public abstract String getBoxLabel();

    /**
     * Установить имя радиогруппы
     *
     * @param groupName - String
     */
    @Export
    public abstract void setGroupName(String groupName);

    /**
     * Получить имя радиогруппы
     *
     * @return String
     */
    @Export
    public abstract String getGroupName();

    /**
     * Проверка на null значения радиокнопки
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(RadioBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения для радиокнопки
     *
     * @param instance - RadioBuilder
     * @param value    - boolean
     */
    @ExportInstanceMethod
    public static void setValue(RadioBuilder instance, boolean value) {
        instance.setValue(value);
    }

    /**
     * Получение значения радиокнопки
     *
     * @param instance - RadioBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean getValue(RadioBuilder instance) {
        if (instance.getValue() == null) {
            return false;
        }
        return instance.getValue().booleanValue();
    }

    @ExportInstanceMethod
    public static DataValue getDataValue(RadioBuilder instance) {
        return instance.getFieldValue();
    }

    @ExportInstanceMethod
    public static void setDataValue(RadioBuilder instance, DataValue value) {
        instance.setFieldValue(value);
    }

    /**
     * Установка свойства "Обязателен для заполнения" для радиокнопки
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у радиокнопки
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Очистка значения радиокнопки
     */
    @Export
    public abstract void clear();

    /**
     * Получение родителя радиокнопки
     *
     * @param instance - RadioBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(RadioBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на радиокнопку
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
