package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.AttachEvent.AttachHandler;

public class AttachEvent extends GwtEvent<AttachHandler> {

	private static Type<AttachHandler> TYPE;

	public static Type<AttachHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<AttachHandler>() {
				@Override
				public String toString() {
					return "AttachHandler";
				}
			};
		}
		return TYPE;
	}

	@Override
	public Type<AttachHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AttachHandler handler) {
		handler.onAttach(this);
	}

	public interface AttachHandler extends EventHandler {

		void onAttach(AttachEvent event);

	}

	public interface HasAttachHandlers {

        HandlerRegistration addAttachHandler(AttachHandler handler);

	}

}
