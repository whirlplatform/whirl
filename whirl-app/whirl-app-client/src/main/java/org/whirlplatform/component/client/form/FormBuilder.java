package org.whirlplatform.component.client.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.ContainerHelper;
import org.whirlplatform.component.client.HasCreateParameters;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.RefreshEvent;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.form.FormCellModel;
import org.whirlplatform.meta.shared.form.FormColumnModel;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.meta.shared.form.FormRowModel;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.ListHolder;
import org.whirlplatform.rpc.shared.SessionToken;

/**
 * Форма
 */
@JsType(name = "Form", namespace = "Whirl")
public class FormBuilder extends ComponentBuilder implements Containable,
    HasCreateParameters, LoadEvent.HasLoadHandlers, RefreshEvent.HasRefreshHandlers {

    private GridLayoutContainer container;
    private GridLayoutDecorator decorator;
    private ParameterHelper paramHelper;

    private List<ComponentBuilder> children;

    private AsyncCallback<FormModel> loadCallback;

    private AsyncCallback<FormModel> refreshCallback;

    @JsConstructor
    public FormBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public FormBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.FormBuilderType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        paramHelper = new ParameterHelper();
        children = new ArrayList<ComponentBuilder>();
        loadCallback = new AsyncCallback<FormModel>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("form-load", caught);
            }

            @Override
            public void onSuccess(FormModel result) {
                onLoad(result);
            }

        };
        refreshCallback = new AsyncCallback<FormModel>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("form-refresh", caught);
            }

            @Override
            public void onSuccess(FormModel result) {
                onRefresh(result);
            }

        };

        container = new GridLayoutContainer();
        decorator = new GridLayoutDecorator(container);
        return container;
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Grid.getCode())) {
            decorator.setGridEnabled(Boolean.TRUE.equals(value.getBoolean()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.GridColor.getCode())) {
            decorator.setGridColor(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Rows.getCode())) {
            container.resizeRows(value.getDouble() == null ? 1 : value.getDouble().intValue());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Columns.getCode())) {
            container.resizeColumns(value.getDouble() == null ? 1 : value.getDouble().intValue());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Parameters.getCode())) {
            paramHelper.addJsonParameters(value.getString());
            return true;
        }
        return super.setProperty(name, value);
    }

    private List<DataValue> defaultParameters(List<DataValue> parameters) {
        List<DataValue> allParams = new ArrayList<DataValue>();
        for (Entry<String, DataValue> entry : paramHelper.getValues(parameters)
            .entrySet()) {
            DataValue v = entry.getValue();
            v.setCode(entry.getKey());
            allParams.add(v);
        }
        return allParams;
    }

    @JsIgnore
    @Override
    public Component create(List<DataValue> parameters) {
        Component component = super.create();
        load(paramHelper.getValueList(parameters));
        return component;
    }

    @JsIgnore
    @Override
    public Component create() {
        return create(Collections.emptyList());
    }

    /**
     * Загружает.
     */
    public void load() {
        load(defaultParameters(Collections.emptyList()));
    }

    @JsIgnore
    public void load(final List<DataValue> parameters) {
        container.mask(DefaultMessages.getMessages().loadMask_msg());
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                DataServiceAsync.Util.getDataService(loadCallback)
                    .getForm(SessionToken.get(), getId(),
                        new ListHolder(parameters));
            }
        });
    }

    /**
     * Обновляет.
     */
    public void refresh() {
        refresh(defaultParameters(Collections.emptyList()));
    }

    @JsIgnore
    public void refresh(final List<DataValue> parameters) {
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            @Override
            public boolean execute() {
                DataServiceAsync.Util.getDataService(refreshCallback)
                    .getForm(SessionToken.get(), getId(),
                        new ListHolder(parameters));
                return false;
            }
        }, 0);
    }

    private void onLoad(FormModel form) {
        if (form == null) {
            return;
        }
        clearContainer();
        //        container.clearCellGroupings();
        container.clearSpans();

        // размеры
        container.resize(form.getRowCount(), form.getColumnCount());

        for (FormColumnModel col : form.getColumns()) {
            container.setColumnWidth(col.getCol(), col.getWidth());
        }

        // Сначала устанавливаем объединение, т.к. потом компонент может
        // добавиться не в ту ячейку.
        for (FormRowModel row : form.getRows()) {
            for (FormCellModel cell : row.getCells()) {
                if (cell.getRowSpan() != 1 || cell.getColSpan() != 1) {
                    container.setSpan(cell.getRow().getRow(), cell.getColumn()
                            .getCol(), cell.getRowSpan(), cell.getColSpan(),
                        false);
                }
            }
        }

        // строим компоненты
        for (FormRowModel row : form.getRows()) {
            container.setRowHeight(row.getRow(), row.getHeight());
            for (FormCellModel cell : row.getCells()) {
                int r = cell.getRow().getRow();
                int c = cell.getColumn().getCol();
                decorator.setCellBorderTop(r, c, cell.getBorderTop(),
                    cell.getBorderTopColor());
                decorator.setCellBorderRight(r, c, cell.getBorderRight(),
                    cell.getBorderRightColor());
                decorator.setCellBorderBottom(r, c, cell.getBorderBottom(),
                    cell.getBorderBottomColor());
                decorator.setCellBorderLeft(r, c, cell.getBorderLeft(),
                    cell.getBorderLeftColor());

                decorator.setCellColor(r, c, cell.getColor());

                if (cell.getComponent() != null) {
                    ContainerHelper.buildComponent(
                        Collections.singleton(cell.getComponent()), this);
                }
            }
        }

        //        for (CellGroupModel group : form.getGroups()) {
        //            container.setCellGrouping(
        //                    new Region(group.getTop(), group.getRight(), group
        //                            .getBottom(), group.getLeft()), group.getTitle(),
        //                    true);
        //        }
        decorator.setPaddingInCells(1);
        container.doLayout();
        container.unmask();
        fireEvent(new LoadEvent());
    }

    private void onRefresh(FormModel form) {
        if (container.getRowCount() != form.getRowCount()
            || container.getColumnCount() != form.getColumnCount()) {
            return;
        }
        for (FormRowModel row : form.getRows()) {
            for (FormCellModel cell : row.getCells()) {
                ComponentBuilder b = getChild(row.getRow(), cell.getColumn()
                    .getCol());
                ComponentModel m = cell.getComponent();

                changeProperties(b, m);

            }
        }
        fireEvent(new RefreshEvent());
    }

    public ComponentBuilder getChild(int row, int column) {
        for (ComponentBuilder b : getChildren()) {
            if (b.getRowPosition() == row && b.getColumnPosition() == column) {
                return b;
            }
        }
        return null;
    }

    private void changeProperties(ComponentBuilder builder, ComponentModel model) {
        if (model == null || builder == null
            || !model.getId().equals(builder.getId())) {
            return;
        }
        for (String p : builder.getReplaceableProperties()) {
            builder.setProperty(p, model.getValue(p));
        }

        if (!(builder instanceof Containable)
            || builder.getType() == ComponentType.FormBuilderType
            || builder.getType() == ComponentType.ReportType) {
            return;
        }
        for (ComponentBuilder b : ((Containable) builder).getChildren()) {
            for (ComponentModel m : model.getChildren()) {
                if (m.getId().equals(b.getId())) {
                    changeProperties(b, m);
                    break;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GridLayoutContainer getRealComponent() {
        return container;
    }

    @Override
    public void addChild(ComponentBuilder child) {
        container.setWidget(child.getRowPosition(), child.getColumnPosition(),
            child.getComponent(), child.getGridLayoutData());
        children.add(child);
        child.setParentBuilder(this);
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        if (container.remove(child.getComponent())) {
            children.remove(child);
            child.setParentBuilder(null);
        }
    }

    /**
     * Очищает контейнер.
     */
    @Override
    public void clearContainer() {
        // for (ComponentBuilder c : children) {
        // removeChild(c);
        // }
        Iterator<ComponentBuilder> iter = children.iterator();
        while (iter.hasNext()) {
            ComponentBuilder child = iter.next();
            if (container.remove(child.getComponent())) {
                iter.remove();
                child.setParentBuilder(null);
            }
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
        return children.toArray(new ComponentBuilder[0]);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    /**
     * Устанавливает высоту строки.
     *
     * @param row    - int, строка
     * @param height - double, высота
     */
    public void setRowHeight(int row, double height) {
        container.setRowHeight(row, height);
    }

    /**
     * Устанавливает ширину столбца.
     *
     * @param column - int, столбец
     * @param width  - double, ширина
     */
    public void setColumnWidth(int column, double width) {
        container.setColumnWidth(column, width);
    }

    /**
     * Задает объединение по горизонтали указанной ячейки.
     *
     * @param row     - int, номер строки
     * @param column  - int, номер столбца
     * @param rowSpan - int, количество объединяемых ячеек
     */
    public void setRowSpan(int row, int column, int rowSpan) {
        if (rowSpan < 1) {
            throw new IndexOutOfBoundsException(
                "Row span can not be less than 1.");
        }

        setSpan(row, column, rowSpan, container.getTable()
            .getFlexCellFormatter().getRowSpan(row, column));
    }

    /**
     * Устанавливает интервал столбцов.
     *
     * @param row     - int, строка
     * @param column  - int, столбец
     * @param colSpan - int, интервал
     */
    public void setColSpan(int row, int column, int colSpan) {
        if (colSpan < 1) {
            throw new IndexOutOfBoundsException(
                "Column span can not be less than 1.");
        }
        setSpan(row, column, container.getTable().getFlexCellFormatter()
            .getRowSpan(row, column), colSpan);
    }

    /**
     * Устанавливает объединение области.
     *
     * @param row     - int, строка
     * @param column  - int, столбец
     * @param rowSpan - int, интервал строки
     * @param colSpan - int, интервал  ячейки
     */
    public void setSpan(int row, int column, int rowSpan, int colSpan) {
        container.setSpan(row, column, rowSpan, colSpan);
    }

    /**
     * Устанавливает цвет верхней границы ячейки.
     *
     * @param row       - int, строка
     * @param column    - int, столбец
     * @param thickness - int, толщина
     * @param color     - String, цвет
     */
    public void setCellBorderTop(int row, int column, int thickness,
                                 String color) {
        decorator.setCellBorderTop(row, column, thickness, color);
    }

    /**
     * Устанавливает цвет правой границы ячейки.
     *
     * @param row       - int, строка
     * @param column    - int, столбец
     * @param thickness - int, толщина
     * @param color     - String, цвет
     */
    public void setCellBorderRight(int row, int column, int thickness,
                                   String color) {
        decorator.setCellBorderRight(row, column, thickness, color);
    }

    /**
     * Устанавливает цвет нижней границы ячейки.
     *
     * @param row       - int, строка
     * @param column    - int, столбец
     * @param thickness - int, толщина
     * @param color     - String, цвет
     */
    public void setCellBorderBottom(int row, int column, int thickness,
                                    String color) {
        decorator.setCellBorderBottom(row, column, thickness, color);
    }

    /**
     * Устанавливает цвет левой границы ячейки.
     *
     * @param row       - int, строка
     * @param column    - int, столбец
     * @param thickness - int, толщина
     * @param color     - String, цвет
     */
    public void setCellBorderLeft(int row, int column, int thickness,
                                  String color) {
        decorator.setCellBorderLeft(row, column, thickness, color);
    }

    /**
     * Устанавливает цвет ячейки.
     *
     * @param row    - int, строка
     * @param column - int, столбец
     * @param color  - String, цвет
     */
    public void setCellColor(int row, int column, String color) {
        decorator.setCellColor(row, column, color);
    }

    /**
     * Устанавливает интервал между ячейками.
     *
     * @param spacing - int, интервал
     */
    public void setCellSpacing(int spacing) {
        decorator.setCellSpacing(spacing);
    }

    /**
     * Устанавливает отступы в ячейке.
     *
     * @param padding - int, отступ
     */
    public void setPaddingInCells(int padding) {
        decorator.setPaddingInCells(padding);
    }

    @JsIgnore
    @Override
    public ComponentBuilder getParentBuilder() {
        // TODO Auto-generated method stub
        return super.getParentBuilder();
    }

    @JsIgnore
    @Override
    public void setParentBuilder(ComponentBuilder parentBuilder) {
        // TODO Auto-generated method stub
        super.setParentBuilder(parentBuilder);
    }

    @JsIgnore
    @Override
    public HandlerRegistration addLoadHandler(LoadEvent.LoadHandler handler) {
        return addHandler(handler, LoadEvent.getType());
    }

    @JsIgnore
    @Override
    public HandlerRegistration addRefreshHandler(RefreshEvent.RefreshHandler handler) {
        return addHandler(handler, RefreshEvent.getType());
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
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true, если компонент включен
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
