package org.whirlplatform.component.client.event;

import java.util.List;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.data.DataValue;

@JsType(name = "Context")
public class JavaScriptContext {

    private ComponentBuilder source;
    private List<DataValue> parameters;
    private String nextEventCode;
    @JsIgnore
    public JavaScriptContext() {
    }

    @JsIgnore
    public JavaScriptContext(ComponentBuilder source, List<DataValue> parameters) {
        this.source = source;
        this.parameters = parameters;
    }

    @JsIgnore
    public ComponentBuilder getSource() {
        return source;
    }

    @JsIgnore
    public DataValue[] getParameters() {
        return parameters.toArray(new DataValue[0]);
    }

    @JsIgnore
    public DataValue getParameter(int index) {
        return parameters.get(index);
    }

    @JsIgnore
    public DataValue getParameter(String code) {
        if (code == null) {
            return null;
        }
        for (DataValue v : parameters) {
            if (code.equals(v.getCode())) {
                return v;
            }
        }
        return null;
    }

    public String getNextEvent() {
        return nextEventCode;
    }
    public void setNextEvent(String eventCode) {
        this.nextEventCode = eventCode;
    }
}
