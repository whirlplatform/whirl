package org.whirlplatform.meta.shared.editor;

import lombok.Data;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.EventParameterImpl;
import org.whirlplatform.meta.shared.data.ParameterType;

@SuppressWarnings("serial")
@Data
public class EventParameterElement extends AbstractElement {

    private int index = -1;
    private String code;
    private EventParameterImpl parameter;
    private ComponentElement component;

    private EventElement parentEvent;

    public EventParameterElement() {

    }

    public EventParameterElement(ParameterType type) {
        super();
        parameter = new EventParameterImpl(type);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ParameterType getType() {
        return parameter.getType();
    }

    public void setType(ParameterType type) {
        parameter = new EventParameterImpl(type);
    }

    public String getComponentId() {
        return parameter.getComponentId();
    }

    public void setComponentId(String componentId) {
        this.parameter.setComponentId(componentId);
    }

    public ComponentElement getComponent() {
        return component;
    }

    public void setComponent(ComponentElement component) {
        this.component = component;
    }

    public String getComponentCode() {
        return parameter.getComponentCode();
    }

    public void setComponentCode(String coponentCode) {
        this.parameter.setComponentCode(coponentCode);
    }

    public String getStorageCode() {
        return this.parameter.getStorageCode();
    }

    public void setStorageCode(String storageCode) {
        this.parameter.setStorageCode(storageCode);
    }

    public DataValue getValue() {
        return parameter.getData();
    }

    public void setValue(DataValue value) {
        this.parameter.setData(value);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setParentEvent(EventElement parentEvent) {
        this.parentEvent = parentEvent;
    }

    public void removeFromParent() {
        parentEvent.removeParameter(this);
        parentEvent = null;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }
}
