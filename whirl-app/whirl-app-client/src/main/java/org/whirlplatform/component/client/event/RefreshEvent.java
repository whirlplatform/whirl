package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.RefreshEvent.RefreshHandler;

public class RefreshEvent extends GwtEvent<RefreshHandler> {
    private static Type<RefreshHandler> TYPE;

    public static Type<RefreshHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<RefreshHandler>() {
                @Override
                public String toString() {
                    return "RefreshHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<RefreshHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RefreshHandler handler) {
        handler.onRefresh(this);
    }

    public interface RefreshHandler extends EventHandler {

        void onRefresh(RefreshEvent event);

    }

    public interface HasRefreshHandlers {

        HandlerRegistration addRefreshHandler(RefreshHandler handler);

    }
}
