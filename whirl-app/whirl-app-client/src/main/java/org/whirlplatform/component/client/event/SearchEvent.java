package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.SearchEvent.SearchHandler;

public class SearchEvent extends GwtEvent<SearchHandler> {

	private static Type<SearchHandler> TYPE;

	public static Type<SearchHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SearchHandler>() {
				@Override
				public String toString() {
					return "SearchHandler";
				}
			};
		}
		return TYPE;
	}

	@Override
	public Type<SearchHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchHandler handler) {
		handler.onSearch(this);
	}

	public interface SearchHandler extends EventHandler {

		void onSearch(SearchEvent event);

	}

	public interface HasSearchHandlers {

        HandlerRegistration addSearchHandler(SearchHandler handler);

	}

}
