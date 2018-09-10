package org.whirlplatform.editor.client.tree.toolbar;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.MainAppTreeMenu;

/**
 * Панель инструментов для основного дерева приложения
 */
public class MainAppTreeToolBar extends AbstractAppTreeToolBar<AppTree> {

    public MainAppTreeToolBar() {
        super();
        setContextButtonMenu(new MainAppTreeMenu(appTree));
        addContextButton();
    }
}
