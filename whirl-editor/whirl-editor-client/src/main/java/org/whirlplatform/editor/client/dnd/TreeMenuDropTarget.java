package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.dnd.core.client.Insert;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class TreeMenuDropTarget extends DropTarget {

	private Tree tree;
	private int insertIndex;

	public TreeMenuDropTarget(Tree tree) {
		super(tree);
		this.tree = tree;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void showFeedback(DndDragMoveEvent event) {
		final TreeNode item = tree.findNode(
				event.getDragMoveEvent().getNativeEvent().getEventTarget()
                        .cast());

		if (item != null && !tree.getStore().getRootItems().contains(item.getModel())) {
		}
		if (item != null) {
			if (tree.getStore().getRootItems().contains(item.getModel())) {
				showInsert(item.getElement(), true);
				insertIndex = tree.getStore().indexOf(item.getModel());
			} else {
				Object parent = item.getModel();
				while (tree.getStore().getParent(parent) != null) {
					parent = tree.getStore().getParent(parent);
				}
				showInsert(tree.findNode(parent).getElement(), true);
				insertIndex = tree.getStore().indexOf(parent);
			}
		} else {
			showInsert(tree.findNode(tree.getStore().getChild(tree.getStore().getRootCount() - 1)).getElement(), false);
			insertIndex = tree.getStore().getRootCount();
		}
	}

	@Override
	protected void onDragMove(DndDragMoveEvent event) {
		ComponentType type = null;
		if (event.getData() instanceof ComponentType) {
			type = (ComponentType) event.getData();
		} else if (event.getData() instanceof ComponentElement) {
			type = ((ComponentElement) event.getData()).getType();
		}

		if (type != null && type.equals(ComponentType.HorizontalMenuItemType)) {
			event.setCancelled(false);
			event.getStatusProxy().setStatus(true);
		} else {
			event.setCancelled(true);
			event.getStatusProxy().setStatus(false);
		}
	}

	public int getIndex() {
		return insertIndex;
	}

	private void showInsert(Element elem, boolean before) {
	    Insert insert = Insert.get();

	    insert.show(elem);
	    Rectangle rect = elem.<XElement> cast().getBounds();

	    int y = before ? rect.getY() - 2 : (rect.getY() + rect.getHeight() - 4);

	    insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);
	}
}
