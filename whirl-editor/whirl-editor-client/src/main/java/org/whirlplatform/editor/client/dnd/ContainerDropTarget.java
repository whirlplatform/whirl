package org.whirlplatform.editor.client.dnd;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.*;
import com.sencha.gxt.widget.core.client.container.Container;
import org.whirlplatform.editor.client.component.surface.Surface;

public class ContainerDropTarget extends DropTarget {

	private Container container;

	public ContainerDropTarget(Container target) {
		super(target);
		this.container = target;
	}

	@Override
	protected void onDragDrop(DndDropEvent event) {
		hideSurface();
	}

	@Override
	protected void onDragEnter(DndDragEnterEvent event) {
		super.onDragEnter(event);
		event.setCancelled(true);
		event.getStatusProxy().setStatus(true);
	}

	@Override
	protected void onDragLeave(DndDragLeaveEvent event) {
		super.onDragLeave(event);
		hideSurface();
	}

	private void hideSurface() {
		Surface surface = Surface.get();
		surface.setVisible(false);
	}

	@Override
	protected void onDragMove(DndDragMoveEvent event) {
		XElement target = event.getDragMoveEvent().getNativeEvent()
				.getEventTarget().cast();
		if (!container.getElement().isOrHasChild(target)) {
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
        XElement el = container.getElement().cast();
		Rectangle rect = el.getBounds();
		Surface.get()
				.getElement()
				.setBounds(rect.getX(), rect.getY(), rect.getWidth(),
						rect.getHeight());
		Surface.get().show(el);
	}

}
