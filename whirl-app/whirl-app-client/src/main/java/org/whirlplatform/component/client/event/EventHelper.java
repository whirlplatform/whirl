package org.whirlplatform.component.client.event;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.whirlplatform.component.client.event.AttachEvent.AttachHandler;
import org.whirlplatform.component.client.event.ChangeEvent.ChangeHandler;
import org.whirlplatform.component.client.event.FocusEvent.FocusHandler;
import org.whirlplatform.component.client.event.LoadEvent.LoadHandler;
import org.whirlplatform.component.client.event.RefreshEvent.RefreshHandler;
import org.whirlplatform.component.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import org.whirlplatform.component.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.component.client.event.ShowEvent.ShowHandler;
import org.whirlplatform.component.client.event.UpdateEvent.UpdateHandler;
import org.whirlplatform.meta.shared.EventResult;

public interface EventHelper extends AttachHandler, ChangeHandler, ClickEvent.ClickHandler, CreateEvent.CreateHandler, DeleteEvent.DeleteHandler,
        DetachEvent.DetachHandler, DoubleClickEvent.DoubleClickHandler, InsertEvent.InsertHandler, KeyPressEvent.KeyPressHandler, LoadHandler, SelectHandler, ShowHandler,
        HideEvent.HideHandler, TimeEvent.TimeHandler, UpdateHandler, FocusHandler, BlurEvent.BlurHandler, RefreshHandler, RowDoubleClickHandler {

    void onEvent(Object source);

    void prepairEventResult(final EventResult eventResult, final Command command,
                            final AsyncCallback<Void> callback);

    void setAfterEvent(AsyncCallback<EventCallbackResult> callback);
}
