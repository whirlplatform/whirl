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

/**
 * Центральный контейнер
 */
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
     * Фокусирует компонент.
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
     * @param enabled true - для включения компонента,
     *                false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
