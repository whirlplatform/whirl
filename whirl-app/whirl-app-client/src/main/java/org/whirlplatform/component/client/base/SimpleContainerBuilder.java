package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class SimpleContainerBuilder extends ComponentBuilder implements
		Containable {

	private ComponentBuilder topComponent;
	protected SimpleContainer container;

	public SimpleContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public SimpleContainerBuilder() {
		super();
	}
	
	@Override
	public ComponentType getType() {
		return ComponentType.SimpleContainerType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		container = new SimpleContainer();
		return container;
	}

	@Override
	public void addChild(ComponentBuilder child) {
		container.setWidget(child.getComponent());
		topComponent = child;
		child.setParentBuilder(this);
	}

	@Override
	public void removeChild(ComponentBuilder child) {
		if (container.remove(child.getComponent())) {
			topComponent = null;
			child.setParentBuilder(null);
		}
	}

	@Override
	public void clearContainer() {
		if (topComponent != null) {
			removeChild(topComponent);
		}
	}

	@Override
	public void forceLayout() {
		container.forceLayout();
	}

	@Override
	public ComponentBuilder[] getChildren() {
		if (topComponent != null) {
			ComponentBuilder[] result = { topComponent };
			return result;
		} else {
			return new ComponentBuilder[0];
		}
	}

	@Override
	protected <C> C getRealComponent() {
		return (C)container;
	}

	@Override
	public int getChildrenCount() {
		if (topComponent == null) {
			return 0;
		}
		return 1;
	}
	
}
