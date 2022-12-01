package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.SelectEvent.SelectHandler;

public class SelectEvent extends GwtEvent<SelectHandler> {

    private static Type<SelectHandler> TYPE;

    public SelectEvent() {
    }

    public SelectEvent(ComponentBuilder source) {
        setSource(source);
    }

    public static Type<SelectHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SelectHandler>() {
                @Override
                public String toString() {
                    return "SelectHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<SelectHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectHandler handler) {
        handler.onSelect(this);
    }

    public interface SelectHandler extends EventHandler {

        void onSelect(SelectEvent event);

    }

    public interface HasSelectHandlers {

        HandlerRegistration addSelectHandler(SelectHandler handler);

    }

}
