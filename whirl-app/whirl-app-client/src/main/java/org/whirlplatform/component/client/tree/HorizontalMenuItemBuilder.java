package org.whirlplatform.component.client.tree;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

public class HorizontalMenuItemBuilder extends ComponentBuilder implements
        ClickEvent.HasClickHandlers, Containable {

    private ComponentHorizontalMenuItem wrapper;
    private String imageUrl;
    private String html;
    private Menu menu;
    private MenuItem menuItem;
    private List<ComponentBuilder> children;

    public HorizontalMenuItemBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public HorizontalMenuItemBuilder() {
        super();
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        menu = new Menu();
        menuItem = new MenuItem();
        menuItem.addSelectionHandler(new SelectionHandler<Item>() {

            @Override
            public void onSelection(SelectionEvent<Item> event) {
                fireEvent(new ClickEvent());
            }
        });
        wrapper = new ComponentHorizontalMenuItem();
        return wrapper;
    }

    protected void initChildren() {
        children = new ArrayList<ComponentBuilder>();
    }

    public Menu getMenu() {
        return menu;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.ImageUrl.getCode())) {
            if (value != null && value.getString() != null) {
                setImage(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Html.getCode())) {
            if (value != null) {
                setHtml(value.getString());
            }
        }
        return super.setProperty(name, value);
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
        menuItem.setHTML(html);
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String url) {
        imageUrl = url;
        menuItem.setIcon(getImageResource(url));
    }

    private ImageResource getImageResource(String imageUrl) {
        return IconHelper.getImageResource(UriUtils.fromString(imageUrl), 16,
                16);
    }

    @Override
    public void addChild(ComponentBuilder child) {
        if (child instanceof HorizontalMenuItemBuilder) {
            HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
            menu.add(builder.getMenuItem());
            children.add(child);
        }
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        if (children.remove(child)) {
            HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
            menu.remove(builder.getMenuItem());
        }
    }

    @Override
    public void clearContainer() {
        for (ComponentBuilder b : children) {
            removeChild(b);
        }
    }

    @Override
    public void forceLayout() {
        for (ComponentBuilder child : children) {
            HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
            if (builder.getChildrenCount() > 0) {
                builder.getMenuItem().setSubMenu(builder.getMenu());
            }
            builder.forceLayout();
        }
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[children.size()]);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.HorizontalMenuItemType;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ComponentHorizontalMenuItem getRealComponent() {
        return wrapper;
    }

    public class ComponentHorizontalMenuItem extends Container {

        public ComponentHorizontalMenuItem() {
            setElement(menuItem.getElement());
        }

    }

}
