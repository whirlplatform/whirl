package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.window.WindowBuilder;

/**
 * Окно вывода
 */
public abstract class WindowBuilderOverlay {

    /**
     * Инициализация окна
     *
     * @param instance - WindowBuilder
     * @return WindowBuilder
     */
    public static WindowBuilder create(WindowBuilder instance) {
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
     * Установить код на окно
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить код окна
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить активность окна
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности окна
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить скрытость окна
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости окна
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить стиль на окно
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Скрыть окно
     */
    public abstract void hide();

    /**
     * Отобразить окно
     */
    public abstract void show();

    /**
     * Максимизировать окно
     */
    public abstract void maximize();

    /**
     * Минимизировать окно
     */
    public abstract void minimize();

    public abstract void center();

    /**
     * Установить позицию окна в страннице
     *
     * @param left - int
     * @param top  - int
     */
    public abstract void setPosition(int left, int top);

    /**
     * Установить позицию окна на экране
     *
     * @param x - int
     * @param y - int
     */
    public abstract void setPagePosition(int x, int y);

    /**
     * Установить потомка для окна
     *
     * @param instance - WindowBuilder
     * @param builder  - ComponentBuilder
     */
    public static void setChild(WindowBuilder instance, ComponentBuilder builder) {
        instance.addChild(builder);
    }

    /**
     * Удаление окна
     *
     * @param instance - WindowBuilder
     * @param builder  - ComponentBuilder
     */
    public static void remove(WindowBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    /**
     * Получение массива потомков
     *
     * @return ComponentBuilder[]
     */
    public abstract ComponentBuilder[] getChildren();

    /**
     * Перерисовка окна вывода
     */
    public abstract void forceLayout();

    /**
     * Получение родительского компонента для окна
     *
     * @param instance - NumberFieldBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(WindowBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Получить количество потомков окна
     *
     * @return int
     */
    public abstract int getChildrenCount();

    /**
     * Установить фокус на окно
     */
    public abstract void focus();

    /**
     * Блокирует содержимое(компоненты) окна (визуально - покрывает серым оверлеем).
     *
     */
    public abstract void mask();

    /**
     * Блокирует содержимое(компоненты) окна и отображает сообщение по центру окна.
     *
     * @param message
     */
    public abstract void mask(String message);

    /**
     * Разблокировывает содержимое окна и скрывает служебное сообщение, установленное командой mask
     */
    public abstract void unmask();

    /**
     * Устанавливает заголовок окна
     */
    public abstract void setTitle(String title);

}
