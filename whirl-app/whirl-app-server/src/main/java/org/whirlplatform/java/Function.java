package org.whirlplatform.java;

import org.whirlplatform.meta.shared.data.DataValue;

import java.util.List;

public interface Function {

    EventResult execute(List<DataValue> parameters);

    void setContext(FunctionContext context);

}
