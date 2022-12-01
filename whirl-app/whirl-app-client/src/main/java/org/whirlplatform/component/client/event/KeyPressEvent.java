package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.KeyPressEvent.KeyPressHandler;

public class KeyPressEvent extends GwtEvent<KeyPressHandler> {

    private static Type<KeyPressHandler> TYPE;

    public static Type<KeyPressHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<KeyPressHandler>() {
                @Override
                public String toString() {
                    return "KeyPressHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<KeyPressHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(KeyPressHandler handler) {
        handler.onKeyPress(this);
    }

    public interface KeyPressHandler extends EventHandler {

        void onKeyPress(KeyPressEvent event);

    }

    public interface HasKeyPressHandlers {

        HandlerRegistration addKeyPressHandler(KeyPressHandler handler);

    }

}
