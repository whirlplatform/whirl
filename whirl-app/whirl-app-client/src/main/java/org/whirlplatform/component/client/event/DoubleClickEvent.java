package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.DoubleClickEvent.DoubleClickHandler;

public class DoubleClickEvent extends GwtEvent<DoubleClickHandler> {

    private static Type<DoubleClickHandler> TYPE;

    public static Type<DoubleClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DoubleClickHandler>() {
                @Override
                public String toString() {
                    return "DoubleClickHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<DoubleClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DoubleClickHandler handler) {
        handler.onDoubleClick(this);
    }

    public interface DoubleClickHandler extends EventHandler {

        void onDoubleClick(DoubleClickEvent event);

    }

    public interface HasDoubleClickHandlers {

        HandlerRegistration addDoubleClickHandler(
            DoubleClickHandler handler);

    }

}
