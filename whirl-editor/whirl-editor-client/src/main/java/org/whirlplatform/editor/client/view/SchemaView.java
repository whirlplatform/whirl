package org.whirlplatform.editor.client.view;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.whirlplatform.component.client.data.ClassKeyProvider;
import org.whirlplatform.component.client.data.RowModelDataValueProvider;
import org.whirlplatform.editor.client.presenter.SchemaPresenter.ISchemaView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.RowModelData;

public class SchemaView extends ContentPanel implements ISchemaView {

    private VerticalLayoutContainer container;

    private FieldLabel labelSchema;
    private TextField fieldSchema;

    private TextButton refreshButton;
    private TextButton importButton;
    private Label importLabel;
    private ListStore<RowModelData> importStore;
    private Grid<RowModelData> importTables;

    private AutoProgressMessageBox processingMessageBox;

    public SchemaView() {
        super();
        initUI();
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }

    private void initUI() {
        setHeaderVisible(true);
        container = new VerticalLayoutContainer();
        container.setAdjustForScroll(true);
        container.setScrollMode(ScrollMode.AUTO);
        setWidget(container);
        initFields();
        initImport();
        initProcessing();
    }

    private void initFields() {
        fieldSchema = new TextField();

        fieldSchema = new TextField();
        fieldSchema.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                refreshButton.setEnabled(false);
                importButton.setEnabled(false);
                importLabel.setText(EditorMessage.Util.MESSAGE.schema_name_changed_message());
            }
        });
        labelSchema = new FieldLabel(fieldSchema, EditorMessage.Util.MESSAGE.schema_schema());
        container.add(labelSchema, new VerticalLayoutData(1, -1, new Margins(
                10, 10, 0, 10)));
    }

    private void initImport() {
        refreshButton = new TextButton(EditorMessage.Util.MESSAGE.refresh());
        importButton = new TextButton(EditorMessage.Util.MESSAGE.schema_import());
        importLabel = new Label(EditorMessage.Util.MESSAGE.schema_check_import_objects());
        HorizontalLayoutContainer bar = new HorizontalLayoutContainer();
        bar.add(refreshButton, new HorizontalLayoutData(0.15, -1));
        bar.add(importButton, new HorizontalLayoutData(0.15, -1));
        bar.add(importLabel, new HorizontalLayoutData(0.7, -1,
                new Margins(0, 0, 0, 5)));
        container.add(bar, new VerticalLayoutData(1, 40, new Margins(10, 5, 0,
                5)));

        IdentityValueProvider<RowModelData> identityProvider =
                new IdentityValueProvider<RowModelData>();
        CheckBoxSelectionModel<RowModelData> selectionModel =
                new CheckBoxSelectionModel<RowModelData>(
                        identityProvider);
        selectionModel.setSelectionMode(SelectionMode.MULTI);

        List<ColumnConfig<RowModelData, ?>> columns =
                new ArrayList<ColumnConfig<RowModelData, ?>>();
        columns.add(selectionModel.getColumn());
        ColumnConfig<RowModelData, String> tableNameColumn = new ColumnConfig<RowModelData, String>(
                new RowModelDataValueProvider<String>("tableName"), 250,
                EditorMessage.Util.MESSAGE.schema_table());
        ColumnConfig<RowModelData, String> titleColumn = new ColumnConfig<RowModelData, String>(
                new RowModelDataValueProvider<String>("title"), 250,
                EditorMessage.Util.MESSAGE.schema_name());
        ColumnConfig<RowModelData, String> sourceColumn = new ColumnConfig<RowModelData, String>(
                new RowModelDataValueProvider<String>("source"), 250,
                EditorMessage.Util.MESSAGE.schema_source());
        ColumnConfig<RowModelData, String> typeColumn = new ColumnConfig<RowModelData, String>(
                new RowModelDataValueProvider<String>("objectType"), 250,
                EditorMessage.Util.MESSAGE.schema_object_type());
        columns.add(tableNameColumn);
        columns.add(titleColumn);
        columns.add(sourceColumn);
        columns.add(typeColumn);

        ColumnModel<RowModelData> model = new ColumnModel<RowModelData>(columns);
        importStore = new ListStore<RowModelData>(new ClassKeyProvider());
        importTables = new Grid<RowModelData>(importStore, model);
        importTables.setSelectionModel(selectionModel);
        importTables.setBorders(true);
        container.add(importTables, new VerticalLayoutData(1, 500, new Margins(
                0, 5, 0, 5)));
    }

    private void initProcessing() {
        processingMessageBox =
                new AutoProgressMessageBox(EditorMessage.Util.MESSAGE.schema_processing());
    }

    @Override
    public void setSchemaName(String schema) {
        fieldSchema.setValue(schema);
    }

    @Override
    public String getSchema() {
        return fieldSchema.getValue();
    }

    @Override
    public void setImports(Collection<RowModelData> imports) {
        importStore.clear();
        TreeSet<RowModelData> data = new TreeSet<>(new Comparator<RowModelData>() {
            @Override
            public int compare(RowModelData o1, RowModelData o2) {
                String o1name = o1.get("tableName");
                String o2name = o2.get("tableName");
                return o1name.compareToIgnoreCase(o2name);
            }
        });
        data.addAll(imports);
        importStore.addAll(data);
    }

    @Override
    public Collection<RowModelData> getCheckedImports() {
        return importTables.getSelectionModel().getSelectedItems();
    }

    @Override
    public void addRefreshImportHandler(SelectHandler handler) {
        refreshButton.addSelectHandler(handler);
    }

    @Override
    public void addImportHandler(SelectHandler handler) {
        importButton.addSelectHandler(handler);
    }

    @Override
    public void startProcessing() {
        processingMessageBox.auto();
        processingMessageBox.show();
    }

    @Override
    public void stopProcessing() {
        processingMessageBox.hide();
    }

    @Override
    public void clearValues() {
        fieldSchema.clear();
        importStore.clear();
        refreshButton.setEnabled(true);
        importButton.setEnabled(true);
        importLabel.setText(EditorMessage.Util.MESSAGE.schema_check_import_objects());
    }

}
