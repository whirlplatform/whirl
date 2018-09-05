package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** Данные о текущем приложении */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@SuppressWarnings("serial")
public class ApplicationData implements Serializable {

	private String rootComponentId;

	private String name;

	private String logoutPageUrl;

	private String startMessage;

	private boolean guest;

	private String applicationCode;

	private String headerHtml;

	private boolean blocked = false;

	private Map<String, List<String>> preferences = new HashMap<>();

	private List<String> scripts = new ArrayList<String>();

	private List<String> cssList = new ArrayList<String>();

	private Map<String, List<EventMetadata>> events = new HashMap<String, List<EventMetadata>>();

	public ApplicationData() {
	}

	public ApplicationData(String rootComponentId, String name, String logoutPageUrl) {
		this.rootComponentId = rootComponentId;
		this.name = name;
		this.logoutPageUrl = logoutPageUrl;
	}

	public String getRootComponentId() {
		return rootComponentId;
	}

	public void setRootComponentId(String componentId) {
		this.rootComponentId = componentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoutPage() {
		return logoutPageUrl;
	}

	public void setLogoutPage(String dflogoutpage) {
		this.logoutPageUrl = dflogoutpage;
	}

	public String getStartMessage() {
		return startMessage;
	}

	public void setStartMessage(String startMessage) {
		this.startMessage = startMessage;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Map<String, List<String>> getPreferences() {
		return preferences;
	}

	public void addPreference(String name, String value) {
		List<String> l = preferences.get(name);
		if (l == null) {
			l = new ArrayList<String>();
			preferences.put(name, l);
		}
		l.add(value);
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getHeaderHtml() {
		return headerHtml;
	}

	public void setHeaderHtml(String headerHtml) {
		this.headerHtml = headerHtml;
	}

	public void addScript(String url) {
		scripts.add(url);
	}

	public List<String> getScripts() {
		return scripts;
	}

	public void addCss(String css) {
		cssList.add(css);
	}

	public List<String> getCss() {
		return cssList;
	}

	public void addEvent(String handlerType, EventMetadata metadata) {
		List<EventMetadata> l = events.get(handlerType);
		if (l == null) {
			l = new ArrayList<EventMetadata>();
			events.put(handlerType, l);
		}
		l.add(metadata);
	}

	public List<EventMetadata> getCreateEvents() {
		List<EventMetadata> result = events.get("CreateEvent");
		if (result == null) {
			result = new ArrayList<EventMetadata>();
		}
		return result;
	}

	public List<EventMetadata> getNonCreateEvents() {
		List<EventMetadata> result = new ArrayList<EventMetadata>();
		for (Entry<String, List<EventMetadata>> e : events.entrySet()) {
			if (!"CreateEvent".equals(e.getKey())) {
				result.addAll(e.getValue());
			}
		}
		return result;
	}

}
