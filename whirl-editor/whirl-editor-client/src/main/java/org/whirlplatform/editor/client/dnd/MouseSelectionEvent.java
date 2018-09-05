package org.whirlplatform.editor.client.dnd;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent.MouseSelectionHandler;

public class MouseSelectionEvent extends GwtEvent<MouseSelectionHandler> {

	private static Type<MouseSelectionHandler> TYPE;

	private boolean select;
	private int x;
	private int y;
	private int height;
	private int width;

	public MouseSelectionEvent(boolean select, int x, int y, int width,
			int height) {
		this.select = select;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean isSelect() {
		return select;
	}

	public int getClientX() {
		return x;
	}

	public int getClientY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static Type<MouseSelectionHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<MouseSelectionHandler>();
		}
		return TYPE;
	}

	@Override
	public Type<MouseSelectionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MouseSelectionHandler handler) {
		handler.onMouseSelection(this);
	}

    public interface MouseSelectionHandler extends EventHandler {

		void onMouseSelection(MouseSelectionEvent event);

	}

    public interface HasMouseSelectionHandlers {

        HandlerRegistration addMouseSelectionHandler(
                MouseSelectionHandler handler);

	}

}
