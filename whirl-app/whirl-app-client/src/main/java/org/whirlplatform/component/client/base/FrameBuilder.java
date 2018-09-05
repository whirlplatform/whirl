package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.user.client.ui.Frame;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class FrameBuilder extends ComponentBuilder {

	private Frame frame;

	private WidgetComponent wrapper;

	private String template;

	public FrameBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public FrameBuilder() {
		super();
	}
	
	
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

}
