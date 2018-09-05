package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import org.whirlplatform.component.client.event.SortEvent.SortHandler;

public class SortEvent extends GwtEvent<SortHandler> {

    private static Type<SortHandler> TYPE;

    public static Type<SortHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SortHandler>() {
                @Override
                public String toString() {
                    return "SortHandler";
                }
            };
        }
        return TYPE;
    }

    @Override
    public Type<SortHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SortHandler handler) {
        handler.onSort(this);
    }

    public interface SortHandler extends EventHandler {
        void onSort(SortEvent event);
    }

    public interface HasSortHandlers {
        HandlerRegistration addSortHandler(SortHandler handler);
    }
}
