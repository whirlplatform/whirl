package org.whirlplatform.component.client.window;

import com.sencha.gxt.widget.core.client.Component;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "TaskBar", namespace = "Whirl")
public class TaskBarBuilder extends ComponentBuilder {

	private TaskBar taskBar;

	@JsConstructor
	public TaskBarBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public TaskBarBuilder() {
		this(Collections.emptyMap());
	}
	
	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.TaskBarType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		taskBar = new TaskBar();
		return taskBar;
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) taskBar;
	}

	/**
	 * Checks if component is in hidden state.
	 *
	 * @return true if component is hidden
	 */
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Sets component's hidden state.
	 *
	 * @param hidden true - to hide component, false - to show component
	 */
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Focuses component.
	 */
	public void focus() {
		if (componentInstance == null) {
			return;
		}
		componentInstance.focus();
	}

	/**
	 * Checks if component is enabled.
	 *
	 * @return true if component is enabled
	 */
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets component's enabled state.
	 *
	 * @param enabled true - to enable component, false - to disable component
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

}
