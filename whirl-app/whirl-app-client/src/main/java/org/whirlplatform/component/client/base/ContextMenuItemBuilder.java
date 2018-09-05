package org.whirlplatform.component.client.base;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class ContextMenuItemBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers {

	public ContextMenuItemBuilder() {
		super();
	}

	public ContextMenuItemBuilder(Map<String, DataValue> params) {
		super(params);
	}

	@Override
	public ComponentType getType() {
		return ComponentType.ContextMenuItemType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		return new MenuItem();
	}

	@Override
	protected void initHandlers() {
		((MenuItem) componentInstance).addSelectionHandler(new SelectionHandler<Item>() {

			@Override
			public void onSelection(SelectionEvent<Item> event) {
				fireEvent(new ClickEvent());
			}
		});
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Title.getCode())) {
			if (value != null) {
				setTitle(value.getString());
			}
		} else if (name.equalsIgnoreCase(PropertyType.ImageUrl.getCode())) {
			if (value != null && value.getString() != null) {
				setImageUrl(value.getString());
			}
		}
		return super.setProperty(name, value);
	}

	@Override
	public void setTitle(String title) {
		((MenuItem) componentInstance).setHTML(title == null ? "" : title);
	}

	public void setImageUrl(String imageUrl) {
		((MenuItem) componentInstance).setIcon(IconHelper.getImageResource(UriUtils.fromString(imageUrl), 16, 16));
	}

	@Override
	protected void buildContextMenu() {
		if (!contextMenuItems.isEmpty()) {
			Menu menu = new Menu();
			for (ContextMenuItemBuilder item : contextMenuItems) {
				menu.add(item.create());
			}
			((MenuItem) componentInstance).setSubMenu(menu);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C> C getRealComponent() {
		return (C) componentInstance;
	}

	@Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
		return ensureHandler().addHandler(ClickEvent.getType(), handler);
	}
}
