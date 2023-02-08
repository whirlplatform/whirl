package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.data.DataValue;

public abstract class InsertContainerBuilder extends ComponentBuilder implements
    Containable {

    protected List<ComponentBuilder> children;
    protected InsertResizeContainer container;

    public InsertContainerBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public InsertContainerBuilder() {
        super();
    }

    protected void initChildren() {
        children = new ArrayList<ComponentBuilder>();
    }

    @Override
    public void addChild(ComponentBuilder child) {
        int index = child.getIndexPosition();
        if (index != -1 && index <= container.getWidgetCount()) {
            container.insert(child.getComponent(), index);
        } else {
            container.add(child.getComponent());
        }
        children.add(child);
        child.setParentBuilder(this);
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        if (container.remove(child.getComponent())) {
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
        container.forceLayout();
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[0]);
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) componentInstance;
    }

    public int getChildrenCount() {
        return children.size();
    }

}
