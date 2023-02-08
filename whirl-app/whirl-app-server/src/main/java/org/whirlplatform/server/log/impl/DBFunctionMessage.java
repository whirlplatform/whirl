package org.whirlplatform.server.log.impl;

import java.util.List;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.server.login.ApplicationUser;

public class DBFunctionMessage extends AbstractMessage {

    private EventElement event;
    private List<DataValue> params;
    private String error;

    public DBFunctionMessage(ApplicationUser user, EventElement event, List<DataValue> params) {
        super(user);
        this.event = event;
        this.params = params;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\": \"dbFunction\", \"function\": \"").append(event.getFunction())
            .append("\", ");
        builder.append("\"params\": ").append(getParamsString()).append("}");
        return getFullLogMessage(builder.toString());
    }

    public String getFunction() {
        return event.getFunction();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getParamsString() {
        if (params == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (DataValue v : params) {
            if (v == null) {
                continue;
            }
            builder.append("{\"").append(v.getCode()).append("\": ");
            builder.append("\"").append(v.isNull() ? "" : v.asString()).append("\"},");
        }
        if (params.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }
}
