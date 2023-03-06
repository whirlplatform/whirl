package org.whirlplatform.meta.shared;

import com.google.gwt.user.client.Command;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;
import org.whirlplatform.meta.shared.data.EventParameter;

@Data
public class JavaScriptEventResult implements EventResult {

    private String nextEventCode;
    private String title;
    private String message;
    private String messageType;
    private Map<Integer, EventParameter> parameters = new TreeMap<Integer, EventParameter>();
    private EventMetadata nextEvent;
    private String rawValue;

    public JavaScriptEventResult() {
    }

    @Override
    public boolean isReady() {
        return getNextEvent() != null;
    }

    @Override
    public EventMetadata getNextEvent() {
        return nextEvent;
    }

    @Override
    public void setNextEvent(EventMetadata event) {
        this.nextEvent = event;
    }

    @Override
    public boolean hasNextEvent() {
        return getNextEvent() != null;
    }

    @Override
    public String getNextEventCode() {
        return nextEventCode;
    }

    @Override
    public void setNextEventCode(String nextEventCode) {
        this.nextEventCode = nextEventCode;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(String type) {
        this.messageType = type;
    }

    @Override
    public void addParameter(EventParameter parameter) {
        parameters.put(parameters.size(), parameter);
    }

    @Override
    public void setParameter(Integer index, EventParameter parameter) {
        parameters.put(index, parameter);
    }

    @Override
    public Map<Integer, EventParameter> getParametersMap() {
        return parameters;
    }

    private final void commandExecute(Command command) {
        if (isReady()) {
            command.execute();
        }
    }

    @Override
    public String getRawValue() {
        return rawValue;
    }

    @Override
    public void setRawValue(String value) {
        this.rawValue = value;
    }
}
