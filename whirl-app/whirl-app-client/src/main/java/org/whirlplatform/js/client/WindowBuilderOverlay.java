package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.window.WindowBuilder;

/**
 * Окно вывода
 */
@Export("Window")
@ExportPackage("Whirl")
public abstract class WindowBuilderOverlay implements
        ExportOverlay<WindowBuilder> {

    /**
     * Инициализация окна
     *
     * @param instance - WindowBuilder
     * @return WindowBuilder
     */
    @ExportInstanceMethod
    public static WindowBuilder create(WindowBuilder instance) {
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
     * Установить код на окно
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код окна
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность окна
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности окна
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость окна
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости окна
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на окно
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Скрыть окно
     */
    @Export
    public abstract void hide();

    /**
     * Отобразить окно
     */
    @Export
    public abstract void show();

    /**
     * Максимизировать окно
     */
    @Export
    public abstract void maximize();

    /**
     * Минимизировать окно
     */
    @Export
    public abstract void minimize();

    @Export
    public abstract void center();

    /**
     * Установить позицию окна в страннице
     *
     * @param left - int
     * @param top  - int
     */
    @Export
    public abstract void setPosition(int left, int top);

    /**
     * Установить позицию окна на экране
     *
     * @param x - int
     * @param y - int
     */
    @Export
    public abstract void setPagePosition(int x, int y);

    /**
     * Установить потомка для окна
     *
     * @param instance - WindowBuilder
     * @param builder  - ComponentBuilder
     */
    @ExportInstanceMethod
    public static void setChild(WindowBuilder instance, ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Удаление окна
     *
     * @param instance - WindowBuilder
     * @param builder  - ComponentBuilder
     */
    @ExportInstanceMethod
    public static void remove(WindowBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    /**
     * Получение массива потомков
     *
     * @return ComponentBuilder[]
     */
    @Export
    public abstract ComponentBuilder[] getChildren();

    /**
     * Перерисовка окна вывода
     */
    @Export
    public abstract void forceLayout();

    /**
     * Получение родительского компонента для окна
     *
     * @param instance - NumberFieldBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(WindowBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Получить количество потомков окна
     *
     * @return int
     */
    @Export
    public abstract int getChildrenCount();

    /**
     * Установить фокус на окно
     */
    @Export
    public abstract void focus();

    /**
     * Блокирует содержимое(компоненты) окна (визуально - покрывает серым оверлеем).
     *
     */
    @Export
    public abstract void mask();

    /**
     * Блокирует содержимое(компоненты) окна и отображает сообщение по центру окна.
     *
     * @param message
     */
    @Export
    public abstract void mask(String message);

    /**
     * Разблокировывает содержимое окна и скрывает служебное сообщение, установленное командой mask
     */
    @Export
    public abstract void unmask();

    /**
     * Устанавливает заголовок окна
     */
    @Export
    public abstract void setTitle(String title);

}
