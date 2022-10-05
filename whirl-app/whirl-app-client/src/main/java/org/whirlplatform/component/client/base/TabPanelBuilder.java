package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelMessages;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.CloseProvider;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.*;
import java.util.Map.Entry;

/**
 * Панель вкладок
 */
@JsType(namespace = "Whirl", name = "TabPane")
public class TabPanelBuilder extends ComponentBuilder implements Containable {

	private TabPanel panel;

	private List<ComponentBuilder> children;

	private Map<ComponentBuilder, TabItemBuilder> builderToTabItem;

	public TabPanelBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public TabPanelBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.TabPanelType;
	}

	protected Component init(Map<String, DataValue> builderProperties) {
		children = new ArrayList<ComponentBuilder>();
		builderToTabItem = new HashMap<ComponentBuilder, TabItemBuilder>();

		panel = new TabPanel();
		panel.setTabScroll(true);
		panel.setCloseContextMenu(true);
		panel.addSelectionHandler(new SelectionHandler() {
			public void onSelection(
					final com.google.gwt.event.logical.shared.SelectionEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						((ResizeContainer) event.getSelectedItem())
								.forceLayout();
					}
				});

            }
        });
		panel.addCloseHandler(new CloseHandler<Widget>() {
			@Override
			public void onClose(CloseEvent<Widget> event) {
				Widget widget = event.getItem();
				ComponentBuilder builder = null;
				for (ComponentBuilder b : children) {
					if (b.getComponent() == widget) {
						builder = b;
					}
				}
				for (Entry<ComponentBuilder, TabItemBuilder> b : builderToTabItem
						.entrySet()) {
					if (b.getValue().getComponent() == widget) {
						builder = b.getKey();
					}
				}
				removeChild(builder, true);
			}
		});
		
		TabPanelMessages messages = new TabPanelMessages() {
			@Override
			public String closeOtherTabs() {
				return AppMessage.Util.MESSAGE.closeOthers();
			}
			
			@Override
			public String closeTab() {
				return AppMessage.Util.MESSAGE.close();
			}
		};
		panel.setMessages(messages);
		return panel;
	}

	@Override
	protected void setInitProperties(Map<String, DataValue> properties) {
		super.setInitProperties(properties);
		if (panel.getWidgetCount() > 0) {
			panel.setActiveWidget(panel.getWidget(0));
		}
	}

	public void setBorder(boolean show) {
		TabPanel panel = (TabPanel) componentInstance;
		panel.setBodyBorder(show);
	}

	protected void update(TabItemBuilder item) {
		TabPanel panel = (TabPanel) componentInstance;
		panel.update(item.getComponent(), item.getTabItemConfig());
	}

	@Override
	public void addChild(ComponentBuilder child) {
		TabItemBuilder itemBuilder;
		if (child instanceof TabItemBuilder) {
			itemBuilder = (TabItemBuilder) child;
		} else {
			itemBuilder = new TabItemBuilder();
			itemBuilder.setProperties(child.getProperties(), false);
			itemBuilder.create();
			if (child instanceof CloseProvider) {
                itemBuilder.setClosable(child.isClosable());
			}
			String title = null;
			if (child instanceof TitleProvider) {
                title = child.getTitle();
			}
			if (title == null || title.isEmpty()) {
				title = "...";
			}
			itemBuilder.setProperty(PropertyType.Title.getCode(),new DataValueImpl(DataType.STRING, title));
			itemBuilder.setProperty(PropertyType.LayoutDataIndex.getCode(),
					new DataValueImpl(DataType.NUMBER, panel.getWidgetCount()));
			itemBuilder.addChild(child);
			builderToTabItem.put(child, itemBuilder);
		}
		panel.insert(itemBuilder.getComponent(),
				itemBuilder.getIndexPosition(), itemBuilder.getTabItemConfig());
		children.add(child);
		child.setParentBuilder(this);
		if(child.getProperties().containsKey(PropertyType.Active.getCode())){
			Boolean active = child.getProperties().get(PropertyType.Active.getCode()).getBoolean();
			if(active){
				setActive(child);
			}
		}
	}

	@Override
	public void removeChild(ComponentBuilder child) {
		removeChild(child, false);
	}

	private void removeChild(ComponentBuilder child, boolean ignoreComponent) {
		ComponentBuilder removable = child;
		if (!(child instanceof TabItemBuilder)) {
			removable = builderToTabItem.remove(child);
		}
		TabPanel panel = (TabPanel) componentInstance;
		if (panel.remove(removable.getComponent()) || ignoreComponent) {
			children.remove(child);
			child.setParentBuilder(null);
		}
	}

	@Override
	public void clearContainer() {
		for (ComponentBuilder c : children) {
			removeChild(c);
		}
	}

	@Override
	public void forceLayout() {
		TabPanel panel = (TabPanel) componentInstance;
		panel.forceLayout();
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) panel;
	}

	@Override
	public ComponentBuilder[] getChildren() {
		return children.toArray(new ComponentBuilder[0]);
	}

	public ComponentBuilder getActive() {
		Widget active = panel.getActiveWidget();
		for (ComponentBuilder c : children) {
			if (active == c.getComponent()) {
				return c;
			}
		}
		return null;
	}

	public void setActive(ComponentBuilder child) {
		if (!children.contains(child)) {
			return;
		}
		Widget widget;
		if (panel.getWidgetIndex(child.getComponent()) > -1) {
			widget = child.getComponent();
		} else {
			widget = builderToTabItem.get(child).getComponent();
		}
		panel.setActiveWidget(widget);
	}

	@Override
	public int getChildrenCount() {
		return children.size();
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
