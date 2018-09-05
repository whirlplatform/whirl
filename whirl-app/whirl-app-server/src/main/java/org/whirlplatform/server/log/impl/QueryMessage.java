package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.login.ApplicationUser;

public class QueryMessage extends AbstractMessage {

	String sql;
	
	public QueryMessage(ApplicationUser user, String sql) {
		super(user);
		this.sql = sql;
	}
	
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{\"type\": \"executeQuery\", \"sql\": \"").append(sql.replaceAll("\"", "\\\"")).append("\"}");
		
		return getFullLogMessage(builder.toString());
	}

	public String getSql() {
		return sql;
	}
	
	public String toString() {
		return getMessage();
	}
}
