package org.whirlplatform.editor.client.tree;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import org.whirlplatform.editor.client.tree.cell.AppTreeMainCell;
import org.whirlplatform.editor.client.tree.menu.MainAppTreeMenu;
import org.whirlplatform.editor.client.util.InlineEditor;
import org.whirlplatform.meta.shared.editor.AbstractElement;

import java.util.Date;

/**
 * This widget represents the application tree
 *
 * @author bedritckiy_mr
 */
public abstract class AppTreeWidget extends AbstractAppTree {
    private Timer clickTimer;
    private long prevTime = 0;
    private Element prevItem;
    private Element selectedItem;
    private InlineEditor inlineEditor;

    public AppTreeWidget() {
        super();
        this.setContextMenu(new MainAppTreeMenu(this));
        this.setCell(new AppTreeMainCell(this));
        inlineEditor = new InlineEditor(this);
        clickTimer = new Timer() {
            @Override
            public void run() {
                renameSelectedElement();
            }
        };
    }

    @Override
    protected void onDoubleClick(Event event) {
        super.onDoubleClick(event);
        clickTimer.cancel();
    }

    @Override
    public boolean renameSelectedElement() {
        AbstractElement selected = getSelectedElement();
        boolean result = false;
        if (selected != null && isRenaming(selected)) {
            final String previousName = selected.getName();
            inlineEditor.edit(getSelectedNode());
            if (!previousName.equals(selected.getName())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    protected void onClick(Event event) {
        super.onClick(event);
        clickTimer.cancel();
        TreeNode<AbstractElement> node = findNode(event.getEventTarget().cast());
        if (node != null) {
            selectedItem = findNode(event.getEventTarget().cast()).getTextElement();
            long currentTime = new Date().getTime();
            if (prevTime != 0 && prevItem != null && currentTime - prevTime > 800 && prevItem == selectedItem
                    && isRenaming(node.getModel())) {
                clickTimer.schedule(500);
            }
            prevTime = currentTime;
            prevItem = selectedItem;
        }
    }

    @Override
    protected void onScroll(Event event) {
        if (inlineEditor.isShown()) {
            inlineEditor.hide();
        }
        super.onScroll(event);
    }
}
