package org.whirlplatform.editor.client.util;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.component.client.event.AttachEvent;
import org.whirlplatform.component.client.event.BlurEvent;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.CreateEvent;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.DetachEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.component.client.event.FocusEvent;
import org.whirlplatform.component.client.event.HideEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.RowDoubleClickEvent;
import org.whirlplatform.component.client.event.SearchEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.event.ShowEvent;
import org.whirlplatform.component.client.event.TimeEvent;
import org.whirlplatform.component.client.event.UpdateEvent;

public class HandlerTypes {

    private static final Set<Type<? extends EventHandler>> types;

    static {
        types = new HashSet<Type<? extends EventHandler>>();
        types.add(AttachEvent.getType());
        types.add(BlurEvent.getType());
        types.add(ChangeEvent.getType());
        types.add(ClickEvent.getType());
        types.add(DoubleClickEvent.getType());
        types.add(CreateEvent.getType());
        types.add(DeleteEvent.getType());
        types.add(DetachEvent.getType());
        types.add(RowDoubleClickEvent.getType());
        types.add(FocusEvent.getType());
        types.add(HideEvent.getType());
        types.add(InsertEvent.getType());
        types.add(KeyPressEvent.getType());
        types.add(LoadEvent.getType());
        types.add(SearchEvent.getType());
        types.add(SelectEvent.getType());
        types.add(ShowEvent.getType());
        types.add(TimeEvent.getType());
        types.add(UpdateEvent.getType());
    }

    public static Type<? extends EventHandler> valueOf(String type) {
        for (Type<? extends EventHandler> t : types) {
            if (t.toString().equals(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException();
    }

}
