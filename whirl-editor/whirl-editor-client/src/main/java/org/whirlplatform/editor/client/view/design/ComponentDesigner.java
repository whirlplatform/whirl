package org.whirlplatform.editor.client.view.design;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.ComponentTypeUtil;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.component.client.form.GridLayoutContainer;
import org.whirlplatform.editor.client.component.surface.DefaultSurfaceAppearance;
import org.whirlplatform.editor.client.component.surface.GrayBorderSurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceAppearance;
import org.whirlplatform.editor.client.dnd.ContainerDropTarget;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.util.EditorHelper;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class ComponentDesigner extends AbstractDesigner {

    private static final double DEFAULT_COMPONENT_WIDTH = 600;
    private static final double DEFAULT_COMPONENT_HEIGHT = 350;

    protected ComponentBuilder rootBuilder;
    protected Map<ComponentElement, ComponentBuilder> builders = new HashMap<>();

    protected ToolBar toolBar;
    /**
     * Компонент, выделенный по клику мышкой
     */
    protected ComponentElement selectedElement;
    protected Map<Element, Surface> selectedSurface = new HashMap<>();
    private TextButton delete;
    private SimpleContainer container;
    private Resizable resizable;
    private FlowLayoutContainer outer;
    private FlowLayoutContainer inner;

    public ComponentDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
        initUI();
        initRootComponent(element);
        setWidth((int) designWidth(element));
        setHeight((int) designHeight(element));
    }

    protected void initUI() {
        setBorders(true);
        getElement().getStyle().setBackgroundColor("#FFFFFF");

        initToolbar();

        outer = new FlowLayoutContainer();
        outer.setScrollMode(ScrollMode.ALWAYS);

        inner = new FlowLayoutContainer();

        container = new SimpleContainer();
        container.setBorders(true);
        container.setLayoutData(new MarginData(8));
        setAllowTextSelection(false);
        inner.add(container);
        outer.add(inner);
        add(outer);

        resizable = new Resizable(container, Dir.E, Dir.SE, Dir.S);

        resizable.setMinHeight(3);
        resizable.setMinWidth(3);
        resizable.setMaxHeight(25000);
        resizable.setMaxWidth(25000);
    }

    protected void initToolbar() {
        toolBar = new ToolBar();
        toolBar.setHeight(26);

        delete = new TextButton();
        delete.setTitle(EditorMessage.Util.MESSAGE.design_remove());
        delete.setIcon(EditorBundle.INSTANCE.cross());
        delete.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                if (selectedElement != null) {
                    fireRemoveComponentEvent(
                        new RemoveComponentEvent(rootElement, selectedElement));
                }
            }
        });
        toolBar.add(delete);

        add(toolBar);
    }

    protected Container getDesignContainer() {
        return inner;
    }

    private void initRootComponent(ComponentElement element) {
        rootBuilder = createComponent(element, false);

        // rootBuilder.getComponent().getElement().applyStyles("border: dashed
        // 1px red; position: relative;");

        if (rootBuilder instanceof Containable) {
            for (ComponentElement e : element.getChildren()) {
                ComponentBuilder child = initChildComponent(e);
                if (child == null) {
                    continue;
                }
                ((Containable) rootBuilder).addChild(child);
                builders.put(e, child);
            }
        }

        bindRootComponent(rootElement, rootBuilder);
        container.setWidget(rootBuilder.getComponent());
    }

    protected void clearSelection() {
        selectedElement = null;
        setAllBuildersSelected(false);
        fireSelectComponentEvent(new SelectComponentEvent());
    }

    protected void repaint() {
        builders.clear();
        initRootComponent(rootElement);
    }

    /**
     * Установка обработчиков событий DnD для селектора
     *
     * @param element Компонент
     * @param builder Билдер компонента
     */
    protected DragSource initChildDragSource(ComponentElement element, ComponentBuilder builder) {
        Widget component = builder.getComponent();
        DragSource source = new DragSource(component);
        source.setData(element);
        return source;
    }

    protected ComponentBuilder initChildComponent(ComponentElement element) {
        ComponentBuilder child = createComponent(element, true);
        if (child == null) {
            return null;
        }
        DesignerHelper.setConstantComponentStyles(element, child);

        ((Containable) rootBuilder).addChild(child);
        initChildDragSource(element, child);
        setBuilderZIndex(child);

        return child;
    }

    private void setBuilderZIndex(ComponentBuilder builder) {
        Component c = builder.getComponent();
        if (c != null) {
            c.getElement().getStyle().setZIndex(10);
        }
    }

    protected void bindRootComponent(ComponentElement element, ComponentBuilder builder) {
        initRootDropTarget(element, builder);
        initRootEvents(element);
    }

    protected void initRootDropTarget(final ComponentElement element,
                                      final ComponentBuilder builder) {
        if (builder instanceof Containable) {
            new ContainerDropTarget((Container) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onRootDrop(element, builder, event.getData(), null);
                    super.onDragDrop(event);
                }
            };
        }
    }

    protected void onRootDrop(ComponentElement rootElement, ComponentBuilder rootBuilder,
                              Object dropData,
                              Object locationData) {
        ComponentElement element = onRootDropCreate(dropData);
        onRootDropSetLocationData(rootElement, element, locationData);
        onRootDropAddChild(rootElement, element);
    }

    protected ComponentElement onRootDropCreate(Object dropData) {
        ComponentElement element = null;
        if (dropData instanceof ComponentElement) {
            element = (ComponentElement) dropData;
        } else if (dropData instanceof ComponentType) {
            element = EditorHelper.newComponentElement((ComponentType) dropData, defaultLocale);
        }
        return element;
    }

    protected void onRootDropSetLocationData(ComponentElement rootElement, ComponentElement element,
                                             Object locationData) {
        // nothing
    }

    protected void onRootDropAddChild(ComponentElement parentElement, ComponentElement element) {
        fireAddComponentEvent(new AddComponentEvent(parentElement, element));
    }

    /**
     * Выделение компонента по клику мышкой
     *
     * @param element Компонент
     */
    private void initRootEvents(ComponentElement element) {
        if (rootBuilder instanceof Containable) {
            for (final ComponentElement e : element.getChildren()) {
                final ComponentBuilder child = builders.get(e);
                if (child == null) {
                    continue;
                }
                initChildEvents(e, child);
            }
        }
    }

    protected void initChildEvents(final ComponentElement element, final ComponentBuilder builder) {
        Widget component;
        if (builder instanceof FormBuilder) {
            component = ((GridLayoutContainer) builder.getComponent()).getTable();
        } else {
            component = builder.getComponent();
        }
        component.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onSelectComponent(element, builder);
            }
        }, ClickEvent.getType());
    }

    private void onSelectComponent(final ComponentElement element, final ComponentBuilder builder) {
        // Снимаем выделение со всех компонентов
        setAllBuildersSelected(false);
        XElement el = builder.getComponent().getElement();
        if (selectedElement != null) {
            if (!selectedElement.equals(element)) {
                selectedElement = element;
                // Устанавливаем выделение на компонент
                setBuilderSelected(el, true);

                // Подгружаем свойства выделенного компонента
                fireSelectComponentEvent(new SelectComponentEvent(element));
            } else {
                selectedElement = null;
            }
        } else {
            selectedElement = element;
            // Устанавливаем выделение на компонент
            setBuilderSelected(el, true);
            // Подгружаем свойства выделенного компонента
            fireSelectComponentEvent(new SelectComponentEvent(element));
        }
    }

    /**
     * Изменение стиля компонента при его выделении
     *
     * @param selected Установить или снять выделение
     */
    protected void setAllBuildersSelected(boolean selected) {
        for (ComponentBuilder builder : builders.values()) {
            XElement el = builder.getComponent().getElement();
            setBuilderSelected(el, selected);
        }
    }

    protected void setBuilderSelected(Element el, boolean selected) {
        Surface surface = setElementSelected(el,
            new DefaultSurfaceAppearance(
                GWT.<GrayBorderSurfaceResources>create(GrayBorderSurfaceResources.class)),
            selected);
        if (surface != null) {
            surface.getElement().getStyle().setZIndex(2);
        }
    }

    /**
     * Изменение стиля компонента при его выделении
     *
     * @param element  DOM-элемент
     * @param selected Установить или снять выделение
     */
    protected Surface setElementSelected(Element element, SurfaceAppearance appearance,
                                         boolean selected) {
        Surface surface;
        if (selected) {
            surface = selectedSurface.get(element);
            if (surface == null) {
                surface = new Surface(appearance);
                selectedSurface.put(element, surface);
            }
            surface.show(element, true);
        } else {
            surface = selectedSurface.get(element);
            if (surface != null) {
                surface.hide();
            }
            selectedSurface.remove(element);
        }
        return surface;
    }

    protected ComponentType componentType(ComponentElement element) {
        return element.getType();
    }

    protected ComponentBuilder createComponent(ComponentElement element, boolean createChildren) {
        ComponentType type = componentType(element);
        ComponentBuilder builder = ComponentTypeUtil.findBuilder(type);
        if (builder == null) {
            return null;
        }

        setUIProperties(builder, element.getProperties());
        DesignerHelper.setConstantComponentProperties(element, builder);

        if (createChildren && builder instanceof Containable) {
            for (ComponentElement child : element.getChildren()) {
                ComponentBuilder childBuilder = createComponent(child, true);
                ((Containable) builder).addChild(childBuilder);

                builders.put(child, childBuilder);
            }
        }
        return builder;
    }

    protected void setUIProperties(ComponentBuilder builder,
                                   Map<PropertyType, PropertyValue> properties) {
        for (Entry<PropertyType, PropertyValue> entry : properties.entrySet()) {
            String name = entry.getKey().getCode();
            DataValue tmpVal = entry.getValue().getValue(null);
            DataType type = (tmpVal == null) ? null : tmpVal.getType();
            if (!entry.getValue().isReplaceable() || DataType.STRING.equals(type)) {
                setUIProperty(builder, name, tmpVal);
            }
        }
    }

    private void setUIProperty(ComponentBuilder builder, String name, DataValue value) {
        PropertyType type = PropertyType.parse(name, builder.getType());
        if (type != null && type.isUI()) {
            builder.setProperty(name, value);
        }
    }

    protected double designWidth(ComponentElement element) {
        Double v = element.getProperty(PropertyType.Width).getValue(defaultLocale).getDouble();
        if (v == null) {
            v = 1.0;
        }
        if (v == 0 && rootBuilder instanceof Containable && element.getChildren().size() == 1) {
            ComponentElement child = (ComponentElement) element.getChildren().toArray()[0];
            Double childWidth =
                child.getProperty(PropertyType.Width).getValue(defaultLocale).getDouble();
            if (childWidth != null) {
                v = childWidth;
            }
        }
        if (v == 0) {
            return DEFAULT_COMPONENT_WIDTH;
        }
        return v;
    }

    protected double designHeight(ComponentElement element) {
        Double v = element.getProperty(PropertyType.Height).getValue(defaultLocale).getDouble();
        if (v == null) {
            v = 1.0;
        }
        if (v == 0 && rootBuilder instanceof Containable && element.getChildren().size() == 1) {
            ComponentElement child = (ComponentElement) element.getChildren().toArray()[0];
            Double childHeight =
                child.getProperty(PropertyType.Height).getValue(defaultLocale).getDouble();
            if (childHeight != null) {
                v = childHeight;
            }
        }
        if (v == 0) {
            return DEFAULT_COMPONENT_HEIGHT;
        }
        return v;
    }
}
