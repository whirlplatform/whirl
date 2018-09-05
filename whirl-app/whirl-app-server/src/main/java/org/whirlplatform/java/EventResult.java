package org.whirlplatform.java;

import org.whirlplatform.meta.shared.JavaEventResult;
import org.whirlplatform.meta.shared.data.EventParameter;

public class EventResult {

    private JavaEventResult result;

    public EventResult() {
        result = new JavaEventResult();
    }

    public void setMessage(String message) {
        result.setMessage(message);
    }

    public String getMessage() {
        return result.getMessage();
    }

    public void setTitle(String title) {
        result.setTitle(title);
    }

    public String getTitle() {
        return result.getTitle();
    }

    public void setMessageType(String messageType) {
        result.setMessageType(messageType);
    }

    public String getMessageType() {
        return result.getMessageType();
    }

    public void addParameter(EventParameter value) {
        result.addParameter(value);
    }

    public void setNextEventCode(String nextEventCode) {
        result.setNextEventCode(nextEventCode);
    }

    public String getNextEventCode() {
        return result.getNextEventCode();
    }

    public JavaEventResult asInternal() {
        return result;
    }
}
