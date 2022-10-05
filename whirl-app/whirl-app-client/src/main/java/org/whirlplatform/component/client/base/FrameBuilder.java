package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.user.client.ui.Frame;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
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

@JsType(name = "Frame", namespace = "Whirl")
public class FrameBuilder extends ComponentBuilder {

	private Frame frame;

	private WidgetComponent wrapper;

	private String template;

	@JsConstructor
	public FrameBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public FrameBuilder() {
		this(Collections.emptyMap());
	}
	
	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.FrameType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		frame = new Frame();
		wrapper = new WidgetComponent(frame);
		return wrapper;
	}

	@JsIgnore
	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Template.getCode())) {
			if (value != null && value.getString() != null){
				setTemplate(value.getString());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Url.getCode())) {
			if (value != null && value.getString() != null){
				frame.setUrl(value.getString());
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	private void setTemplate(String template) {
		this.template = template;
		if (template != null) {
			FrameElement frameEl = frame.getElement().cast();
			BodyElement bodyEl = frameEl.getContentDocument().getBody();
			bodyEl.setInnerHTML(template);
		}
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) frame;
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
