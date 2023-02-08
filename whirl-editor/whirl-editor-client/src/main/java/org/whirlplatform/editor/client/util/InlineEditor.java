package org.whirlplatform.editor.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public class InlineEditor {

    private Tree<AbstractElement, String> tree;
    private TextField field;
    private BaseEventPreview eventPreview;
    private AbstractElement currentElement;

    public InlineEditor(Tree<AbstractElement, String> tree) {
        this.tree = tree;
        field = new TextField();
        field.getElement().setZIndex(1000);
        field.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    hide();
                }
            }
        });
        eventPreview = new BaseEventPreview() {
            @Override
            protected boolean onAutoHide(NativePreviewEvent pe) {
                return InlineEditor.this.onAutoHide(pe);
            }
        };
    }

    public void edit(TreeNode<AbstractElement> node) {
        eventPreview.add();
        currentElement = node.getModel();
        Element el = node.getTextElement();
        field.setValue(currentElement.getName());
        // Не всегда setValue меняет текст внутри field
        field.setText(currentElement.getName());
        field.setWidth(
            tree.getView().getElementContainer(node).getWidth(true) - el.getOffsetLeft());
        RootPanel.get().add(field);
        field.getElement().makePositionable(true);
        field.setPagePosition(el.getAbsoluteLeft(), el.getAbsoluteTop() - 3);

        Scheduler.ScheduledCommand command = new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                field.focus();
                field.selectAll();
            }
        };
        Scheduler.get().scheduleDeferred(command);
    }

    private boolean onAutoHide(NativePreviewEvent pe) {
        XElement target = pe.getNativeEvent().getEventTarget().cast();
        if (field.getElement().isOrHasChild(target)) {
            return false;
        }
        hide();
        return true;
    }

    public void hide() {
        currentElement.setName(field.getText());
        RootPanel.get().remove(field);
        eventPreview.remove();
        tree.refresh(currentElement);
    }

    public boolean isShown() {
        return field.isAttached();
    }
}
