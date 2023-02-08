package org.whirlplatform.editor.client.view.design;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent;
import com.sencha.gxt.dnd.core.client.DndDragCancelEvent.DndDragCancelHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.form.GridLayoutContainer;
import org.whirlplatform.component.client.form.GridLayoutContainer.Cell;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.component.ColorMenu;
import org.whirlplatform.editor.client.component.surface.DefaultSurfaceAppearance;
import org.whirlplatform.editor.client.component.surface.Surface;
import org.whirlplatform.editor.client.dnd.GridLayoutDropTarget;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent.MouseSelectionHandler;
import org.whirlplatform.editor.client.dnd.MouseSelectionWrapper;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.CellRangeElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class FormDesigner extends ComponentDesigner {

    private TextButton addRow;
    private TextButton addColumn;
    private TextButton deleteRow;
    private TextButton deleteColumn;
    private TextButton union;
    private TextButton divide;
    private TextButton borderTop;
    private TextButton borderRight;
    private TextButton borderBottom;
    private TextButton borderLeft;
    private SimpleComboBox<Integer> borderWidth;
    private TextButton borderColor;
    private ColorMenu borderColorMenu;
    private TextButton backgroundColor;
    private ColorMenu backgroundColorMenu;
    private TextButton clearBackgroundColor;
    private SeparatorToolItem separator;

    /**
     * Список компонентов, выделенных с помощью селектора
     */
    private List<ComponentElement> selectedElements = new ArrayList<ComponentElement>();

    /**
     * Список ячеек, выделенных с помощью селектора
     */
    private List<CellRangeElement> selectedCells = new ArrayList<CellRangeElement>();
    private Map<Element, Surface> gridSurface = new HashMap<Element, Surface>();

    private MouseSelectionWrapper selector;
    private MouseSelectionHandler selectionHandler = new MouseSelectionHandler() {

        @Override
        public void onMouseSelection(MouseSelectionEvent event) {
            if (event.isSelect()) {
                setSelection(new Rectangle(event.getClientX(),
                    event.getClientY(), event.getWidth(), event.getHeight()));
            } else {
                clearSelection();
            }
        }
    };

    public FormDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected void initUI() {
        super.initUI();
        initSelectors();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        addRow = new TextButton();
        addRow.setTitle(EditorMessage.Util.MESSAGE.design_row_add());
        addRow.setIcon(EditorBundle.INSTANCE.insertRow());
        addRow.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddRow();
            }
        });
        addColumn = new TextButton();
        addColumn.setTitle(EditorMessage.Util.MESSAGE.design_column_add());
        addColumn.setIcon(EditorBundle.INSTANCE.insertColumn());
        addColumn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddColumn();
            }
        });
        deleteRow = new TextButton();
        deleteRow.setTitle(EditorMessage.Util.MESSAGE.design_row_remove());
        deleteRow.setIcon(EditorBundle.INSTANCE.deleteRow());
        deleteRow.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onDeleteRow();
            }
        });
        deleteColumn = new TextButton();
        deleteColumn
            .setTitle(EditorMessage.Util.MESSAGE.design_column_remove());
        deleteColumn.setIcon(EditorBundle.INSTANCE.deleteColumn());
        deleteColumn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onDeleteColumn();
            }
        });
        union = new TextButton();
        union.setTitle(EditorMessage.Util.MESSAGE.design_union());
        union.setIcon(EditorBundle.INSTANCE.union());
        union.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onUnion();
            }
        });
        divide = new TextButton();
        divide.setTitle(EditorMessage.Util.MESSAGE.design_split());
        divide.setIcon(EditorBundle.INSTANCE.split());
        divide.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onDivide();
            }
        });
        borderTop = new TextButton();
        borderTop.setTitle(EditorMessage.Util.MESSAGE.design_border_top());
        borderTop.setIcon(EditorBundle.INSTANCE.borderTop());
        borderTop.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddBorderTop(borderWidth.getValue(),
                    borderColorMenu.getColor());
            }
        });
        borderRight = new TextButton();
        borderRight.setTitle(EditorMessage.Util.MESSAGE.design_border_right());
        borderRight.setIcon(EditorBundle.INSTANCE.borderRight());
        borderRight.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddBorderRight(borderWidth.getValue(),
                    borderColorMenu.getColor());
            }
        });
        borderBottom = new TextButton();
        borderBottom
            .setTitle(EditorMessage.Util.MESSAGE.design_border_bottom());
        borderBottom.setIcon(EditorBundle.INSTANCE.borderBottom());
        borderBottom.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddBorderBottom(borderWidth.getValue(),
                    borderColorMenu.getColor());
            }
        });
        borderLeft = new TextButton();
        borderLeft.setTitle(EditorMessage.Util.MESSAGE.design_border_left());
        borderLeft.setIcon(EditorBundle.INSTANCE.borderLeft());
        borderLeft.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onAddBorderLeft(borderWidth.getValue(),
                    borderColorMenu.getColor());
            }
        });

        borderWidth = new SimpleComboBox<Integer>(
            new StringLabelProvider<Integer>());
        borderWidth.setAllowBlank(false);
        borderWidth.setForceSelection(true);
        borderWidth.setEditable(false);
        borderWidth.setTriggerAction(TriggerAction.ALL);
        borderWidth.setWidth(50);
        borderWidth.setValue(1);
        borderWidth.add(Arrays.asList(0, 1, 2, 3, 4, 5));
        borderWidth.setTitle(EditorMessage.Util.MESSAGE.design_border_width());
        borderWidth.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                borderWidth.setValue(event.getSelectedItem());
            }
        });

        borderColor = new TextButton();
        borderColor = new TextButton();
        borderColor.setTitle(EditorMessage.Util.MESSAGE.design_border_color());
        borderColor.setIcon(EditorBundle.INSTANCE.color());
        borderColorMenu = new ColorMenu();
        borderColor.setMenu(borderColorMenu);
        borderColorMenu.getPalette().addValueChangeHandler(
            new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    borderColorMenu.hide();
                }
            });

        backgroundColor = new TextButton();
        backgroundColor.setTitle(EditorMessage.Util.MESSAGE
            .design_background_color());
        backgroundColor.setIcon(EditorBundle.INSTANCE.color());
        backgroundColorMenu = new ColorMenu();
        backgroundColor.setMenu(backgroundColorMenu);
        backgroundColorMenu.getPalette().addValueChangeHandler(
            new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    backgroundColorMenu.hide();
                    backgroundColorMenu.getPalette().setValue("");
                    onSetColor(event.getValue());
                }
            });

        clearBackgroundColor = new TextButton();
        clearBackgroundColor.setTitle(EditorMessage.Util.MESSAGE
            .design_clear_color());
        clearBackgroundColor.setIcon(EditorBundle.INSTANCE.clearColor());
        clearBackgroundColor.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                onSetColor(null);
            }
        });
        separator = new SeparatorToolItem();

        toolBar.add(addRow);
        toolBar.add(addColumn);
        toolBar.add(deleteRow);
        toolBar.add(deleteColumn);
        toolBar.add(union);
        toolBar.add(divide);
        toolBar.add(backgroundColor);
        toolBar.add(clearBackgroundColor);
        toolBar.add(separator);
        toolBar.add(borderTop);
        toolBar.add(borderRight);
        toolBar.add(borderBottom);
        toolBar.add(borderLeft);
        toolBar.add(borderWidth);
        toolBar.add(borderColor);
    }

    @Override
    protected void repaint() {
        gridSurface.clear();
        super.repaint();
    }

    @Override
    protected void initRootDropTarget(final ComponentElement element,
                                      final ComponentBuilder builder) {
        new GridLayoutDropTarget((GridLayoutContainer) builder.getComponent()) {
            protected void onDragDrop(DndDropEvent event) {
                FormDesigner.super.onRootDrop(element, builder,
                    event.getData(), getCell());
                super.onDragDrop(event);
            }

        };
    }

    @Override
    protected DragSource initChildDragSource(ComponentElement element,
                                             ComponentBuilder builder) {
        DragSource source = super.initChildDragSource(element, builder);
        source.addDragStartHandler(new DndDragStartHandler() {
            @Override
            public void onDragStart(DndDragStartEvent event) {
                selector.setEnabled(false);
            }
        });
        source.addDropHandler(new DndDropHandler() {
            @Override
            public void onDrop(DndDropEvent event) {
                selector.setEnabled(true);
            }
        });
        source.addDragCancelHandler(new DndDragCancelHandler() {
            @Override
            public void onDragCancel(DndDragCancelEvent event) {
                selector.setEnabled(true);
            }
        });
        return source;
    }

    private void initSelectors() {
        selector = new MouseSelectionWrapper(getDesignContainer());
        selector.addMouseSelectionHandler(selectionHandler);
    }

    private CellRangeElement getSelectedCellsArea() {
        CellRangeElement model = null;
        if (selectedCells.size() > 0) {
            model = new CellRangeElement(-1, -1, -1, -1);
            GridLayoutContainer container = (GridLayoutContainer) rootBuilder
                .getComponent();
            for (CellRangeElement m : selectedCells) {
                Cell cellIndexes = container.getGridPositionByTable(m.getTop(),
                    m.getLeft());
                if (cellIndexes == null) {
                    continue;
                }
                int top = cellIndexes.getRow() > m.getTop() ? cellIndexes
                    .getRow() : m.getTop();
                int right = cellIndexes.getColumn() > m.getRight() ? cellIndexes
                    .getColumn() : m.getRight();
                int bottom = cellIndexes.getRow() > m.getBottom() ? cellIndexes
                    .getRow() : m.getBottom();
                int left = cellIndexes.getColumn() > m.getLeft() ? cellIndexes
                    .getColumn() : m.getLeft();

                if (model.getTop() < 0 || model.getTop() > cellIndexes.getRow()) {
                    model.setTop(top);
                }
                if (model.getRight() < 0
                    || model.getRight() < cellIndexes.getColumn()) {
                    model.setRight(right);
                }
                if (model.getBottom() < 0
                    || model.getBottom() < cellIndexes.getRow()) {
                    model.setBottom(bottom);
                }
                if (model.getLeft() < 0
                    || model.getLeft() > cellIndexes.getColumn()) {
                    model.setLeft(left);
                }
            }
        }
        return model;
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
                if (el != null
                    && el.getAbsoluteLeft() <= rectangle.getX()
                    + rectangle.getWidth()
                    && el.getAbsoluteLeft() + el.getClientWidth() >= rectangle
                    .getX()
                    && el.getAbsoluteTop() <= rectangle.getY()
                    + rectangle.getHeight()
                    && el.getAbsoluteTop() + el.getClientHeight() >= rectangle
                    .getY()) {

                    selectedCells.add(new CellRangeElement(i, j, i, j));
                }
            }
        }
        selectGroupCell();
    }

    /**
     * Снятие выделения с ячеек и компонентов
     */
    protected void clearSelection() {
        super.clearSelection();
        selectedCells.clear();
        selectedElements.clear();
        setCellSelected(false);
        fireSelectAreaEvent(new SelectAreaEvent(getSelectedCellsArea()));
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
        Surface surface = setElementSelected(
            el,
            GWT.<DefaultSurfaceAppearance>create(DefaultSurfaceAppearance.class),
            selected);
        if (surface != null) {
            surface.getElement().getStyle().setZIndex(1);
        }
    }

    /**
     * Выделение группы ячеек и находящихся на них компонентов
     *
     * @param model    Модель данных группы ячеек
     * @param selected Установить или снять выделение
     */
    private void selectGroupCell() {
        FlexTable table = getFormTable(rootBuilder.getComponent());
        for (CellRangeElement model : selectedCells) {
            if (model.getTop() >= table.getRowCount()
                || model.getLeft() >= table.getCellCount(model.getTop())) {
                continue;
            }
            Element el = table.getFlexCellFormatter().getElement(
                model.getTop(), model.getLeft());
            if (el != null) {
                setBuilderSelection(((XElement) el).getBounds());
                setCellSelected(el, true);
            }
        }
        if (selectedElements.size() == 1) {
            selectedElement = selectedElements.get(0);
            fireSelectComponentEvent(new SelectComponentEvent(selectedElement));
        }
        fireSelectAreaEvent(new SelectAreaEvent(getSelectedCellsArea()));
    }

    /**
     * Изменение стиля для выделенных селектором компонентов ( {@link #selectedElements})
     *
     * @param rectangle Координаты селектора
     */
    private void setBuilderSelection(Rectangle rectangle) {
        for (Entry<ComponentElement, ComponentBuilder> entry : builders
            .entrySet()) {
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

            if (rectangle.contains(p1) && rectangle.contains(p2)
                && rootElement.getChildren().contains(e)) {
                selectedElements.add(e);
                setBuilderSelected(el, true);
            }
        }
    }

    private void onAddRow() {
        CellRangeElement model = getSelectedCellsArea();
        // Может ли быть не formElement?
        FormElement form = (FormElement) rootElement;
        int rows = form.getProperty(PropertyType.Rows).getValue(defaultLocale)
            .getDouble().intValue();
        int cols = form.getProperty(PropertyType.Columns)
            .getValue(defaultLocale).getDouble().intValue();

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
                    int compRow = comp
                        .getProperty(PropertyType.LayoutDataFormRow)
                        .getValue(defaultLocale).getDouble()
                        .intValue();
                    if (compRow >= model.getTop()) {
                        fireComponentPropertyChangeEvent(new ComponentPropertyChangeEvent(
                            form, PropertyType.LayoutDataFormRow,
                            new PropertyValue(DataType.NUMBER,
                                defaultLocale, ++compRow)));
                    }
                }
            }
        }
        firePropertyChangeEvent(new PropertyChangeEvent(form,
            new FormRowInsertData(form, index)));
        repaint();
    }

    private void onAddColumn() {
        CellRangeElement model = getSelectedCellsArea();
        FormElement form = (FormElement) rootElement;
        int rows = form.getProperty(PropertyType.Rows).getValue(defaultLocale)
            .getDouble().intValue();
        int cols = form.getProperty(PropertyType.Columns)
            .getValue(defaultLocale).getDouble().intValue();

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
                    int compCol = comp
                        .getProperty(PropertyType.LayoutDataFormColumn)
                        .getValue(defaultLocale).getDouble()
                        .intValue();
                    if (compCol >= model.getLeft()) {
                        fireComponentPropertyChangeEvent(new ComponentPropertyChangeEvent(
                            comp, PropertyType.LayoutDataFormColumn,
                            new PropertyValue(DataType.NUMBER,
                                defaultLocale, ++compCol)));
                    }
                }
            }
        }
        firePropertyChangeEvent(new PropertyChangeEvent(form,
            new FormColumnInsertData(form, index)));
        repaint();
    }

    // Поменять название?
    private void onDeleteRow() {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            int rowsCount = model.getBottom() - model.getTop() + 1;
            int rowNum = model.getTop();
            if (!checkRowsHasSpans()) {
                for (int i = 0; i < rowsCount; i++) {
                    int rows = rootElement.getProperty(PropertyType.Rows)
                        .getValue(defaultLocale).getDouble()
                        .intValue();
                    if (rowNum < rows && rows > 1) {
                        deleteRow(rowNum);
                    }
                }
                clearSelection();
                repaint();
            } else {
                Info.display(EditorMessage.Util.MESSAGE.warn(),
                    EditorMessage.Util.MESSAGE.warn_need_to_split());
            }
        }
    }

    /**
     * Удаление строки на форме
     *
     * @param index Индекс удаляемой строки
     */
    public void deleteRow(int index) {
        FormElement form = (FormElement) rootElement;
        int rows = rootElement.getProperty(PropertyType.Rows)
            .getValue(defaultLocale).getDouble().intValue();
        int cols = rootElement.getProperty(PropertyType.Columns)
            .getValue(defaultLocale).getDouble().intValue();
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
                    .getValue(defaultLocale).getDouble().intValue();
                if (compRow > index) {
                    comp.setProperty(PropertyType.LayoutDataFormRow,
                        new PropertyValue(DataType.NUMBER, defaultLocale,
                            --compRow));
                } else if (compRow == index) {
                    // перемещаем компоненты, расположенные на удаляемых
                    // ячейках в
                    // группу "Свободные компоненты"
                    fireRemoveComponentEvent(new RemoveComponentEvent(form, comp));
                }
            }
        }

        firePropertyChangeEvent(new PropertyChangeEvent(form,
            new FormRowDeleteData(form, index)));
    }

    private void onDeleteColumn() {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {

            int colsCount = model.getRight() - model.getLeft() + 1;
            int colNum = model.getLeft();
            if (!checkColumnsHasSpans()) {
                for (int i = 0; i < colsCount; i++) {
                    int cols = rootElement.getProperty(PropertyType.Columns)
                        .getValue(defaultLocale).getDouble()
                        .intValue();
                    if (colNum < cols && cols > 1) {
                        deleteColumn(colNum);
                    }
                }

                clearSelection();
                repaint();
            } else {
                Info.display(EditorMessage.Util.MESSAGE.warn(),
                    EditorMessage.Util.MESSAGE.warn_need_to_split());
            }
        }
    }

    /**
     * Удаление колонки на форме
     *
     * @param index Индекс удаляемой колонки
     */
    public void deleteColumn(int index) {
        FormElement form = (FormElement) rootElement;
        int rows = rootElement.getProperty(PropertyType.Rows)
            .getValue(defaultLocale).getDouble().intValue();
        int cols = rootElement.getProperty(PropertyType.Columns)
            .getValue(defaultLocale).getDouble().intValue();

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
                int compCol = comp
                    .getProperty(PropertyType.LayoutDataFormColumn)
                    .getValue(defaultLocale).getDouble().intValue();
                if (compCol > index) {
                    comp.setProperty(PropertyType.LayoutDataFormColumn,
                        new PropertyValue(DataType.NUMBER, defaultLocale,
                            --compCol));
                } else if (compCol == index) {
                    // перемещаем компоненты, расположенные на удаляемых
                    // ячейках в
                    // группу "Свободные компоненты"
                    fireRemoveComponentEvent(new RemoveComponentEvent(form, comp));
                }
            }
        }
        firePropertyChangeEvent(new PropertyChangeEvent(form,
            new FormColumnDeleteData(form, index)));
    }

    private boolean checkRowsHasSpans() {
        boolean hasSpans = false;
        FlexTable table = getFormTable(rootBuilder.getComponent());

        int top = table.getElement().getAbsoluteBottom();
        int bottom = 0;
        for (CellRangeElement model : selectedCells) {
            int topTmp = table.getFlexCellFormatter()
                .getElement(model.getTop(), model.getLeft())
                .getAbsoluteTop() + 1;
            if (topTmp < top) {
                top = topTmp;
            }
            int botTmp = table.getFlexCellFormatter()
                .getElement(model.getBottom(), model.getLeft())
                .getAbsoluteBottom() - 1;
            if (botTmp > bottom) {
                bottom = botTmp;
            }
        }
        int width = table.getElement().getClientWidth();

        clearSelection();
        setSelection(new Rectangle(table.getAbsoluteLeft(), top + 1, width,
            bottom - top - 1));

        CellRangeElement model = getSelectedCellsArea();
        for (int i = model.getTop(); i <= model.getBottom(); i++) {
            for (int j = model.getLeft(); j <= model.getRight(); j++) {
                if (((FormElement) rootElement).findCellElement(i, j)
                    .getRowSpan() > 1) {
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
            int leftTmp = table.getFlexCellFormatter()
                .getElement(model.getTop(), model.getLeft())
                .getAbsoluteLeft() + 1;
            if (leftTmp < left) {
                left = leftTmp;
            }
            int rightTmp = table.getFlexCellFormatter()
                .getElement(model.getTop(), model.getRight())
                .getAbsoluteRight() - 1;
            if (rightTmp > right) {
                right = rightTmp;
            }
        }

        int height = table.getElement().getClientHeight();

        clearSelection();
        // Прибавляю/отнимаю 2 чтобы учитывать padding
        setSelection(new Rectangle(left + 1, table.getAbsoluteTop(), right
            - left - 1, height));

        CellRangeElement model = getSelectedCellsArea();
        for (int i = model.getTop(); i <= model.getBottom(); i++) {
            for (int j = model.getLeft(); j <= model.getRight(); j++) {
                if (((FormElement) rootElement).findCellElement(i, j)
                    .getColSpan() > 1) {
                    hasSpans = true;
                }
            }
        }
        return hasSpans;
    }

    private void onUnion() {
        if (rootElement instanceof FormElement) {
            CellRangeElement model = getSelectedCellsArea();
            if (model != null) {
                int row = model.getTop();
                int col = model.getLeft();
                int bottomSpan = ((FormElement) rootElement).findCellElement(
                    model.getBottom(), col).getRowSpan();
                int rightSpan = ((FormElement) rootElement).findCellElement(
                    row, model.getRight()).getColSpan();

                if (selectedElements.size() > 1) {
                    InfoHelper.error("more-than-one-element", EditorMessage.Util.MESSAGE.error(),
                        EditorMessage.Util.MESSAGE
                            .error_more_than_one_element());
                    return;
                } else if (selectedElements.size() == 1) {
                    ComponentElement comp = selectedElements.get(0);
                    if (comp.getProperty(PropertyType.LayoutDataFormRow)
                        .getValue(defaultLocale).getDouble()
                        .intValue() != row) {
                        comp.setProperty(PropertyType.LayoutDataFormRow,
                            new PropertyValue(DataType.NUMBER,
                                defaultLocale, row));
                    }
                    if (comp.getProperty(PropertyType.LayoutDataFormColumn)
                        .getValue(defaultLocale).getDouble()
                        .intValue() != col) {
                        comp.setProperty(PropertyType.LayoutDataFormColumn,
                            new PropertyValue(DataType.NUMBER,
                                defaultLocale, col));
                    }
                }

                // Разбиваем все выделенные объединенные ячейки
                for (int i = model.getTop(); i < model.getBottom() + bottomSpan; i++) {
                    for (int j = model.getLeft(); j < model.getRight()
                        + rightSpan; j++) {
                        ((FormElement) rootElement).findCellElement(i, j)
                            .setColSpan(1);
                        ((FormElement) rootElement).findCellElement(i, j)
                            .setRowSpan(1);
                    }
                }
                int rowSpan = model.getBottom() - model.getTop() + bottomSpan;
                int colSpan = model.getRight() - model.getLeft() + rightSpan;
                CellElement m = ((FormElement) rootElement).findCellElement(
                    row, col);
                m.setRowSpan(rowSpan);
                m.setColSpan(colSpan);
                clearSelection();
                repaint();
            }
        }
    }

    private void onDivide() {
        if (rootElement instanceof FormElement) {
            CellRangeElement model = getSelectedCellsArea();
            if (model != null) {
                for (int i = model.getTop(); i <= model.getBottom(); i++) {
                    for (int j = model.getLeft(); j <= model.getRight(); j++) {
                        CellElement m = ((FormElement) rootElement)
                            .findCellElement(i, j);
                        m.setRowSpan(1);
                        m.setColSpan(1);
                    }
                }
                clearSelection();
                repaint();
            }
        }
    }

    private void onAddBorderTop(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getLeft(); i <= model.getRight(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(
                    model.getTop(), i);
                m.setBorderTop(width);
                m.setBorderTopColor(color);
            }
            repaint();
        }
    }

    private void onAddBorderRight(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(i,
                    model.getRight());
                m.setBorderRight(width);
                m.setBorderRightColor(color);
            }
            repaint();
        }
    }

    private void onAddBorderBottom(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getLeft(); i <= model.getRight(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(
                    model.getBottom(), i);
                m.setBorderBottom(width);
                m.setBorderBottomColor(color);
            }
            repaint();
        }
    }

    private void onAddBorderLeft(int width, String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                CellElement m = ((FormElement) rootElement).findCellElement(i,
                    model.getLeft());
                m.setBorderLeft(width);
                m.setBorderLeftColor(color);
            }
            repaint();
        }
    }

    private void onSetColor(String color) {
        CellRangeElement model = getSelectedCellsArea();
        if (model != null) {
            for (int i = model.getTop(); i <= model.getBottom(); i++) {
                for (int j = model.getLeft(); j <= model.getRight(); j++) {
                    CellElement m = ((FormElement) rootElement)
                        .findCellElement(i, j);
                    m.setBackgroundColor(color);
                }
            }
            repaint();
        }
    }

}
