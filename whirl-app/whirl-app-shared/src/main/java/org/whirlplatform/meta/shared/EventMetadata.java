package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.whirlplatform.meta.shared.data.EventParameter;

@SuppressWarnings("serial")
@JsonAutoDetect(fieldVisibility = Visibility.ANY,
    creatorVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public class EventMetadata implements Serializable, Cloneable {

    private String id;
    private String code;
    private EventType type;
    private String source;

    private String componentId;
    private String targetComponentId;

    private TreeMap<Integer, EventParameter> parameters = new TreeMap<Integer, EventParameter>();
    private boolean confirm = false;
    private String confirmText;
    private boolean wait = false;
    private String waitText;
    private boolean named = false;
    private boolean createNew;

    @SuppressWarnings("unused")
    public EventMetadata() {
    }

    public EventMetadata(EventType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EventType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getTargetComponentId() {
        return targetComponentId;
    }

    public void setTargetComponentId(String targetComponentId) {
        this.targetComponentId = targetComponentId;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public String getWaitText() {
        return waitText;
    }

    public void setWaitText(String waitText) {
        this.waitText = waitText;
    }

    public boolean isNamed() {
        return named;
    }

    public void setNamed(boolean named) {
        this.named = named;
    }

    public void addParameter(EventParameter parameter) {
        Integer key;
        if (parameters.isEmpty()) {
            key = 0;
        } else {
            key = parameters.lastKey() + 1;
        }
        parameters.put(key, parameter);
    }

    public void setParameter(Integer index, EventParameter parameter) {
        parameters.put(index, parameter);
    }

    public List<EventParameter> getParametersList() {
        return new ArrayList<EventParameter>(parameters.values());
    }

    public EventMetadata clone() {
        EventMetadata newEvent = new EventMetadata(type);
        newEvent.setId(id);
        newEvent.setCode(code);
        newEvent.setSource(source);
        newEvent.setComponentId(componentId);
        newEvent.setTargetComponentId(targetComponentId);
        newEvent.setConfirm(confirm);
        newEvent.setConfirmText(confirmText);
        newEvent.setWait(wait);
        newEvent.setWaitText(waitText);
        newEvent.setCreateNew(createNew);

        for (Entry<Integer, EventParameter> v : parameters.entrySet()) {
            newEvent.parameters.put(v.getKey(), v.getValue());
        }
        return newEvent;
    }

    public boolean isCreateNew() {
        return createNew;
    }

    public void setCreateNew(boolean createNew) {
        this.createNew = createNew;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public TreeMap<Integer, EventParameter> getParameters() {
        return parameters;
    }

    public void setParameters(TreeMap<Integer, EventParameter> parameters) {
        this.parameters = parameters;
    }
}