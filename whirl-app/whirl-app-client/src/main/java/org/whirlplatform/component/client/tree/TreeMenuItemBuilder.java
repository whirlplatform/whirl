package org.whirlplatform.component.client.tree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeMenuItemBuilder extends ComponentBuilder implements
        ClickEvent.HasClickHandlers, Containable {

    public class ComponentMenuItem extends Component {

        public ComponentMenuItem() {
            // TODO Auto-generated constructor stub
        }

    }

    private String imageUrl;
    private ComponentMenuItem menuItem;

    private List<ComponentBuilder> children;

    public TreeMenuItemBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public TreeMenuItemBuilder() {
        super();
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        menuItem = new ComponentMenuItem();
        return menuItem;
    }

    public ComponentMenuItem getMenuItem() {
        return menuItem;
    }

    protected void initChildren() {
        children = new ArrayList<ComponentBuilder>();
    }

    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.ImageUrl.getCode())) {
            if (value != null) {
                setImage(value.getString());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String url) {
        imageUrl = url;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        if (child instanceof TreeMenuItemBuilder) {
            children.add(child);
        }
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        // TODO не будет работать в яваскрипте для уже построенного
        children.remove(child);
    }

    @Override
    public void clearContainer() {
        // TODO не правильно, надо удалять из children тоже
        // container.clear();
    }

    @Override
    public void forceLayout() {
        // container.forceLayout();
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
    protected SimpleContainer getRealComponent() {
        return null;
    }

    protected void initHandlers() {
        // container.addDomHandler(
        // new com.google.gwt.event.dom.client.ClickHandler() {
        //
        // @Override
        // public void onClick(
        // com.google.gwt.event.dom.client.ClickEvent event) {
        // if (isEnabled()) {
        // fireEvent(new ClickEvent());
        // }
        // }
        // }, com.google.gwt.event.dom.client.ClickEvent.getType());
    }
}
