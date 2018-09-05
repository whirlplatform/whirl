package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.HtmlBuilder;

/**
 * Компонент, отображающий разметку HTML
 */
@Export("Html")
@ExportPackage("Whirl")
public abstract class HtmlBuilderOverlay implements ExportOverlay<HtmlBuilder> {

    /**
     * Инициализация HtmlBuilder
     *
     * @param instance - HtmlBuilder
     * @return HtmlBuilder
     */
    @ExportInstanceMethod
    public static HtmlBuilder create(HtmlBuilder instance) {
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
     * Установка кода на HtmlBuilder
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получение кода HtmlBuilder
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установка активности HtmlBuilder
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности HtmlBuilder
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытности HtmlBuilder
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации о скрытности HtmlBuilder
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установка стиля HtmlBuilder
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Установка необходимого контента в HtmlBuilder
     *
     * @param html - String, контент
     */
    @Export
    public abstract void setHTML(String html);

    /**
     * Получение контента из HtmlBuilder
     *
     * @return String
     */
    @Export
    public abstract String getHTML();

    /**
     * Полчение родительского компонента HtmlBuilder
     *
     * @param instance - HtmlBuilder
     * @return ComponentBuilder, компонент
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(HtmlBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на HtmlBuilder
     */
    @Export
    public abstract void focus();

}
