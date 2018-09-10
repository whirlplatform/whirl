package org.whirlplatform.editor.client.tree.menu;

import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.client.tree.menu.item.ChangesComparableAppTreeMenuItem;

/**
 * Меню для дерева сравнения приложений
 */
public class ComparableAppTreeMenu extends AbstractAppTreeMenu<ComparableAppTree>
		implements AppTreeMenu<ComparableAppTree> {

	public ComparableAppTreeMenu(ComparableAppTree appTree) {
		super(appTree);
		add(new ChangesComparableAppTreeMenuItem());
	}
}
