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
import org.whirlplatform.component.client.*;
import org.whirlplatform.component.client.data.ClassKeyProvider;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.ext.XTree;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.component.client.utils.SimpleEditorError;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

import java.util.*;

public class TreeBuilder extends ComponentBuilder
        implements Clearable, ListParameter<RowListValue>, Validatable, SelectEvent.HasSelectHandlers, ChangeEvent.HasChangeHandlers, HasState {

    private XTree<RowModelData, String> tree;
    protected TreeStore<RowModelData> store;

    /**
     * Колонка наименования
     */
    protected String nameExpression;
    protected String nameExpressionColumn;

    /**
     * Идентификатор таблицы (DataSource)
     */
    private String dataSourceId;

    /**
     * Выражение для вычисления наличия родителей
     */
    protected String leafExpression;
    protected String leafExpressionColumn;

    /**
     * Колонка родителя
     */
    private String parentColumn;

    /**
     * Колонка со значением для чекбокса (checked/unchecked)
     */
    private String checkExpression;
    private String checkExpressionColumn;

    /**
     * Колонка хранящая состояние выбора
     */
    private String selectExpression;
    private String selectExpressionColumn;

    /**
     * Колонка со значением для ветки (expand/collapse)
     */
    private String stateExpression;
    private String stateExpressionColumn;

    /**
     * Колонка со ссылкой на картинку
     */
    private String imageColumn;

    /**
     * Флаг, указывающий, что элемент выбираемый
     */
    private Boolean checkable;

    /**
     * Флаг, указывающий нужно ли сохранять состояние дерева в БД
     */
    private boolean saveState;

    protected boolean restoreState;

    protected StorageWrapper<RowListValue> stateStore;
    protected StateStore<RowListValue> selectionStateStore;

    /**
     * Флаг, указывающий является ли поле обязательным для заполнения
     */
    private boolean required = false;
    private String forceInvalidText;

    private Boolean singleSelection = false;
    private String whereSql;
    private DelayedTask saveCurrentTask;
    private SideErrorHandler errorHandler;
    protected HandlerRegistration checkChangedHandler;
    private TreeSelectionModel<RowModelData> selModel;
    private ParameterHelper paramHelper;
    private List<DataValue> lastParameters;

    @SuppressWarnings("rawtypes")
    private TreeLoader loader;
    private ValueProvider<RowModelData, String> valueProvider;

    private IconProvider<RowModelData> iconProvider;

    private boolean useSearchField;
    private int minChars;

    public TreeBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public TreeBuilder() {
        super();
    }

    public ComponentType getType() {
        return ComponentType.TreePanelType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        nameExpressionColumn = "NAME_COLUMN";
        leafExpressionColumn = "PROPERTY_HAS_CHILDREN";
        checkExpressionColumn = "CHECK_COLUMN";
        stateExpressionColumn = "STATE_COLUMN";
        selectExpressionColumn = "SELECT_COLUMN";

        minChars = 2;

        checkable = true;

        paramHelper = new ParameterHelper();
        lastParameters = Collections.emptyList();

        valueProvider = new ValueProvider<RowModelData, String>() {

            @Override
            public String getValue(RowModelData object) {
                return object.get(nameExpressionColumn);
            }

            @Override
            public void setValue(RowModelData object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }

        };
        iconProvider = new IconProvider<RowModelData>() {

            @Override
            public ImageResource getIcon(RowModelData model) {
                if (imageColumn != null) {
                    String data = model.get(imageColumn);
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
        store = new TreeStore<RowModelData>(new ClassKeyProvider());
        // tree = initTree(initLoader(store));
        loader = initLoader(store);
        tree = initTree(loader);
        selModel = tree.getSelectionModel();
        initSaveTask();
        container.add(tree);
        return container;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DataSource.getCode()) && value != null) {
            if (value.getListModelData() != null) {
                setDataSourceId(value.getListModelData().getId());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.NameExpression.getCode()) && value != null) {
            nameExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.LeafExpression.getCode()) && value != null) {
            leafExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ParentColumn.getCode()) && value != null) {
            parentColumn = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.CheckExpression.getCode()) && value != null) {
            checkExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StateExpression.getCode()) && value != null) {
            stateExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ImageColumn.getCode()) && value != null) {
            imageColumn = value.getString();
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
                if (tree.getCheckStyle() == CheckCascade.NONE) // Пока только
                    // для
                    // CheckStyle.NONE
                    tree.setSingleSelectionCheckMode();
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
        selectionStateStore = new SelectionClientStateStore<RowListValue>(StateScope.LOCAL, getClassMetadata());
    }

    protected TreeLoader<RowModelData> initLoader(final TreeStore<RowModelData> store) {
        RpcProxy<RowModelData, List<RowModelData>> proxy = createProxy();

        TreeLoader<RowModelData> loader = new TreeLoader<RowModelData>(proxy) {
            @Override
            public boolean hasChildren(RowModelData parent) {
                return parent.<Boolean>get(leafExpressionColumn);
            }

            @Override
            protected void onLoadSuccess(RowModelData loadConfig, List<RowModelData> result) {
                super.onLoadSuccess(loadConfig, result);
                tree.unmask();
                restoreState(result);
            }
        };
        loader.addLoadHandler(new ChildTreeStoreBinding<RowModelData>(store) {
            @Override
            public void onLoad(LoadEvent<RowModelData, List<RowModelData>> event) {
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

    private void loadQueryResult(TreeStore<RowModelData> store, List<RowModelData> models) {
        boolean hasChanged = true;
        Map<String, RowModelData> added = new HashMap<String, RowModelData>();
        while (hasChanged) {
            hasChanged = false;
            for (RowModelData m : models) {

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
        for (RowModelData md : store.getAll()) {
            if (store.hasChildren(md)) {
                tree.findNode(md).setLoaded(true);
                tree.setExpanded(md, true);
            }
        }

    }

    protected RpcProxy<RowModelData, List<RowModelData>> createProxy() {
        RpcProxy<RowModelData, List<RowModelData>> proxy = new RpcProxy<RowModelData, List<RowModelData>>() {
            @Override
            public void load(final RowModelData parent, final AsyncCallback<List<RowModelData>> callback) {
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        DataServiceAsync.Util.getDataService(callback).getTreeClassData(SessionToken.get(),
                                getClassMetadata(), getLoadConfig(parent));
                        lastParameters = Collections.emptyList();
                    }
                });
            }
        };
        return proxy;
    }

    @SuppressWarnings({"rawtypes"})
    protected XTree<RowModelData, String> initTree(TreeLoader<RowModelData> loader) {
        tree = new XTree<RowModelData, String>(store, valueProvider) {
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
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<RowModelData>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<RowModelData> event) {
                if (saveState) {
                    saveCurrentTask.delay(100);
                } else {
                    fireEvent(new SelectEvent());
                    fireEvent(new ChangeEvent());
                }
            }
        });

        tree.addValueChangeHandler(new ValueChangeHandler<RowModelData>() {
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
            public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
                String q = tree.getSearchText();
                RowModelData model = store.findModelWithKey((String) context.getKey());
                String style = model.getStyle(nameExpressionColumn);

                String data = value == null ? "" : SafeHtmlUtils.htmlEscape(value);
                StringBuilder result = new StringBuilder();
                result.append("<span title=\"" + data + "\"");
                result.append(" style=\"");
                // серым отображаются элементы не найденный в поиске
                if (isQuery() && !(!Util.isEmptyString(value) && value.toLowerCase().contains(q.toLowerCase()))) {
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

    protected void restoreState(List<RowModelData> list) {
        if (checkExpression == null && stateExpression == null && selectExpression == null) {
            return;
        }
        RowModelData firstChecked = null;
        for (RowModelData m : list) {
            // Отмечаем чекбокс
            if (m.<Boolean>get(checkExpressionColumn)) {
                if (firstChecked == null) {
                    firstChecked = m;
                }
                tree.setChecked(m, CheckState.CHECKED);
            }
            // Раскрываем ветку
            if (m.<Boolean>get(stateExpressionColumn) && !tree.isLeaf(m)) {
                tree.setExpanded(m, true);
            }
            if (m.<Boolean>get(selectExpressionColumn)) {
                // tree.getSelectionModel().setSelection(Arrays.asList(m));
                tree.getSelectionModel().select(m, true);

            }
        }
        if (firstChecked != null) {
            tree.scrollIntoView(firstChecked);
        }
    }

    protected ClassMetadata getClassMetadata() {
        ClassMetadata metadata = new ClassMetadata(dataSourceId);
        if (parentColumn != null && !parentColumn.isEmpty()) {
            metadata.addField(new FieldMetadata(parentColumn, DataType.STRING, null));
        }
        if (imageColumn != null && !imageColumn.isEmpty()) {
            metadata.addField(new FieldMetadata(imageColumn, DataType.STRING, null));
        }
        return metadata;
    }

    @SuppressWarnings("rawtypes")
    protected TreeClassLoadConfig getLoadConfig(RowModelData parent) {
        TreeClassLoadConfig config = new TreeClassLoadConfig();
        config.setParameters(paramHelper.getValues(lastParameters));
        config.setLeafExpression(leafExpression);
        config.setStateExpression(stateExpression);
        config.setCheckExpression(checkExpression);
        config.setSelectExpression(selectExpression);
        config.setNameExpression(nameExpression);
        config.setParentField(parentColumn);
        config.setParent(parent);
        config.setWhereSql(whereSql);
        config.setAll(true);
        if (parent == null && isQuery()) {
            config.setQuery(tree.getSearchText());
        }
        return config;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Tree<RowModelData, String> getRealComponent() {
        return tree;
    }

    @Override
    public void clear() {
        selModel.deselectAll();
    }

    @Override
    public boolean isValid() {
        return isValid(false);
    }

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

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public RowListValue getFieldValue() {
        boolean containSelected = false;
        RowListValue result = new RowListValueImpl();
        result.setCheckable(checkable);
        result.setCode(getCode());
        RowModelData last = tree.getSelectionModel().getSelectedItem();
        List<RowModelData> checked = tree.getCheckedSelection();
        for (RowModelData m : checked) {
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

    @Override
    public void setFieldValue(RowListValue value) {
        if (value == null || value.getRowList().isEmpty()) {
            tree.setCheckedSelection(Collections.emptyList());
            tree.getSelectionModel().deselectAll();
            return;
        }
        List<RowModelData> models = new ArrayList<RowModelData>();
        RowModelData selected = null;
        for (RowValue row : value.getRowList()) {
            RowModelData model = new RowModelDataImpl();
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

    public TreeStore<RowModelData> getStore() {
        return store;
    }

    @Override
    public void markInvalid(String msg) {
        EditorError error = new SimpleEditorError(msg);
        errorHandler.markInvalid(Arrays.asList(error));
        forceInvalidText = msg;
    }

    @Override
    public void clearInvalid() {
        errorHandler.clearInvalid();
        forceInvalidText = null;
    }

    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler, SelectEvent.getType());
    }

    public void load() {
        load(Collections.emptyList());
    }

    public void load(List<DataValue> parameters) {
        lastParameters = parameters;
        loader.load();
    }

    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;

    }

    @Override
    public boolean isSaveState() {
        return saveState;
    }

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
            }
        }
    }

    @Override
    public StateScope getStateScope() {
        return getStateStore().getScope();
    }

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

    @SuppressWarnings("rawtypes")
    public void clearLabelFilter() {
        if (tree.hasSearch()) {
            tree.clearSearch();
        }
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
        return addHandler(handler, ChangeEvent.getType());
    }

    private static class LocatorParams {

        private static String TYPE_SEARCH_FIELD = "SearchField";
        private static String TYPE_INPUT = "Input";
        private static String TYPE_SEARCH_BUTTON = "SearchButton";

        private static String TYPE_ITEM = "Item";
        private static String TYPE_CHECK = "Check";
        private static String TYPE_JOINT = "Joint";
        private static String TYPE_LABEL = "Label";

        private static String PARAMETER_ID = "id";
        private static String PARAMETER_INDEX = "index";
        private static String PARAMETER_LABEL = "label";
    }

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
        } else if (tree.getSearchButton() != null && tree.getSearchButton().getElement().isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_SEARCH_BUTTON);
        } else {
            Tree.TreeNode<RowModelData> itemNode = tree.findNode(element);
            if (itemNode != null) {
                part = new Locator(LocatorParams.TYPE_ITEM);
                part.setParameter(LocatorParams.PARAMETER_ID, itemNode.getModelId());
                part.setParameter(LocatorParams.PARAMETER_INDEX, String.valueOf(store.indexOf(itemNode.getModel())));
                part.setParameter(LocatorParams.PARAMETER_LABEL, valueProvider.getValue(itemNode.getModel()));
                TreeView<RowModelData> view = tree.getView();
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

    @Override
    public Element getElementByLocator(Locator locator) {
        if (!fitsLocator(locator)) {
            return null;
        }

        Locator part = locator.getPart();
        if (part != null) {
            if (LocatorParams.TYPE_SEARCH_FIELD.equals(part.getType()) && tree.getSearchField() != null
                    && part.getPart() != null && LocatorParams.TYPE_INPUT.equals(part.getPart().getType())) {
                return tree.getSearchField().getCell().getInputElement(tree.getSearchField().getElement());
            } else if (LocatorParams.TYPE_SEARCH_BUTTON.equals(part.getType()) && tree.getSearchButton() != null) {
                return tree.getSearchButton().getElement();
            } else if (LocatorParams.TYPE_ITEM.equals(part.getType())) {
                RowModelData model = null;
                Tree.TreeNode<RowModelData> itemNode = null;
                if (part.hasParameter(LocatorParams.PARAMETER_ID)) {
                    model = store.findModelWithKey(part.getParameter(LocatorParams.PARAMETER_ID));
                } else if (part.hasParameter(LocatorParams.PARAMETER_LABEL)
                        && !Util.isEmptyString(part.getParameter(LocatorParams.PARAMETER_LABEL))) {
                    for (RowModelData m : store.getAll()) {
                        if (part.getParameter(LocatorParams.PARAMETER_LABEL).equals(valueProvider.getValue(model))) {
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
                    TreeView<RowModelData> view = tree.getView();
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

}
