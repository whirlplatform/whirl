package org.whirlplatform.component.client.grid;

import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.List;

public interface LoadConfigProvider {

	ClassLoadConfig getLoadConfig(List<DataValue> parameters);

}
