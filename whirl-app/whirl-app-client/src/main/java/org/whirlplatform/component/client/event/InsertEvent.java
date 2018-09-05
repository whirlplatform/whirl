package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.InsertEvent.InsertHandler;

public class InsertEvent extends GwtEvent<InsertHandler> {

    private static Type<InsertHandler> TYPE;

    public static Type<InsertHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<InsertHandler>() {
                @Override
                public String toString() {
                    return "InsertHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<InsertHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InsertHandler handler) {
        handler.onInsert(this);
    }

    public interface InsertHandler extends EventHandler {

        void onInsert(InsertEvent event);

    }

    public interface HasInsertHandlers {

        HandlerRegistration addInsertHandler(InsertHandler handler);

    }

}
