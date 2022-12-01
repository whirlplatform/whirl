package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.LoadEvent.LoadHandler;

public class LoadEvent extends GwtEvent<LoadHandler> {

    private static Type<LoadHandler> TYPE;

    public static Type<LoadHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<LoadHandler>() {
                @Override
                public String toString() {
                    return "LoadHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<LoadHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoadHandler handler) {
        handler.onLoad(this);
    }

    public interface LoadHandler extends EventHandler {

        void onLoad(LoadEvent event);

    }

    public interface HasLoadHandlers {

        HandlerRegistration addLoadHandler(LoadHandler handler);

    }

}
