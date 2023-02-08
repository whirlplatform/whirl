package org.whirlplatform.server.log.impl;

import java.util.Collection;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.server.login.ApplicationUser;

public class JavaFunctionMessage extends AbstractMessage {

    private EventMetadata event;
    private Collection<DataValue> params;
    private String error;

    public JavaFunctionMessage(ApplicationUser user, EventMetadata event,
                               Collection<DataValue> params) {
        super(user);
        this.event = event;
        this.params = params;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"type\": \"javaFunction\", \"code\": \"").append(event.getCode())
            .append("\", ");
        builder.append("\"params\": ").append(getParamsString()).append("}");

        return getFullLogMessage(builder.toString());
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

    @Override
    public String toString() {
        return getMessage();
    }
}
