package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.ClickEvent.ClickHandler;

public class ClickEvent extends GwtEvent<ClickHandler> {

    private static Type<ClickHandler> TYPE;

    public static Type<ClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<ClickHandler>() {
                @Override
                public String toString() {
                    return "ClickHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<ClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClickHandler handler) {
        handler.onClick(this);
    }

    public interface ClickHandler extends EventHandler {

        void onClick(ClickEvent event);

    }

    public interface HasClickHandlers {

        HandlerRegistration addClickHandler(ClickHandler handler);

    }

}
