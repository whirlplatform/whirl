package org.whirlplatform.editor.client.tree.toolbar;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.SortAppTreeMenu;
import org.whirlplatform.editor.client.tree.menu.item.SortAppTreeMenuItem;

public class AppTreeToolBarWithSort extends AbstractAppTreeToolBar<AppTree> {

    private SortAppTreeMenu menu;

    public AppTreeToolBarWithSort(AppTree appTree) {
        super();
        menu = new SortAppTreeMenu(appTree,
            new SortAppTreeMenuItem<AppTree>(appTree));
        setContextButtonMenu(menu);
        addContextButton();
    }

    public void setSortItemEnabled(boolean enabled) {
        menu.setSortItemEnabled(enabled);
    }
}
