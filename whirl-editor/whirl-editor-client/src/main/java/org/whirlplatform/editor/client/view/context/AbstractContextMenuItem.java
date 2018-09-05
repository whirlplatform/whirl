package org.whirlplatform.editor.client.view.context;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Базовая реализация Контекстного пункта меню. Предполагается что при
 * наследовании будут реализованы:
 * <li>метод описывающий поведение при нажатии</li>
 * <li>метод изменяющий состояние пункта</li>
 * <li>методы внешнего представления - источник для иконки, текста</li>
 * <p/>
 *
 * @param <C> - класс или интерфейс контекста
 * @author bedritckiy_mr
 */
public abstract class AbstractContextMenuItem<C> extends MenuItem implements ContextMenuItem<C> {
    private C context;

    public AbstractContextMenuItem() {
        this.setText(getItemTitle());
        this.addSelectionHandler(createSelectionHandler());
        this.setIcon(getItemIcon());
    }

    public AbstractContextMenuItem(C context) {
        this();
        setContext(context);
    }

    @Override
    public void setContext(C context) {
        this.context = context;
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public MenuItem asMenuItem() {
        return this;
    }

    /**
     * Описывает поведение при нажатии. Предполагается использование методов контекста.
     *
     * @return объект описывающий поведение при выборе пункта
     */
    protected abstract SelectionHandler<Item> createSelectionHandler();

    /**
     * Возвращает текст пункта меню
     *
     * @return текст пункта или null
     */
    protected abstract String getItemTitle();

    /**
     * Возвращает иконку для пункта меню
     *
     * @return иконка или null
     */
    protected abstract ImageResource getItemIcon();
}
