package org.whirlplatform.app.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import org.whirlplatform.component.client.utils.URLHelper;

import java.util.HashMap;
import java.util.Map;

public class FragmentManager implements ValueChangeHandler<String> {

	private Map<String, String> fragments = new HashMap<>();

	private static FragmentManager _instance;

	private Map<String, String> oldFragments;

	private FragmentManager() {
		renewFragments(History.getToken());
	}

	public static FragmentManager get() {
		if (_instance == null) {
			_instance = new FragmentManager();
		}
		return _instance;
	}

	public void initHistory() {
		History.addValueChangeHandler(this);
		renewFragments(History.getToken());
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		renewFragments(token);

		// активируем компонент из истории
		String compId = fragments.get("hs");
		String oldCompId = oldFragments.get("hs");
		if (compId != null && !compId.equals(oldCompId)) {
			// TODO активировать компонент GWTMethod.activateComponent(compId,
			// null);
		}

	}

	private void renewFragments(String token) {
		oldFragments = fragments;
		fragments = URLHelper.fromURLEncoded(token);
	}

	public Map<String, String> getFragments() {
		return fragments;
	}

	public String getParameter(String name) {
		return fragments.get(name);
	}

	public void setParameter(String name, String value, boolean issueEvent) {
		if (name != null
				&& (fragments.get(name) == null || (fragments.get(name) != null && !fragments
						.get(name).equals(value)))) {
			oldFragments = new HashMap<>();
			oldFragments.putAll(fragments);
			if (value == null) {
				fragments.remove(name);
			} else {
				fragments.put(name, value);
			}
			History.newItem(getFullFragment(), issueEvent);
		}
	}

	public void setParameter(String name, String value) {
		setParameter(name, value, false);
	}

	public String getFullFragment() {
		StringBuilder result = new StringBuilder();
		for (String name : fragments.keySet()) {
			result.append(name + "=" + fragments.get(name) + "&");
		}
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}

	public void removeAll() {
		History.newItem(null, false);
	}

}