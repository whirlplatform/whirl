package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "HBoxContainer", namespace = "Whirl")
public class HBoxContainerBuilder extends BoxContainerBuilder {

	@JsConstructor
	public HBoxContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public HBoxContainerBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
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

	@JsIgnore
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
