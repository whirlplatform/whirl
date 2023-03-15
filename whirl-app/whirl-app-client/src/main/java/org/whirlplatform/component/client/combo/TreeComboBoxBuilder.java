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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

/**
 * Древовидный список
 */
@JsType(name = "TreeComboBox", namespace = "Whirl")
public class TreeComboBoxBuilder extends MultiComboBoxBuilder<TreeComboBox> {

    protected TreeLoader<ListModelData> loader;
    /**
     * Колонка указывающая на родителя.
     */
    protected String parentExpression;
    protected String isLeafExpression;
    private String expandExpression;
    private TreeStore<TreeModelData> store;

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
        store = new TreeStore<TreeModelData>(new ListKeyProvider());
        comboBox = initCombo(initLoader(store));
        initCountElement();
        return comboBox;
    }

    @JsIgnore
    @Override
    public Component create() {
        Component c = super.create();
        return c;
    }

    private boolean isQuery() {
        return !Util.isEmptyString(comboBox.getText()) && comboBox.getText().length() >= minChars;
    }

    protected TreeLoader<TreeModelData> initLoader(final TreeStore<TreeModelData> store) {
        RpcProxy<TreeModelData, List<TreeModelData>> proxy = createProxy();

        TreeLoader<TreeModelData> ldr = new TreeLoader<TreeModelData>(proxy) {
            @Override
            public boolean hasChildren(TreeModelData parent) {
                return parent.<Boolean>get(isLeafExpression);
            }

            @Override
            protected void onLoadSuccess(TreeModelData loadConfig, List<TreeModelData> result) {
                super.onLoadSuccess(loadConfig, result);
                comboBox.onStoreLoad();
                restoreState(result);
            }
        };
        ldr.addLoadHandler(new ChildTreeStoreBinding<TreeModelData>(store) {
            @Override
            public void onLoad(LoadEvent<TreeModelData, List<TreeModelData>> event) {
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

    private void loadQueryResult(TreeStore<TreeModelData> store, List<TreeModelData> models) {
        boolean hasChanged = true;
        Map<String, TreeModelData> added = new HashMap<String, TreeModelData>();
        while (hasChanged) {
            hasChanged = false;
            for (TreeModelData m : models) {

                String parentId = m.get(parentExpression);
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
        for (TreeModelData md : store.getAll()) {
            if (store.hasChildren(md)) {
                comboBox.getTree().findNode(md).setLoaded(true);
                comboBox.getTree().setExpanded(md, true);
            }
        }

    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.IsLeafExpression.getCode())) {
            isLeafExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ParentExpression.getCode())) {
            parentExpression = value.getString();
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
        } else if (name.equalsIgnoreCase(PropertyType.ExpandExpression.getCode())) {
            expandExpression = value.getString();
            return true;
        }
        return super.setProperty(name, value);
    }

    @Override
    protected void parseValue(String value, boolean labels) {
        super.parseValue(value, labels);
        if (checkedModels.isReady()) {
            for (ListModelData m : checkedModels.models) {
                comboBox.setChecked((TreeModelData) m, CheckState.CHECKED, false);
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
    protected RpcProxy<TreeModelData, List<TreeModelData>> createProxy() {
        RpcProxy proxy = new RpcProxy<ListModelData, List<ListModelData>>() {

            @Override
            public void load(ListModelData loadConfig,
                             final AsyncCallback<List<ListModelData>> callback) {
                AsyncCallback<LoadData<ListModelData>> proxyCallback =
                    new AsyncCallback<LoadData<ListModelData>>() {
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
                    .getListClassData(SessionToken.get(), getClassMetadata(),
                        getLoadConfig((TreeModelData) loadConfig));
            }
        };
        return proxy;
    }

    protected ClassMetadata getClassMetadata() {
        ClassMetadata metadata = new ClassMetadata(classId);
        //        metadata.addField(new FieldMetadata(nameField, DataType.STRING, null));
        return metadata;
    }

    protected ClassLoadConfig getLoadConfig(TreeModelData parent) {
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
        config.setIsLeafExpression(isLeafExpression);
        config.setExpandExpression(expandExpression);
        config.setParentExpression(parentExpression);
        config.setParent(parent);
        config.setWhereSql(whereSql);
        config.setAll(true);
        config.setUseSearchParameters(useSearchParameters);

        return config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private TreeComboBox initCombo(TreeLoader<TreeModelData> loader) {
        ModelKeyProvider<ListModelData> keyProvider = new ModelKeyProvider<ListModelData>() {

            @Override
            public String getKey(ListModelData item) {
                return String.valueOf(item.hashCode());
            }
        };

        ComboBoxCell cell = new ComboBoxCell(new ListStore<ListModelData>(keyProvider),
            new StringLabelProvider()) {
            @Override
            protected void onSelect(Object item) {
            }
        };
        Cell<String> treeCell = new SimpleSafeHtmlCell(new SafeHtmlRenderer<String>() {

            private void renderContains(SafeHtmlBuilder builder, String object) {
                String q = comboBox.getText();
                if (isQuery() && !(!Util.isEmptyString(object)
                    && object.toLowerCase().contains(q.toLowerCase()))) {
                    builder.append(
                        SafeHtmlUtils.fromTrustedString(
                            "<span style=\"color: darkgray;\">" + object + "</span>"));
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
        List<TreeModelData> selection = new ArrayList<TreeModelData>();
        if (value != null) {
            for (RowValue row : value.getRowList()) {
                TreeModelData model = new TreeModelDataImpl();
                model.setId(row.getId());
                selection.add(model);
            }
        }
        comboBox.setSelection(selection);
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для не валидного поля
     * @return true если поле доступно
     */
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

    /**
     * Очищает значение поля.
     */
    @Override
    public void clear() {
        comboBox.clear();
        // ValueChangeEvent.fire(getRealComponent(), null);
    }

    private void restoreState(List<TreeModelData> models) {
        if (expandExpression == null) {
            return;
        }
        Tree<TreeModelData, String> tree = comboBox.getTree();
        for (TreeModelData m : models) {
            if (m.<Boolean>get(expandExpression) && tree.findNode(m) != null && !tree.isLeaf(m)) {
                tree.setExpanded(m, true);
            }
        }
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

    /**
     * Проверяет доступность для редактирования.
     *
     * @return true, если доступен для редактирования
     */
    @Override
    public boolean isEditable() {
        return super.isEditable();
    }

    /**
     * Устанавливает доступность для редактирования.
     *
     * @param editable true, доступ для редактирования
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
    }

    @JsIgnore
    @Override
    public boolean isSaveState() {
        return super.isSaveState();
    }

    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        super.setSaveState(save);
    }

    @JsIgnore
    public void setRestoreState(boolean restore) {
        super.setRestoreState(restore);
    }

    @JsIgnore
    @Override
    public StateScope getStateScope() {
        return super.getStateScope();
    }

    @JsIgnore
    @Override
    public void setStateScope(StateScope scope) {
        super.setStateScope(scope);
    }

    @JsIgnore
    @Override
    public void saveState() {
        super.saveState();
    }

    /**
     * Получает маску поля.
     *
     * @return маска поля
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Устанавливает маску поля.
     *
     * @param mask новая маска поля
     */
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Очищает статус недействительности для поля.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Проверяет, обязательно ли поле для заполнения.
     *
     * @return true, если обязательно
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Устанавливает обязательность для заполнения поля.
     *
     * @param required true, если поле обязательно для заполнения
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Устанавливает значение только для чтения.
     *
     * @param readOnly true, если поле доступно только для чтения
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @JsIgnore
    @Override
    public ListModelData getValue() {
        throw new UnsupportedOperationException();
    }

    @JsIgnore
    @Override
    public void setValue(ListModelData value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Возвращает текст объекта.
     *
     * @return новый текст объекта
     */
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
