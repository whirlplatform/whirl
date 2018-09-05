package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class CenterContainerBuilder extends SimpleContainerBuilder {

	public CenterContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public CenterContainerBuilder() {
		super();
	}
	
	@Override
	public ComponentType getType() {
		return ComponentType.CenterContainerType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		container = new CenterLayoutContainer();
		return container;
	}

}
