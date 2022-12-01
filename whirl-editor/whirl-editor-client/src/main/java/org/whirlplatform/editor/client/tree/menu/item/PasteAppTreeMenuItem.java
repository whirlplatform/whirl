package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 * Пункт меню дерева приложения - Вставить
 */
public class PasteAppTreeMenuItem extends AbstractAppTreeMenuItem<AppTree> {
    private final static String TITLE = EditorMessage.Util.MESSAGE.context_menu_paste();
    private final static ImageResource ICON = ComponentBundle.INSTANCE.paste();

    public PasteAppTreeMenuItem() {
        super();
    }

    public PasteAppTreeMenuItem(final AppTree tree) {
        super(tree);
    }

    @Override
    public void updateState() {
        if (getAppTree() != null) {
            setEnabled(getAppTree().isPasting(getAppTree().getSelectedElement()));
        } else {
            setEnabled(false);
        }
    }

    @Override
    protected SelectionHandler<Item> createSelectionHandler() {
        return new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                getAppTree().doPasteElement(getAppTree().getSelectedElement());
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
