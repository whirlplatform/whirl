package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Контейнер с одним элементом.
 */
@JsType(name = "SimpleContainer", namespace = "Whirl")
public class SimpleContainerBuilder extends ComponentBuilder implements
    Containable {

    protected SimpleContainer container;
    private ComponentBuilder topComponent;

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

    /**
     * Очищает контейнер.
     */
    @Override
    public void clearContainer() {
        if (topComponent != null) {
            removeChild(topComponent);
        }
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
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
