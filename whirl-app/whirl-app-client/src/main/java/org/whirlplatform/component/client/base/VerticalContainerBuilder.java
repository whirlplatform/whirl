package org.whirlplatform.component.client.base;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/**
 * Вертикальный контейнер
 */
@JsType(name = "VerticalContainer", namespace = "Whirl")
public class VerticalContainerBuilder extends InsertContainerBuilder {

    @JsConstructor
    public VerticalContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public VerticalContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.VerticalContainerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        container = new VerticalLayoutContainer();
        return container;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        child.getComponent().setLayoutData(child.getVerticalLayoutData());
        super.addChild(child);
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.ScrollMode.getCode())) {
            if (value != null) {
                ScrollMode mode = ScrollMode.valueOf(value.getString());
                if (mode != null) {
                    ((VerticalLayoutContainer) container).setScrollMode(mode);
                }
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.AdjustForScroll.getCode())) {
            if (value != null) {
                Boolean adjust = value.getBoolean();
                if (adjust != null) {
                    ((VerticalLayoutContainer) container).setAdjustForScroll(adjust);
                }
            }
            return true;
        }
        return super.setProperty(name, value);
    }

    @Override
    public void removeChild(ComponentBuilder child) {
		super.removeChild(child);
    }

    /**
     * Очищает контейнер.
     */
    @Override
    public void clearContainer() {
        super.clearContainer();
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
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
}
