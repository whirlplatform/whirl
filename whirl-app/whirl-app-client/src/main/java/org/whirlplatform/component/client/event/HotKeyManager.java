package org.whirlplatform.component.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import org.whirlplatform.component.client.hotkey.HotKeyHandler;

import java.util.HashMap;
import java.util.Map;

public class HotKeyManager {

	private static HotKeyManager _instance;

	private Map<HotKeyHandler, HandlerRegistration> registrations = new HashMap<HotKeyHandler, HandlerRegistration>();

	private HotKeyManager() {
	}

	public static HotKeyManager get() {
		if (_instance == null) {
			_instance = new HotKeyManager();
		}
		return _instance;
	}

	public void addHotKeyHandler(HotKeyHandler handler) {
		HandlerRegistration r = Event.addNativePreviewHandler(handler);
		registrations.put(handler, r);
	}

	public void removeHotKeyHandler(HotKeyHandler handler) {
		HandlerRegistration r = registrations.get(handler);
		if (r != null) {
			r.removeHandler();
			registrations.remove(handler);
		}
	}

	public void clear() {
		for (HotKeyHandler h : registrations.keySet()) {
			HandlerRegistration r = registrations.get(h);
			r.removeHandler();
			registrations.remove(h);
		}
	}
}
