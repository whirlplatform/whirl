package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class HBoxContainerBuilder extends BoxContainerBuilder {

	public HBoxContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public HBoxContainerBuilder() {
		super();
	}
	
	@Override
	public ComponentType getType() {
		return ComponentType.HBoxContainerType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		initChildren();
		container = new HBoxLayoutContainer();
		return container;
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.HBoxAlign.getCode())) {
			if (value != null && value.getString() != null){
				HBoxLayoutAlign align = HBoxLayoutAlign.valueOf(value.getString());
				if (align != null) {
					((HBoxLayoutContainer) container).setHBoxLayoutAlign(align);
				}
				return true;
			}
		}
		return super.setProperty(name, value);
	}

}
