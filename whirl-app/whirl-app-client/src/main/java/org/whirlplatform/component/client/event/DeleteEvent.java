package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.DeleteEvent.DeleteHandler;

public class DeleteEvent extends GwtEvent<DeleteHandler> {

	private static Type<DeleteHandler> TYPE;

	public static Type<DeleteHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<DeleteHandler>() {
				@Override
				public String toString() {
					return "DeleteHandler";
				}
			};
		}
		return TYPE;
	}

	@Override
	public Type<DeleteHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DeleteHandler handler) {
		handler.onDelete(this);
	}

	public interface DeleteHandler extends EventHandler {

		void onDelete(DeleteEvent event);

	}

	public interface HasDeleteHandlers {

        HandlerRegistration addDeleteHandler(DeleteHandler handler);

	}

}
