package org.whirlplatform.app.client;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window.Location;
import com.sencha.gxt.core.client.util.Util;
import org.whirlplatform.meta.shared.AppConstant;

public class LocationManager {

	private static LocationManager _instance;

	private String role;
	private String token;
	private boolean newSession = false;
	private String oldToken;

	public static LocationManager get() {
		if (_instance == null) {
			_instance = new LocationManager();
		}
		return _instance;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return Location.getParameter(AppConstant.TOKEN);
	}

	public void setNewSession(boolean newSession) {
		this.newSession = newSession;
	}

	public boolean isNewSession() {
		return Boolean.valueOf(Location.getParameter(AppConstant.NEW_SESSION));
	}

	public void setOldToken(String oldToken) {
		this.oldToken = oldToken;
	}

	public String getOldToken() {
		return Location.getParameter(AppConstant.OLD_TOKEN);
	}

	private String getURL() {
		UrlBuilder builder = Location.createUrlBuilder();
		if (!Util.isEmptyString(role)) {
			builder.setParameter(AppConstant.ROLE_URL, role);
		}
		if (!Util.isEmptyString(token)) {
			builder.setParameter(AppConstant.TOKEN, token);
		}
		if (!Util.isEmptyString(oldToken)) {
			builder.setParameter(AppConstant.OLD_TOKEN, oldToken);
		} else {
			builder.removeParameter(AppConstant.OLD_TOKEN);
		}
		if (newSession) {
			builder.setParameter(AppConstant.NEW_SESSION, String.valueOf(newSession));
		} else {
			builder.removeParameter(AppConstant.NEW_SESSION);
		}
		return builder.buildString();
	}

	public void reload() {
		Location.assign(getURL());
	}

}
