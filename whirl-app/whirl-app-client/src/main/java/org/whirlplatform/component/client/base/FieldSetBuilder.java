package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Style.Unit;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Набор полей
 */
@JsType(name = "FieldSet", namespace = "Whirl")
public class FieldSetBuilder extends SimpleContainerBuilder implements
        Containable {

    @JsConstructor
    public FieldSetBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public FieldSetBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.FieldSetType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        container = new FieldSet();

        DataValue collapsibleData = builderProperties
                .get(PropertyType.Collapsible.getCode());
        if (collapsibleData != null) {
            Boolean collapsible = collapsibleData.getBoolean();
            if (collapsible != null && collapsible) {
                ((FieldSet) container).setCollapsible(collapsible);
            }
        }

        // По умолчанию padding 10, из-за чего неправильно рассчитываются
        // размеры компонента
        container.getElement().setPadding(new Padding(1, 1, 0, 1));

        // Чтобы при сворачивании/разворачивании не было сдвига на пиксель
        // (как-то проще переделать?)
        ((FieldSet) container).addCollapseHandler(new CollapseHandler() {
            @Override
            public void onCollapse(CollapseEvent event) {
                int margin = container.getElement().getMargins(Side.BOTTOM);
                container.getElement().getStyle()
                        .setMarginBottom(margin + 1, Unit.PX);
            }
        });
        ((FieldSet) container).addExpandHandler(new ExpandHandler() {
            @Override
            public void onExpand(ExpandEvent event) {
                int margin = container.getElement().getMargins(Side.BOTTOM);
                container.getElement().getStyle()
                        .setMarginBottom(margin - 1, Unit.PX);
            }
        });
        return container;
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Title.getCode())) {
            if (value != null && value.getString() != null) {
                ((FieldSet) container).setHeading(value.getString());
                setTitle(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Collapsible.getCode())) {
            // Устанавливается в init, т.к. должно быть установлено до
            // отображения компонента
            return true;
        }
        return super.setProperty(name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <C> C getRealComponent() {
        return (C) container;
    }

    /**
     * Возвращает установленное значение увеличения или свертывания поля.
     *
     * @return - boolean, true для расширения набора полей
     */
    public boolean isExpanded() {
        return ((FieldSet) container).isExpanded();
    }

    /**
     * Устанавливает увеличения или свертывания поля.
     *
     * @param expand - boolean, true для расширения набора полей
     */
    public void setExpanded(boolean expand) {
        ((FieldSet) container).setExpanded(expand);
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
