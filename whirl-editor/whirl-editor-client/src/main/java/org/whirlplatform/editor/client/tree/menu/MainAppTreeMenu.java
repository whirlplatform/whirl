package org.whirlplatform.editor.client.tree.menu;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.item.AddAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.CopyAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.CutAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.DeleteAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.PasteAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.RenameAppTreeMenuItem;
import org.whirlplatform.editor.client.tree.menu.item.RightsAppTreeMenuItem;

/**
 * Меню для основного дерева приложения
 */
public class MainAppTreeMenu extends AbstractAppTreeMenu<AppTree> implements AppTreeMenu<AppTree> {
    public MainAppTreeMenu(final AppTree appTree) {
        super(appTree);
        add(new RenameAppTreeMenuItem());
        addSeparator();
        add(new AddAppTreeMenuItem());
        add(new DeleteAppTreeMenuItem());
        addSeparator();
        add(new CopyAppTreeMenuItem());
        add(new CutAppTreeMenuItem());
        add(new PasteAppTreeMenuItem());
        addSeparator();
        add(new RightsAppTreeMenuItem());
    }
}
