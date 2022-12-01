package org.whirlplatform.editor.client.view.context;

import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Базовая реализация контекстного меню. Предполагается что внутри его будут содержаться Контекстные
 * пункты меню с тем же контекстом. При добавлении пунктов принудительно заменяет у них контекст на
 * свой собственный. При наследовании необходимо реализовать метод initMenu, в котором должны быть
 * созданы и добавлены экземпляры пунктов и разделители. Изменение состояния пунктов меню
 * производится каждый раз при открытии меню.
 *
 * @param <C> - класс или интерфейс контекста, общий для меню и пунктов меню.
 * @see ContextMenu
 * @see ContextMenuItem
 * @see HasContext
 */
public abstract class AbstractContextMenu<C> extends Menu implements ContextMenu<C> {
    private final List<ContextMenuItem<C>> menuItems;
    private C context;

    public AbstractContextMenu() {
        menuItems = new ArrayList<>();
        addShowHandler(new ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                updateMenuItemsState();
            }
        });
        initMenu();
    }

    public AbstractContextMenu(final C context) {
        this();
        setContext(context);
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public void setContext(C context) {
        this.context = context;
        for (ContextMenuItem<C> item : menuItems) {
            item.setContext(getContext());
        }
    }

    @Override
    public Menu asMenu() {
        return this;
    }

    @Override
    public void addMenuItem(ContextMenuItem<C> menuItem) {
        menuItem.setContext(getContext());
        menuItems.add(menuItem);
        super.add(menuItem.asMenuItem());
    }

    @Override
    public void removeMenuItem(ContextMenuItem<C> menuItem) {
        if (menuItems.contains(menuItem)) {
            menuItems.remove(menuItem);
            super.remove(menuItem.asMenuItem());
        }
    }

    @Override
    public void addSeparator() {
        super.add(new SeparatorMenuItem());
    }

    /**
     * инициализирует меню
     */
    protected abstract void initMenu();

    private void updateMenuItemsState() {
        for (ContextMenuItem<C> menuItem : menuItems) {
            menuItem.updateState();
        }
    }
}
