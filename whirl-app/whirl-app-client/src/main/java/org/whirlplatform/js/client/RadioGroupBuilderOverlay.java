package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.RadioGroupBuilder;

/**
 * Радиогруппа
 */
@Export("RadioGroup")
@ExportPackage("Whirl")
public abstract class RadioGroupBuilderOverlay implements
        ExportOverlay<RadioGroupBuilder> {

    /**
     * Инициализация радиогруппы
     *
     * @param instance - RadioGroupBuilder
     * @return RadioGroupBuilder
     */
    @ExportInstanceMethod
    public static RadioGroupBuilder create(RadioGroupBuilder instance) {
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
     * Установить код на радиогруппу
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код радиогруппы
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность радиогруппы
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности радиогруппы
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость радиогруппы
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости радиогруппы
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на радиогруппу
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Получение родителя радиогруппы
     *
     * @param instance - RadioGroupBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(RadioGroupBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на радиогруппу
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

//    @Export
//    public abstract RowListValue getFieldValue();
}
