package org.whirlplatform.editor.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TriggerField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.util.ComponentComboBox;
import org.whirlplatform.editor.client.util.ComponentStore;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class ParameterEditorComponent extends TriggerField<String> {

    private HorizontalLayoutData comboLayoutData =
            new HorizontalLayoutData(150, -1, new Margins(0, 5, 0, 0));
    private HorizontalLayoutData halfLayoutData =
            new HorizontalLayoutData(0.5, -1, new Margins(0, 5, 0, 0));
    private HorizontalLayoutData fullLayoutData =
            new HorizontalLayoutData(1, -1, new Margins(0, 5, 0, 0));
    private HorizontalLayoutData buttonLayoutData =
            new HorizontalLayoutData(30, -1, new Margins(0, 10, 0, 0));
    private Window window;
    private VerticalLayoutContainer container;
    private Set<ParameterRow> parameters;
    private Map<String, ParameterType> valuesType = new HashMap<String, ParameterType>();
    private ComponentStore store;
    public ParameterEditorComponent(ComponentStore store) {

        super(new TriggerFieldCell(GWT.<ParameterFieldDefaultAppearance>create(
                ParameterFieldDefaultAppearance.class)));
        ClickHandler handler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showWindow();
            }
        };
        addHandler(handler, ClickEvent.getType());

        this.store = store;
        setEditable(false);
        setValue("[0]", false);

    }

    private void showWindow() {
        window = new Window();
        window.setSize("600", "500");
        window.setHeading(EditorMessage.Util.MESSAGE.event_parameter_title());
        window.setModal(true);
        window.setBlinkModal(true);
        window.setResizable(true);
        parameters = new HashSet<ParameterRow>();
        container = new VerticalLayoutContainer();

        SimpleContainer tmpContainer = new SimpleContainer();
        TextButton addButton = new TextButton();
        addButton.setTitle(EditorMessage.Util.MESSAGE.event_parameter_add());
        addButton.setIcon(EditorBundle.INSTANCE.plus());
        addButton.setWidth(20);
        addButton.setIconAlign(IconAlign.TOP);
        addButton.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                addRow(ParameterType.COMPONENTCODE, null);
            }
        });
        tmpContainer.add(addButton, new MarginData(0, 11, 0, 0));
        tmpContainer.getElement().setAttribute("align", "right");

        container.add(tmpContainer);

        container.setScrollMode(ScrollMode.AUTOY);
        window.add(container);

        for (Entry<String, ParameterType> entry : valuesType.entrySet()) {
            addRow(entry.getValue(), entry.getKey());

        }

        TextButton save = new TextButton(EditorMessage.Util.MESSAGE.apply());
        save.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                valuesType.clear();
                for (ParameterRow param : parameters) {
                    String val = param.getValue();
                    if (val != null && !val.isEmpty()) {
                        valuesType.put(val, param.getType());
                    }
                }
                ValueChangeEvent.fire(ParameterEditorComponent.this, getValue());
                window.hide();
                setValue("[" + valuesType.size() + "]", false);
            }
        });

        TextButton cancel = new TextButton(EditorMessage.Util.MESSAGE.close());
        cancel.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                window.hide();
            }
        });
        window.addButton(save);
        window.addButton(cancel);
        window.show();
    }

    private void addRow(ParameterType parameterType, String value) {
        ParameterRow p = new ParameterRow(store, parameterType, value);
        Margins margins = new Margins(0, 2, 5, 2);
        VerticalLayoutData vData = new VerticalLayoutData(1, 30, margins);
        container.insert(p, container.getWidgetCount() - 1, vData);
        container.forceLayout();
        parameters.add(p);
    }

    @Override
    public String getValue() {

        JSONArray ids = new JSONArray();
        JSONArray codes = new JSONArray();
        JSONArray storage = new JSONArray();
        JSONArray values = new JSONArray();

        for (Entry<String, ParameterType> entry : valuesType.entrySet()) {
            JSONString value = new JSONString(entry.getKey());
            if (entry.getValue() == ParameterType.COMPONENTCODE) {
                codes.set(codes.size(), value);
            } else if (entry.getValue() == ParameterType.COMPONENTID) {
                ids.set(ids.size(), value);
            } else if (entry.getValue() == ParameterType.STORAGECODE) {
                storage.set(storage.size(), value);
            } else if (entry.getValue() == ParameterType.STATICVALUE) {
                values.set(values.size(), JSONParser.parseLenient(entry.getKey()).isObject());
            }
        }

        JSONObject obj = new JSONObject();
        obj.put(ParameterType.COMPONENTID.getJsonName(), ids);
        obj.put(ParameterType.COMPONENTCODE.getJsonName(), codes);
        obj.put(ParameterType.STORAGECODE.getJsonName(), storage);
        obj.put(ParameterType.STATICVALUE.getJsonName(), values);

        return obj.toString();
    }

    @Override
    public void setValue(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        JSONValue json = JSONParser.parseLenient(value);
        JSONObject object = json.isObject();

        if (object.containsKey(ParameterType.COMPONENTID.getJsonName())) {
            JSONArray ids = json.isObject().get(ParameterType.COMPONENTID.getJsonName()).isArray();
            for (int i = 0; i < ids.size(); i++) {
                valuesType.put(ids.get(i).isString().stringValue(), ParameterType.COMPONENTID);
            }
        }

        if (object.containsKey(ParameterType.COMPONENTCODE.getJsonName())) {
            JSONArray codes = object.get(ParameterType.COMPONENTCODE.getJsonName()).isArray();
            for (int i = 0; i < codes.size(); i++) {
                valuesType.put(codes.get(i).isString().stringValue(), ParameterType.COMPONENTCODE);
            }
        }

        if (object.containsKey(ParameterType.STORAGECODE.getJsonName())) {
            JSONArray storage =
                    json.isObject().get(ParameterType.STORAGECODE.getJsonName()).isArray();
            for (int i = 0; i < storage.size(); i++) {
                valuesType.put(storage.get(i).isString().stringValue(), ParameterType.STORAGECODE);
            }
        }

        if (object.containsKey(ParameterType.STATICVALUE.getJsonName())) {
            JSONArray values =
                    json.isObject().get(ParameterType.STATICVALUE.getJsonName()).isArray();
            for (int i = 0; i < values.size(); i++) {
                valuesType.put(values.get(i).isObject().toString(), ParameterType.STATICVALUE);
            }
        }

        setValue("[" + valuesType.size() + "]", false);
    }

    @Override
    public void clear() {
        if (valuesType != null) {
            valuesType.clear();
        }
        setValue("[0]", false);
    }

    public enum ParameterType {
        COMPONENTCODE("codes"), COMPONENTID("ids"), STORAGECODE("storage"), STATICVALUE("values");

        private String jsonName;

        ParameterType(String name) {
            this.jsonName = name;
        }

        public String getJsonName() {
            return jsonName;
        }
    }

    public interface ParameterFieldAppearance extends TriggerFieldAppearance {
    }

    private class ParameterRow extends HorizontalLayoutContainer {
        LabelProvider<ParameterType> parameterTypeLabelProvider =
                new LabelProvider<ParameterType>() {

                    @Override
                    public String getLabel(ParameterType item) {
                        if (item == ParameterType.COMPONENTCODE) {
                            return EditorMessage.Util.MESSAGE.event_parameter_component_code();
                        } else if (item == ParameterType.COMPONENTID) {
                            return EditorMessage.Util.MESSAGE.event_parameter_component();
                        } else if (item == ParameterType.STORAGECODE) {
                            return EditorMessage.Util.MESSAGE.event_parameter_storage_code();
                        } else if (item == ParameterType.STATICVALUE) {
                            return EditorMessage.Util.MESSAGE.event_parameter_static();
                        }
                        return null;
                    }
                };
        TextButton removeButton;
        private SimpleComboBox<ParameterType> codeOrIdCombo;
        private TextField textField;
        private TextField codeField;
        private ComponentComboBox componentField;
        private ParameterType type;
        private ComponentStore store;

        public ParameterRow(ComponentStore store, ParameterType propertyType,
                            String value) {
            type = propertyType;
            this.store = store;
            componentField = new ComponentComboBox(store);
            textField = new TextField();
            codeField = new TextField();
            codeOrIdCombo = new SimpleComboBox<ParameterType>(
                    parameterTypeLabelProvider);
            codeOrIdCombo.setTriggerAction(TriggerAction.ALL);
            codeOrIdCombo.setForceSelection(true);
            codeOrIdCombo.setEditable(false);
            codeOrIdCombo.add(Arrays.asList(ParameterType.COMPONENTCODE,
                    ParameterType.COMPONENTID, ParameterType.STORAGECODE,
                    ParameterType.STATICVALUE));
            codeOrIdCombo
                    .addSelectionHandler(new SelectionHandler<ParameterType>() {

                        @Override
                        public void onSelection(
                                SelectionEvent<ParameterType> event) {
                            type = event.getSelectedItem();
                            rebuild(null);
                        }
                    });

            removeButton = new TextButton();
            removeButton.setTitle(EditorMessage.Util.MESSAGE.event_parameter_remove());
            removeButton.setIcon(EditorBundle.INSTANCE.minus());
            removeButton.setIconAlign(IconAlign.TOP);
            removeButton.setWidth(20);
            removeButton.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    // Берутся объекты из внешнего класса. Передавать
                    // параметрами?
                    container.remove(ParameterRow.this);
                    parameters.remove(ParameterRow.this);
                }
            });

            rebuild(value);
        }

        private void rebuild(String value) {
            clear();
            codeOrIdCombo.setValue(type);
            add(codeOrIdCombo, comboLayoutData);

            if (type == ParameterType.COMPONENTCODE
                    || type == ParameterType.STORAGECODE) {
                textField.setValue(value);
                add(textField, fullLayoutData);
            } else if (type == ParameterType.STATICVALUE) {
                if (value != null && !value.isEmpty()) {
                    JSONValue json = JSONParser.parseLenient(value);
                    JSONObject object = json.isObject();
                    String code = object.keySet().iterator().next();
                    codeField.setValue(code);
                    textField.setValue(object.get(code).isString().stringValue());
                }
                add(codeField, halfLayoutData);
                add(textField, halfLayoutData);
            } else {
                if (store.size() == 0) {
                    store.getLoader().load();
                }
                componentField.setValue(store.findModelWithKey(value));
                add(componentField, fullLayoutData);
            }
            add(removeButton, buttonLayoutData);
            forceLayout();
        }

        public String getValue() {
            if (type == ParameterType.COMPONENTCODE
                    || type == ParameterType.STORAGECODE) {
                return textField.getValue();
            } else if (type == ParameterType.STATICVALUE) {
                JSONObject val = new JSONObject();
                val.put(codeField.getValue(), new JSONString(textField.getValue()));
                return val.toString();
            } else if (type == ParameterType.COMPONENTID) {
                if (componentField.getValue() != null) {
                    return componentField.getValue().getId();
                } else {
                    return null;
                }
            }
            return null;
        }

        public ParameterType getType() {
            return type;
        }
    }
}
