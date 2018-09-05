package org.whirlplatform.editor.client.tree.menu;

import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.whirlplatform.editor.client.tree.AppTree;

public class SortAppTreeMenu extends MainAppTreeMenu {

    private MenuItem item;

    public SortAppTreeMenu(AppTree appTree, MenuItem sortItem) {
        super(appTree);
        this.item = sortItem;
        addSeparator();
        add(item);
    }

    public void setSortItemEnabled(boolean enabled) {
        item.setEnabled(enabled);
    }
}
