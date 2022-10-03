package org.whirlplatform.component.client.combo;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ext.TreeComboBox;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.tree.CheckStyleHelper;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.*;

@JsType(name = "TreeComboBox", namespace = "Whirl")
public class TreeComboBoxBuilder extends MultiComboBoxBuilder<TreeComboBox> {

    protected TreeLoader<ListModelData> loader;
    /**
     * Колонка указывающая на родителя.
     */
    protected String parentColumn;
    protected String isLeafColumn;
    private String stateColumn;
    private TreeStore<ListModelData> store;

    @JsConstructor
    public TreeComboBoxBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public TreeComboBoxBuilder() {
        this(Collections.emptyMap());
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {

        required = false;
        singleSelection = false;

        minChars = 2;
        delayTimeMs = 1200;
        saveState = false;
        restoreState = false;

        initParamHelper();
        initLabelProvider();

        checkedModels = new CheckedModels();
        store = new TreeStore<ListModelData>(new ListKeyProvider());
        comboBox = initCombo(initLoader(store));
        initCountElement();
        return comboBox;
    }

    @Override
    public Component create() {
        Component c = super.create();
        return c;
    }

    private boolean isQuery() {
        return !Util.isEmptyString(comboBox.getText()) && comboBox.getText().length() >= minChars;
    }

    protected TreeLoader<ListModelData> initLoader(final TreeStore<ListModelData> store) {
        RpcProxy<ListModelData, List<ListModelData>> proxy = createProxy();

        TreeLoader<ListModelData> ldr = new TreeLoader<ListModelData>(proxy) {
            @Override
            public boolean hasChildren(ListModelData parent) {
                return parent.<Boolean>get(isLeafColumn);
            }

            @Override
            protected void onLoadSuccess(ListModelData loadConfig, List<ListModelData> result) {
                super.onLoadSuccess(loadConfig, result);
                comboBox.onStoreLoad();
                restoreState(result);
            }
        };
        ldr.addLoadHandler(new ChildTreeStoreBinding<ListModelData>(store) {
            @Override
            public void onLoad(LoadEvent<ListModelData, List<ListModelData>> event) {
                if (event.getLoadConfig() != null || !isQuery()) {
                    super.onLoad(event);
                } else {
                    store.clear();
                    if (!event.getLoadResult().isEmpty()) {
                        loadQueryResult(store, event.getLoadResult());
                    }
                }
            }
        });
        return ldr;
    }

    private void loadQueryResult(TreeStore<ListModelData> store, List<ListModelData> models) {
        boolean hasChanged = true;
        Map<String, ListModelData> added = new HashMap<String, ListModelData>();
        while (hasChanged) {
            hasChanged = false;
            for (ListModelData m : models) {

                String parentId = m.get(parentColumn);
                if (added.containsKey(m.getId())) {
                    continue;
                }
                if (parentId == null || parentId.isEmpty()) {
                    store.add(m);
                    added.put(m.getId(), m);
                    hasChanged = true;
                } else if (added.containsKey(parentId)) {
                    store.add(added.get(parentId), m);
                    added.put(m.getId(), m);
                    hasChanged = true;
                }
            }
        }
        for (ListModelData md : store.getAll()) {
            if (store.hasChildren(md)) {
                comboBox.getTree().findNode(md).setLoaded(true);
                comboBox.getTree().setExpanded(md, true);
            }
        }

    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.IsLeafColumn.getCode())) {
            isLeafColumn = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ParentColumn.getCode())) {
            parentColumn = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode())) {
            whereSql = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.CheckStyle.getCode())) {
            comboBox.setCheckStyle(CheckStyleHelper.parseTreePanelCheckStyle(value.getString()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SingleSelection.getCode())) {
            singleSelection = value.getBoolean();
            if (singleSelection) {
                comboBox.setCheckStyle(CheckCascade.NONE);
                comboBox.setSingleSelectionCheckMode();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StateColumn.getCode())) {
            stateColumn = value.getString();
            return true;
        }
        return super.setProperty(name, value);
    }

    @Override
    protected void parseValue(String value, boolean labels) {
        super.parseValue(value, labels);
        if (checkedModels.isReady()) {
            for (ListModelData m : checkedModels.models) {
                comboBox.setChecked(m, CheckState.CHECKED, false);
            }
        }
    }

    @Override
    protected void addListener() {
        comboBox.addValueChangeHandler(new ValueChangeHandler<ListModelData>() {
            @Override
            public void onValueChange(ValueChangeEvent<ListModelData> event) {
                saveState();
            }
        });

        comboBox.addBeforeQueryHandler(new BeforeQueryHandler<ListModelData>() {
            @Override
            public void onBeforeQuery(BeforeQueryEvent<ListModelData> event) {
                if (isQuery()) {
                    comboBox.getTree().mask();
                    comboBox.getLoader().load();
                }
                event.setCancelled(true);
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected RpcProxy<ListModelData, List<ListModelData>> createProxy() {
        RpcProxy proxy = new RpcProxy<ListModelData, List<ListModelData>>() {

            @Override
            public void load(ListModelData loadConfig, final AsyncCallback<List<ListModelData>> callback) {
                AsyncCallback<LoadData<ListModelData>> proxyCallback = new AsyncCallback<LoadData<ListModelData>>() {
                    @Override
                    public void onSuccess(LoadData<ListModelData> result) {
                        callback.onSuccess(result.getData());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }
                };
                DataServiceAsync.Util.getDataService(proxyCallback)
                        .getListClassData(SessionToken.get(), getClassMetadata(), getLoadConfig(loadConfig));
            }
        };
        return proxy;
    }

    protected ClassMetadata getClassMetadata() {
        ClassMetadata metadata = new ClassMetadata(classId);
        //		metadata.addField(new FieldMetadata(nameField, DataType.STRING, null));
        return metadata;
    }

    protected ClassLoadConfig getLoadConfig(RowModelData parent) {
        TreeClassLoadConfig config = new TreeClassLoadConfig();
        Map<String, DataValue> params =
                paramHelper == null ? new HashMap<String, DataValue>() : paramHelper.getValues();

        if (useSearchParameters) {
            DataValue v = new DataValueImpl(DataType.STRING, comboBox.getText());
            params.put(SEARCH_QUERY, v);
        } else if (parent == null) {
            // Если загрузка внутренних элементов, то не учитываем поиск
            config.setQuery(comboBox.getText());
        }

        config.setParameters(params);
        config.setIsLeafColumn(isLeafColumn);
        config.setStateColumn(stateColumn);
        config.setParentColumn(parentColumn);
        config.setParent(parent);
        config.setWhereSql(whereSql);
        config.setAll(true);
        config.setUseSearchParameters(useSearchParameters);

        return config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private TreeComboBox initCombo(TreeLoader<ListModelData> loader) {
        ModelKeyProvider<ListModelData> keyProvider = new ModelKeyProvider<ListModelData>() {

            @Override
            public String getKey(ListModelData item) {
                return String.valueOf(item.hashCode());
            }
        };

        ComboBoxCell cell = new ComboBoxCell(new ListStore<ListModelData>(keyProvider), new StringLabelProvider()) {
            @Override
            protected void onSelect(Object item) {
            }
        };
        Cell<String> treeCell = new SimpleSafeHtmlCell(new SafeHtmlRenderer<String>() {

            private void renderContains(SafeHtmlBuilder builder, String object) {
                String q = comboBox.getText();
                if (isQuery() && !(!Util.isEmptyString(object) && object.toLowerCase().contains(q.toLowerCase()))) {
                    builder.append(
                            SafeHtmlUtils.fromTrustedString("<span style=\"color: darkgray;\">" + object + "</span>"));
                } else {
                    builder.append(SafeHtmlUtils.fromTrustedString(object));
                }
            }

            @Override
            public SafeHtml render(String object) {
                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                renderContains(builder, object);
                return builder.toSafeHtml();
            }

            @Override
            public void render(String object, SafeHtmlBuilder builder) {
                renderContains(builder, object);
            }
        });
        comboBox = new TreeComboBox(cell, treeCell, store, loader);
        comboBox.setQueryDelay(700);
        listView = comboBox.getListView();
        return comboBox;
    }

    @JsIgnore
    @Override
    public RowListValue getFieldValue() {
        RowListValue list = new RowListValueImpl();
        for (ListModelData model : comboBox.getSelection()) {
            RowValue row = new RowValueImpl(model.getId());
            row.setChecked(true);
            list.addRowValue(row);
        }
        return list;
    }

    @JsIgnore
    @Override
    public void setFieldValue(RowListValue value) {
        List<ListModelData> selection = new ArrayList<ListModelData>();
        if (value != null) {
            for (RowValue row : value.getRowList()) {
                ListModelData model = new ListModelDataImpl();
                model.setId(row.getId());
                selection.add(model);
            }
        }
        comboBox.setSelection(selection);
    }

    @Override
    public boolean isValid(boolean invalidate) {
        if (isRequired() && comboBox.getSelection().size() == 0) {
            if (invalidate) {
                comboBox.markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        }
        if (invalidate) {
            comboBox.clearInvalid();
        }
        return true;
    }

    @Override
    public void clear() {
        comboBox.clear();
        // ValueChangeEvent.fire(getRealComponent(), null);
    }

    private void restoreState(List<ListModelData> models) {
        if (stateColumn == null) {
            return;
        }
        Tree<ListModelData, String> tree = comboBox.getTree();
        for (ListModelData m : models) {
            if (m.<Boolean>get(stateColumn) && tree.findNode(m) != null && !tree.isLeaf(m)) {
                tree.setExpanded(m, true);
            }
        }
    }

    /**
     * Checks if component is in hidden state.
     *
     * @return true if component is hidden
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Sets component's hidden state.
     *
     * @param hidden true - to hide component, false - to show component
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Focuses component.
     */
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Checks if component is enabled.
     *
     * @return true if component is enabled
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Sets component's enabled state.
     *
     * @param enabled true - to enable component, false - to disable component
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEditable() {
        return super.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
    }

    @Override
    public boolean isSaveState() {
        return super.isSaveState();
    }

    @Override
    public void setSaveState(boolean save) {
        super.setSaveState(save);
    }

    public void setRestoreState(boolean restore) {
        super.setRestoreState(restore);
    }

    @Override
    public StateScope getStateScope() {
        return super.getStateScope();
    }

    @Override
    public void setStateScope(StateScope scope) {
        super.setStateScope(scope);
    }

    @Override
    public void saveState() {
        super.saveState();
    }

    /**
     * Gets the field mask.
     *
     * @return the field mask
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Sets the field mask.
     *
     * @param mask the new field mask
     */
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Sets the required to fill.
     *
     * @param required true, if the field is required to be filled
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    @JsIgnore
    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public void setValue(ListModelData value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListModelData getValue() {
        throw new UnsupportedOperationException();
    }

    public String getText() {
        return comboBox.getText();
    }


    private class ListKeyProvider implements ModelKeyProvider<ListModelData> {

        @Override
        public String getKey(ListModelData item) {
            return item.getId();
        }
    }

}
