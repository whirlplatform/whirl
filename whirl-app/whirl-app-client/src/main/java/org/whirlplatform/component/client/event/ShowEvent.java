package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.ShowEvent.ShowHandler;

public class ShowEvent extends GwtEvent<ShowHandler> {

	private static Type<ShowHandler> TYPE;

	public static Type<ShowHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<ShowHandler>() {
				@Override
				public String toString() {
					return "ShowHandler";
				}
			};
		}
		return TYPE;
	}

	@Override
	public Type<ShowHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowHandler handler) {
		handler.onShow(this);
	}

	public interface ShowHandler extends EventHandler {
		void onShow(ShowEvent event);
	}

	public interface HasShowHandlers {

        HandlerRegistration addShowHandler(ShowHandler handler);

	}

}
