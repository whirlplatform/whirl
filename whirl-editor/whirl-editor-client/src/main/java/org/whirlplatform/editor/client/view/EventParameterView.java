package org.whirlplatform.editor.client.view;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import java.util.Arrays;
import java.util.Date;
import org.whirlplatform.editor.client.presenter.EventParameterPresenter;
import org.whirlplatform.editor.client.presenter.EventParameterPresenter.IEventParameterView;
import org.whirlplatform.editor.client.util.ComponentComboBox;
import org.whirlplatform.editor.client.util.ComponentStore;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ParameterType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class EventParameterView extends ContentPanel implements
        IEventParameterView {

    private EventParameterPresenter presenter;
    private VerticalLayoutContainer container;

    private ComboBox<ParameterType> type;
    private FieldLabel typeLabel;
    private TextField code;
    private FieldLabel codeLabel;
    private ComboBox<ComponentElement> component;
    private FieldLabel componentLabel;
    private TextField componentCode;
    private FieldLabel componentCodeLabel;
    private TextField storageCode;
    private FieldLabel storageCodeLabel;
    private ComboBox<DataType> dataType;
    private FieldLabel dataTypeLabel;
    private TextField string;
    private FieldLabel stringLabel;
    private NumberField<Double> number;
    private FieldLabel numberLabel;
    private DateField date;
    private FieldLabel dateLabel;
    private CheckBox bool;
    private FieldLabel boolLabel;
    private TextField listId;
    private FieldLabel listIdLabel;
    private TextField listLabel;
    private FieldLabel listLabelLabel;

    public EventParameterView() {
        super();
    }

    @Override
    public void initUI() {
        container = new VerticalLayoutContainer();
        add(container);

        type = initTypeField();
        type.addSelectionHandler(new SelectionHandler<ParameterType>() {

            @Override
            public void onSelection(SelectionEvent<ParameterType> event) {
                changeTypeVizibility(event.getSelectedItem());
            }
        });
        typeLabel = new FieldLabel(type,
                EditorMessage.Util.MESSAGE.event_parameter_type());
        container.add(typeLabel, new VerticalLayoutData(1, -1, new Margins(10,
                10, 0, 10)));

        code = new TextField();
        codeLabel = new FieldLabel(code,
                EditorMessage.Util.MESSAGE.event_parameter_code());
        container.add(codeLabel, new VerticalLayoutData(1, -1, new Margins(10,
                10, 0, 10)));

        component = new ComponentComboBox(new ComponentStore(getPresenter()
                .getEventBus(), false));
        componentLabel = new FieldLabel(component,
                EditorMessage.Util.MESSAGE.event_parameter_component());
        container.add(componentLabel, new VerticalLayoutData(1, -1,
                new Margins(10, 10, 0, 10)));

        componentCode = new TextField();
        componentCodeLabel = new FieldLabel(componentCode,
                EditorMessage.Util.MESSAGE.event_parameter_component_code());
        container.add(componentCodeLabel, new VerticalLayoutData(1, -1,
                new Margins(10, 10, 0, 10)));

        storageCode = new TextField();
        storageCodeLabel = new FieldLabel(storageCode,
                EditorMessage.Util.MESSAGE.event_parameter_storage_code());
        container.add(storageCodeLabel, new VerticalLayoutData(1, -1,
                new Margins(10, 10, 0, 10)));

        dataType = initDataTypeField();
        dataType.addSelectionHandler(new SelectionHandler<DataType>() {
            @Override
            public void onSelection(SelectionEvent<DataType> event) {
                changeDataTypeVizibility(event.getSelectedItem());
            }
        });
        dataTypeLabel = new FieldLabel(dataType,
                EditorMessage.Util.MESSAGE.event_parameter_data_type());
        container.add(dataTypeLabel, new VerticalLayoutData(1, -1, new Margins(
                10, 10, 0, 10)));

        string = new TextField();
        stringLabel = new FieldLabel(string,
                EditorMessage.Util.MESSAGE.event_parameter_value());
        container.add(stringLabel, new VerticalLayoutData(1, -1, new Margins(
                10, 10, 0, 10)));

        number = new NumberField<Double>(new DoublePropertyEditor());
        number.setHeight(300);
        numberLabel = new FieldLabel(number,
                EditorMessage.Util.MESSAGE.event_parameter_value());
        container.add(numberLabel, new VerticalLayoutData(1, -1, new Margins(
                10, 10, 0, 10)));

        date = new DateField();
        dateLabel = new FieldLabel(date,
                EditorMessage.Util.MESSAGE.event_parameter_value());
        container.add(dateLabel, new VerticalLayoutData(1, -1, new Margins(10,
                10, 0, 10)));

        bool = new CheckBox();
        boolLabel = new FieldLabel(bool,
                EditorMessage.Util.MESSAGE.event_parameter_value());
        container.add(boolLabel, new VerticalLayoutData(1, -1, new Margins(10,
                10, 0, 10)));

        listId = new TextField();
        listIdLabel = new FieldLabel(listId,
                EditorMessage.Util.MESSAGE.event_parameter_list_id());
        container.add(listIdLabel, new VerticalLayoutData(1, -1, new Margins(
                10, 10, 0, 10)));

        listLabel = new TextField();
        listLabelLabel = new FieldLabel(listLabel,
                EditorMessage.Util.MESSAGE.event_parameter_list_value());
        container.add(listLabelLabel, new VerticalLayoutData(1, -1,
                new Margins(10, 10, 0, 10)));
    }

    // TODO просмотреть дублирующиеся
    private ComboBox<DataType> initDataTypeField() {
        ListStore<DataType> store = new ListStore<DataType>(
                new ModelKeyProvider<DataType>() {

                    @Override
                    public String getKey(DataType item) {
                        return item.name();
                    }
                });
        store.add(DataType.STRING);
        store.add(DataType.NUMBER);
        store.add(DataType.DATE);
        store.add(DataType.BOOLEAN);
        store.add(DataType.LIST);
        ComboBox<DataType> result = new ComboBox<DataType>(store,
                new LabelProvider<DataType>() {

                    @Override
                    public String getLabel(DataType item) {
                        return item.name();
                    }
                });

        result.setTriggerAction(TriggerAction.ALL);
        result.setEditable(false);
        result.setValue(DataType.STRING);
        return result;
    }

    private ComboBox<ParameterType> initTypeField() {
        ListStore<ParameterType> store = new ListStore<ParameterType>(
                new ModelKeyProvider<ParameterType>() {

                    @Override
                    public String getKey(ParameterType item) {
                        return item.name();
                    }
                });
        store.addAll(Arrays.asList(ParameterType.values()));
        ComboBox<ParameterType> result = new ComboBox<ParameterType>(store,
                new LabelProvider<ParameterType>() {

                    @Override
                    public String getLabel(ParameterType item) {
                        return item.name();
                    }
                });
        result.setAllowBlank(false);
        result.setEditable(false);
        result.setTriggerAction(TriggerAction.ALL);
        return result;
    }

    private void setAllInvisible() {
        componentLabel.setVisible(false);
        componentCodeLabel.setVisible(false);
        storageCodeLabel.setVisible(false);
        dataTypeLabel.setVisible(false);

        setAllDataTypeInvisible();
    }

    private void setAllDataTypeInvisible() {
        stringLabel.setVisible(false);
        numberLabel.setVisible(false);
        dateLabel.setVisible(false);
        boolLabel.setVisible(false);
        listIdLabel.setVisible(false);
        listLabelLabel.setVisible(false);
    }

    private void changeTypeVizibility(ParameterType type) {
        setAllInvisible();
        switch (type) {
            case COMPONENT:
                componentLabel.setVisible(true);
                break;
            case COMPONENTCODE:
                componentCodeLabel.setVisible(true);
                break;
            case STORAGE:
                storageCodeLabel.setVisible(true);
                break;
            case DATAVALUE:
                dataTypeLabel.setVisible(true);
                if (dataType.getValue() == null) {
                    dataType.setValue(DataType.STRING);
                }
                changeDataTypeVizibility(dataType.getValue());
                break;
        }
        forceLayout();
    }

    private void changeDataTypeVizibility(DataType type) {
        setAllDataTypeInvisible();
        switch (type) {
            case STRING:
                stringLabel.setVisible(true);
                break;
            case NUMBER:
                numberLabel.setVisible(true);
                break;
            case DATE:
                dateLabel.setVisible(true);
                break;
            case BOOLEAN:
                boolLabel.setVisible(true);
                break;
            case LIST:
                listIdLabel.setVisible(true);
                listLabelLabel.setVisible(true);
                break;
            case FILE:
                break;
            default:
                break;
        }
        forceLayout();
    }

    @Override
    public ParameterType getType() {
        return type.getValue();
    }

    @Override
    public void setType(ParameterType type) {
        this.type.setValue(type);
        changeTypeVizibility(type);
    }

    @Override
    public String getCode() {
        return code.getValue();
    }

    @Override
    public void setCode(String code) {
        this.code.setValue(code);
    }

    @Override
    public ComponentElement getComponent() {
        return component.getValue();
    }

    @Override
    public void setComponent(ComponentElement element) {
        component.setValue(element);
    }

    @Override
    public void setComponentCode(String componentCode) {
        this.componentCode.setValue(componentCode);
    }

    @Override
    public String getCompnentCode() {
        return componentCode.getValue();
    }

    @Override
    public String getStorageCode() {
        return storageCode.getValue();
    }

    @Override
    public void setStorageCode(String storageName) {
        storageCode.setValue(storageName);
    }

    @Override
    public DataType getDataType() {
        return dataType.getValue();
    }

    @Override
    public void setDataType(DataType dataType) {
        this.dataType.setValue(dataType);
        changeDataTypeVizibility(dataType);
    }

    @Override
    public String getString() {
        return string.getValue();
    }

    @Override
    public void setString(String value) {
        string.setValue(value);
    }

    @Override
    public Double getNumber() {
        return number.getValue();
    }

    @Override
    public void setNumber(Double value) {
        number.setValue(value);
    }

    @Override
    public Date getDate() {
        return date.getValue();
    }

    @Override
    public void setDate(Date value) {
        date.setValue(value);
    }

    @Override
    public Boolean getBoolean() {
        return bool.getValue();
    }

    @Override
    public void setBoolean(Boolean value) {
        bool.setValue(value);
    }

    @Override
    public void setList(String id, String label) {
        listId.setValue(id);
        listLabel.setValue(label);
    }

    @Override
    public String getListId() {
        return listId.getValue();
    }

    @Override
    public String getListLabel() {
        return listLabel.getValue();
    }

    @Override
    public EventParameterPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(EventParameterPresenter presenter) {
        this.presenter = presenter;
        // initUI();
    }

    @Override
    public void clearFields() {
        type.clear();
        code.clear();
        component.clear();
        componentCode.clear();
        storageCode.clear();
        dataType.clear();
        string.clear();
        number.clear();
        date.clear();
        bool.clear();
        listId.clear();
        listLabel.clear();
    }

    @Override
    public void setSelectedDataType(DataType type) {
        dataType.setValue(type);
        changeDataTypeVizibility(type);
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }
}
