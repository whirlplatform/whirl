package org.whirlplatform.component.client.tree;

import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.base.InsertContainerBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.*;

/**
 * Горизонтальное меню
 */
@JsType(name = "HorizontalMenu", namespace = "Whirl")
public class HorizontalMenuBuilder extends InsertContainerBuilder implements Containable {

    private MenuBar menuBar;
    private Map<Menu, MenuBarItem> map = new HashMap<Menu, MenuBarItem>();
    private List<ComponentBuilder> children;

    @JsConstructor
    public HorizontalMenuBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public HorizontalMenuBuilder() {
        this(Collections.emptyMap());
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        children = new ArrayList<ComponentBuilder>();
        menuBar = new MenuBar() {
            @Override
            protected void onClick(Event event) {
                event.stopPropagation();
                event.preventDefault();

                MenuBarItem item = (MenuBarItem) findWidget(event.getEventTarget().cast());
                if (item != null) {
                    Menu m = item.getMenu();
                    // TODO Если пустое, не открываем
//					if (m.getChildren().size() > 0) {
                    toggle(item);
//					}
                    // Выполнение события
                    for (ComponentBuilder child : children) {
                        HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
                        if (builder.getMenu() == m) {
                            child.fireEvent(new ClickEvent());
                            break;
                        }
                    }
                }
            }
        };
        return menuBar;
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.HorizontalMenuType;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        if (child instanceof HorizontalMenuItemBuilder) {
            HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
            Menu menu = builder.getMenu();
            MenuBarItem menuBarItem = map.get(menu);
            if (menuBarItem != null) {
                menuBar.remove(menuBarItem);
                map.remove(menu);
                menuBarItem = null;
            }
            if (menuBarItem == null) {
                menuBarItem = new MenuBarItem(builder.getHtml(), menu);
            }

            menuBar.add(menuBarItem);
            map.put(menu, menuBarItem);
            children.add(child);
            child.setParentBuilder(this);
        }
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        if (child instanceof HorizontalMenuItemBuilder) {
            if (children.remove(child)) {
                HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
                menuBar.remove(map.get(builder.getMenu()));
                map.remove(builder.getMenu());
                child.setParentBuilder(null);
            }
        }
    }

    /**
     * Очищает контейнер.
     */
    @Override
    public void clearContainer() {
        for (ComponentBuilder b : children) {
            removeChild(b);
        }
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    @Override
    public void forceLayout() {
        for (ComponentBuilder child : children) {
            HorizontalMenuItemBuilder builder = (HorizontalMenuItemBuilder) child;
            builder.forceLayout();
        }
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[children.size()]);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MenuBar getRealComponent() {
        return menuBar;
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
