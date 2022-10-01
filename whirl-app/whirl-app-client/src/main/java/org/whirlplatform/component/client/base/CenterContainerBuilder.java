package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

@JsType(name = "CenterContainer", namespace = "Whirl")
public class CenterContainerBuilder extends SimpleContainerBuilder {

    @JsConstructor
    public CenterContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public CenterContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.CenterContainerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        container = new CenterLayoutContainer();
        return container;
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
