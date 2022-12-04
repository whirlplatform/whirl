package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.HideEvent.HideHandler;

public class HideEvent extends GwtEvent<HideHandler> {

    private static Type<HideHandler> TYPE;

    public static Type<HideHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<HideHandler>() {
                @Override
                public String toString() {
                    return "HideHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<HideHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HideHandler handler) {
        handler.onHide(this);
    }

    public interface HideHandler extends EventHandler {

        void onHide(HideEvent event);

    }

    public interface HasHideHandlers {

        HandlerRegistration addHideHandler(HideHandler handler);

    }

}
