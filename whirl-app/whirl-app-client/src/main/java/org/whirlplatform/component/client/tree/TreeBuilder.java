package org.whirlplatform.component.client.tree;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;
import com.sencha.gxt.widget.core.client.tree.TreeView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Clearable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.HasState;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.Validatable;
import org.whirlplatform.component.client.data.ClassKeyProvider;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.ext.XTree;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.component.client.utils.SimpleEditorError;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.data.TreeModelDataImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

/**
 * Дерево
 */
@JsType(name = "Tree", namespace = "Whirl")
public class TreeBuilder extends ComponentBuilder
    implements Clearable, ListParameter<RowListValue>, Validatable,
    SelectEvent.HasSelectHandlers, ChangeEvent.HasChangeHandlers, HasState {

    protected TreeStore<TreeModelData> store;
    /**
     * Колонка лейбла
     */
    protected String labelExpression;
    /**
     * Выражение для вычисления наличия родителей
     */
    protected String isLeafExpression;
    protected boolean restoreState;
    protected StorageWrapper<RowListValue> stateStore;
    protected StateStore<RowListValue> selectionStateStore;
    protected HandlerRegistration checkChangedHandler;
    private XTree<TreeModelData, String> tree;
    /**
     * Идентификатор таблицы (DataSource)
     */
    private String dataSourceId;
    /**
     * Колонка родителя
     */
    private String parentExpression;
    /**
     * Колонка со значением для чекбокса (checked/unchecked)
     */
    private String checkExpression;
    /**
     * Колонка хранящая состояние выбора
     */
    private String selectExpression;
    /**
     * Колонка со значением для ветки (expand/collapse)
     */
    private String expandExpression;
    /**
     * Колонка со ссылкой на картинку
     */
    private String imageExpression;
    /**
     * Флаг, указывающий, что элемент выбираемый imageExpression
     */
    private Boolean checkable;
    /**
     * Флаг, указывающий нужно ли сохранять состояние дерева в БД
     */
    private boolean saveState;
    /**
     * Флаг, указывающий является ли поле обязательным для заполнения
     */
    private boolean required = false;
    private String forceInvalidText;
    private Boolean singleSelection = false;
    private String whereSql;
    private DelayedTask saveCurrentTask;
    private SideErrorHandler errorHandler;
    private TreeSelectionModel<TreeModelData> selModel;
    private ParameterHelper paramHelper;
    private List<DataValue> lastParameters;

    @SuppressWarnings("rawtypes")
    private TreeLoader loader;
    private ValueProvider<TreeModelData, String> valueProvider;

    private IconProvider<TreeModelData> iconProvider;

    private boolean useSearchField;
    private int minChars;

    @JsConstructor
    public TreeBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public TreeBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    public ComponentType getType() {
        return ComponentType.TreePanelType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        minChars = 2;

        checkable = true;

        paramHelper = new ParameterHelper();
        lastParameters = Collections.emptyList();

        valueProvider = new ValueProvider<TreeModelData, String>() {

            @Override
            public String getValue(TreeModelData object) {
                //return object.get(labelExpression);
                return object.getLabel();
            }

            @Override
            public void setValue(TreeModelData object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }

        };
        iconProvider = new IconProvider<TreeModelData>() {

            @Override
            public ImageResource getIcon(TreeModelData model) {
                if (imageExpression != null) {
                    //String data = model.get(imageExpression);
                    String data = model.getImage();
                    if (data != null) {
                        SafeUri url = UriUtils.fromString(data);
                        return IconHelper.getImageResource(url, 16, 16);
                    }
                }
                return null;
            }
        };

        SimpleContainer container = new SimpleContainer();
        errorHandler = new SideErrorHandler(container);
        store = new TreeStore<>(new ClassKeyProvider());

        // tree = initTree(initLoader(store));
        loader = initLoader(store);
        tree = initTree(loader);
        selModel = tree.getSelectionModel();
        initSaveTask();
        container.add(tree);
        return container;
    }

    @JsIgnore
    @SuppressWarnings("rawtypes")
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DataSource.getCode()) && value != null) {
            if (value.getListModelData() != null) {
                setDataSourceId(value.getListModelData().getId());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.LabelExpression.getCode()) && value != null) {
            labelExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.IsLeafExpression.getCode()) && value != null) {
            isLeafExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ParentExpression.getCode()) && value != null) {
            parentExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.CheckExpression.getCode()) && value != null) {
            checkExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ExpandExpression.getCode()) && value != null) {
            expandExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ImageExpression.getCode()) && value != null) {
            imageExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SelectExpression.getCode()) && value != null) {
            selectExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Checkable.getCode()) && value != null) {
            checkable = Boolean.TRUE.equals(value.getBoolean());
            tree.setCheckable(checkable);
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SaveState.getCode()) && value != null) {
            saveState = Boolean.TRUE.equals(value.getBoolean());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StateScope.getCode())) {
            if (value != null && value.getString() != null) {
                setStateScope(StateScope.valueOf(value.getString()));
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.RestoreState.getCode()) && value != null) {
            restoreState = Boolean.TRUE.equals(value.getBoolean());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Required.getCode()) && value != null) {
            required = Boolean.TRUE.equals(value.getBoolean());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SingleSelection.getCode()) && value != null) {
            singleSelection = Boolean.TRUE.equals(value.getBoolean());
            if (tree != null && singleSelection) {
                if (tree.getCheckStyle() == CheckCascade.NONE) {
                    // Пока только
                    // для
                    // CheckStyle.NONE
                    tree.setSingleSelectionCheckMode();
                }
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode()) && value != null) {
            whereSql = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.CheckStyle.getCode()) && value != null) {
            if (!singleSelection) {
                tree.setCheckStyle(CheckStyleHelper.parseTreePanelCheckStyle(value.getString()));
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Parameters.getCode()) && value != null) {
            paramHelper.addJsonParameters(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SearchField.getCode()) && value != null) {
            useSearchField = Boolean.TRUE.equals(value.getBoolean());
            if (useSearchField) {
                tree.addSearch(getSearchCommand());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MinChars.getCode())) {
            if (value != null && value.getDouble() != null) {
                minChars = value.getDouble().intValue();
            }
        }
        return super.setProperty(name, value);
    }

    private void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
        selectionStateStore =
            new SelectionClientStateStore<RowListValue>(StateScope.LOCAL, new ClassMetadata(dataSourceId));
    }

    protected String getDataSourceId() {
        return dataSourceId;
    }

    protected TreeLoader<TreeModelData> initLoader(final TreeStore<TreeModelData> store) {
        RpcProxy<TreeModelData, List<TreeModelData>> proxy = createProxy();

        TreeLoader<TreeModelData> loader = new TreeLoader<TreeModelData>(proxy) {
            @Override
            public boolean hasChildren(TreeModelData parent) {
                return Optional.ofNullable(parent.<Boolean>get(isLeafExpression)).orElse(true);
            }

            @Override
            protected void onLoadSuccess(TreeModelData loadConfig, List<TreeModelData> result) {
                super.onLoadSuccess(loadConfig, result);
                tree.unmask();
                restoreState(result);
            }
        };
        loader.addLoadHandler(new ChildTreeStoreBinding<TreeModelData>(store) {
            @Override
            public void onLoad(LoadEvent<TreeModelData, List<TreeModelData>> event) {
                if (event.getLoadConfig() == null && isQuery()) {
                    store.clear();
                    if (!event.getLoadResult().isEmpty()) {
                        loadQueryResult(store, event.getLoadResult());
                    }
                } else {
                    super.onLoad(event);
                }
            }
        });
        return loader;
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
                tree.findNode(md).setLoaded(true);
                tree.setExpanded(md, true);
            }
        }
    }

    protected RpcProxy<TreeModelData, List<TreeModelData>> createProxy() {
        RpcProxy<TreeModelData, List<TreeModelData>> proxy =
            new RpcProxy<TreeModelData, List<TreeModelData>>() {
                @Override
                public void load(final TreeModelData parent,
                                 final AsyncCallback<List<TreeModelData>> callback) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            DataServiceAsync.Util.getDataService(callback)
                                .getTreeClassData(
                                        SessionToken.get(),
                                        dataSourceId,
                                        getLoadConfig(parent)
                                );
                            lastParameters = Collections.emptyList();
                        }
                    });
                }
            };
        return proxy;
    }

    @SuppressWarnings({"rawtypes"})
    protected XTree<TreeModelData, String> initTree(TreeLoader<TreeModelData> loader) {
        tree = new XTree<TreeModelData, String>(store, valueProvider) {
            @Override
            protected void onAfterFirstAttach() {
                super.onAfterFirstAttach();
                mask(DefaultMessages.getMessages().loadMask_msg());
                update();
            }
        };

        tree.setLoader(loader);
        tree.setCheckable(checkable);
        tree.setCheckStyle(CheckCascade.CHILDREN);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Будет работать при нажатии чекбокса?
        tree.getSelectionModel()
            .addSelectionChangedHandler(new SelectionChangedHandler<TreeModelData>() {
                @Override
                public void onSelectionChanged(SelectionChangedEvent<TreeModelData> event) {
                    if (saveState) {
                        saveCurrentTask.delay(100);
                    } else {
                        fireEvent(new SelectEvent());
                        fireEvent(new ChangeEvent());
                    }
                }
            });

        tree.addValueChangeHandler(new ValueChangeHandler<TreeModelData>() {
            @Override
            public void onValueChange(ValueChangeEvent event) {
                if (saveState) {
                    saveCurrentTask.delay(100);
                } else {
                    fireEvent(new ChangeEvent());
                }
            }
        });

        tree.setIconProvider(iconProvider);

        // Вставить в SelectionHandler не получится, т.к. в коде
        // вызывается select, и событие отрабатывает несколько раз
        AbstractCell<String> cell = new AbstractCell<String>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value,
                               SafeHtmlBuilder sb) {
                String q = tree.getSearchText();
                TreeModelData model = store.findModelWithKey((String) context.getKey());
                String style = model.getStyle(labelExpression);

                String data = value == null ? "" : SafeHtmlUtils.htmlEscape(value);
                StringBuilder result = new StringBuilder();
                result.append("<span title=\"" + data + "\"");
                result.append(" style=\"");
                // серым отображаются элементы не найденный в поиске
                if (isQuery() && !(!Util.isEmptyString(value)
                    && value.toLowerCase().contains(q.toLowerCase()))) {
                    result.append("color: darkgray;");
                }
                if (!Util.isEmptyString(style)) {
                    result.append(style);
                }
                result.append("\">" + data + "</span>");
                sb.append(SafeHtmlUtils.fromTrustedString(result.toString()));
            }
        };

        tree.setCell(cell);
        return tree;
    }

    private void initSaveTask() {
        saveCurrentTask = new DelayedTask() {

            @Override
            public void onExecute() {
                saveState();
            }
        };
    }

    protected void restoreState(List<TreeModelData> list) {
        if (checkExpression == null && expandExpression == null && selectExpression == null) {
            return;
        }
        TreeModelData firstChecked = null;
        for (TreeModelData m : list) {
            // Отмечаем чекбокс
            if (m.isIsCheck()) {
                if (firstChecked == null) {
                    firstChecked = m;
                }
                tree.setChecked(m, CheckState.CHECKED);
            }
            // Раскрываем ветку
            if (m.isIsExpand() && !tree.isLeaf(m)) {
                tree.setExpanded(m, true);
            }
            if (m.isIsSelect()) {
                // tree.getSelectionModel().setSelection(Arrays.asList(m));
                tree.getSelectionModel().select(m, true);

            }
        }
        if (firstChecked != null) {
            tree.scrollIntoView(firstChecked);
        }
    }

    @SuppressWarnings("rawtypes")
    protected ClassLoadConfig getLoadConfig(TreeModelData parent) {
        ClassLoadConfig config = new ClassLoadConfig();
        config.setParameters(paramHelper.getValues(lastParameters));
        config.setIsLeafExpression(isLeafExpression);
        config.setExpandExpression(expandExpression);
        config.setCheckExpression(checkExpression);
        config.setSelectExpression(selectExpression);
        config.setParentExpression(parentExpression);
        config.setParent(parent);
        config.setWhereSql(whereSql);
        config.setLabelExpression(labelExpression);
        config.setImageExpression(imageExpression);
        config.setAll(true);
        if (parent == null && isQuery()) {
            config.setQuery(tree.getSearchText());
        }
        return config;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Tree<TreeModelData, String> getRealComponent() {
        return tree;
    }

    /**
     * Очищает значение поля.
     */
    @Override
    public void clear() {
        selModel.deselectAll();
    }

    @JsIgnore
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
     */
    @Override
    public boolean isValid(boolean invalidate) {
        if (forceInvalidText != null) {
            return false;
        }
        if (isRequired() && ((checkable && tree.getCheckedSelection().size() == 0)
            || (!checkable && tree.getSelectionModel().getSelectedItem() == null))) {
            if (invalidate) {
                markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        }
        if (invalidate) {
            clearInvalid();

        }
        return true;
    }

    /**
     * Проверяет, обязательно ли поле для заполнения.
     *
     * @return true, если обязательно
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Устанавливает обязательность для заполнения поля.
     *
     * @param required true, если поле обязательно для заполнения
     */
    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @JsIgnore
    @Override
    public RowListValue getFieldValue() {
        boolean containSelected = false;
        RowListValue result = new RowListValueImpl();
        result.setCheckable(checkable);
        result.setCode(getCode());
        TreeModelData last = tree.getSelectionModel().getSelectedItem();
        List<TreeModelData> checked = tree.getCheckedSelection();
        for (TreeModelData m : checked) {
            RowValue row = new RowValueImpl(m.getId());
            row.setChecked(true);
            if (m == last) {
                row.setSelected(true);
                containSelected = true;
            }
            row.setExpanded(tree.isExpanded(m));
            result.addRowValue(row);
        }
        if (!containSelected && last != null) {
            RowValue selectedRow = new RowValueImpl(last.getId());
            selectedRow.setSelected(true);
            selectedRow.setExpanded(tree.isExpanded(last));
            result.addRowValue(selectedRow);
        }
        return result;
    }

    @JsIgnore
    @Override
    public void setFieldValue(RowListValue value) {
        if (value == null || value.getRowList().isEmpty()) {
            tree.setCheckedSelection(Collections.emptyList());
            tree.getSelectionModel().deselectAll();
            return;
        }
        List<TreeModelData> models = new ArrayList<TreeModelData>();
        TreeModelData selected = null;
        for (RowValue row : value.getRowList()) {
            TreeModelData model = new TreeModelDataImpl();
            model.setId(row.getId());
            models.add(model);
            if (row.isSelected()) {
                selected = model;
            }
        }
        if (selected != null) {
            tree.getSelectionModel().select(selected, false);
        }
        tree.setCheckedSelection(models);
    }

    @JsIgnore
    public TreeStore<TreeModelData> getStore() {
        return store;
    }

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        EditorError error = new SimpleEditorError(msg);
        errorHandler.markInvalid(Collections.singletonList(error));
        forceInvalidText = msg;
    }

    /**
     * Очищает статус недействительности для поля.
     */
    @Override
    public void clearInvalid() {
        errorHandler.clearInvalid();
        forceInvalidText = null;
    }

    @JsIgnore
    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler, SelectEvent.getType());
    }

    /**
     * Загружает данные, используя текущую конфигурацию
     */
    public void load() {
        load(Collections.emptyList());
    }

    @JsIgnore
    public void load(List<DataValue> parameters) {
        lastParameters = parameters;
        loader.load();
    }

    /**
     * Проверяет необходимость сохранения состояния дерева в БД.
     *
     * @return true, если состояние нужно сохранить
     */
    @JsIgnore
    @Override
    public boolean isSaveState() {
        return saveState;
    }

    /**
     * Устанавливает необходимо ли сохранять состояние дерева в БД.
     *
     * @param save true, если состояние нужно сохранить
     */
    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;

    }

    @JsIgnore
    @Override
    public StateScope getStateScope() {
        return getStateStore().getScope();
    }

    @JsIgnore
    @Override
    public void setStateScope(StateScope scope) {
        if (stateStore != null && scope != null && scope != stateStore.getScope()) {
            switch (scope) {
                case MEMORY:
                    stateStore = StorageHelper.memory();
                    break;
                case SESSION:
                    stateStore = StorageHelper.session();
                    break;
                case LOCAL:
                    stateStore = StorageHelper.local();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported scope");
            }
        }
    }

    @JsIgnore
    @Override
    public void saveState() {
        RowListValue v = getFieldValue();
        if (saveState) {
            getStateStore().put(getCode(), v);
        }
        selectionStateStore.save(getId() + "/select", v);
    }

    private StorageWrapper<RowListValue> getStateStore() {
        if (stateStore == null) {
            setStateScope(StateScope.MEMORY);
        }
        return stateStore;
    }

    protected void restoreSelectionState() {
        if (restoreState) {
            RowListValue v = selectionStateStore.restore(getId() + "/select");
            setFieldValue(v);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Command getSearchCommand() {
        return new Command() {
            @Override
            public void execute() {
                tree.mask();
                tree.setFiltered(isQuery());
                load();
            }
        };
    }

    @SuppressWarnings("rawtypes")
    protected boolean isQuery() {
        return tree.hasSearch() && tree.getSearchText().length() >= minChars;
    }

    @JsIgnore
    @SuppressWarnings("rawtypes")
    public void clearLabelFilter() {
        if (tree.hasSearch()) {
            tree.clearSearch();
        }
    }

    @JsIgnore
    @Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
        return addHandler(handler, ChangeEvent.getType());
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator locator = super.getLocatorByElement(element);
        if (locator == null) {
            return null;
        }

        Locator part = null;
        if (tree.getSearchField() != null && tree.getSearchField().getCell()
            .getInputElement(tree.getSearchField().getElement()).isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_SEARCH_FIELD);
            part.setPart(new Locator(LocatorParams.TYPE_INPUT));
        } else if (tree.getSearchButton() != null
            && tree.getSearchButton().getElement().isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_SEARCH_BUTTON);
        } else {
            Tree.TreeNode<TreeModelData> itemNode = tree.findNode(element);
            if (itemNode != null) {
                part = new Locator(LocatorParams.TYPE_ITEM);
                part.setParameter(LocatorParams.PARAMETER_ID, itemNode.getModelId());
                part.setParameter(LocatorParams.PARAMETER_INDEX,
                    String.valueOf(store.indexOf(itemNode.getModel())));
                part.setParameter(LocatorParams.PARAMETER_LABEL,
                    valueProvider.getValue(itemNode.getModel()));
                TreeView<TreeModelData> view = tree.getView();
                if (view.getCheckElement(itemNode).isOrHasChild(element)) {
                    part.setPart(new Locator(LocatorParams.TYPE_CHECK));
                } else if (view.getJointElement(itemNode).isOrHasChild(element)) {
                    part.setPart(new Locator(LocatorParams.TYPE_JOINT));
                } else if (view.getTextElement(itemNode).isOrHasChild(element)) {
                    part.setPart(new Locator(LocatorParams.TYPE_LABEL));
                }
            }
        }
        if (part != null) {
            locator.setPart(part);
        }

        return locator;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (!fitsLocator(locator)) {
            return null;
        }

        Locator part = locator.getPart();
        if (part != null) {
            if (LocatorParams.TYPE_SEARCH_FIELD.equals(part.getType())
                && tree.getSearchField() != null
                && part.getPart() != null
                && LocatorParams.TYPE_INPUT.equals(part.getPart().getType())) {
                return tree.getSearchField().getCell()
                    .getInputElement(tree.getSearchField().getElement());
            } else if (LocatorParams.TYPE_SEARCH_BUTTON.equals(part.getType())
                && tree.getSearchButton() != null) {
                return tree.getSearchButton().getElement();
            } else if (LocatorParams.TYPE_ITEM.equals(part.getType())) {
                TreeModelData model = null;
                Tree.TreeNode<TreeModelData> itemNode = null;
                if (part.hasParameter(LocatorParams.PARAMETER_ID)) {
                    model = store.findModelWithKey(part.getParameter(LocatorParams.PARAMETER_ID));
                } else if (part.hasParameter(LocatorParams.PARAMETER_LABEL)
                    && !Util.isEmptyString(part.getParameter(LocatorParams.PARAMETER_LABEL))) {
                    for (TreeModelData m : store.getAll()) {
                        if (part.getParameter(LocatorParams.PARAMETER_LABEL)
                            .equals(valueProvider.getValue(model))) {
                            model = m;
                            break;
                        }
                    }
                }
                if (model != null) {
                    itemNode = tree.findNode(model);
                }
                if (itemNode != null && part.getPart() != null) {
                    Locator subPart = part.getPart();
                    TreeView<TreeModelData> view = tree.getView();
                    if (LocatorParams.TYPE_CHECK.equals(subPart.getType())) {
                        return view.getCheckElement(itemNode);
                    } else if (LocatorParams.TYPE_JOINT.equals(subPart.getType())) {
                        return view.getJointElement(itemNode);
                    } else if (LocatorParams.TYPE_LABEL.equals(subPart.getType())) {
                        return view.getTextElement(itemNode);
                    }
                }
            }
        }
        return super.getElementByLocator(locator);
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
     * Устанавливает фокус на компоненте.
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
     * @return true если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private static class LocatorParams {

        private static final String TYPE_SEARCH_FIELD = "SearchField";
        private static final String TYPE_INPUT = "Input";
        private static final String TYPE_SEARCH_BUTTON = "SearchButton";

        private static final String TYPE_ITEM = "Item";
        private static final String TYPE_CHECK = "Check";
        private static final String TYPE_JOINT = "Joint";
        private static final String TYPE_LABEL = "Label";

        private static final String PARAMETER_ID = "id";
        private static final String PARAMETER_INDEX = "index";
        private static final String PARAMETER_LABEL = "label";
    }

}
