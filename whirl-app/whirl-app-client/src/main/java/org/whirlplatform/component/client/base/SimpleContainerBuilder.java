package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "SimpleContainer", namespace = "Whirl")
public class SimpleContainerBuilder extends ComponentBuilder implements
        Containable {

    private ComponentBuilder topComponent;
    protected SimpleContainer container;

    @JsConstructor
    public SimpleContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public SimpleContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.SimpleContainerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        container = new SimpleContainer();
        return container;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        container.setWidget(child.getComponent());
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

    @Override
    public ComponentBuilder[] getChildren() {
        if (topComponent != null) {
            ComponentBuilder[] result = {topComponent};
            return result;
        } else {
            return new ComponentBuilder[0];
        }
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) container;
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
