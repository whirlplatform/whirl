package org.whirlplatform.editor.client.tree.menu;

import com.sencha.gxt.widget.core.client.menu.Menu;
import org.whirlplatform.editor.client.tree.AppTree;

/**
 * Интерфейс для всех меню дерева приложения
 */
public interface AppTreeMenu<T extends AppTree> {

    void setAppTree(T appTree);

    Menu asMenu();
}
