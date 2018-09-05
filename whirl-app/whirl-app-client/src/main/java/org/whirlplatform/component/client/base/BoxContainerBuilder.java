package org.whirlplatform.component.client.base;

import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public abstract class BoxContainerBuilder extends InsertContainerBuilder {

	public BoxContainerBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public BoxContainerBuilder() {
		super();
	}

	@Override
	public void addChild(ComponentBuilder child) {
		child.getComponent().setLayoutData(child.getBoxLayoutData());
		super.addChild(child);
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.BoxLayoutPack.getCode())) {
			if (value != null && value.getString() != null) {
				BoxLayoutPack pack = BoxLayoutPack.valueOf(value.getString());
				if (pack != null) {
					((BoxLayoutContainer) container).setPack(pack);
				}
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.PaddingTop.getCode())) {
			if (value != null && value.getInteger() != null) {
				Padding padding = ((BoxLayoutContainer) container).getPadding();
				if (padding == null) {
					padding = new Padding();
				}
				padding.setTop(value.getInteger());
				((BoxLayoutContainer) container).setPadding(padding);
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.PaddingRight.getCode())) {
			if (value != null && value.getInteger() != null) {
				Padding padding = ((BoxLayoutContainer) container).getPadding();
				if (padding == null) {
					padding = new Padding();
				}
				padding.setRight(value.getInteger());
				((BoxLayoutContainer) container).setPadding(padding);
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.PaddingBottom.getCode())) {
			if (value != null && value.getInteger() != null) {
				Padding padding = ((BoxLayoutContainer) container).getPadding();
				if (padding == null) {
					padding = new Padding();
				}
				padding.setBottom(value.getInteger());
				((BoxLayoutContainer) container).setPadding(padding);
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.PaddingLeft.getCode())) {
			if (value != null && value.getInteger() != null) {
				Padding padding = ((BoxLayoutContainer) container).getPadding();
				if (padding == null) {
					padding = new Padding();
				}
				padding.setLeft(value.getInteger());
				((BoxLayoutContainer) container).setPadding(padding);
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.ScrollOffset.getCode())) {
			if (value != null && value.getInteger() != null) {
				((BoxLayoutContainer) container).setScrollOffset(value.getInteger());
				return true;
			}
		}
		return super.setProperty(name, value);
	}
}
