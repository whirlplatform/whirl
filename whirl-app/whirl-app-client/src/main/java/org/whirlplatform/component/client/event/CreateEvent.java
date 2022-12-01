package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.CreateEvent.CreateHandler;

public class CreateEvent extends GwtEvent<CreateHandler> {

    private static Type<CreateHandler> TYPE;

    public static Type<CreateHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<CreateHandler>() {
                @Override
                public String toString() {
                    return "CreateHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<CreateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateHandler handler) {
        handler.onCreate(this);
    }

    public interface CreateHandler extends EventHandler {

        void onCreate(CreateEvent event);

    }

    public interface HasCreateHandlers {

        HandlerRegistration addCreateHandler(CreateHandler handler);

    }

}
