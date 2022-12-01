package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.RowDoubleClickEvent.RowDoubleClickHandler;

public class RowDoubleClickEvent extends GwtEvent<RowDoubleClickHandler> {

    private static Type<RowDoubleClickHandler> TYPE;

    public static Type<RowDoubleClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<RowDoubleClickHandler>() {
                @Override
                public String toString() {
                    return "RowDoubleClickHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<RowDoubleClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RowDoubleClickHandler handler) {
        handler.onRowDoubleClick(this);
    }

    public interface RowDoubleClickHandler extends EventHandler {

        void onRowDoubleClick(RowDoubleClickEvent event);

    }

    public interface HasRowDoubleClickHandlers {

        HandlerRegistration addRowDoubleClickHandler(
                RowDoubleClickHandler handler);

    }

}
