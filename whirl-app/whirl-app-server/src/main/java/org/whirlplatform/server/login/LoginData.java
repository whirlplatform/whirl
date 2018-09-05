package org.whirlplatform.server.login;

public class LoginData {

	private String login;
	private String password;
	private String ip;

	public LoginData(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

}
