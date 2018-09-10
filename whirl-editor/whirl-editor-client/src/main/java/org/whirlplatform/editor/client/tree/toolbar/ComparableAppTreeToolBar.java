package org.whirlplatform.editor.client.tree.toolbar;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.client.tree.menu.ComparableAppTreeMenu;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 * Панель инструментов для сравниваемого дерева приложений
 */
public class ComparableAppTreeToolBar extends AbstractAppTreeToolBar<ComparableAppTree> {
    private final static String OPEN_BUTTON = EditorMessage.Util.MESSAGE.open();
    private final TextButton openButton;

    public ComparableAppTreeToolBar() {
        super();
        openButton = new TextButton();
        openButton.setToolTip(OPEN_BUTTON);
        openButton.setIcon(ComponentBundle.INSTANCE.open());
        add(openButton);
        setContextButtonMenu(new ComparableAppTreeMenu(appTree));
        addContextButton();
    }

    public void addOpenButtonHandler(final SelectHandler handler) {
        openButton.addSelectHandler(handler);
    }
}
