package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Style.Unit;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "FieldSet", namespace = "Whirl")
public class FieldSetBuilder extends SimpleContainerBuilder implements
		Containable {

	@JsConstructor
	public FieldSetBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public FieldSetBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.FieldSetType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		container = new FieldSet();
		
		DataValue collapsibleData = builderProperties
				.get(PropertyType.Collapsible.getCode());
		if (collapsibleData != null){
		    Boolean collapsible = collapsibleData.getBoolean();
		    if (collapsible != null && collapsible){
		        ((FieldSet) container).setCollapsible(collapsible);
		    }
		}

		// По умолчанию padding 10, из-за чего неправильно рассчитываются
		// размеры компонента
		container.getElement().setPadding(new Padding(1, 1, 0, 1));

		// Чтобы при сворачивании/разворачивании не было сдвига на пиксель
		// (как-то проще переделать?)
		((FieldSet) container).addCollapseHandler(new CollapseHandler() {
			@Override
			public void onCollapse(CollapseEvent event) {
				int margin = container.getElement().getMargins(Side.BOTTOM);
				container.getElement().getStyle()
						.setMarginBottom(margin + 1, Unit.PX);
			}
		});
		((FieldSet) container).addExpandHandler(new ExpandHandler() {
			@Override
			public void onExpand(ExpandEvent event) {
				int margin = container.getElement().getMargins(Side.BOTTOM);
				container.getElement().getStyle()
						.setMarginBottom(margin - 1, Unit.PX);
			}
		});
		return container;
	}

	@JsIgnore
	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Title.getCode())) {
			if (value != null && value.getString() != null){
				((FieldSet) container).setHeading(value.getString());
				setTitle(value.getString());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Collapsible.getCode())) {
			// Устанавливается в init, т.к. должно быть установлено до
			// отображения компонента
			return true;
		}
		return super.setProperty(name, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C> C getRealComponent() {
		return (C) container;
	}
		
	public void setExpanded(boolean expand) {
		((FieldSet)container).setExpanded(expand);
	}
	
	public boolean isExpanded() {
		return ((FieldSet)container).isExpanded();
	}

	@Override
	public void addChild(ComponentBuilder child) {
	super.addChild(child);
	}

	@Override
	public void removeChild(ComponentBuilder child) {
	super.removeChild(child);
	}

	@Override
	public void clearContainer() {
	super.clearContainer();
	}

	@Override
	public void forceLayout() {
		super.forceLayout();
	}

	@Override
	public ComponentBuilder[] getChildren() {
		return super.getChildren();
	}

	@Override
	public int getChildrenCount() {
		return super.getChildrenCount();
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
