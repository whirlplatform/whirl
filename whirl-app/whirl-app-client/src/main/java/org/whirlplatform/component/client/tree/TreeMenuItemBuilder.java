package org.whirlplatform.component.client.tree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Элемент меню дерева
 */
@JsType(name = "TreeMenuItem", namespace = "Whirl")
public class TreeMenuItemBuilder extends ComponentBuilder implements
        ClickEvent.HasClickHandlers, Containable {

    public class ComponentMenuItem extends Component {

        public ComponentMenuItem() {
            // TODO Auto-generated constructor stub
        }

    }

    private String imageUrl;
    private ComponentMenuItem menuItem;

    private List<ComponentBuilder> children;

    @JsConstructor
    public TreeMenuItemBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public TreeMenuItemBuilder() {
        this(Collections.emptyMap());
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        initChildren();
        menuItem = new ComponentMenuItem();
        return menuItem;
    }

    @JsIgnore
    public ComponentMenuItem getMenuItem() {
        return menuItem;
    }

    protected void initChildren() {
        children = new ArrayList<ComponentBuilder>();
    }

    @JsIgnore
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.ImageUrl.getCode())) {
            if (value != null) {
                setImage(value.getString());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    /**
     *
     * @return
     */
    public String getImage() {
        return imageUrl;
    }

    /**
     * Устанавливает изображение.
     *
     * @param url - String
     */
    public void setImage(String url) {
        imageUrl = url;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        if (child instanceof TreeMenuItemBuilder) {
            children.add(child);
        }
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        // TODO не будет работать в яваскрипте для уже построенного
        children.remove(child);
    }

    @JsIgnore
    @Override
    public void clearContainer() {
        // TODO не правильно, надо удалять из children тоже
        // container.clear();
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    @Override
    public void forceLayout() {
        // container.forceLayout();
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[children.size()]);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.HorizontalMenuItemType;
    }

    @JsIgnore
    @Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected SimpleContainer getRealComponent() {
        return null;
    }

    protected void initHandlers() {
        // container.addDomHandler(
        // new com.google.gwt.event.dom.client.ClickHandler() {
        //
        // @Override
        // public void onClick(
        // com.google.gwt.event.dom.client.ClickEvent event) {
        // if (isEnabled()) {
        // fireEvent(new ClickEvent());
        // }
        // }
        // }, com.google.gwt.event.dom.client.ClickEvent.getType());
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true если компонент скрыт
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
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
