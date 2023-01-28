package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 * Пункт меню дерева приложения - Добавить
 */
public class AddAppTreeMenuItem extends AbstractAppTreeMenuItem<AppTree> {
    private static final String TITLE = EditorMessage.Util.MESSAGE.context_menu_add();
    private static final ImageResource ICON = ComponentBundle.INSTANCE.plus();

    public AddAppTreeMenuItem() {
        super();
    }

    public AddAppTreeMenuItem(final AppTree tree) {
        super(tree);
    }

    @Override
    public void updateState() {
        if (getAppTree() != null) {
            this.setEnabled(getAppTree().isAdding(getAppTree().getSelectedElement()));
        } else {
            this.setEnabled(false);
        }
    }

    @Override
    protected SelectionHandler<Item> createSelectionHandler() {
        return new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                getAppTree().doAddElement(getAppTree().getSelectedElement(), null);
            }
        };
    }

    @Override
    protected String getItemTitle() {
        return TITLE;
    }

    @Override
    protected ImageResource getItemIcon() {
        return ICON;
    }
}
