package org.whirlplatform.component.client.base;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Компонент - Бордер-контейнер
 */
@JsType(name = "BorderContainer", namespace = "Whirl")
public class BorderContainerBuilder extends ComponentBuilder implements
        Containable {

    // Добавление стиля position:absolute !important нужно чтобы перебить relative в некоторых компонентах,
    // т.к. с relative неправильно рассчитываются отступы
    public interface ChildBundle extends ClientBundle {
        //		@Source("BorderContainerChild.css")
        @Source("BorderContainerChild.gss")
        ChildStyle getCss();
    }

    public interface ChildStyle extends CssResource {
        String borderContainerChild();
    }

    private static ChildStyle containerChildStyle;

    static {
        ChildBundle bundle = GWT.create(ChildBundle.class);
        containerChildStyle = bundle.getCss();
        containerChildStyle.ensureInjected();
    }

    private BorderLayoutContainer borderContainer;

    private List<ComponentBuilder> children = new ArrayList<ComponentBuilder>();

    @JsConstructor
    public BorderContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public BorderContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.BorderContainerType;
    }

    protected Component init(Map<String, DataValue> builderProperties) {
        borderContainer = new BorderLayoutContainer();
        borderContainer.setBorders(false);
        return borderContainer;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        BorderLayoutData data = child.getBorderLayoutData();
        child.getComponent().getElement().addClassName(containerChildStyle.borderContainerChild());
        if (child.isCenter()) {
            borderContainer.setCenterWidget(child.getComponent(), data);
        } else if (child.isNorth()) {
            borderContainer.setNorthWidget(child.getComponent(), data);
        } else if (child.isWest()) {
            borderContainer.setWestWidget(child.getComponent(), data);
        } else if (child.isSouth()) {
            borderContainer.setSouthWidget(child.getComponent(), data);
        } else if (child.isEast()) {
            borderContainer.setEastWidget(child.getComponent(), data);
        } else {
            borderContainer.setWidget(child.getComponent());
        }
        children.add(child);
        child.setParentBuilder(this);
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        BorderLayoutContainer borderContainer = (BorderLayoutContainer) componentInstance;
        if (borderContainer.remove(child.getComponent())) {
            children.remove(child);
            child.setParentBuilder(null);
            child.getComponent().getElement().removeClassName(containerChildStyle.borderContainerChild());
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
        BorderLayoutContainer borderContainer = (BorderLayoutContainer) componentInstance;
        borderContainer.forceLayout();
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) borderContainer;
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[0]);
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