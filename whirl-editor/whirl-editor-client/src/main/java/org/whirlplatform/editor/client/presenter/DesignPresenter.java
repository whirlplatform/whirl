package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent.DndDragCancelHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.ComponentTypeUtil;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.base.*;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.component.client.form.GridLayoutContainer;
import org.whirlplatform.component.client.form.GridLayoutContainer.Cell;
import org.whirlplatform.component.client.hotkey.HotKeyBuilder;
import org.whirlplatform.component.client.tree.HorizontalMenuBuilder;
import org.whirlplatform.component.client.tree.TreeMenuBuilder;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.component.surface.BlueDottedSurfaceResources;
import org.whirlplatform.editor.client.component.surface.DefaultSurfaceAppearance;
import org.whirlplatform.editor.client.component.surface.GrayBorderSurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceAppearance;
import org.whirlplatform.editor.client.dnd.*;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent.MouseSelectionHandler;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.meta.NewPropertyElement;
import org.whirlplatform.editor.client.meta.NullFreeComponentElement;
import org.whirlplatform.editor.client.util.EditorHelper;
import org.whirlplatform.editor.client.view.DesignView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.FormElement;

import java.util.*;
import java.util.Map.Entry;

//TODO здесь все очень сложно и много в одном месте. большую часть этого кода надо перенести в DesignView
//причем необходимо создать иерархию с общей реализацией и отделные наследники под функционал компонента
//DesignView события на применение свойств к компонентам
@Presenter(view = DesignView.class)
public class DesignPresenter extends BasePresenter<DesignPresenter.IDesignView, EditorEventBus> {

    public interface IDesignView extends IsWidget {

        void setRootComponent(ComponentBuilder builder, double width, double height);

        void addResizeEndHandler(ResizeEndHandler handler);

        void clearSizes();

        Component getContainer();

        HandlerRegistration addResizeHandler(ResizeHandler handler);

        void setShowHidden(boolean showHidden);
    }

    private static final double DEFAULT_COMPONENT_WIDTH = 600;
    private static final double DEFAULT_COMPONENT_HEIGHT = 350;

    private ApplicationElement currentApplication;

    private ComponentElement rootElement;
    private ComponentBuilder rootBuilder;
    private Map<ComponentElement, ComponentBuilder> builders = new HashMap<ComponentElement, ComponentBuilder>();

    /**
     * Компонент, выделенный по клику мышкой
     */
    private ComponentElement selectedElement;

    /**
     * Список компонентов, выделенных с помощью селектора
     */
    private List<ComponentElement> selectedElements = new ArrayList<ComponentElement>();

    /**
     * Список ячеек, выделенных с помощью селектора
     */
    private List<CellRangeElement> selectedCells = new ArrayList<CellRangeElement>();
    private Map<Element, Surface> selectedSurface = new HashMap<Element, Surface>();
    private Map<Element, Surface> gridSurface = new HashMap<Element, Surface>();

    private boolean showHidden;

    private MouseSelectionWrapper selector;
    private MouseSelectionHandler selectionHandler = new MouseSelectionHandler() {

        @Override
        public void onMouseSelection(MouseSelectionEvent event) {
            if (event.isSelect()) {
                setSelection(
                        new Rectangle(event.getClientX(), event.getClientY(), event.getWidth(), event.getHeight()));
            } else {
                clearSelection();
            }
        }
    };

    // Вынес сюда, чтобы не создавался новый handler при каждом изменении
    private ResizeEndHandler viewResizeEndHandler = new ResizeEndHandler() {
        @Override
        public void onResizeEnd(ResizeEndEvent event) {
            int width = event.getTarget().getOffsetWidth();
            int height = event.getTarget().getOffsetHeight();
            rootBuilder.setProperty(PropertyType.Height.getCode(), new DataValueImpl(DataType.NUMBER, height));
            rootBuilder.setProperty(PropertyType.Width.getCode(), new DataValueImpl(DataType.NUMBER, width));
            eventBus.addElement(rootElement,
                    new NewPropertyElement(PropertyType.Width, currentApplication.getDefaultLocale(), width));
            eventBus.addElement(rootElement,
                    new NewPropertyElement(PropertyType.Height, currentApplication.getDefaultLocale(), height));
            eventBus.syncComponentPropertyToElement(rootElement);

            if (ComponentType.FormBuilderType.equals(rootBuilder.getType())) {
                // ((GridLayoutContainer) rootBuilder.getComponent())
                // .forceLayout();
            }
            selectGroupCell();
            view.clearSizes();
            refreshFormGrid(rootBuilder);
        }
    };

    public DesignPresenter() {
        super();
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.currentApplication = application;
    }

    public void onBuildApp() {
        selector = new MouseSelectionWrapper(view.getContainer());
        selector.addMouseSelectionHandler(selectionHandler);
        view.addResizeEndHandler(viewResizeEndHandler);

        // Изменение размеров внешнего контейнера
        view.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                refreshFormGrid(rootBuilder);
            }
        });
    }

    private void clear() {
        rootElement = null;
        rootBuilder = null;
        builders.clear();
        clearSelection();
        clearSelectSurface();
        clearGridSurface();
    }

    private double designWidth() {
        Double v = rootElement.getProperty(PropertyType.Width).getValue(currentApplication.getDefaultLocale())
                .getDouble();
        if (v == null) {
            v = 1.0;
        }
        if (v == 0 && rootBuilder instanceof Containable && rootElement.getChildren().size() == 1) {
            ComponentElement child = (ComponentElement) rootElement.getChildren().toArray()[0];
            Double childWidth = child.getProperty(PropertyType.Width).getValue(currentApplication.getDefaultLocale())
                    .getDouble();
            if (childWidth != null) {
                v = childWidth;
            }
        }
        if (v == 0) {
            return DEFAULT_COMPONENT_WIDTH;
        }
        return v;
    }

    private double designHeight() {
        Double v = rootElement.getProperty(PropertyType.Height).getValue(currentApplication.getDefaultLocale())
                .getDouble();
        if (v == null) {
            v = 1.0;
        }
        if (v == 0 && rootBuilder instanceof Containable && rootElement.getChildren().size() == 1) {
            ComponentElement child = (ComponentElement) rootElement.getChildren().toArray()[0];
            Double childHeight = child.getProperty(PropertyType.Height).getValue(currentApplication.getDefaultLocale())
                    .getDouble();
            if (childHeight != null) {
                v = childHeight;
            }
        }
        if (v == 0) {
            return DEFAULT_COMPONENT_HEIGHT;
        }
        return v;
    }

    public void onOpenElement(AbstractElement element) {
        selectedElement = null;
        reloadElement(element);
    }


    public void reloadElement(AbstractElement element) {
        reloadElement(element, true);
    }

    public void reloadElement(AbstractElement element, boolean clearSelections) {
        if (!(element instanceof ComponentElement)) {
            return;
        }
        boolean isRepaint = rootElement == element;
        if (clearSelections) {
            clear();
        }
        this.rootElement = (ComponentElement) element;
        rootBuilder = createComponent(rootElement, false);

        // rootBuilder.getComponent().getElement().applyStyles("border: dashed
        // 1px red; position: relative;");

        if (rootBuilder instanceof Containable) {
            for (ComponentElement e : rootElement.getChildren()) {
                ComponentBuilder child = createComponent(e, true);
                if (child == null) {
                    continue;
                }
                // Куда-то вынести из этого класса?
                // Окаймление пустых контейнеров и радио/чек группы
                if ((e.getType().isContainer() || ComponentType.RadioGroupType.equals(e.getType())
                        || ComponentType.CheckGroupType.equals(e.getType()))
                        && !ComponentType.TabPanelType.equals(e.getType())) {
                    // TabPanel странно себя ведет при установке setWidth <= 0
                    // (ставит ширину равной 1000000px)
                    XElement childEl = child.getComponent().getElement();
                    if (e.getChildren() == null || e.getChildren().size() == 0) {
                        child.getComponent().setHeight(30);
                        child.getComponent().setWidth(-1);
                    }
                    childEl.applyStyles("border: 1px dotted red; position: relative;");
                } else if (ComponentType.ReportType.equals(e.getType())
                        || ComponentType.EditGridType.equals(e.getType())) {
                    XElement childEl = child.getComponent().getElement();
                    child.getComponent().setHeight(30);
                    child.getComponent().setWidth(-1);
                    childEl.applyStyles("border: 1px dotted red; position: relative;");
                }

                if (child instanceof TabPanelBuilder) {
                    bindComponent(e, child);
                }

                ((Containable) rootBuilder).addChild(child);
                initComponentDND(e, child);
                setBuilderZIndex(child);

                builders.put(e, child);
            }
        }

        view.setRootComponent(rootBuilder, designWidth(), designHeight());
        eventBus.openElementView(view);
        bindComponent(rootElement, rootBuilder);

        if (isRepaint) {
            if (!selectedCells.isEmpty()) {
                selectGroupCell();
            }
        }

        // Включаем селектор, только если находимся на форме
        if (rootBuilder instanceof FormBuilder) {
            selector.setEnabled(true);
            setCellSelected(false);
            refreshFormGrid(rootBuilder);
        } else {
            selector.setEnabled(false);
        }
    }

    /**
     * Нарисовать сетку для формы.
     */
    private void refreshFormGrid(final ComponentBuilder builder) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                if (builder instanceof FormBuilder) {
                    clearGridSurface();
                    GridLayoutContainer layout = (GridLayoutContainer) builder.getComponent();
                    FlexTable table = layout.getTable();
                    for (int r = 0; r < table.getRowCount(); r++) {
                        for (int c = 0; c < table.getCellCount(r); c++) {
                            Element e = table.getCellFormatter().getElement(r, c);
                            Surface s = new Surface(new DefaultSurfaceAppearance(
                                    GWT.<BlueDottedSurfaceResources>create(BlueDottedSurfaceResources.class)));
                            s.show(e, true);
                            gridSurface.put(e, s);
                        }
                    }
                    if (!selectedCells.isEmpty()) {
                        selectGroupCell();
                    }
                }
                if (selectedElement != null && builders.containsKey(selectedElement)) {
                    Element el = builders.get(selectedElement).getComponent().getElement();
                    setBuilderSelected(el, true);
                }
            }
        });
    }

    private void bindComponent(ComponentElement element, ComponentBuilder builder) {
        initDND(element, builder);
        initEvents(element);
    }

    private void initDND(final ComponentElement element, final ComponentBuilder builder) {
        if (builder instanceof BorderContainerBuilder) {
            @SuppressWarnings("unused")
            BorderLayoutDropTarget target = new BorderLayoutDropTarget((BorderLayoutContainer) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getLocation());
                    super.onDragDrop(event);
                    repaint();
                }

            };
        } else if (builder instanceof HorizontalContainerBuilder || builder instanceof HBoxContainerBuilder) {
            HorizontalLayoutDropTarget target = new HorizontalLayoutDropTarget(
                    (InsertResizeContainer) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getIndex());
                    super.onDragDrop(event);
                    repaint();
                }

            };
            target.setFeedback(Feedback.BOTH);
        } else if (builder instanceof VerticalContainerBuilder || builder instanceof VBoxContainerBuilder) {
            VerticalLayoutDropTarget target = new VerticalLayoutDropTarget(
                    (InsertResizeContainer) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getIndex());
                    super.onDragDrop(event);
                    repaint();
                }

            };
            target.setFeedback(Feedback.BOTH);
        } else if (builder instanceof FormBuilder) {
            @SuppressWarnings("unused")
            GridLayoutDropTarget target = new GridLayoutDropTarget((GridLayoutContainer) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getCell());
                    super.onDragDrop(event);
                    repaint();
                }

            };
        } else if (builder instanceof TabPanelBuilder) {
            @SuppressWarnings("unused")
            TabPanelDropTarget target = new TabPanelDropTarget((TabPanel) builder.getComponent()) {
                @Override
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getIndex());
                    super.onDragDrop(event);
                    repaint();
                }
            };
        } else if (builder instanceof TreeMenuBuilder) {
            @SuppressWarnings("unused")
            TreeMenuDropTarget target = new TreeMenuDropTarget(((TreeMenuBuilder) builder).getTree()) {
                @Override
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getIndex());
                    super.onDragDrop(event);
                    repaint();
                }

            };
        } else if (builder instanceof HorizontalMenuBuilder) {
            @SuppressWarnings("unused")
            HorizontalMenuDropTarget target = new HorizontalMenuDropTarget((MenuBar) builder.getComponent()) {
                @Override
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), getIndex());
                    super.onDragDrop(event);
                    repaint();
                }
            };
        } else if (builder instanceof Containable) {
            @SuppressWarnings("unused")
            ContainerDropTarget target = new ContainerDropTarget((Container) builder.getComponent()) {
                protected void onDragDrop(DndDropEvent event) {
                    onDrop(element, builder, event.getData(), null);
                    super.onDragDrop(event);
                    repaint();
                }

            };
        }
    }

    /**
     * Выделение компонента по клику мышкой
     *
     * @param element Компонент
     */
    private void initEvents(ComponentElement element) {
        if (rootBuilder instanceof Containable) {
            for (final ComponentElement e : element.getChildren()) {
                final ComponentBuilder builder = builders.get(e);
                if (builder == null) {
                    continue;
                }
                Widget component;
                if (builder instanceof FormBuilder) {
                    component = ((GridLayoutContainer) builder.getComponent()).getTable();
                } else {
                    component = builder.getComponent();
                }
                component.addDomHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        // Снимаем выделение со всех компонентов
                        setBuilderSelected(false);
                        XElement el = builder.getComponent().getElement();
                        if (selectedElement != null) {
                            if (!selectedElement.equals(e)) {
                                selectedElement = e;
                                // Подгружаем свойства выделенного компонента
                                eventBus.selectTreeElement(e);
                                // Устанавливаем выделение на компонент
                                setBuilderSelected(el, true);
                            } else {
                                selectedElement = null;
                            }
                        } else {
                            selectedElement = e;
                            // Устанавливаем выделение на компонент
                            setBuilderSelected(el, true);
                            // Подгружаем свойства выделенного компонента
                            eventBus.selectTreeElement(e);
                        }
                    }
                }, ClickEvent.getType());
            }
        }
    }

    /**
     * Снятие выделения с ячеек
     */
    public void onClearSelection() {
        clearSelection();
    }

    /**
     * Изменение стиля компонента при его выделении
     *
     * @param selected Установить или снять выделение
     */
    private void setBuilderSelected(boolean selected) {
        for (ComponentBuilder builder : builders.values()) {
            XElement el = builder.getComponent().getElement();
            setBuilderSelected(el, selected);
        }
    }

    private void setBuilderSelected(Element el, boolean selected) {
        Surface surface = setElementSelected(el,
                new DefaultSurfaceAppearance(GWT.<GrayBorderSurfaceResources>create(GrayBorderSurfaceResources.class)),
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
    private Surface setElementSelected(Element element, SurfaceAppearance appearance, boolean selected) {
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

    private void clearSelectSurface() {
        clearSurfaceMap(selectedSurface);
    }

    private void clearGridSurface() {
        clearSurfaceMap(gridSurface);
    }

    private void clearSurfaceMap(Map<Element, Surface> map) {
        Iterator<Element> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            Element e = iter.next();
            Surface s = map.get(e);
            if (s != null) {
                s.hide();
            }
            iter.remove();
        }
    }

    /**
     * Изменение стиля для выделенных селектором компонентов (
     * {@link #selectedElements})
     *
     * @param rectangle Координаты селектора
     */
    private void setBuilderSelection(Rectangle rectangle) {
        for (Entry<ComponentElement, ComponentBuilder> entry : builders.entrySet()) {
            ComponentElement e = entry.getKey();
            ComponentBuilder b = entry.getValue();
            XElement el = b.getComponent().getElement();
            Rectangle r = el.getBounds();

            Point p1 = new Point(r.getX(), r.getY());
            int x = r.getX() + r.getWidth();
            int y = r.getY() + r.getHeight();
            Point p2 = new Point(x - 1, y - 1); // Если точка лежит на верхней
            // границе, rectangle.contains
            // вернет false

            if (rectangle.contains(p1) && rectangle.contains(p2) && rootElement.getChildren().contains(e)) {
                selectedElements.add(e);
                setBuilderSelected(el, true);
            }
        }
    }

    /**
     * Изменение стиля ячейки при её выделении
     *
     * @param selected Установить или снять выделение
     */
    private void setCellSelected(boolean selected) {
        if (rootBuilder == null) {
            return;
        }
        Component comp = rootBuilder.getComponent();
        if (comp instanceof GridLayoutContainer) {
            FlexTable table = getFormTable(comp);
            TableElement el = table.getElement().cast();
            NodeList<TableRowElement> rows = el.getRows();
            for (int i = 0; i < rows.getLength(); i++) {
                TableRowElement rowEl = rows.getItem(i);
                NodeList<TableCellElement> cells = rowEl.getCells();
                for (int j = 0; j < cells.getLength(); j++) {
                    TableCellElement cellEl = cells.getItem(j);
                    if (cellEl != null) {
                        setCellSelected(cellEl, false);
                    }
                }
            }
        }
    }

    private void setCellSelected(Element el, boolean selected) {
        Surface surface = setElementSelected(el, GWT.<DefaultSurfaceAppearance>create(DefaultSurfaceAppearance.class),
                selected);
        if (surface != null) {
            surface.getElement().getStyle().setZIndex(1);
        }
    }

    /**
     * Выделение компонентов и ячеек с помощью селектора
     *
     * @param rectangle Координаты селектора
     */
    private void setSelection(Rectangle rectangle) {
        FlexTable table = getFormTable(rootBuilder.getComponent());
        int rows = table.getRowCount();

        for (int i = 0; i < rows; i++) {
            int cells = table.getCellCount(i);
            for (int j = 0; j < cells; j++) {
                Element el = table.getFlexCellFormatter().getElement(i, j);
                if (el != null && el.getAbsoluteLeft() <= rectangle.getX() + rectangle.getWidth()
                        && el.getAbsoluteLeft() + el.getClientWidth() >= rectangle.getX()
                        && el.getAbsoluteTop() <= rectangle.getY() + rectangle.getHeight()
                        && el.getAbsoluteTop() + el.getClientHeight() >= rectangle.getY()) {

                    selectedCells.add(new CellRangeElement(i, j, i, j));
                }
            }
        }
        selectGroupCell();
    }

    /**
     * Снятие выделения с ячеек и компонентов
     */
    private void clearSelection() {
        selectedCells.clear();
        selectedElements.clear();
        // selectedElement = null;
        setCellSelected(false);
        setBuilderSelected(false);
        eventBus.setSelectedCellsArea(getSelectedCellsArea());
    }

    /**
     * Установка обработчиков событий DnD для селектора
     *
     * @param element Компонент
     * @param builder Билдер компонента
     */
    private void initComponentDND(ComponentElement element, ComponentBuilder builder) {
        Widget component;
        if (builder instanceof FormBuilder) {
            component = ((GridLayoutContainer) builder.getComponent()).getTable();
        } else {
            component = builder.getComponent();
        }
        DragSource source = new DragSource(component);
        source.addDragStartHandler(new DndDragStartHandler() {
            @Override
            public void onDragStart(DndDragStartEvent event) {
                if (rootBuilder instanceof FormBuilder) {
                    selector.setEnabled(false);
                }
            }
        });
        source.addDropHandler(new DndDropHandler() {
            @Override
            public void onDrop(DndDropEvent event) {
                if (rootBuilder instanceof FormBuilder) {
                    selector.setEnabled(true);
                }
            }
        });
        source.addDragCancelHandler(new DndDragCancelHandler() {
            @Override
            public void onDragCancel(DndDragCancelEvent event) {
                if (rootBuilder instanceof FormBuilder) {
                    selector.setEnabled(true);
                }
            }
        });
        source.setData(element);
    }

    private void onDrop(ComponentElement parentElement, ComponentBuilder parentBuilder, Object dropData,
                        Object locationData) {
        ComponentElement element = null;
        if (dropData instanceof ComponentElement) {
            element = (ComponentElement) dropData;
            setLocationData(parentElement, element, locationData);
        } else if (dropData instanceof ComponentType) {
            element = EditorHelper.newComponentElement((ComponentType) dropData, currentApplication.getDefaultLocale());
            setLocationData(parentElement, element, locationData);
        }
        if (element.getType() == ComponentType.TabItemType && parentElement.getType() != ComponentType.TabPanelType) {
            return;
        }
        if (element.getType() == ComponentType.HorizontalMenuItemType) {
            if (parentElement.getType() == ComponentType.HorizontalMenuType
                    || parentElement.getType() == ComponentType.TreeMenuType
                    || parentElement.getType() == ComponentType.HorizontalMenuItemType) {
            } else {
                return;
            }
        }

        addChildComponent(parentElement, element);
    }

    private void setLocationData(ComponentElement parentElement, ComponentElement element, Object locationData) {
        if (parentElement.getType() == ComponentType.BorderContainerType) {
            eventBus.addElement(element, new NewPropertyElement(PropertyType.LayoutDataLocation,
                    currentApplication.getDefaultLocale(), locationData));
        } else if (parentElement.getType() == ComponentType.HorizontalContainerType
                || parentElement.getType() == ComponentType.VerticalContainerType
                || parentElement.getType() == ComponentType.HBoxContainerType
                || parentElement.getType() == ComponentType.VBoxContainerType
                || parentElement.getType() == ComponentType.TreeMenuType
                || parentElement.getType() == ComponentType.HorizontalMenuType) {
            int index = locationData == null ? -1 : (Integer) locationData;
            if (index >= 0) {
                eventBus.addElement(element, new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), index));
            }
        } else if (parentElement.getType() == ComponentType.TabPanelType
                || parentElement.getType() == ComponentType.HorizontalMenuType) {
            int index = locationData == null ? -1 : (Integer) locationData;
            if (index >= 0) {
                for (ComponentElement ce : parentElement.getChildren()) {
                    Double idx = ce.getProperty(PropertyType.LayoutDataIndex).getValue(currentApplication.getDefaultLocale()).getDouble();
                    if (idx == null) {
                        continue;
                    }
                    int i = idx.intValue();
                    if (i >= index) {
                        eventBus.addElement(ce, new NewPropertyElement(PropertyType.LayoutDataIndex,
                                currentApplication.getDefaultLocale(), i));
                    }
                }
                eventBus.addElement(element, new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), index));
            }
        } else if (parentElement.getType() == ComponentType.FormBuilderType) {
            Cell cell = (Cell) locationData;
            int row = cell.getRow();
            if (row >= 0) {
                eventBus.addElement(element, new NewPropertyElement(PropertyType.LayoutDataFormRow,
                        currentApplication.getDefaultLocale(), row));
            }
            int column = cell.getColumn();
            if (column >= 0) {
                eventBus.addElement(element, new NewPropertyElement(PropertyType.LayoutDataFormColumn,
                        currentApplication.getDefaultLocale(), column));
            }
        }
    }

    private void repaint() {
        repaint(true);
    }

    private void repaint(boolean clearSelections) {
        reloadElement(rootElement, clearSelections);
    }

    private void setUIProperty(ComponentBuilder builder, String name, DataValue value) {
        PropertyType type = PropertyType.parse(name, builder.getType());
        if (type != null && type.isUI()) {
            builder.setProperty(name, value);
            if (type == PropertyType.Hidden && showHidden) {
                builder.getComponent().removeStyleName(HideMode.DISPLAY.value());
            }
        }
    }

    private void setUIProperties(ComponentBuilder builder, Map<PropertyType, PropertyValue> properties) {
        for (Entry<PropertyType, PropertyValue> entry : properties.entrySet()) {
            String name = entry.getKey().getCode();
            DataType type = entry.getKey().getType();
            DataValue value = entry.getValue().getValue(null);
            if (!entry.getValue().isReplaceable() || DataType.STRING.equals(type)) {
                setUIProperty(builder, name, value);
            }
        }
    }

    private ComponentBuilder createComponent(ComponentElement element, boolean createChildren) {
        ComponentType type = element.getType();

        ComponentBuilder builder;
        if (type == ComponentType.WindowType || type == ComponentType.ContentPanelType) {
            builder = ComponentTypeUtil.findBuilder(ComponentType.SimpleContainerType);
        } else {
            builder = ComponentTypeUtil.findBuilder(type);
        }
        if (builder == null) {
            return null;
        }
        // builder.createClear();

        setUIProperties(builder, element.getProperties());
        if (builder.getType().equals(ComponentType.FormBuilderType)) {
            // Если появились новые ячейки, создать их
            PropertyValue rowsValue = element.getProperty(PropertyType.Rows);
            PropertyValue colsValue = element.getProperty(PropertyType.Columns);
            //
            // ((FormElement) element).getRowsHeight().size()
            // int rowsDiff = rowsValue.getValue(null).<Double>
            // getValue().intValue() - ((FormElement)
            // element).getRowsHeight().size();
            // if (rowsDiff > 0) {
            // for (int i = 0; i < rowsDiff; i ++) {
            // eventBus.insertRow(index);
            // }
            // }

            if (rowsValue != null && rowsValue.getValue(currentApplication.getDefaultLocale()).getInteger() != null && colsValue != null
                    && colsValue.getValue(currentApplication.getDefaultLocale()).getInteger() != null) {
                int rows = rowsValue.getValue(currentApplication.getDefaultLocale()).getInteger();
                int cols = colsValue.getValue(currentApplication.getDefaultLocale()).getInteger();
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (((FormElement) element).findCellElement(i, j) == null) {
                            CellElement cellModel = new CellElement();
                            cellModel.setId(RandomUUID.uuid());
                            ((FormElement) element).addCellElement(i, j, cellModel);
                        }
                    }
                }
            }
            if (((FormElement) element).getRowsHeight().size() > 0) {
                for (RowElement m : ((FormElement) element).getRowsHeight()) {
                    double height = m.getHeight() != null ? m.getHeight() : 0;
                    ((FormBuilder) builder).setRowHeight(m.getRow(), height);
                }
            } else {
                if (rowsValue != null && rowsValue.getValue(currentApplication.getDefaultLocale()).getInteger() != null) {
                    int rows = rowsValue.getValue(currentApplication.getDefaultLocale()).getInteger();
                    double height = 1.0 / rows;
                    List<RowElement> rowsHeight = new ArrayList<RowElement>();
                    for (int i = 0; i < rows; i++) {
                        RowElement m = new RowElement();
                        m.setId(RandomUUID.uuid());
                        m.setRow(i);
                        m.setHeight(height);
                        rowsHeight.add(m);
                        ((FormBuilder) builder).setRowHeight(i, height);
                    }
                    ((FormElement) element).setRowsHeight(rowsHeight);
                }
            }
            if (((FormElement) element).getColumnsWidth().size() > 0) {
                for (ColumnElement m : ((FormElement) element).getColumnsWidth()) {
                    double width = m.getWidth() != null ? m.getWidth() : 0;
                    ((FormBuilder) builder).setColumnWidth(m.getColumn(), width);
                }
            } else {
                if (colsValue != null && colsValue.getValue(currentApplication.getDefaultLocale()).getInteger() != null) {
                    int cols = colsValue.getValue(currentApplication.getDefaultLocale()).getInteger();
                    double width = 1.0 / cols;
                    List<ColumnElement> columnsWidth = new ArrayList<ColumnElement>();
                    for (int i = 0; i < cols; i++) {
                        ColumnElement m = new ColumnElement();
                        m.setId(RandomUUID.uuid());
                        m.setColumn(i);
                        m.setWidth(width);
                        columnsWidth.add(m);
                        ((FormBuilder) builder).setColumnWidth(i, width);
                    }
                    ((FormElement) element).setColumnsWidth(columnsWidth);
                }
            }
            for (Entry<CellRowCol, CellElement> entry : ((FormElement) element).getCells().entrySet()) {
                int row = entry.getKey().getRow();
                int column = entry.getKey().getCol();
                int rowSpan = entry.getValue().getRowSpan();
                int colSpan = entry.getValue().getColSpan();
                int borderTop = entry.getValue().getBorderTop();
                int borderRight = entry.getValue().getBorderRight();
                int borderBottom = entry.getValue().getBorderBottom();
                int borderLeft = entry.getValue().getBorderLeft();
                String borderTopColor = entry.getValue().getBorderTopColor();
                String borderRightColor = entry.getValue().getBorderRightColor();
                String borderBottomColor = entry.getValue().getBorderBottomColor();
                String borderLeftColor = entry.getValue().getBorderLeftColor();
                String color = entry.getValue().getBackgroundColor();
                if (rowSpan > 1 || colSpan > 1) {
                    ((FormBuilder) builder).setSpan(row, column, rowSpan, colSpan);
                }

                if (borderTop > 0) {
                    ((FormBuilder) builder).setCellBorderTop(row, column, borderTop, borderTopColor);
                }
                if (borderRight > 0) {
                    ((FormBuilder) builder).setCellBorderRight(row, column, borderRight, borderRightColor);
                }
                if (borderBottom > 0) {
                    ((FormBuilder) builder).setCellBorderBottom(row, column, borderBottom, borderBottomColor);
                }
                if (borderLeft > 0) {
                    ((FormBuilder) builder).setCellBorderLeft(row, column, borderLeft, borderLeftColor);
                }
                if (color != null && !color.isEmpty()) {
                    ((FormBuilder) builder).setCellColor(row, column, color);
                }
            }
            // ((FormBuilder) builder).setCellSpacing(1);
            //
            // // Нужно ли?
            ((FormBuilder) builder).setPaddingInCells(1);
        } else if (ComponentType.ImageType.equals(element.getType())) {
            ((ImageBuilder) builder).setDefaultImage(ComponentBundle.INSTANCE.simpleImage().getSafeUri().asString());
        } else if (ComponentType.TimerType.equals(element.getType())) {
            ((TimerBuilder) builder).setIcon(EditorBundle.INSTANCE.timerSmall());
        } else if (ComponentType.HotKeyType.equals(element.getType())) {
            ((HotKeyBuilder) builder).setIcon(EditorBundle.INSTANCE.hotKeySmall());
        }
        if (createChildren && builder instanceof Containable) {
            for (ComponentElement child : element.getChildren()) {
                ComponentBuilder childBuilder = createComponent(child, true);
                ((Containable) builder).addChild(childBuilder);

                builders.put(child, childBuilder);
            }
        }
        return builder;
    }

    private void setBuilderZIndex(ComponentBuilder builder) {
        Component c = builder.getComponent();
        if (c != null) {
            c.getElement().getStyle().setZIndex(10);
        }
    }

    public void onSyncComponentPropertyToUI(ComponentElement element, String name, DataValue value) {
        if (element.equals(rootElement)) {
            setUIProperty(rootBuilder, name, value);
        } else {
            for (ComponentElement e : builders.keySet()) {
                if (e.equals(element)) {
                    ComponentBuilder b = builders.get(e);
                    setUIProperty(b, name, value);
                }
            }
        }
        if (PropertyType.parse(name, element.getType()).isUI()) {
            reloadElement(rootElement);
        }
    }

    private void forceRootLayout() {
        if (rootBuilder instanceof Containable) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    ((Containable) rootBuilder).forceLayout();
                }
            });
        }
    }

    public void onRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if (element instanceof ComponentElement) {

        }
        if (!(element instanceof ComponentElement) || rootElement == null
                || EditorHelper.hasNestedElement(rootElement, (ComponentElement) element)) {
            return;
        }
        reloadElement(rootElement);
    }

    /**
     * Выделение группы ячеек и находящихся на них компонентов
     *
     */
    private void selectGroupCell() {
        FlexTable table = getFormTable(rootBuilder.getComponent());
        for (CellRangeElement model : selectedCells) {
            if (model.getTop() >= table.getRowCount() || model.getLeft() >= table.getCellCount(model.getTop())) {
                continue;
            }
            Element el = table.getFlexCellFormatter().getElement(model.getTop(), model.getLeft());
            if (el != null) {
                setBuilderSelection(((XElement) el).getBounds());
                setCellSelected(el, true);
            }
        }
        if (selectedElements.size() == 1) {
            selectedElement = selectedElements.get(0);
            eventBus.selectTreeElement(selectedElement);
        }
        eventBus.setSelectedCellsArea(getSelectedCellsArea());
    }

    /**
     * Получение табличной части формы
     *
     * @param comp Компонент
     * @return Таблица
     */
    private FlexTable getFormTable(Component comp) {
        if (comp instanceof GridLayoutContainer) {
            return ((GridLayoutContainer) comp).getTable();
        }
        return null;
    }

    /**
     * Выделение группы ячеек и находящихся на них компонентов
     *
     * @param model    Модель данных
     * @param selected Установить или снять выделение
     */
    public void onSelectGroupCell(AbstractElement model) {
        clearSelection();
        GridLayoutContainer container = (GridLayoutContainer) rootBuilder.getComponent();

        if (model instanceof RequestElement) {
            int top = ((RequestElement) model).getTop();
            int bottom = ((RequestElement) model).getBottom();
            int columns = rootElement.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                    .getDouble().intValue();

            for (int i = top; i <= bottom; i++) {
                for (int j = 0; j < columns; j++) {
                    GridLayoutContainer.Cell cell = container.getTablePositionByGrid(i, j);
                    if (cell != null) {
                        selectedCells.add(
                                new CellRangeElement(cell.getRow(), cell.getColumn(), cell.getRow(), cell.getColumn()));
                    }
                }
            }
            selectGroupCell();
        } else if (model instanceof CellRangeElement) {
            int top = ((CellRangeElement) model).getTop();
            int right = ((CellRangeElement) model).getRight();
            int bottom = ((CellRangeElement) model).getBottom();
            int left = ((CellRangeElement) model).getLeft();

            for (int i = top; i <= bottom; i++) {
                for (int j = left; j <= right; j++) {
                    GridLayoutContainer.Cell cell = container.getTablePositionByGrid(i, j);
                    if (cell != null) {
                        selectedCells.add(
                                new CellRangeElement(cell.getRow(), cell.getColumn(), cell.getRow(), cell.getColumn()));
                    }
                }
            }
            selectGroupCell();
        }
    }

    /**
     * Выделение колонки на форме
     *
     * @param column   Индекс колонки
     * @param selected Флаг установки выделения
     */
    public void onSelectColumn(int column, boolean selected) {
        clearSelection();

        GridLayoutContainer container = (GridLayoutContainer) rootBuilder.getComponent();

        int rows = rootElement.getProperty(PropertyType.Rows).getValue(null).getDouble().intValue();
        for (int i = 0; i < rows; i++) {
            Cell cellIndexes = container.getTablePositionByGrid(i, column);
            if (cellIndexes != null) {
                int tableRow = cellIndexes.getRow();
                int tableColumn = cellIndexes.getColumn();
                selectedCells.add(new CellRangeElement(tableRow, tableColumn, tableRow, tableColumn));
            }
        }
        selectGroupCell();
    }

    /**
     * Выделение строки на форме
     *
     * @param row      Индекс строки
     * @param selected Флаг установки выделения
     */
    public void onSelectRow(int row, boolean selected) {
        clearSelection();

        GridLayoutContainer container = (GridLayoutContainer) rootBuilder.getComponent();

        int columns = rootElement.getProperty(PropertyType.Columns).getValue(null).getDouble().intValue();
        for (int i = 0; i < columns; i++) {
            Cell cellIndexes = container.getTablePositionByGrid(row, i);
            if (cellIndexes != null) {
                int tableRow = cellIndexes.getRow();
                int tableColumn = cellIndexes.getColumn();
                selectedCells.add(new CellRangeElement(tableRow, tableColumn, tableRow, tableColumn));
            }
        }

        selectGroupCell();
    }

    /**
     * Добавление строки на форме
     *
     * @param index Индекс добавляемой строки
     */
    public void onInsertRow(int index) {
        Component comp = rootBuilder.getComponent();
        if (comp instanceof GridLayoutContainer) {
            ((GridLayoutContainer) comp).insertRow(index);
            int rows = rootElement.getProperty(PropertyType.Rows).getValue(currentApplication.getDefaultLocale())
                    .getDouble().intValue();
            eventBus.addElement(rootElement,
                    new NewPropertyElement(PropertyType.Rows, currentApplication.getDefaultLocale(), ++rows));
        }
        eventBus.syncComponentPropertyToElement(rootElement);
    }

    /**
     * Добавление колонки на форме
     *
     * @param index Индекс добавляемой колонки
     */
    public void onInsertColumn(int index) {
        Component comp = rootBuilder.getComponent();
        if (comp instanceof GridLayoutContainer) {
            ((GridLayoutContainer) comp).insertColumn(index);
            int cols = rootElement.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                    .getDouble().intValue();
            eventBus.addElement(rootElement,
                    new NewPropertyElement(PropertyType.Columns, currentApplication.getDefaultLocale(), ++cols));
        }
        eventBus.syncComponentPropertyToElement(rootElement);
    }

    /**
     * Удаление строки на форме
     *
     * @param index Индекс удаляемой строки
     */
    public void onDeleteRow(int index) {
        int rows = rootElement.getProperty(PropertyType.Rows).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();
        int cols = rootElement.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();
        // if (index < rows && rows > 1) {
        CellElement next;
        for (int i = 0; i < cols; i++) {
            for (int j = index; j < rows - 1; j++) {
                next = ((FormElement) rootElement).findCellElement(j + 1, i);
                ((FormElement) rootElement).addCellElement(j, i, next);
            }
            ((FormElement) rootElement).removeCell(rows - 1, i);
        }
        // Сдвиг компонентов вверх
        Set<ComponentElement> comps = builders.keySet();
        for (ComponentElement comp : comps) {
            if (comp.getParent() == rootElement) {
                int compRow = comp.getProperty(PropertyType.LayoutDataFormRow)
                        .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                if (compRow > index) {
                    eventBus.addElement(comp, new NewPropertyElement(PropertyType.LayoutDataFormRow,
                            currentApplication.getDefaultLocale(), --compRow));

                } else if (compRow == index) {
                    // перемещаем компоненты, расположенные на удаляемых
                    // ячейках в
                    // группу "Свободные компоненты"
                    addFreeComponent(comp);
                }
            }
        }
        // eventBus.addElement(rootElement, new NewPropertyElement(
        // PropertyType.Rows, currentApplication.getDefaultLocale(),
        // --rows));
        // }
    }

    /**
     * Удаление колонки на форме
     *
     * @param index Индекс удаляемой колонки
     */
    public void onDeleteColumn(int index) {
        int rows = rootElement.getProperty(PropertyType.Rows).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();
        int cols = rootElement.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();

        // if (index < cols && cols > 1) {

        CellElement next;
        for (int i = 0; i < rows; i++) {
            for (int j = index; j < cols - 1; j++) {
                next = ((FormElement) rootElement).findCellElement(i, j + 1);
                ((FormElement) rootElement).addCellElement(i, j, next);
            }
            ((FormElement) rootElement).removeCell(i, cols - 1);
        }
        // Сдвиг компонентов влево
        Set<ComponentElement> comps = builders.keySet();
        for (ComponentElement comp : comps) {
            if (comp.getParent() == rootElement) {
                int compCol = comp.getProperty(PropertyType.LayoutDataFormColumn)
                        .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                if (compCol > index) {
                    eventBus.addElement(comp, new NewPropertyElement(PropertyType.LayoutDataFormColumn,
                            currentApplication.getDefaultLocale(), --compCol));
                } else if (compCol == index) {
                    // перемещаем компоненты, расположенные на удаляемых
                    // ячейках в
                    // группу "Свободные компоненты"
                    addFreeComponent(comp);
                }
            }
        }
        // eventBus.addElement(rootElement, new NewPropertyElement(
        // PropertyType.Columns,
        // currentApplication.getDefaultLocale(), --cols));
        // }
    }

    public void onUnion() {
        if (rootElement instanceof FormElement) {
            CellRangeElement model = getSelectedCellsArea();
            if (model != null) {
                int row = model.getTop();
                int col = model.getLeft();
                int bottomSpan = ((FormElement) rootElement).findCellElement(model.getBottom(), col).getRowSpan();
                int rightSpan = ((FormElement) rootElement).findCellElement(row, model.getRight()).getColSpan();

                if (selectedElements.size() > 1) {
                    InfoHelper.error("selected-elements", EditorMessage.Util.MESSAGE.error(),
                            EditorMessage.Util.MESSAGE.error_more_than_one_element());
                    return;
                } else if (selectedElements.size() == 1) {
                    ComponentElement comp = selectedElements.get(0);
                    if (comp.getProperty(PropertyType.LayoutDataFormRow).getValue(currentApplication.getDefaultLocale())
                            .getDouble().intValue() != row) {
                        comp.setProperty(PropertyType.LayoutDataFormRow,
                                new PropertyValue(DataType.NUMBER, currentApplication.getDefaultLocale(), row));
                    }
                    if (comp.getProperty(PropertyType.LayoutDataFormColumn)
                            .getValue(currentApplication.getDefaultLocale()).getDouble().intValue() != col) {
                        comp.setProperty(PropertyType.LayoutDataFormColumn,
                                new PropertyValue(DataType.NUMBER, currentApplication.getDefaultLocale(), col));
                    }
                }

                // Разбиваем все выделенные объединенные ячейки
                for (int i = model.getTop(); i < model.getBottom() + bottomSpan; i++) {
                    for (int j = model.getLeft(); j < model.getRight() + rightSpan; j++) {
                        ((FormElement) rootElement).findCellElement(i, j).setColSpan(1);
                        ((FormElement) rootElement).findCellElement(i, j).setRowSpan(1);
                    }
                }
                int rowSpan = model.getBottom() - model.getTop() + bottomSpan;
                int colSpan = model.getRight() - model.getLeft() + rightSpan;
                CellElement m = ((FormElement) rootElement).findCellElement(row, col);
                m.setRowSpan(rowSpan);
                m.setColSpan(colSpan);
                clearSelection();
                repaint();
            }
        }
    }

    public void onDivide() {
        if (rootElement instanceof FormElement) {
            CellRangeElement model = getSelectedCellsArea();
            if (model != null) {
                for (int i = model.getTop(); i <= model.getBottom(); i++) {
                    for (int j = model.getLeft(); j <= model.getRight(); j++) {
                        CellElement m = ((FormElement) rootElement).findCellElement(i, j);
                        m.setRowSpan(1);
                        m.setColSpan(1);
                    }
                }
                clearSelection();
                repaint();
            }
        }
    }

    public void onDeleteSelectedComponent() {
        if (selectedElements != null) {
            for (ComponentElement comp : selectedElements) {
                removeChildComponent(rootElement, comp);
            }
        }
        if (selectedElement != null) {
            removeChildComponent(rootElement, selectedElement);
        }
    }

    public void onAddRow() {
        CellRangeElement model = getSelectedCellsArea();
        // Может ли быть не formElement?
        FormElement form = (FormElement) rootElement;
        int rows = form.getProperty(PropertyType.Rows).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();
        int cols = form.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();

        // int index = rows;
        int index = model == null ? rows : model.getTop();
        if (model != null) {
            // Сдвиг ячеек вниз
            CellElement prev;
            for (int i = 0; i < cols; i++) {
                prev = new CellElement(RandomUUID.uuid());
                for (int j = model.getTop(); j <= rows; j++) {
                    prev = form.addCellElement(j, i, prev);
                }
            }
            // Сдвиг компонентов вниз
            Set<ComponentElement> comps = builders.keySet();
            for (ComponentElement comp : comps) {
                if (comp.getParent() == rootElement) {
                    int compRow = comp.getProperty(PropertyType.LayoutDataFormRow)
                            .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                    if (compRow >= model.getTop()) {
                        eventBus.addElement(comp, new NewPropertyElement(PropertyType.LayoutDataFormRow,
                                currentApplication.getDefaultLocale(), ++compRow));
                    }
                }
            }
        }
        eventBus.insertRow(index);
        repaint();
    }

    public void onAddColumn() {
        CellRangeElement model = getSelectedCellsArea();
        FormElement form = (FormElement) rootElement;
        int rows = form.getProperty(PropertyType.Rows).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();
        int cols = form.getProperty(PropertyType.Columns).getValue(currentApplication.getDefaultLocale())
                .getDouble().intValue();

        // int index = cols;
        int index = model == null ? cols : model.getLeft();
        if (model != null) {
            index = model.getLeft();
            // Сдвиг ячеек вправо
            CellElement prev;
            for (int i = 0; i < rows; i++) {
                prev = new CellElement(RandomUUID.uuid());
                for (int j = model.getLeft(); j <= cols; j++) {
                    prev = form.addCellElement(i, j, prev);
                }
            }
            // Сдвиг компонентов вправо
            Set<ComponentElement> comps = builders.keySet();
            for (ComponentElement comp : comps) {
                if (comp.getParent() == rootElement) {
                    int compCol = comp.getProperty(PropertyType.LayoutDataFormColumn)
                            .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                    if (compCol >= model.getLeft()) {
                        eventBus.addElement(comp, new NewPropertyElement(PropertyType.LayoutDataFormColumn,
                                currentApplication.getDefaultLocale(), ++compCol));
                    }
                }
            }
        }
        eventBus.insertColumn(index);
        repaint();
    }

    // Поменять название?
    public void onDeleteRow() {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            int rowsCount = model.getBottom() - model.getTop() + 1;
            int rowNum = model.getTop();
            if (!checkRowsHasSpans()) {
                for (int i = 0; i < rowsCount; i++) {
                    int rows = rootElement.getProperty(PropertyType.Rows)
                            .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                    if (rowNum < rows && rows > 1) {
                        eventBus.deleteRow(rowNum);
                        eventBus.addElement(rootElement, new NewPropertyElement(PropertyType.Rows,
                                currentApplication.getDefaultLocale(), --rows));
                    }
                }

                eventBus.syncComponentPropertyToElement(rootElement);
                clearSelection();
                repaint();
            } else {
                Info.display(EditorMessage.Util.MESSAGE.warn(), EditorMessage.Util.MESSAGE.warn_need_to_split());
            }
        }
    }

    public void onDeleteColumn() {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {

            int colsCount = model.getRight() - model.getLeft() + 1;
            int colNum = model.getLeft();
            if (!checkColumnsHasSpans()) {
                for (int i = 0; i < colsCount; i++) {
                    int cols = rootElement.getProperty(PropertyType.Columns)
                            .getValue(currentApplication.getDefaultLocale()).getDouble().intValue();
                    if (colNum < cols && cols > 1) {
                        eventBus.deleteColumn(colNum);
                        eventBus.addElement(rootElement, new NewPropertyElement(PropertyType.Columns,
                                currentApplication.getDefaultLocale(), --cols));
                    }
                }

                eventBus.syncComponentPropertyToElement(rootElement);
                clearSelection();
                repaint();
            } else {
                Info.display(EditorMessage.Util.MESSAGE.warn(), EditorMessage.Util.MESSAGE.warn_need_to_split());
            }
        }
    }

    public void onAddBorderTop(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getLeft(); i <= model.getRight(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(model.getTop(), i);
                m.setBorderTop(width);
                m.setBorderTopColor(color);
            }
            repaint(false /*clear selections*/);
        }
    }

    public void onAddBorderRight(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(i, model.getRight());
                m.setBorderRight(width);
                m.setBorderRightColor(color);
            }
            repaint(false /*clear selections*/);
        }
    }

    public void onAddBorderBottom(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getLeft(); i <= model.getRight(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(model.getBottom(), i);
                m.setBorderBottom(width);
                m.setBorderBottomColor(color);
            }
            repaint(false /*clear selections*/);
        }
    }

    public void onAddBorderLeft(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(i, model.getLeft());
                m.setBorderLeft(width);
                m.setBorderLeftColor(color);
            }
            repaint(false /*clear selections*/);
        }
    }

    public void onSetColor(String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                for (int j = model.getLeft(); j <= model.getRight(); j++) {
                    CellElement m = ((FormElement) rootElement).findCellElement(i, j);
                    m.setBackgroundColor(color);
                }
            }
            repaint(false /*clear selections*/);
        }
    }

    private CellRangeElement getSelectedCellsArea() {
        CellRangeElement model = null;
        if (selectedCells.size() > 0) {
            model = new CellRangeElement(-1, -1, -1, -1);
            GridLayoutContainer container = (GridLayoutContainer) rootBuilder.getComponent();
            for (CellRangeElement m : selectedCells) {
                Cell cellIndexes = container.getGridPositionByTable(m.getTop(), m.getLeft());
                if (cellIndexes == null) {
                    continue;
                }
                int top = cellIndexes.getRow() > m.getTop() ? cellIndexes.getRow() : m.getTop();
                int right = cellIndexes.getColumn() > m.getRight() ? cellIndexes.getColumn() : m.getRight();
                int bottom = cellIndexes.getRow() > m.getBottom() ? cellIndexes.getRow() : m.getBottom();
                int left = cellIndexes.getColumn() > m.getLeft() ? cellIndexes.getColumn() : m.getLeft();

                if (model.getTop() < 0 || model.getTop() > cellIndexes.getRow()) {
                    model.setTop(top);
                }
                if (model.getRight() < 0 || model.getRight() < cellIndexes.getColumn()) {
                    model.setRight(right);
                }
                if (model.getBottom() < 0 || model.getBottom() < cellIndexes.getRow()) {
                    model.setBottom(bottom);
                }
                if (model.getLeft() < 0 || model.getLeft() > cellIndexes.getColumn()) {
                    model.setLeft(left);
                }
            }
        }
        return model;
    }

    private boolean checkRowsHasSpans() {
        boolean hasSpans = false;
        FlexTable table = getFormTable(rootBuilder.getComponent());

        int top = table.getElement().getAbsoluteBottom();
        int bottom = 0;
        for (CellRangeElement model : selectedCells) {
            // GridLayoutContainer.Cell topCell = container
            // .getTablePositionByGrid(model.getTop(), model.getLeft());
            // int topTmp = table.getFlexCellFormatter()
            // .getElement(topCell.getRow(), topCell.getColumn())
            // .getAbsoluteTop();
            int topTmp = table.getFlexCellFormatter().getElement(model.getTop(), model.getLeft()).getAbsoluteTop() + 1;
            if (topTmp < top) {
                top = topTmp;
            }
            // GridLayoutContainer.Cell bottomCell = container
            // .getTablePositionByGrid(model.getBottom(), model.getLeft());
            // int botTmp = table.getFlexCellFormatter()
            // .getElement(bottomCell.getRow(), bottomCell.getColumn())
            // .getAbsoluteBottom();
            int botTmp = table.getFlexCellFormatter().getElement(model.getBottom(), model.getLeft()).getAbsoluteBottom()
                    - 1;
            if (botTmp > bottom) {
                bottom = botTmp;
            }
        }
        int width = table.getElement().getClientWidth();

        clearSelection();
        setSelection(new Rectangle(table.getAbsoluteLeft(), top + 1, width, bottom - top - 1));

        CellRangeElement model = getSelectedCellsArea();
        for (int i = model.getTop(); i <= model.getBottom(); i++) {
            for (int j = model.getLeft(); j <= model.getRight(); j++) {
                if (((FormElement) rootElement).findCellElement(i, j).getRowSpan() > 1) {
                    hasSpans = true;
                }
            }
        }
        return hasSpans;
    }

    private boolean checkColumnsHasSpans() {
        boolean hasSpans = false;
        FlexTable table = getFormTable(rootBuilder.getComponent());

        int left = table.getElement().getAbsoluteRight();
        int right = 0;
        for (CellRangeElement model : selectedCells) {
            // GridLayoutContainer.Cell leftCell = container
            // .getTablePositionByGrid(model.getTop(), model.getLeft());

            // int leftTmp = table.getFlexCellFormatter()
            // .getElement(leftCell.getRow(), leftCell.getColumn())
            // .getAbsoluteLeft();
            int leftTmp = table.getFlexCellFormatter().getElement(model.getTop(), model.getLeft()).getAbsoluteLeft()
                    + 1;
            if (leftTmp < left) {
                left = leftTmp;
            }
            // GridLayoutContainer.Cell rightCell = container
            // .getTablePositionByGrid(model.getTop(), model.getRight());
            // int rightTmp = table.getFlexCellFormatter()
            // .getElement(rightCell.getRow(), rightCell.getColumn())
            // .getAbsoluteRight();
            int rightTmp = table.getFlexCellFormatter().getElement(model.getTop(), model.getRight()).getAbsoluteRight()
                    - 1;
            if (rightTmp > right) {
                right = rightTmp;
            }
        }

        int height = table.getElement().getClientHeight();

        clearSelection();
        // Прибавляю/отнимаю 2 чтобы учитывать padding
        setSelection(new Rectangle(left + 1, table.getAbsoluteTop(), right - left - 1, height));

        CellRangeElement model = getSelectedCellsArea();
        for (int i = model.getTop(); i <= model.getBottom(); i++) {
            for (int j = model.getLeft(); j <= model.getRight(); j++) {
                if (((FormElement) rootElement).findCellElement(i, j).getColSpan() > 1) {
                    hasSpans = true;
                }
            }
        }
        return hasSpans;
    }

    private void addFreeComponent(ComponentElement comp) {
        eventBus.addElement(new NullFreeComponentElement(), comp);
    }

    private void addChildComponent(ComponentElement parentElement, ComponentElement element) {
        eventBus.addElement(parentElement, element);
    }

    private void removeChildComponent(ComponentElement rootElement, ComponentElement selectedElement) {
        eventBus.removeElement(rootElement, selectedElement, true);
    }

    public void onSelectComponentDesignElement(ComponentElement element) {

        ComponentBuilder builder = builders.get(element);
        if (builder == null) {
            return;
        }
        XElement el = builder.getComponent().getElement();
        clearSelection();
        selectedElement = element;
        setBuilderSelected(el, true);
    }

    public void onChangeShowHidden() {
        showHidden = !showHidden;
        view.setShowHidden(showHidden);
        if (showHidden) {
            for (ComponentBuilder b : builders.values()) {
                b.getComponent().removeStyleName(HideMode.DISPLAY.value());
            }
        } else {
            for (ComponentBuilder b : builders.values()) {
                if (b.isHidden()) {
                    b.getComponent().addStyleName(HideMode.DISPLAY.value());
                }
            }
        }
        refreshFormGrid(rootBuilder);
    }
}
