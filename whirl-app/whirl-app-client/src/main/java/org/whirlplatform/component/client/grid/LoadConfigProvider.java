package org.whirlplatform.component.client.grid;

import java.util.List;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataValue;

public interface LoadConfigProvider {

    ClassLoadConfig getLoadConfig(List<DataValue> parameters);

}
