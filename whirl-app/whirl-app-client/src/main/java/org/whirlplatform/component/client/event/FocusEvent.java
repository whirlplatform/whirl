package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.FocusEvent.FocusHandler;

public class FocusEvent extends GwtEvent<FocusHandler> {

    private static Type<FocusHandler> TYPE;

    public static Type<FocusHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<FocusHandler>() {
                @Override
                public String toString() {
                    return "FocusHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<FocusHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FocusHandler handler) {
        handler.onFocus(this);
    }

    public interface FocusHandler extends EventHandler {

        void onFocus(FocusEvent event);

    }

    public interface HasFocusHandlers {

        HandlerRegistration addFocusHandler(FocusHandler handler);

    }

}
