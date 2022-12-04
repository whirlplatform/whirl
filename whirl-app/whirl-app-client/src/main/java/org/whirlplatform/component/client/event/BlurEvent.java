package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.BlurEvent.BlurHandler;

public class BlurEvent extends GwtEvent<BlurHandler> {

    private static Type<BlurHandler> TYPE;

    public static Type<BlurHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<BlurHandler>() {
                @Override
                public String toString() {
                    return "BlurHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<BlurHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BlurHandler handler) {
        handler.onBlur(this);
    }

    public interface BlurHandler extends EventHandler {

        void onBlur(BlurEvent event);

    }

    public interface HasBlurHandlers {

        HandlerRegistration addBlurHandler(BlurHandler handler);

    }

}
