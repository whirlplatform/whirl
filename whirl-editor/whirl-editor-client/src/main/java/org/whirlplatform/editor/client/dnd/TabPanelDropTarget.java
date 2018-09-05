package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.TabPanel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class TabPanelDropTarget extends DropTarget {

	private TabPanel panel;
	private boolean before;
	private int insertIndex;

	public TabPanelDropTarget(TabPanel panel) {
		super(panel);
		this.panel = panel;
	}

	@Override
	protected void showFeedback(DndDragMoveEvent event) {
		event.getStatusProxy().setStatus(true);

		int x = event.getDragMoveEvent().getNativeEvent().getClientX();
		int y = event.getDragMoveEvent().getNativeEvent().getClientY();

		insertIndex = panel.getWidgetCount();

		Element selected = null;

		XElement el = panel.getElement().cast();
		NodeList<Element> ulElements = el.getElementsByTagName("ul");
		for (int i = 0; i < ulElements.getLength(); i++) {
			Element ul = ulElements.getItem(i);
			NodeList<Element> liElements = ul.getElementsByTagName("li");

			before = false;
			selected = liElements.getItem(panel.getWidgetCount() - 1);

			for (int j = 0; j < liElements.getLength(); j++) {
				Element li = liElements.getItem(j);
				if (li.<XElement> cast().getBounds().contains(x, y)) {
					panel.setActiveWidget(panel.getWidget(j));
					before = true;
					selected = li;
					insertIndex = j;
				}
			}
		}
		if (selected != null) {
			showInsert(selected);
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

		if (type != null && type.equals(ComponentType.TabItemType)) {
			event.setCancelled(false);
			event.getStatusProxy().setStatus(true);
		} else {
			event.setCancelled(true);
			event.getStatusProxy().setStatus(false);
		}
	}

	@Override
	protected void onDragDrop(DndDropEvent event) {
		hideInsert();
		insertIndex = -1;
	}

	@Override
	protected void onDragLeave(DndDragLeaveEvent event) {
		hideInsert();
	}

	public int getIndex() {
		return insertIndex;
	}

	private void showInsert(Element el) {
		HorizontalInsert insert = HorizontalInsert.get();
		insert.show(el);

		Rectangle rect = el.<XElement> cast().getBounds();

		int x;
		if (before) {
			x = rect.getX() - 4;
		} else {
			x = rect.getX() + rect.getWidth();
		}

		insert.getElement().makePositionable(true);
		insert.getElement().setBounds(x, rect.getY(), 6, rect.getHeight());
	}

	private void hideInsert() {
		HorizontalInsert insert = HorizontalInsert.get();
		insert.setVisible(false);
	}

}
