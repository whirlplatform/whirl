package org.whirlplatform.editor.client.tree.menu;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.item.*;

/**
 * Меню для основного дерева приложения
 * 
 * @author bedritckiy_mr
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
