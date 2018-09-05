package org.whirlplatform.editor.client.view.allapps;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.view.context.AbstractContextMenuItem;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class CollapseContextMenuItem extends AbstractContextMenuItem<AllApplicationsView> {

    public CollapseContextMenuItem() {
        super();
    }

    public CollapseContextMenuItem(final AllApplicationsView context) {
        super(context);
    }

    @Override
    public void updateState() {
        setEnabled(getContext().getSelectedFolder() != null);
    }

    @Override
    protected SelectionHandler<Item> createSelectionHandler() {
        return new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                getContext().collapseSelectedFolder();
            }
        };
    }

    @Override
    protected String getItemTitle() {
        return EditorMessage.Util.MESSAGE.collapse();
    }

    @Override
    protected ImageResource getItemIcon() {
        return ComponentBundle.INSTANCE.collapse();
    }
}
