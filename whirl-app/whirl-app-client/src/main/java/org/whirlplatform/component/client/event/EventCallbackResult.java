package org.whirlplatform.component.client.event;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventCallbackResult {
    private ComponentBuilder source;
    private List<DataValue> params = new ArrayList<DataValue>();
    private Object rawValue;

    public ComponentBuilder getSource() {
        return source;
    }

    public void setSource(ComponentBuilder source) {
        this.source = source;
    }

    public DataValue[] getParameters() {
        return params.toArray(new DataValue[0]);
    }

    public void setParameters(Collection<DataValue> params) {
        if (params != null) {
            this.params = new ArrayList<DataValue>(params);
        }
    }

    public DataValue getParameter(int index) {
        return params.get(index);
    }

    public void addParameter(DataValue param) {
        params.add(param);
    }

    public Object getRawValue() {
        return rawValue;
    }

    public void setRawValue(Object rawValue) {
        this.rawValue = rawValue;
    }

    public int getParametersCount() {
        return params.size();
    }
}
