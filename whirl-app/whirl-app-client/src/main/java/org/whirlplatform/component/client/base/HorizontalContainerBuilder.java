package org.whirlplatform.component.client.base;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "HorizontalContainer", namespace = "Whirl")
public class HorizontalContainerBuilder extends InsertContainerBuilder {

	@JsConstructor
	public HorizontalContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public HorizontalContainerBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
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

	@JsIgnore
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

	/**
	 * Returns component's code.
	 *
	 * @return component's code
	 */
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * Checks if component is in hidden state.
	 *
	 * @return true if component is hidden
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Sets component's hidden state.
	 *
	 * @param hidden true - to hide component, false - to show component
	 */
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Focuses component.
	 */
	@Override
	public void focus() {
		super.focus();
	}

	/**
	 * Checks if component is enabled.
	 *
	 * @return true if component is enabled
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets component's enabled state.
	 *
	 * @param enabled true - to enable component, false - to disable component
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
}
