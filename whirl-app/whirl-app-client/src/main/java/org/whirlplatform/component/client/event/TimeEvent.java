package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.TimeEvent.TimeHandler;

public class TimeEvent extends GwtEvent<TimeHandler> {

    private static Type<TimeHandler> TYPE;

    public static Type<TimeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<TimeHandler>() {
                @Override
                public String toString() {
                    return "TimeHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<TimeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TimeHandler handler) {
        handler.onTime(this);
    }

    public interface TimeHandler extends EventHandler {

        void onTime(TimeEvent event);

    }

    public interface HasTimeHandlers {

        HandlerRegistration addTimeHandler(TimeHandler handler);

    }

}
