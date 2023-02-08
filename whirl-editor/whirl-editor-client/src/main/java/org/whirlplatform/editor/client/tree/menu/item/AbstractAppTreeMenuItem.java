package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.whirlplatform.editor.client.tree.AppTree;

/**
 * Базовая реализация Пункта меню для Меню дерева приложения. Инкапсулирует ссылку на дерево
 * приложения.
 */
public abstract class AbstractAppTreeMenuItem<T extends AppTree> extends MenuItem
    implements AppTreeMenuItem<T> {
    private T appTree;

    public AbstractAppTreeMenuItem() {
        super();
        this.setText(getItemTitle());
        this.addSelectionHandler(createSelectionHandler());
        this.setIcon(getItemIcon());
    }

    public AbstractAppTreeMenuItem(final T appTree) {
        this();
        this.appTree = appTree;
    }

    @Override
    public abstract void updateState();

    protected T getAppTree() {
        return appTree;
    }

    @Override
    public void setAppTree(final T appTree) {
        this.appTree = appTree;
    }

    /**
     * Функция этого пункта меню
     *
     * @return Обработчик выбора
     */
    protected abstract SelectionHandler<Item> createSelectionHandler();

    /**
     * @return Текст пункта меню
     */
    protected abstract String getItemTitle();

    /**
     * @return Иконка для пункта меню
     */
    protected abstract ImageResource getItemIcon();
}
