package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.login.ApplicationUser;

public class ChartMessage extends AbstractMessage {

	private String chartId;
	private String chartName;
	
	public ChartMessage(ApplicationUser user, String chartId, String chartName) {
		super(user);
		this.chartId = chartId;
		this.chartName = chartName;
	}
	
	// Получение готовой строки
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"type\": \"chart\", \"chartId\": \"").append(chartId).append("\", ");
		builder.append("\"chartName\": \"").append(chartName.replaceAll("\"", "\\\"")).append("\", ");
		builder.append("\"params\": ").append("}");
		
		return getFullLogMessage(builder.toString());
	}

	@Override
	public String toString() {
		return getMessage();
	}
}
