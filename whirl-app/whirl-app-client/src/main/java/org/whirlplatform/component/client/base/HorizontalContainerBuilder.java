package org.whirlplatform.component.client.base;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
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
 * Контейнер располагающей компоненты в один ряд.
 */
@JsType(name = "HorizontalContainer", namespace = "Whirl")
public class HorizontalContainerBuilder extends InsertContainerBuilder {

    @JsConstructor
    public HorizontalContainerBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public HorizontalContainerBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.HorizontalContainerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        container = new HorizontalLayoutContainer();
        return container;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        child.getComponent().setLayoutData(child.getHorizontalLayoutData());
        super.addChild(child);
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.ScrollMode.getCode())) {
            if (value != null && value.getString() != null) {
                ScrollMode mode = ScrollMode.valueOf(value.getString());
                if (mode != null) {
                    ((HorizontalLayoutContainer) container).setScrollMode(mode);
                }
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.AdjustForScroll.getCode())) {
            if (value != null && value.getString() != null) {
                ((HorizontalLayoutContainer) container).setAdjustForScroll(
                    Boolean.valueOf(value.getString()));
            }
            return true;
        }
        return super.setProperty(name, value);
    }

    /**
     * Возвращает код компонента.
     *
     * @return код компонента
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true, если компонент скрыт
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
