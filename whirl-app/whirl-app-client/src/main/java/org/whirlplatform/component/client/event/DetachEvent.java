package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.DetachEvent.DetachHandler;

public class DetachEvent extends GwtEvent<DetachHandler> {

    private static Type<DetachHandler> TYPE;

    public static Type<DetachHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DetachHandler>() {
                @Override
                public String toString() {
                    return "DetachHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<DetachHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DetachHandler handler) {
        handler.onDetach(this);
    }

    public interface DetachHandler extends EventHandler {

        void onDetach(DetachEvent event);

    }

    public interface HasDetachHandlers {

        HandlerRegistration addDetachHandler(DetachHandler handler);

    }

}
