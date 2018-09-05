package org.whirlplatform.component.client;

import org.whirlplatform.meta.shared.data.DataValue;

public interface Parameter<T extends DataValue> {

	T getFieldValue();

	void setFieldValue(T value);

}
