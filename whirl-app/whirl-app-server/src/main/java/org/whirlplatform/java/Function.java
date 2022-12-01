package org.whirlplatform.java;

import java.util.List;
import org.whirlplatform.meta.shared.data.DataValue;

public interface Function {

    EventResult execute(List<DataValue> parameters);

    void setContext(FunctionContext context);

}
