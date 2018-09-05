package org.whirlplatform.component.client.base;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class HorizontalContainerBuilder extends InsertContainerBuilder {

	public HorizontalContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public HorizontalContainerBuilder() {
		super();
	}

	@Override
	public ComponentType getType() {
		return ComponentType.HorizontalContainerType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		initChildren();
		container = new HorizontalLayoutContainer();
		return container;
	}

	@Override
	public void addChild(ComponentBuilder child) {
		child.getComponent().setLayoutData(child.getHorizontalLayoutData());
		super.addChild(child);
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.ScrollMode.getCode())) {
			if (value != null && value.getString() != null) {
				ScrollMode mode = ScrollMode.valueOf(value.getString());
				if (mode != null) {
					((HorizontalLayoutContainer) container).setScrollMode(mode);
				}
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.AdjustForScroll.getCode())) {
			if (value != null && value.getString() != null) {
				((HorizontalLayoutContainer) container).setAdjustForScroll(Boolean.valueOf(value.getString()));
			}
			return true;
		}
		return super.setProperty(name, value);
	}
}
