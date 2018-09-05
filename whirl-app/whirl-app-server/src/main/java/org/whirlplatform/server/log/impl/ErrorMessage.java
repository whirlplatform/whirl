package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.login.ApplicationUser;

public class ErrorMessage extends AbstractMessage {

	private String error;
	
	public ErrorMessage(ApplicationUser user, String error) {
		super(user);
		this.error = error;
	}

	@Override
	public String getMessage() {
		return error;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
