package org.whirlplatform.component.client.base;

import com.google.gwt.event.shared.HandlerManager;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Closable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;
@JsType(namespace = "Whirl", name = "TabItem")
public class TabItemBuilder extends ComponentBuilder implements Containable,
		Closable {

	private ComponentBuilder topComponent;

	private TabItemConfig itemConfig;

	private SimpleContainer container;

	@JsConstructor
	public TabItemBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public TabItemBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.TabItemType;
	}

	protected Component init(Map<String, DataValue> builderProperties) {
		itemConfig = new TabItemConfig("");
		container = new SimpleContainer();
		return container;
	}

	public void activate() {
		TabPanelBuilder tabPanelBuilder = (TabPanelBuilder) parentBuilder;
		TabPanel panel = (TabPanel) tabPanelBuilder.getComponent();
		panel.setActiveWidget(componentInstance);
	}

	public void setClosable(boolean closable) {
		itemConfig.setClosable(closable);
	}

	@Override
	public boolean isClosable() {
		return itemConfig.isClosable();
	}

	@Override
	public void close() {
		TabPanelBuilder panel = (TabPanelBuilder) parentBuilder;
		panel.removeChild(this);
	}

	@Override
	public void setTitle(String title) {
		itemConfig.setHTML(title == null ? "" : title);
	}

	@Override
	public String getTitle() {
		return itemConfig.getHTML();
	}

	protected TabItemConfig getTabItemConfig() {
		return itemConfig;
	}
	
	@Override
	public void addChild(ComponentBuilder child) {
		container.setWidget(child.getComponent());
		if (getTitle() == null || getTitle().isEmpty()
				&& child instanceof TitleProvider) {
            setTitle(child.getTitle());
		}
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

	public void refresh() {
		if (parentBuilder != null && parentBuilder instanceof TabPanelBuilder) {
			TabPanelBuilder panel = (TabPanelBuilder) parentBuilder;
			panel.update(this);
		}
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) container;
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

	public boolean isActive() {
        return parentBuilder != null
                && ((TabPanelBuilder) parentBuilder).getActive() == this;
    }

	@Override
	public int getChildrenCount() {
		if (topComponent == null) {
			return 0;
		}
		return 1;
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
