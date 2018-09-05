package org.whirlplatform.meta.shared;

import org.whirlplatform.rpc.shared.SessionToken;

import java.io.Serializable;

/**
 * Пользователь
 */
//@SuppressWarnings("serial")
public class ClientUser implements Serializable {

	private static final long serialVersionUID = -6627494423245563572L;

	public static transient ClientUser currentUser;

	private SessionToken sessionToken;

	/**
	 * ФИО
	 */
	private String name;

	/** Запрашиваемый URL */
	private String requesUrl;

	/** Имя события */
	private String event;

//	/** Параметры события передаваемые в URL запросе вида p1=...&p2=... */
//	@SuppressWarnings("rawtypes")
//	private LinkedHashMap<String, NativeParameter> eventParams;

	private boolean guest;

	/** Логин */
	private String login;

	private String timeZone;

	public ClientUser() {
	}

	public SessionToken getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(SessionToken token) {
		this.sessionToken = token;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

//	@SuppressWarnings("rawtypes")
//	public LinkedHashMap<String, NativeParameter> getEventParams() {
//		return eventParams;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public void setEventParams(LinkedHashMap<String, NativeParameter> eventParams) {
//		this.eventParams = eventParams;
//	}

	public String getRequestUrl() {
		return requesUrl;
	}

	public void setRequestUrl(String requestUrl) {
		requesUrl = requestUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void setCurrentUser(ClientUser user) {
		ClientUser.currentUser = user;
	}

	public static ClientUser getCurrentUser() {
		return ClientUser.currentUser;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public boolean isGuest() {
		return guest;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		return timeZone;
	}
}
