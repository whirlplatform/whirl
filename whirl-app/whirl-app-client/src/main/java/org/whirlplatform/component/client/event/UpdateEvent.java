package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.UpdateEvent.UpdateHandler;

public class UpdateEvent extends GwtEvent<UpdateHandler> {

    private static Type<UpdateHandler> TYPE;

    public static Type<UpdateHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<UpdateHandler>() {
                @Override
                public String toString() {
                    return "UpdateHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<UpdateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateHandler handler) {
        handler.onUpdate(this);
    }

    public interface UpdateHandler extends EventHandler {

        void onUpdate(UpdateEvent event);

    }

    public interface HasUpdateHandlers {

        HandlerRegistration addUpdateHandler(UpdateHandler handler);

    }

}
