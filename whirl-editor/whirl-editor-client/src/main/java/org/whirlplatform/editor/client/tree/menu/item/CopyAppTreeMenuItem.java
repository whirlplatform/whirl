package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 * Пункт меню дерева приложения - Копировать
 */
public class CopyAppTreeMenuItem extends AbstractAppTreeMenuItem<AppTree> {
    private final static String TITLE = EditorMessage.Util.MESSAGE.context_menu_copy();
    private final static ImageResource ICON = ComponentBundle.INSTANCE.copy();

    public CopyAppTreeMenuItem() {
        super();
    }

    public CopyAppTreeMenuItem(final AppTree tree) {
        super(tree);
    }

    @Override
    public void updateState() {
        if (getAppTree() != null) {
            setEnabled(getAppTree().isCopying(getAppTree().getSelectedElement()));
        } else {
            setEnabled(false);
        }
    }

    @Override
    protected SelectionHandler<Item> createSelectionHandler() {
        return new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                getAppTree().doCopyElement(getAppTree().getSelectedElement());
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
