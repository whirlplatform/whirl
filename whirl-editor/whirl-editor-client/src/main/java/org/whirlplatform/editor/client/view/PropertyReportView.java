package org.whirlplatform.editor.client.view;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import org.whirlplatform.editor.client.presenter.PropertyReportPresenter;
import org.whirlplatform.editor.client.presenter.PropertyReportPresenter.IPropertyReportView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.ListViewType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyReportView extends ContentPanel implements IPropertyReportView {

    private PropertyReportPresenter presenter;

    private Grid<FieldMetadata> grid;
    private ListStore<FieldMetadata> store;

    interface FieldProperties extends PropertyAccess<FieldMetadata> {
        ValueProvider<FieldMetadata, String> name();

        ValueProvider<FieldMetadata, String> label();

        ValueProvider<FieldMetadata, DataType> type();

        ValueProvider<FieldMetadata, String> classId();

        ValueProvider<FieldMetadata, Boolean> required();

        ValueProvider<FieldMetadata, ListViewType> listViewType();
    }

    public PropertyReportView() {
        store = new ListStore<FieldMetadata>(new ModelKeyProvider<FieldMetadata>() {

            @Override
            public String getKey(FieldMetadata item) {
                return item.getName();
            }
        });

        store.setAutoCommit(true);
        initGrid();
        initButtons();
        add(grid);
    }

    private void initGrid() {
        IdentityValueProvider<FieldMetadata> identity = new IdentityValueProvider<FieldMetadata>();
        CheckBoxSelectionModel<FieldMetadata> sm = new CheckBoxSelectionModel<FieldMetadata>(identity);
        sm.setSelectionMode(SelectionMode.SINGLE);
        // sm.addSelectionHandler(new SelectionHandler<RowColModel>() {
        // @Override
        // public void onSelection(SelectionEvent<RowColModel> event) {
        // RowColModel model = event.getSelectedItem();
        // getPresenter().getEventBus().selectRow(model.getIndex());
        // }
        // });
        // sm.addSelectionChangedHandler(new
        // SelectionChangedHandler<RowColModel>() {
        // @Override
        // public void onSelectionChanged(
        // SelectionChangedEvent<RowColModel> event) {
        // if (event.getSelection().isEmpty()) {
        // getPresenter().getEventBus().clearSelection();
        // }
        // }
        // });
        // ValueProvider<FieldMetadata, ListModelData> classIdProvider = new
        // ValueProvider<FieldMetadata, ListModelData>() {
        //
        // @Override
        // public ListModelData getValue(FieldMetadata object) {
        // ListModelData result = new ListModelData();
        // result.setId(object.getClassId());
        // return result;
        // }
        //
        // @Override
        // public void setValue(FieldMetadata object, ListModelData value) {
        // }
        //
        // @Override
        // public String getPath() {
        // return null;
        // }
        //
        // };

        ValueProvider<FieldMetadata, String> labelProvider = new ValueProvider<FieldMetadata, String>() {

            @Override
            public String getValue(FieldMetadata object) {
                return object.getRawLabel();
            }

            @Override
            public void setValue(FieldMetadata object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        };

        FieldProperties props = GWT.create(FieldProperties.class);

        // ColumnConfig<FieldMetadata, ListModelData> classIdColumn = new
        // ColumnConfig<FieldMetadata, ListModelData>(
        // classIdProvider, 100, "Мастер-таблица");
        ColumnConfig<FieldMetadata, String> classIdColumn = new ColumnConfig<FieldMetadata, String>(props.classId(),
                100, EditorMessage.Util.MESSAGE.property_report_master_table());

        // TODO: Доделать
        // classIdColumn.setCell(new AbstractCell<ListModelData>() {
        //
        // @Override
        // public void render(Context context, ListModelData value,
        // SafeHtmlBuilder sb) {
        // ComboBoxBuilder masterComboBuilder = new ComboBoxBuilder();
        // masterComboBuilder.setProperty("DataSource", "3756");
        // ComboBox<ListModelData> combo = (ComboBox<ListModelData>)
        // masterComboBuilder.create();
        // combo.fireEvent(new TriggerClickEvent());
        // combo.setValue(value);
        // sb.appendHtmlConstant(combo.asWidget().getElement().getString());
        // }
        // });

        ColumnConfig<FieldMetadata, DataType> typeColumn = new ColumnConfig<FieldMetadata, DataType>(props.type(), 100,
                EditorMessage.Util.MESSAGE.property_report_field_type());

        ColumnConfig<FieldMetadata, Boolean> requiredColumn = new ColumnConfig<FieldMetadata, Boolean>(props.required(),
                100, EditorMessage.Util.MESSAGE.property_report_required());
        requiredColumn.setCell(new CheckBoxCell());

        ColumnConfig<FieldMetadata, String> nameColumn = new ColumnConfig<FieldMetadata, String>(labelProvider, 100,
                EditorMessage.Util.MESSAGE.property_report_field_name());

        ColumnConfig<FieldMetadata, String> eNameColumn = new ColumnConfig<FieldMetadata, String>(props.name(), 100,
                EditorMessage.Util.MESSAGE.property_report_field_code());

        ColumnConfig<FieldMetadata, ListViewType> viewTypeColumn = new ColumnConfig<FieldMetadata, ListViewType>(
                props.listViewType(), 100, EditorMessage.Util.MESSAGE.property_report_list_view_type());

        List<ColumnConfig<FieldMetadata, ?>> columns = new ArrayList<ColumnConfig<FieldMetadata, ?>>();
        columns.add(nameColumn);
        columns.add(eNameColumn);
        columns.add(typeColumn);
        columns.add(classIdColumn);
        columns.add(requiredColumn);
        columns.add(viewTypeColumn);

        ColumnModel<FieldMetadata> columnModel = new ColumnModel<FieldMetadata>(columns);

        grid = new Grid<FieldMetadata>(store, columnModel);

        GridEditing<FieldMetadata> editing = new GridInlineEditing<FieldMetadata>(grid);
        editing.addEditor(nameColumn, new TextField());
        editing.addEditor(eNameColumn, new TextField());
        // editing.addEditor(typeColumn, new NumberField<Integer>(
        // new NumberPropertyEditor.IntegerPropertyEditor(null)));
        // editing.addEditor(classIdColumn, new NumberField<Integer>(
        // new NumberPropertyEditor.IntegerPropertyEditor(null)));
        editing.addEditor(classIdColumn, new TextField());
        SimpleComboBox<DataType> typeCombo = new SimpleComboBox<DataType>(new StringLabelProvider());
        typeCombo.setTriggerAction(TriggerAction.ALL);
        typeCombo.setEditable(false);
        typeCombo.add(Arrays.asList(DataType.values()));
        editing.addEditor(typeColumn, typeCombo);
        typeCombo.setForceSelection(true);
        typeCombo.setValue(DataType.STRING, true, true);

        editing.addEditor(requiredColumn, new CheckBox());

        SimpleComboBox<ListViewType> viewTypeCombo = new SimpleComboBox<ListViewType>(new StringLabelProvider());
        viewTypeCombo.setTriggerAction(TriggerAction.ALL);
        viewTypeCombo.setEditable(false);
        viewTypeCombo.add(Arrays.asList(ListViewType.values()));
        editing.addEditor(viewTypeColumn, viewTypeCombo);
        // editing.addCompleteEditHandler(new
        // CompleteEditHandler<FieldMetadata>() {
        //
        // @Override
        // public void onCompleteEdit(CompleteEditEvent<FieldMetadata> event) {
        // store.update(event.getSource().completeEditing(););
        // }
        // });
    }

    @Override
    public void setParams(List<FieldMetadata> fields) {
        store.clear();
        if (fields != null) {
            store.addAll(fields);
        }
    }

    public void initButtons() {
        SelectHandler addHandler = new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                int count = (store.size() + 1);
                FieldMetadata field = new FieldMetadata();
                field.setId(RandomUUID.uuid());
                field.setLabel(EditorMessage.Util.MESSAGE.property_report_parameter() + count);
                field.setName("param" + count);
                store.add(field);
                presenter.addParam(field);
            }
        };

        SelectHandler deleteHandler = new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                List<FieldMetadata> fields = grid.getSelectionModel().getSelectedItems();
                for (FieldMetadata field : fields) {
                    store.remove(field);
                    presenter.removeParam(field);
                }
            }
        };

        addButton(new TextButton(EditorMessage.Util.MESSAGE.property_report_append(), addHandler));
        addButton(new TextButton(AppMessage.Util.MESSAGE.delete(), deleteHandler));
        setButtonAlign(BoxLayoutPack.CENTER);
    }

    @Override
    public void setPresenter(PropertyReportPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PropertyReportPresenter getPresenter() {
        return presenter;
    }
}
