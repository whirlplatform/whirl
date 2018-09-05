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
import org.whirlplatform.component.client.ext.TreeComboBox;
import org.whirlplatform.component.client.tree.CheckStyleHelper;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeComboBoxBuilder extends MultiComboBoxBuilder<TreeComboBox> {

    private class ListKeyProvider implements ModelKeyProvider<ListModelData> {

        @Override
        public String getKey(ListModelData item) {
            return item.getId();
        }
    }

    protected TreeLoader<ListModelData> loader;

    /**
     * Колонка наименования.
     */
//	protected String nameField;

    /**
     * Колонка указывающая на родителя.
     */
    protected String parentField;

    protected String leafExpression;
    protected String leafExprColumn;

    private String stateExpression;
    private String stateExpressionColumn;

    private TreeStore<ListModelData> store;

    public TreeComboBoxBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public TreeComboBoxBuilder() {
        super();
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {

        required = false;

        leafExprColumn = "PROPERTY_HAS_CHILDREN";
        stateExpressionColumn = "STATE_COLUMN";
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

        TreeLoader<ListModelData> loader = new TreeLoader<ListModelData>(proxy) {
            @Override
            public boolean hasChildren(ListModelData parent) {
                return parent.<Boolean>get(leafExprColumn);
            }

            @Override
            protected void onLoadSuccess(ListModelData loadConfig, List<ListModelData> result) {
                super.onLoadSuccess(loadConfig, result);
                comboBox.onStoreLoad();
                restoreState(result);
            }
        };
        loader.addLoadHandler(new ChildTreeStoreBinding<ListModelData>(store) {
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
        return loader;
    }

    private void loadQueryResult(TreeStore<ListModelData> store, List<ListModelData> models) {
        boolean hasChanged = true;
        Map<String, ListModelData> added = new HashMap<String, ListModelData>();
        while (hasChanged) {
            hasChanged = false;
            for (ListModelData m : models) {

                String parentId = m.get(parentField);
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

    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.LeafExpression.getCode())) {
            leafExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ParentColumn.getCode())) {
            parentField = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode())) {
            whereSql = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.CheckStyle.getCode())) {
            comboBox
                    .setCheckStyle(CheckStyleHelper.parseTreePanelCheckStyle(value.getString()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SingleSelection.getCode())) {
            singleSelection = value.getBoolean();
            if (singleSelection) {
                comboBox.setCheckStyle(CheckCascade.NONE);
                comboBox.setSingleSelectionCheckMode();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StateExpression.getCode())) {
            stateExpression = value.getString();
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
            public void load(ListModelData loadConfig,
                             final AsyncCallback<List<ListModelData>> callback) {
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
                DataServiceAsync.Util.getDataService(proxyCallback).getListClassData(SessionToken.get(), getClassMetadata(),
                        getLoadConfig(loadConfig));
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
        Map<String, DataValue> params = paramHelper == null ? new HashMap<String, DataValue>()
                : paramHelper.getValues();

        if (useSearchParameters) {
            DataValue v = new DataValueImpl(DataType.STRING, comboBox.getText());
            params.put(SEARCH_QUERY, v);
        } else if (parent == null) {
            // Если загрузка внутренних элементов, то не учитываем поиск
            config.setQuery(comboBox.getText());
        }

        config.setParameters(params);
        config.setLeafExpression(leafExpression);
        config.setStateExpression(stateExpression);
        config.setParentField(parentField);
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
    public RowListValue getFieldValue() {
        RowListValue list = new RowListValueImpl();
        for (ListModelData model : comboBox.getSelection()) {
            RowValue row = new RowValueImpl(model.getId());
            row.setChecked(true);
            list.addRowValue(row);
        }
        return list;
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
        if (stateExpression == null) {
            return;
        }
        Tree<ListModelData, String> tree = comboBox.getTree();
        for (ListModelData m : models) {
            if (m.<Boolean>get(stateExpressionColumn) && tree.findNode(m) != null && !tree.isLeaf(m)) {
                tree.setExpanded(m, true);
            }
        }
    }

}
