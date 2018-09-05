package org.whirlplatform.component.client;

import com.sencha.gxt.widget.core.client.Component;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.List;

public interface HasCreateParameters {

	Component create(List<DataValue> parameters);
	
}
