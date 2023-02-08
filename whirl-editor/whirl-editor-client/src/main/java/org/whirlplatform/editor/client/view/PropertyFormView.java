package org.whirlplatform.editor.client.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.geomajas.codemirror.client.Config;
import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.component.PropertyValueFieldConverter;
import org.whirlplatform.editor.client.presenter.PropertyFormPresenter;
import org.whirlplatform.editor.client.presenter.PropertyFormPresenter.IPropertyFormView;
import org.whirlplatform.editor.client.util.DataSourceComboBox;
import org.whirlplatform.editor.client.util.DataSourceStore;
import org.whirlplatform.editor.client.util.Random;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.CellRangeElement;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

public class PropertyFormView extends AccordionLayoutContainer
    implements IPropertyFormView, ReverseViewInterface<PropertyFormPresenter> {

    private PropertyFormPresenter presenter;

    private ListStore<RowElement> rowsStore;
    private ListStore<ColumnElement> colsStore;
    private ListStore<RequestElement> reqsStore;
    // private ListStore<CellGroupElement> groupsStore;
    private Window requestWindow;

    // private PropertyValueField groupTitleField;
    private PropertyValueField requestNoDataField;

    private AccordionLayoutAppearance appearance = GWT
        .create(AccordionLayoutAppearance.class);
    private Comparator<RowElement> rowComparator = new Comparator<RowElement>() {
        @Override
        public int compare(RowElement o1, RowElement o2) {
            return o1.getRow() - o2.getRow();
        }
    };
    private Comparator<ColumnElement> colComparator = new Comparator<ColumnElement>() {
        @Override
        public int compare(ColumnElement o1, ColumnElement o2) {
            return o1.getColumn() - o2.getColumn();
        }
    };

    public PropertyFormView() {
        super();
        // initUI();
    }

    @Override
    public void initUI() {
        setExpandMode(ExpandMode.SINGLE_FILL);

        initRowsPanel();
        initColsPanel();
        initRequestsPanel();
        // initGroupsPanel();
    }

    /**
     * Панель с настройками строк формы
     */
    private void initRowsPanel() {
        ContentPanel cp = new ContentPanel(appearance);
        cp.setAnimCollapse(false);
        cp.setHeading(EditorMessage.Util.MESSAGE.property_form_row_height());

        final RowProperties props = GWT.create(RowProperties.class);

        rowsStore = new ListStore<RowElement>(props.id());
        rowsStore.addStoreRecordChangeHandler(new StoreRecordChangeHandler<RowElement>() {
            @Override
            public void onRecordChange(StoreRecordChangeEvent<RowElement> event) {
                ListStore<RowElement>.Record rec = event.getRecord();
                rec.commit(true);
                getPresenter().getEventBus().openElement(getPresenter().getElement());
            }
        });
        rowsStore.addSortInfo(new StoreSortInfo<RowElement>(rowComparator, SortDir.ASC));

        IdentityValueProvider<RowElement> identity = new IdentityValueProvider<RowElement>();
        final CheckBoxSelectionModel<RowElement> sm =
            new CheckBoxSelectionModel<RowElement>(identity);
        sm.setSelectionMode(SelectionMode.SINGLE);
        sm.addSelectionHandler(new SelectionHandler<RowElement>() {
            @Override
            public void onSelection(SelectionEvent<RowElement> event) {
                RowElement model = event.getSelectedItem();
                int index = rowsStore.indexOf(model);
                selectRow(index, true);
            }
        });
        sm.addSelectionChangedHandler(new SelectionChangedHandler<RowElement>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<RowElement> event) {
                if (event.getSelection().isEmpty()) {
                    getPresenter().getEventBus().clearSelection();
                }
            }
        });
        List<ColumnConfig<RowElement, ?>> columns = new ArrayList<ColumnConfig<RowElement, ?>>();
        columns.add(sm.getColumn());
        columns.add(new ColumnConfig<RowElement, Integer>(props.row(), 100,
            EditorMessage.Util.MESSAGE.property_form_row_index()));
        ColumnConfig<RowElement, Double> cc1 =
            new ColumnConfig<RowElement, Double>(props.height(), 100,
                EditorMessage.Util.MESSAGE.property_form_row_h());
        cc1.setCell(new AbstractCell<Double>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Double value,
                               SafeHtmlBuilder sb) {
                if (value != null && !value.isNaN()) {
                    NumberFormat nf = NumberFormat.getDecimalFormat();
                    sb.appendEscaped(nf.format(value));
                }
            }
        });
        columns.add(cc1);
        ColumnModel<RowElement> columnModel = new ColumnModel<RowElement>(columns);
        final Grid<RowElement> grid = new Grid<RowElement>(rowsStore, columnModel);
        GridEditing<RowElement> editing = new GridInlineEditing<RowElement>(grid);

        NumberField<Double> editor = new NumberField<Double>(
            new NumberPropertyEditor.DoublePropertyEditor(NumberFormat.getDecimalFormat()));
        editing.addEditor(cc1, editor);
        grid.setSelectionModel(sm);

        cp.setWidget(grid);
        add(cp);
        setActiveWidget(cp);
    }

    /**
     * Панель с настройками колонок формы
     */
    private void initColsPanel() {
        ContentPanel cp = new ContentPanel(appearance);
        cp.setAnimCollapse(false);
        cp.setHeading(EditorMessage.Util.MESSAGE.property_form_column_width());

        final ColumnProperties props = GWT.create(ColumnProperties.class);

        colsStore = new ListStore<ColumnElement>(props.id());
        colsStore.addStoreRecordChangeHandler(new StoreRecordChangeHandler<ColumnElement>() {
            @Override
            public void onRecordChange(StoreRecordChangeEvent<ColumnElement> event) {
                ListStore<ColumnElement>.Record rec = event.getRecord();
                rec.commit(true);
                getPresenter().getEventBus().openElement(getPresenter().getElement());
            }
        });
        colsStore.addSortInfo(new StoreSortInfo<ColumnElement>(colComparator, SortDir.ASC));
        IdentityValueProvider<ColumnElement> identity = new IdentityValueProvider<ColumnElement>();
        CheckBoxSelectionModel<ColumnElement> sm =
            new CheckBoxSelectionModel<ColumnElement>(identity);
        sm.setSelectionMode(SelectionMode.SINGLE);
        sm.addSelectionHandler(new SelectionHandler<ColumnElement>() {
            @Override
            public void onSelection(SelectionEvent<ColumnElement> event) {
                ColumnElement model = event.getSelectedItem();
                int index = colsStore.indexOf(model);
                selectColumn(index, true);
            }
        });
        sm.addSelectionChangedHandler(new SelectionChangedHandler<ColumnElement>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<ColumnElement> event) {
                if (event.getSelection().isEmpty()) {
                    getPresenter().getEventBus().clearSelection();
                }
            }
        });
        List<ColumnConfig<ColumnElement, ?>> columns =
            new ArrayList<ColumnConfig<ColumnElement, ?>>();
        columns.add(sm.getColumn());
        columns.add(new ColumnConfig<ColumnElement, Integer>(props.column(), 100,
            EditorMessage.Util.MESSAGE.property_form_column_index()));
        ColumnConfig<ColumnElement, Double> cc1 =
            new ColumnConfig<ColumnElement, Double>(props.width(), 100,
                EditorMessage.Util.MESSAGE.property_form_column_w());
        cc1.setCell(new AbstractCell<Double>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Double value,
                               SafeHtmlBuilder sb) {
                if (value != null && !value.isNaN()) {
                    NumberFormat nf = NumberFormat.getDecimalFormat();
                    sb.appendEscaped(nf.format(value));
                }
            }
        });
        columns.add(cc1);
        ColumnModel<ColumnElement> columnModel = new ColumnModel<ColumnElement>(columns);
        final Grid<ColumnElement> grid = new Grid<ColumnElement>(colsStore, columnModel);
        GridEditing<ColumnElement> editing = new GridInlineEditing<ColumnElement>(grid);
        NumberField<Double> editor = new NumberField<Double>(
            new NumberPropertyEditor.DoublePropertyEditor(NumberFormat.getDecimalFormat()));
        editing.addEditor(cc1, editor);

        grid.setSelectionModel(sm);

        cp.setWidget(grid);
        add(cp);
    }

    /**
     * Панель с настройками SQL-запросов
     */
    private void initRequestsPanel() {

        requestNoDataField = new PropertyValueField();

        ContentPanel cp = new ContentPanel(appearance);
        cp.setAnimCollapse(false);
        cp.setHeading(EditorMessage.Util.MESSAGE.property_form_sql_by_row());
        final RequestProperties props = GWT.create(RequestProperties.class);
        reqsStore = new ListStore<RequestElement>(props.id());
        reqsStore.addStoreRecordChangeHandler(new StoreRecordChangeHandler<RequestElement>() {
            @Override
            public void onRecordChange(StoreRecordChangeEvent<RequestElement> event) {
                ListStore<RequestElement>.Record rec = event.getRecord();
                RequestElement model = rec.getModel();
                int top = model.getTop();
                int bottom = model.getBottom();
                if (rec.getChange(props.top()) != null) {
                    top = rec.getChange(props.top()).getValue();
                }
                if (rec.getChange(props.bottom()) != null) {
                    bottom = rec.getChange(props.bottom()).getValue();
                }
                boolean duplicate = false;
                for (RequestElement m : reqsStore.getAll()) {
                    if (model == m) {
                        continue;
                    }
                    if (top == m.getTop() && bottom == m.getBottom()) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    rec.commit(true);
                } else {
                    rec.revert();
                    Info.display(EditorMessage.Util.MESSAGE.error(),
                        EditorMessage.Util.MESSAGE.property_form_sql_already_exists());
                }
            }
        });
        IdentityValueProvider<RequestElement> identity =
            new IdentityValueProvider<RequestElement>();
        final CheckBoxSelectionModel<RequestElement> sm =
            new CheckBoxSelectionModel<RequestElement>(identity);
        sm.setSelectionMode(SelectionMode.SINGLE);
        sm.addSelectionHandler(new SelectionHandler<RequestElement>() {
            @Override
            public void onSelection(SelectionEvent<RequestElement> event) {
                RequestElement model = event.getSelectedItem();
                getPresenter().getEventBus().selectGroupCell(model);
            }
        });
        sm.addSelectionChangedHandler(new SelectionChangedHandler<RequestElement>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<RequestElement> event) {
                if (event.getSelection().isEmpty()) {
                    getPresenter().getEventBus().clearSelection();
                }
            }
        });
        List<ColumnConfig<RequestElement, ?>> columns =
            new ArrayList<ColumnConfig<RequestElement, ?>>();
        columns.add(sm.getColumn());
        ColumnConfig<RequestElement, String> cc1 =
            new ColumnConfig<RequestElement, String>(props.sql(), 100,
                EditorMessage.Util.MESSAGE.property_form_sql_query());
        ColumnConfig<RequestElement, Integer> cc2 =
            new ColumnConfig<RequestElement, Integer>(props.top(), 50,
                EditorMessage.Util.MESSAGE.property_form_sql_top_index());
        ColumnConfig<RequestElement, Integer> cc3 =
            new ColumnConfig<RequestElement, Integer>(props.bottom(), 50,
                EditorMessage.Util.MESSAGE.property_form_sql_bottom_index());
        ColumnConfig<RequestElement, PropertyValue> cc4 =
            new ColumnConfig<RequestElement, PropertyValue>(
                props.emptyText(), 100,
                EditorMessage.Util.MESSAGE.property_form_sql_no_data());
        ColumnConfig<RequestElement, DataSourceElement> cc5 =
            new ColumnConfig<RequestElement, DataSourceElement>(
                props.dataSource(), 100,
                EditorMessage.Util.MESSAGE.property_form_sql_datasource());
        columns.add(cc1);
        columns.add(cc2);
        columns.add(cc3);
        columns.add(cc4);
        columns.add(cc5);
        ColumnModel<RequestElement> columnModel = new ColumnModel<RequestElement>(columns);

        final Grid<RequestElement> grid = new Grid<RequestElement>(reqsStore, columnModel);
        GridInlineEditing<RequestElement> editing = new GridInlineEditing<RequestElement>(grid);
        editing.setClicksToEdit(ClicksToEdit.TWO);
        editing.addEditor(cc1, new TextArea());
        editing.addEditor(cc2,
            new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor(null)));
        editing.addEditor(cc3,
            new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor(null)));
        editing.addEditor(cc4, new PropertyValueFieldConverter(requestNoDataField),
            requestNoDataField);
        editing.addEditor(cc5,
            new DataSourceComboBox(new DataSourceStore(getPresenter().getEventBus())));
        grid.setSelectionModel(sm);
        SelectHandler sh1 = new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                CellRangeElement selectedCells = getPresenter().getSelectedCellsArea();
                if (selectedCells != null) {
                    boolean duplicate = false;
                    for (RequestElement m : reqsStore.getAll()) {
                        if (selectedCells.getTop() == m.getTop()
                            && selectedCells.getBottom() == m.getBottom()) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate) {
                        // TODO перенести создание в ElementEventHandler
                        RequestElement model = new RequestElement();
                        model.setId(String.valueOf(Random.nextLong()));
                        model.setTop(selectedCells.getTop());
                        model.setBottom(selectedCells.getBottom());

                        sm.deselectAll();
                        reqsStore.add(model);

                        List<RequestElement> ls = new ArrayList<RequestElement>();
                        ls.addAll(reqsStore.getAll());

                        setRequests(getPresenter().getElement().getType(), ls);
                    } else {
                        Info.display(EditorMessage.Util.MESSAGE.error(),
                            EditorMessage.Util.MESSAGE.property_form_sql_already_exists());
                    }
                }
            }
        };
        SelectHandler sh2 = new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                RequestElement model = grid.getSelectionModel().getSelectedItem();
                if (model != null) {
                    reqsStore.remove(model);

                    List<RequestElement> ls = new ArrayList<RequestElement>();
                    ls.addAll(reqsStore.getAll());

                    setRequests(getPresenter().getElement().getType(), ls);
                    getPresenter().getEventBus().clearSelection();
                }
            }
        };
        SelectHandler sh3 = new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                RequestElement model = grid.getSelectionModel().getSelectedItem();
                if (requestWindow != null && requestWindow.isVisible()) {
                    return;
                }
                if (model != null) {
                    showRequestWindow(model, grid);
                }
            }
        };
        cp.addButton(new TextButton(EditorMessage.Util.MESSAGE.property_form_sql_add(), sh1));
        cp.addButton(new TextButton(EditorMessage.Util.MESSAGE.property_form_sql_remove(), sh2));
        cp.addButton(new TextButton(EditorMessage.Util.MESSAGE.property_form_sql_edit(), sh3));
        cp.setButtonAlign(BoxLayoutPack.CENTER);

        cp.setWidget(grid);
        add(cp);
    }

    private void showRequestWindow(final RequestElement model, final Grid<RequestElement> grid) {
        final CodeMirrorPanel panel = new CodeMirrorPanel();

        requestWindow = new Window();
        requestWindow.getHeader().setText(EditorMessage.Util.MESSAGE.property_form_sql_editing());

        TextButton accept = new TextButton(EditorMessage.Util.MESSAGE.apply());
        accept.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                model.setSql(panel.getValue());
                grid.getView().refresh(false);
                requestWindow.hide();
            }
        });

        TextButton cancel = new TextButton(EditorMessage.Util.MESSAGE.close());
        cancel.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                requestWindow.hide();
            }
        });
        requestWindow.setSize("500", "400");
        panel.setValue(model.getSql());
        requestWindow.setWidget(panel);

        requestWindow.addButton(accept);
        requestWindow.addButton(cancel);
        requestWindow.show();
        panel.showEditor(Config.forSql());
    }

    private void selectRow(int row, boolean selected) {
        getPresenter().getEventBus().selectRow(row, selected);
    }

    private void selectColumn(int column, boolean selected) {
        getPresenter().getEventBus().selectColumn(column, selected);
    }

    @Override
    public List<RowElement> getRowsHeight() {
        return rowsStore.getAll();
    }

    @Override
    public void setRowsHeight(ComponentType componentType, List<RowElement> rowsHeight) {
        rowsStore.clear();
        rowsStore.addAll(rowsHeight);
        presenter.getElement().setRowsHeight(rowsHeight);
    }

    @Override
    public List<ColumnElement> getColumnsWidth() {
        return colsStore.getAll();
    }

    @Override
    public void setColumnsWidth(ComponentType componentType, List<ColumnElement> colsWidth) {
        colsStore.clear();
        colsStore.addAll(colsWidth);
        presenter.getElement().setColumnsWidth(colsWidth);
    }

    @Override
    public List<RequestElement> getRequests() {
        return reqsStore.getAll();
    }

    @Override
    public void setRequests(ComponentType componentType, List<RequestElement> requests) {
        reqsStore.clear();
        reqsStore.addAll(requests);
        presenter.getElement().setRowRequests(requests);
    }

    @Override
    public void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale) {
        // groupTitleField.setLocales(defaultLocale, locales);
        requestNoDataField.setLocales(defaultLocale, locales);
    }

    @Override
    public PropertyFormPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(PropertyFormPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onExpand(ExpandEvent event) {
        super.onExpand(event);
        //getPresenter().getEventBus().clearSelection();
    }

    interface CellRangeProperties extends PropertyAccess<CellRangeElement> {
        ModelKeyProvider<CellRangeElement> id();

        ValueProvider<CellRangeElement, Integer> top();

        ValueProvider<CellRangeElement, Integer> right();

        ValueProvider<CellRangeElement, Integer> bottom();

        ValueProvider<CellRangeElement, Integer> left();
    }

    interface RequestProperties extends CellRangeProperties {
        ValueProvider<RequestElement, String> sql();

        ValueProvider<RequestElement, PropertyValue> emptyText();

        ValueProvider<RequestElement, DataSourceElement> dataSource();
    }

    interface RowProperties extends PropertyAccess<RowElement> {
        ModelKeyProvider<RowElement> id();

        ValueProvider<RowElement, Integer> row();

        ValueProvider<RowElement, Double> height();
    }

    interface ColumnProperties extends PropertyAccess<ColumnElement> {
        ModelKeyProvider<ColumnElement> id();

        ValueProvider<ColumnElement, Integer> column();

        ValueProvider<ColumnElement, Double> width();
    }

}
