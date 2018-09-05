package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class VBoxContainerBuilder extends BoxContainerBuilder {

	public VBoxContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public VBoxContainerBuilder() {
		super();
	}
	
	@Override
	public ComponentType getType() {
		return ComponentType.VBoxContainerType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		initChildren();
		container = new VBoxLayoutContainer();
		return container;
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.VBoxAlign.getCode())) {
			if (value != null && value.getString() != null){
				VBoxLayoutAlign align = VBoxLayoutAlign.valueOf(value.getString());
				if (align != null) {
					((VBoxLayoutContainer) container).setVBoxLayoutAlign(align);
				}
				return true;
			}
		}
		return super.setProperty(name, value);
	}
}
