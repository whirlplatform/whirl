package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.LabelBuilder;

/**
 * Текстовая строка вывода, надпись
 */
@Export("Label")
@ExportPackage("Whirl")
public abstract class LabelBuilderOverlay implements
        ExportOverlay<LabelBuilder> {

    /**
     * Инициализация LabelBuilder
     *
     * @param instance - LabelBuilder
     * @return LabelBuilder
     */
    @ExportInstanceMethod
    public static LabelBuilder create(LabelBuilder instance) {
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
     * Установка кода тексту
     *
     * @param name - Strinng, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получение кода у текста
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установка активности текста
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности текста
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытости текста
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение инфформации об скрытости текста
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль тексту
     *
     * @param styleName - String
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Установка текста
     *
     * @param value - Stirng, текст
     */
    @Export
    public abstract void setHtml(String value);

    /**
     * Получение текста
     *
     * @return String
     */
    @Export
    public abstract String getHtml();

    /**
     * Получение родительского компонента
     *
     * @param instance - LabelBuilder
     * @return ComponentBuilder, компонент
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(LabelBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на надпись
     */
    @Export
    public abstract void focus();

}
