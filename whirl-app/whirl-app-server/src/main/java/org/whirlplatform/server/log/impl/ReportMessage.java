package org.whirlplatform.server.log.impl;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.server.login.ApplicationUser;

import java.util.Collection;

public class ReportMessage extends AbstractMessage {

    private String reportId;
    private String reportName;
    private Collection<DataValue> params;

    public ReportMessage(ApplicationUser user, String reportId, String reportName, Collection<DataValue> params) {
        super(user);
        this.reportId = reportId;
        this.reportName = reportName;
        this.params = params;
    }

    // Получение готовой строки
    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\": \"report\", \"reportId\": \"").append(reportId).append("\", ");
        builder.append("\"reportName\": \"").append(reportName.replaceAll("\"", "\\\"")).append("\", ");
        builder.append("\"params\": ").append(getParamsString()).append("}");

        return getFullLogMessage(builder.toString());
    }

    public String getParamsString() {
        if (params == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (DataValue v : params) {
            if (v == null)
                continue;
            builder.append("{\"").append(v.getCode()).append("\": ");
            builder.append("\"").append(v.isNull() ? "" : v.asString()).append("\"},");
        }
        if (params.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
