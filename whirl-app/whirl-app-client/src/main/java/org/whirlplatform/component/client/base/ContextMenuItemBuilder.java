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
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

@JsType(name = "ContextMenuItem", namespace = "Whirl")
public class ContextMenuItemBuilder extends ComponentBuilder
    implements ClickEvent.HasClickHandlers {

    @JsIgnore
    public ContextMenuItemBuilder() {
        this(Collections.emptyMap());
    }

    @JsConstructor
    public ContextMenuItemBuilder(@JsOptional Map<String, DataValue> params) {
        super(params);
    }

    @JsIgnore
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

    @JsIgnore
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
        ((MenuItem) componentInstance).setIcon(
            IconHelper.getImageResource(UriUtils.fromString(imageUrl), 16, 16));
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

    @JsIgnore
    @Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
        return ensureHandler().addHandler(ClickEvent.getType(), handler);
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
