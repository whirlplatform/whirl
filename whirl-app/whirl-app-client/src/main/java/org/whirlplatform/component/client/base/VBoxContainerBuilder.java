package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Контейнер располагающей компоненты в виде вертикальных колонок.
 */
@JsType(name = "VBoxContainer", namespace = "Whirl")
public class VBoxContainerBuilder extends BoxContainerBuilder {

    @JsConstructor
    public VBoxContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public VBoxContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.VBoxContainerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        container = new VBoxLayoutContainer();
        return container;
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.VBoxAlign.getCode())) {
            if (value != null && value.getString() != null) {
                VBoxLayoutAlign align = VBoxLayoutAlign.valueOf(value.getString());
                if (align != null) {
                    ((VBoxLayoutContainer) container).setVBoxLayoutAlign(align);
                }
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    @Override
    public void addChild(ComponentBuilder child) {
        super.addChild(child);
    }
}
