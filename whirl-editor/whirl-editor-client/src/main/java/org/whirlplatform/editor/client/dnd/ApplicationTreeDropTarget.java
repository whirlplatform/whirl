package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.tree.Tree;
import java.util.List;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public class ApplicationTreeDropTarget extends DropTarget {

    public ApplicationTreeDropTarget(Tree<AbstractElement, ?> tree) {
        super(tree);
    }

    public AbstractElement getDropTargetElement(DndDropEvent event) {
        Element element = event.getDragEndEvent().getNativeEvent()
                .getEventTarget().cast();
        return getDropTargetElement(element);
    }

    public AbstractElement getDropTargetElement(DndDragMoveEvent event) {
        Element element = event.getDragMoveEvent().getNativeEvent()
                .getEventTarget().cast();
        return getDropTargetElement(element);
    }

    @SuppressWarnings("unchecked")
    private AbstractElement getDropTargetElement(Element element) {
        AbstractElement result = null;
        if (((Tree<AbstractElement, ?>) getWidget()).findNode(element) != null) {
            result = ((Tree<AbstractElement, ?>) getWidget()).findNode(element)
                    .getModel();
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object getDropComponent(DndDropEvent event) {
        Object data = event.getData();
        Object component;
        if (data instanceof List) {
            component = ((List<TreeNode>) data).get(0).getData();
        } else {
            component = data;
        }
        return component;
    }

}
