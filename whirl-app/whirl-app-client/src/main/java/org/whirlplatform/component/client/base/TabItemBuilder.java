package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.whirlplatform.component.client.Closable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class TabItemBuilder extends ComponentBuilder implements Containable,
		Closable {

	private ComponentBuilder topComponent;

	private TabItemConfig itemConfig;

	private SimpleContainer container;

	public TabItemBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public TabItemBuilder() {
		super();
	}
	

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
	
}
