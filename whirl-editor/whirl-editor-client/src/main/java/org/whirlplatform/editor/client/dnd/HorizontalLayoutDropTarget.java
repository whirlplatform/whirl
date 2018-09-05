package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.*;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;

public class HorizontalLayoutDropTarget extends DropTarget {

	private InsertResizeContainer horizontalLayout;

	private int insertIndex;

	private boolean before;

	public HorizontalLayoutDropTarget(InsertResizeContainer container) {
		super(container);
		this.horizontalLayout = container;
	}

	@Override
	protected void onDragDrop(DndDropEvent event) {
		// if (insertIndex == -1) {
		// insertIndex = 0;
		// }
		// horizontalLayout.insert(widget, insertIndex);
		// }
		//
		// Scheduler.get().scheduleFinally(new ScheduledCommand() {
		// @Override
		// public void execute() {
		// horizontalLayout.forceLayout();
		// }
		// });
		//
		// hideInsert();
		//
		// lastInsertIndex = insertIndex;
		insertIndex = -1;
	}

	@Override
	protected void onDragLeave(DndDragLeaveEvent event) {
		super.onDragLeave(event);
		hideInsert();
	}

	@Override
	protected void onDragEnter(DndDragEnterEvent event) {
		super.onDragEnter(event);
		event.setCancelled(true);
		event.getStatusProxy().setStatus(true);
	}

	@Override
	protected void onDragMove(DndDragMoveEvent event) {
		XElement target = event.getDragMoveEvent().getNativeEvent()
				.getEventTarget().cast();
		if (!horizontalLayout.getElement().isOrHasChild(target)) {
			event.setCancelled(true);
			event.getStatusProxy().setStatus(false);
		} else {
			event.setCancelled(false);
			event.getStatusProxy().setStatus(true);
		}
	}

	@Override
	protected void showFeedback(DndDragMoveEvent event) {
		event.getStatusProxy().setStatus(true);
		NativeEvent e = event.getDragMoveEvent().getNativeEvent().cast();
        Element el = event.getDragMoveEvent().getNativeEvent()
				.getEventTarget().cast();
		Widget w = horizontalLayout.findWidget(el);
		if (w != null
				&& (feedback == Feedback.INSERT || feedback == Feedback.BOTH)) {
			int width = el.getOffsetWidth();
			int mid = width / 2;
			mid += el.getAbsoluteLeft();
			int y = e.getClientX();
			before = y < mid;
			insertIndex = horizontalLayout.getWidgetIndex(w) + 1;
			if (before && insertIndex > 0) {
				insertIndex = insertIndex - 1;
			}
		} else {
			before = false;
			insertIndex = horizontalLayout.getWidgetCount();
			if (insertIndex > 0) {
				w = horizontalLayout.getWidget(insertIndex - 1);
			}
		}
		if (w != null) {
			showInsert(w.getElement());
		}
	}

	private void showInsert(Element row) {
		HorizontalInsert insert = HorizontalInsert.get();
		insert.show(row);
		Rectangle rect = row.<XElement> cast().getBounds();
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

	public int getIndex() {
		return insertIndex;
	}

}
