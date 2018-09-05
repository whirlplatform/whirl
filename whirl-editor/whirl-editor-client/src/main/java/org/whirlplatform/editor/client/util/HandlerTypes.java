package org.whirlplatform.editor.client.util;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import org.whirlplatform.component.client.event.*;

import java.util.HashSet;
import java.util.Set;

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
