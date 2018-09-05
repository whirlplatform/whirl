package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.ChangeEvent.ChangeHandler;

public class ChangeEvent extends GwtEvent<ChangeHandler> {

	private static Type<ChangeHandler> TYPE;

	public static Type<ChangeHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<ChangeHandler>() {
				@Override
				public String toString() {
					return "ChangeHandler";
				}
			};
		}
		return TYPE;
	}

	@Override
	public Type<ChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeHandler handler) {
		handler.onChange(this);
	}

	public interface ChangeHandler extends EventHandler {

		void onChange(ChangeEvent event);

	}

	public interface HasChangeHandlers {

        HandlerRegistration addChangeHandler(ChangeHandler handler);

	}

}
