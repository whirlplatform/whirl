package org.whirlplatform.component.client.grid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.error.ErrorHandler;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Clearable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.HasState;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.component.client.Validatable;
import org.whirlplatform.component.client.data.ClassKeyProvider;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.TableClassProxy;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.RowDoubleClickEvent;
import org.whirlplatform.component.client.event.SearchEvent;
import org.whirlplatform.component.client.event.SortEvent;
import org.whirlplatform.component.client.event.UpdateEvent;
import org.whirlplatform.component.client.form.FieldFormWindow;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.state.AbstractMetadataStateStore;
import org.whirlplatform.component.client.state.ColumnConfigStore;
import org.whirlplatform.component.client.state.FilterClientStateStore;
import org.whirlplatform.component.client.state.PagingClientStateStore;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.SortClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.Pair;
import org.whirlplatform.component.client.utils.SimpleEditorError;
import org.whirlplatform.component.client.window.WindowManager;
import org.whirlplatform.component.client.window.dialog.DialogManager;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FilterValue;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.PageConfig;
import org.whirlplatform.meta.shared.SortValue;
import org.whirlplatform.meta.shared.TableConfig;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Редактируемый грид
 */
@JsType(name = "EditGrid", namespace = "Whirl")
public class EditGridBuilder extends ComponentBuilder implements Clearable, Validatable, TitleProvider,
        ListParameter<RowListValue>, org.whirlplatform.component.client.event.LoadEvent.HasLoadHandlers, org.whirlplatform.component.client.event.SelectEvent.HasSelectHandlers, InsertEvent.HasInsertHandlers, UpdateEvent.HasUpdateHandlers,
        DeleteEvent.HasDeleteHandlers, LoadConfigProvider, HasState, RowDoubleClickEvent.HasRowDoubleClickHandlers {

    private LoadHandler<ClassLoadConfig, LoadData<RowModelData>> handler;

    private String classId;
    private boolean loading = false;

    private ClassMetadata metadata;
    private ClassStore<RowModelData, ClassLoadConfig> store;
    private ClassAction action;
    private ColumnModelHelper columnModel;
    private EditGridToolBar toolbar;
    private SortPanel sortPanel;

    private Grid<RowModelData> grid;
    private GridEditing<RowModelData> editing;
    private GridPagingToolBar paginator;

    private VerticalLayoutContainer wrapper;

    private ErrorHandler errorSupport;

    private boolean required;
    private int maxRows;
    private String maxRowsMessage;
    private String forceInvalidText;

    private boolean loadAll;
    private SelectionMode selectionMode;
    private boolean reloadMetadata;
    private String whereSql;

    private boolean showPagingToolbar;

    private boolean showButtonsData;

    private boolean showButtonsFind;

    private boolean showButtonsExport;

    private boolean showButtonsProcess;

    private boolean showButtonsRefresh;

    private boolean hideButtonsGroup;

    private FilterPanel filterPanel;

    private boolean skipInitialLoad;

    private boolean haveFile;

    private ParameterHelper paramHelper;

    private AbstractMetadataStateStore<ArrayList<FilterValue>> filterStateStore;
    private AbstractMetadataStateStore<ArrayList<SortValue>> sortStateStore;
    private AbstractMetadataStateStore<PageConfig> pageStateStore;

    protected StorageWrapper<RowListValue> stateStore;
    protected StateStore<RowListValue> selectionStateStore;
    protected boolean saveState;
    protected boolean restoreState;

    private int defaultRowsPerPage = 0;

    private boolean showLoadMask;

    private boolean cellToolTip;

    private ColumnConfigStore columnConfigStore;

    // Окна редактирования-просмотра полей грида
    private Set<FieldFormWindow> activeFieldForms = new HashSet<>();

    // TODO фикс на обновление заголовка если он строится не видимым
    // private boolean refreshHeader = true;
    private HandlerRegistration headerRefreshRegistration;

    @JsConstructor
    public EditGridBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public EditGridBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип редактируемого грида
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.EditGridType;
    }

    /**
     * Создание компонента - редактируемый грид
     *
     * @return Component
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;
        maxRows = -1;
        loadAll = false;
        reloadMetadata = false;
        showPagingToolbar = true;
        showButtonsData = true;
        showButtonsFind = true;
        showButtonsExport = true;
        showButtonsProcess = true;
        showButtonsRefresh = false;
        hideButtonsGroup = false;
        skipInitialLoad = false;
        haveFile = false;
        cellToolTip = false;

        paramHelper = new ParameterHelper();
        selectionMode = SelectionMode.MULTI;
        handler = new LoadHandler<ClassLoadConfig, LoadData<RowModelData>>() {
            @Override
            public void onLoad(LoadEvent<ClassLoadConfig, LoadData<RowModelData>> event) {
                grid.getView().refresh(true);
                if (paginator != null) {
                    paginator.setRows(event.getLoadResult().getRows());
                    paginator.enable();
                    paginator.recalcParams();
                    if (filterPanel != null) {
                        paginator.setFilter(filterPanel.getFilterString());
                    }
                }
                fireEvent(new org.whirlplatform.component.client.event.LoadEvent());
                wrapper.forceLayout();
                restoreSelectionState();
                if (showLoadMask) {
                    grid.unmask();
                    if (!isEnabled()) {
                        setEnabled(false);
                    }
                }
                loading = false;
            }
        };
        wrapper = new VerticalLayoutContainer();
        grid = new Grid<RowModelData>(temporaryStore(), temporaryColmnModel());
        // grid.setLoadMask(true);
        grid.getView().setTrackMouseOver(false);
        wrapper.add(grid, new VerticalLayoutData(1, 1));
        errorSupport = new SideErrorHandler(wrapper);
        setCheckable(true);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellClick(CellDoubleClickEvent event) {
                fireEvent(new RowDoubleClickEvent());
            }
        });

        return wrapper;
    }

    /**
     * Установка атрибута для редактируемого грида
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DataSource.getCode())) {
            if (value != null && value.getListModelData() != null) {
                classId = value.getListModelData().getId();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MaxRowSelected.getCode())) {
            if (value != null && value.getDouble() != null) {
                maxRows = value.getDouble().intValue();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MaxRowMessage.getCode())) {
            if (value != null) {
                maxRowsMessage = value.getString();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowLoadMask.getCode())) {
            if (value != null) {
                showLoadMask = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.LoadAll.getCode())) {
            if (value != null) {
                loadAll = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.SingleSelection.getCode())) {
            if (value != null && Boolean.TRUE.equals(value.getBoolean())) {
                setSelectionMode(SelectionMode.SINGLE);
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ReloadStructure.getCode())) {
            if (value != null && value.getBoolean() != null) {
                reloadMetadata = value.getBoolean();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.HideColumnHeader.getCode())) {
            if (value != null) {
                grid.setHideHeaders(Boolean.TRUE.equals(value.getBoolean()));
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowPagingToolbar.getCode())) {
            if (value != null) {
                showPagingToolbar = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowDataButtons.getCode())) {
            if (value != null) {
                showButtonsData = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowFindButtons.getCode())) {
            if (value != null) {
                showButtonsFind = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowExportButtons.getCode())) {
            if (value != null) {
                showButtonsExport = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowMethodButtons.getCode())) {
            if (value != null) {
                showButtonsProcess = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ShowRefreshButtons.getCode())) {
            if (value != null) {
                showButtonsRefresh = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.HideButtonGroups.getCode())) {
            if (value != null) {
                hideButtonsGroup = Boolean.TRUE.equals(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Checkable.getCode())) {
            if (value != null) {
                setCheckable(Boolean.TRUE.equals(value.getBoolean()));
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode())) {
            if (value != null) {
                whereSql = value.getString();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Parameters.getCode())) {
            if (value != null) {
                paramHelper.addJsonParameters(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.SkipInitialLoad.getCode()) && value.getBoolean() != null) {
            if (value != null) {
                skipInitialLoad = value.getBoolean();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.DefaultRowsPerPage.getCode())) {
            if (value != null && value.getDouble() != null) {
                defaultRowsPerPage = value.getDouble().intValue();
            }
        } else if (name.equalsIgnoreCase(PropertyType.SaveState.getCode())) {
            if (value != null) {
                setSaveState(Boolean.TRUE.equals(value.getBoolean()));
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.RestoreState.getCode())) {
            if (value != null) {
                setRestoreState(Boolean.TRUE.equals(value.getBoolean()));
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.StateScope.getCode())) {
            if (value != null && value.getString() != null) {
                StateScope scope = StateScope.valueOf(value.getString());
                setStateScope(scope);
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.CellToolTip.getCode())) {
            if (value != null) {
                cellToolTip = Boolean.TRUE.equals(value.getBoolean());
                if (cellToolTip) {
                    ToolTipConfig conf = new ToolTipConfig();
                    conf.setDismissDelay(40000);
                    new QuickTip(grid).setToolTipConfig(conf);
                }
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.MoveColumns.getCode())) {
            if (value != null) {
                grid.setColumnReordering(Boolean.TRUE.equals(value.getBoolean()));
            }
        }
        return super.setProperty(name, value);
    }

    /**
     * Перезагрузка метаданных редактируемого грида
     */
    private void reloadMetadata(List<DataValue> parameters) {
        reloadMetadata(parameters, false);
    }

    private void reloadMetadata(List<DataValue> parameters, final boolean skipLoad) {
        loading = true;
        AsyncCallback<TableConfig> callback = new AsyncCallback<TableConfig>() {

            @Override
            public void onFailure(Throwable caught) {
                loading = false;
                InfoHelper.throwInfo(getId() + "-throw", caught);
            }

            @Override
            public void onSuccess(TableConfig result) {
                reconfigure(result, skipLoad);
            }

        };
        DataServiceAsync.Util.getDataService(callback).getTableConfig(SessionToken.get(), classId, whereSql,
                paramHelper.getValues(parameters));
    }

    /**
     * Создание и загрузка редактируемого грида
     */
    @JsIgnore
    @Override
    public Component create() {
        Component comp = super.create();

        initPaginator();

        reloadMetadata(Collections.emptyList(), skipInitialLoad);
        return comp;
    }

    /**
     * Установка параметра "выделяемости" (мультивыбора) в гриде
     *
     * @param mode - SelectionMode
     */
    private void setSelectionMode(SelectionMode mode) {
        this.selectionMode = mode;
        grid.getSelectionModel().setSelectionMode(selectionMode);
    }

    /**
     * Установка параметра "выбираемости"
     *
     * @param checkable - boolean
     */
    private void setCheckable(boolean checkable) {
        if (checkable) {
            grid.setSelectionModel(new CheckBoxSelectionModel<RowModelData>(new IdentityValueProvider<RowModelData>()));

        } else {
            grid.setSelectionModel(new GridSelectionModel<RowModelData>());
        }
        setSelectionMode(selectionMode);
        initSelectHandler();
    }

    /**
     * Получение сущности грида
     *
     * @return Grid<        RowModelData        >
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Grid<RowModelData> getRealComponent() {
        return grid;
    }

    private ListStore<RowModelData> temporaryStore() {
        return new ListStore<RowModelData>(new ClassKeyProvider());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ColumnModel<RowModelData> temporaryColmnModel() {
        List<ColumnConfig> list = new ArrayList<ColumnConfig>();
        return new ColumnModel(list);
    }

    /**
     * Переконфигурация грида
     *
     * @param config   - TableConfig
     * @param skipLoad - boolean
     */
    @JsIgnore
    public void reconfigure(TableConfig config, boolean skipLoad) {
        this.metadata = config.getMetadata();

        if (columnConfigStore == null && saveState) {
            columnConfigStore = new ColumnConfigStore(StateScope.LOCAL, metadata, getId());
        }

        store = new ClassStore<RowModelData, ClassLoadConfig>(metadata, new TableClassProxy(metadata));
        store.getLoader().addLoadHandler(handler);
        columnModel = new ColumnModelHelper(metadata, store, cellToolTip, columnConfigStore);

        if (grid.getSelectionModel() instanceof CheckBoxSelectionModel) {
            CheckBoxSelectionModel<RowModelData> sm = (CheckBoxSelectionModel<RowModelData>) grid.getSelectionModel();
            columnModel.setFirstColumn(sm.getColumn());
        }

        grid.reconfigure(store, columnModel.build());

        if (metadata.isUpdatable()) {
            editing = new GridInlineEditing<RowModelData>(grid);
            ((GridInlineEditing<RowModelData>) editing).setClicksToEdit(ClicksToEdit.TWO);
            ((GridInlineEditing<RowModelData>) editing).setRevertInvalid(true);

            columnModel.setEditing(editing);
            store.addStoreRecordChangeHandler(new StoreRecordChangeHandler<RowModelData>() {
                @Override
                public void onRecordChange(StoreRecordChangeEvent<RowModelData> event) {
                    Iterator<Change<RowModelData, ?>> iter = event.getRecord().getChanges().iterator();
                    while (iter.hasNext()) {
                        Change<RowModelData, ?> c = iter.next();
                        // event.getRecord().getModel().setChanged((String)
                        // c.getChangeTag());
                        event.getRecord().getModel().addChangedField((String) c.getChangeTag());
                    }
                    store.commitChanges();
                }
            });
        }

        initToolbar(metadata);

        initFilter(metadata);
        initSortPanel(metadata);

        if (paginator != null) {
            // При первой загрузке устанавливаются все параметры,
            // при последующих - только общее количество строк
            initPagingStateStore(metadata);
        }

        if (!skipLoad) {
            load(false);
        }

        if (metadata.isInsertable() || metadata.isUpdatable() || metadata.isDeletable()) {
            action = new ClassAction(metadata, store, paramHelper);
            action.addDeleteHandler(new DeleteEvent.DeleteHandler() {

                @Override
                public void onDelete(DeleteEvent event) {
                    fireEvent(new DeleteEvent());
                }
            });
            action.addUpdateHandler(new UpdateEvent.UpdateHandler() {
                @Override
                public void onUpdate(UpdateEvent event) {
                    fireEvent(new UpdateEvent());
                }
            });
        } else {
            action = null;
        }

        // Обработчики сохранения состояния колонок
        if (columnConfigStore != null) {
            grid.getColumnModel().addColumnHiddenChangeHandler(columnConfigStore);
            grid.getColumnModel().addColumnWidthChangeHandler(columnConfigStore);
            if (grid.isColumnReordering()) {
                grid.getColumnModel().addColumnMoveHandler(columnConfigStore);
            }
        }
        wrapper.forceLayout();

        // TODO фикс на обновление заголовка если он строится не видимым
        if (headerRefreshRegistration != null) {
            headerRefreshRegistration.removeHandler();
        }
        headerRefreshRegistration = grid.getView().getHeader().addHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                    @Override
                    public void execute() {
                        grid.getView().getHeader().refresh();
                        // перестраиваем заголовок и удаляем handler
                        headerRefreshRegistration.removeHandler();
                    }
                });
            }
        }, MouseOverEvent.getType());

        if (!skipLoad) {
            loading = false;
        }
    }

    /**
     * Загрузка грида.
     */
    @JsIgnore
    public void load() {
        load(reloadMetadata);
    }

    /**
     * Загрузка грида с параметрами.
     *
     * @param parameters - List< DataValue >
     */
    @JsIgnore
    public void load(List<DataValue> parameters) {
        load(reloadMetadata, parameters);
    }

    /**
     * Загрузка грида.
     *
     * @param reconfigure - boolean
     */
    public void load(@JsOptional Boolean reconfigure) {
        load(reconfigure, Collections.emptyList());
    }

    /**
     * Загрузка грида с параметрами.
     *
     * @param reconfigure - boolean
     * @param parameters  - List< DataValue >
     */
    @JsIgnore
    public void load(boolean reconfigure, final List<DataValue> parameters) {
        loading = true;
        if (showLoadMask) {
            grid.mask(AppMessage.Util.MESSAGE.loadingMask());
        }
        if (reconfigure) {
            reloadMetadata(parameters);
        } else {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    store.getLoader().load(getLoadConfig(parameters));
                }
            });
        }
    }

    /**
     * Получить загруженную конфигурацию грида.
     *
     * @return
     */
    @JsIgnore
    public ClassLoadConfig getLoadConfig(List<DataValue> parameters) {
        ClassLoadConfig loadConfig = new ClassLoadConfig();
        loadConfig.setAll(loadAll);

        // Получение значений параметров, добавление их в ClassLoadConfig
        loadConfig.setParameters(paramHelper.getValues(parameters));

        loadConfig.setWhereSql(whereSql);
        if (paginator != null) {
            // loadConfig.setPage(paginator.getPage());
            loadConfig.setPageNum(paginator.getPage());
            loadConfig.setRowsPerPage(paginator.getRows());
        }
        if (filterPanel != null) {
            filterPanel.syncToLoadConfig(loadConfig);
        }
        if (sortPanel != null) {
            loadConfig.addSortAll(sortPanel.getSort());
        }
        return loadConfig;
    }

    /**
     * Инициализация панели инструментов грида.
     *
     * @param metadata - ClassMetadata
     */
    private void initToolbar(final ClassMetadata metadata) {
        if (toolbar != null) {
            toolbar.removeFromParent();
        }
        toolbar = new EditGridToolBar(metadata, hideButtonsGroup, showButtonsData, showButtonsFind, showButtonsExport,
                showButtonsProcess, showButtonsRefresh, this, paramHelper, getId());
        toolbar.addViewHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                for (RowModelData m : grid.getSelectionModel().getSelectedItems()) {
                    final FieldFormWindow fieldForm = openFieldPanel(m, true, "view-" + m.getId());
                    fieldForm.setHeading(AppMessage.Util.MESSAGE.viewRecord() + metadata.getTitle());
                }
            }
        });
        toolbar.addInsertHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final FieldFormWindow fieldForm = openFieldPanel(null, false, "insert");
                final Command insertCommand = new Command() {
                    @Override
                    public void execute() {
                        if (fieldForm.checkUpload()) {
                            insertRecord(fieldForm, toRowModelData(fieldForm, new RowModelDataImpl(), false));
                        }
                    }
                };
                fieldForm.addSaveHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        if (!fieldForm.isValid()) {
                            return;
                        }
                        showConfirmation(AppMessage.Util.MESSAGE.confirmAsk(), new SelectHandler() {
                            @Override
                            public void onSelect(SelectEvent event) {
                                fieldForm.setUploadCommand(insertCommand);
                                if (!haveFile) {
                                    insertCommand.execute();
                                }
                            }
                        }, "insert");
                    }
                });
                fieldForm.setHeading(AppMessage.Util.MESSAGE.insertRecord() + metadata.getTitle());
            }
        });
        toolbar.addUpdateHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                for (final RowModelData m : grid.getSelectionModel().getSelectedItems()) {
                    if (!m.isEditable()) {
                        InfoHelper.info("update-" + getId(), AppMessage.Util.MESSAGE.info(),
                                AppMessage.Util.MESSAGE.infoNotEditable());
                        continue;
                    }
                    final FieldFormWindow fieldForm = openFieldPanel(m, false, "update-" + m.getId());
                    final Command updateCommand = new Command() {
                        @Override
                        public void execute() {
                            if (fieldForm.checkUpload()) {
                                updateRecord(fieldForm, toRowModelData(fieldForm, m, true));
                            }
                        }
                    };
                    fieldForm.addSaveHandler(new SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            if (!fieldForm.isValid()) {
                                return;
                            }
                            showConfirmation(AppMessage.Util.MESSAGE.confirmAsk(), new SelectHandler() {
                                @Override
                                public void onSelect(SelectEvent event) {
                                    fieldForm.setUploadCommand(updateCommand);
                                    if (!haveFile) {
                                        updateCommand.execute();
                                    }
                                }
                            }, "update-" + m.getId());
                        }
                    });
                    fieldForm.setHeading(AppMessage.Util.MESSAGE.editRecord() + metadata.getTitle());
                }
            }

        });
        toolbar.addCopyHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                for (final RowModelData m : grid.getSelectionModel().getSelectedItems()) {
                    final FieldFormWindow fieldForm = openFieldPanel(m, false, "copy-" + m.getId());
                    final Command copyCommand = new Command() {
                        @Override
                        public void execute() {
                            if (fieldForm.checkUpload()) {
                                insertRecord(fieldForm, toRowModelData(fieldForm, new RowModelDataImpl(), false));
                            }
                        }
                    };
                    fieldForm.addSaveHandler(new SelectHandler() {
                        @Override
                        public void onSelect(SelectEvent event) {
                            if (!fieldForm.isValid()) {
                                return;
                            }
                            showConfirmation(AppMessage.Util.MESSAGE.confirmAsk(), new SelectHandler() {
                                @Override
                                public void onSelect(SelectEvent event) {
                                    fieldForm.setUploadCommand(copyCommand);
                                    if (!haveFile) {
                                        copyCommand.execute();
                                    }
                                }
                            }, "copy-" + m.getId());
                        }
                    });
                    fieldForm.setHeading(AppMessage.Util.MESSAGE.copyRecord() + metadata.getTitle());
                }
            }
        });
        toolbar.addDeleteHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                // удаление записей
                showConfirmation(AppMessage.Util.MESSAGE.confirmDelete(), new SelectHandler() {
                    @Override
                    public void onSelect(com.sencha.gxt.widget.core.client.event.SelectEvent event) {
                        List<RowModelData> models = new ArrayList<RowModelData>();
                        for (RowModelData m : grid.getSelectionModel().getSelectedItems()) {
                            if (!m.isDeletable()) {
                                InfoHelper.info("delete-" + getId(), AppMessage.Util.MESSAGE.info(),
                                        AppMessage.Util.MESSAGE.infoNotDeletable());
                                continue;
                            }
                            models.add(m);
                        }
                        deleteRecords(models);
                    }
                }, "delete");
            }
        });
        toolbar.addSearchHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                openFilter();
            }
        });
        toolbar.addRefreshHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                load();
            }
        });
        toolbar.addSortHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                sortPanel.show(sortStateStore.restore(getId() + "/sort"));
            }
        });
        toolbar.setEnabled(isEnabled());
        wrapper.insert(toolbar, 0, new VerticalLayoutData(1, -1));
    }

    /**
     * Отобразить конфигурацию грида.
     *
     * @param confirmText - String
     * @param handler     - SelectHandler
     */
    @SuppressWarnings("unchecked")
    private void showConfirmation(String confirmText, SelectHandler handler, String idSuffix) {
        final String gridCode = getCode();
        boolean useId = (gridCode == null || "".equals(gridCode) || ComponentBuilder.DEFAULT_CODE.equals(gridCode));
        final String id = ((useId) ? getId() : gridCode) + "-" + idSuffix;
        Dialog dialog = DialogManager.createDialog(id, AppMessage.Util.MESSAGE.confirm(), confirmText,
                new Pair<PredefinedButton, SelectHandler>(PredefinedButton.YES, handler),
                new Pair<PredefinedButton, SelectHandler>(PredefinedButton.NO, null));
        dialog.show();
    }

    /**
     * Инициализация фильтра грида.
     *
     */
    private void initFilter(ClassMetadata meta) {
        if ((filterPanel == null || reloadMetadata) && showButtonsFind) {
            filterPanel = new FilterPanel(metadata);

            // filterPanel.setFilters(reloadMetadata ?
            // Collections.<FilterValue>emptyList() : loadFilters(meta));
            List<FilterValue> values;
            if (reloadMetadata) {
                if (filterStateStore == null) {
                    filterStateStore = new FilterClientStateStore(StateScope.LOCAL, meta);
                }
                values = Collections.emptyList();
                filterStateStore.setMetadata(metadata);
            } else {
                values = loadFilters(meta);
            }
            filterPanel.setFilters(values);

            filterPanel.addSearchHandler(new SearchEvent.SearchHandler() {

                @Override
                public void onSearch(SearchEvent event) {
                    ClassLoadConfig config = getLoadConfig(Collections.emptyList());
                    filterStateStore.save(getId() + "/filter", new ArrayList<FilterValue>(config.getFilters()));
                    load(false);
                }
            });
        }
    }

    private List<FilterValue> loadFilters(ClassMetadata meta) {
        if (filterStateStore == null) {
            filterStateStore = new FilterClientStateStore(StateScope.LOCAL, meta);
        } else {
            filterStateStore.setMetadata(meta);
        }
        List<FilterValue> restored = filterStateStore.restore(getId() + "/filter");
        if (restored == null) {
            return Collections.emptyList();
        }
        return restored;
    }

    /**
     * Инициализация панели сортировки в гриде.
     *
     */
    private void initSortPanel(ClassMetadata meta) {
        if (sortPanel == null && showButtonsFind) {
            sortPanel = new SortPanel(metadata);
            sortPanel.fillSorts(loadSorts(meta));
            sortPanel.addSortHandler(new SortEvent.SortHandler() {
                @Override
                public void onSort(SortEvent event) {
                    ClassLoadConfig config = getLoadConfig(Collections.emptyList());
                    sortStateStore.save(getId() + "/sort", new ArrayList<SortValue>(config.getSorts()));
                    grid.getStore().clearSortInfo();
                    load();
                }
            });
        }
    }

    private List<SortValue> loadSorts(ClassMetadata meta) {
        if (sortStateStore == null) {
            sortStateStore = new SortClientStateStore(StateScope.LOCAL, meta);
        } else {
            sortStateStore.setMetadata(meta);
        }
        List<SortValue> restored = sortStateStore.restore(getId() + "/sort");
        if (restored == null) {
            return Collections.emptyList();
        }
        return restored;
    }

    /**
     * Открыть фильтр грида.
     */
    private void openFilter() {
        filterPanel.show();
    }

    // private FieldFormWindow openFieldPanel(RowModelData row, boolean view) {
    // return openFieldPanel(row, view, null);
    // }

    /**
     * Создает и открывает окно редактирования полей строки грида
     *
     * @param row  - RowModelData
     * @param view - признак "только для просмотра"
     * @return FieldFormWindow
     */
    private FieldFormWindow openFieldPanel(RowModelData row, boolean view, String componentId) {
        final FieldFormWindow result = new FieldFormWindow(metadata.getFields(), view, componentId);
        result.addHideHandler(new HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                activeFieldForms.remove(result);
            }
        });
        if (row != null) {
            for (FieldMetadata field : metadata.getFields()) {
                DataValue value = new DataValueImpl(field.getType());
                value.setValue(row.get(field.getName()));
                result.setValue(field, value);
                if (DataType.FILE == field.getType()) {
                    haveFile = true;
                }
            }
        }
        activeFieldForms.add(result);
        WindowManager.get().add(result);
        WindowManager.get().showWindow(result);
        return result;
    }

    /**
     * Перевод данных в строки для грида
     *
     * @param panel       - FieldFormWindow
     * @param model       - RowModelData
     * @param changedOnly - boolean
     * @return RowModelData
     */
    private RowModelData toRowModelData(FieldFormWindow panel, RowModelData model, boolean changedOnly) {
        for (FieldMetadata f : metadata.getFields()) {
            if (!f.isEdit() || (changedOnly && !panel.isChanged(f))) {
                continue;
            }
            DataValue value = panel.getValue(f);
            if (value != null) {
                model.set(f.getName(), value.getObject());
                // model.setChanged(f.getName());
                model.addChangedField(f.getName());
            }
        }
        return model;
    }

    /**
     * Спрятать окно редактирования грида.
     *
     * @param window - Window
     */
    private void hideEditWindow(Window window) {
        window.hide();
        if (window.getData("hideRegistration") != null) {
            HandlerRegistration registration = window.getData("hideRegistration");
            registration.removeHandler();
            window.setData("hideRegistration", null);
        }
    }

    /**
     * Вставить запись в грид.
     *
     * @param window - Window
     * @param model  - RowModelData
     */
    private void insertRecord(final Window window, final RowModelData model) {
        HandlerRegistration registration = action.addInsertHandler(new InsertEvent.InsertHandler() {

            @Override
            public void onInsert(InsertEvent event) {
                hideEditWindow(window);
                fireEvent(new InsertEvent());
                load();
            }
        });
        window.setData("hideRegistration", registration);
        action.insert(model, true);
        // store.add(0, model);
    }

    /**
     * Обновить запись в гриде.
     *
     * @param window - Window
     * @param model  - RowModelData
     */
    private void updateRecord(final Window window, final RowModelData model) {
        HandlerRegistration registration = action.addUpdateHandler(new UpdateEvent.UpdateHandler() {

            @Override
            public void onUpdate(UpdateEvent event) {
                hideEditWindow(window);
                // TODO: переделать события. Незачем каждый раз создавать
                // обработчик
            }
        });
        window.setData("hideRegistration", registration);
        action.update(model, true);
        // store.update(model);
    }

    /**
     * Удалить запись из грида.
     *
     */
    // private void deleteRecord(RowModelData model) {
    // action.delete(Collections.singletonList(model), true);
    // }
    private void deleteRecords(List<RowModelData> models) {
        action.delete(models, true);
    }

    /**
     * Инициализация разбивки на страницы в гриде.
     */
    private void initPaginator() {
        if (!showPagingToolbar) {
            return;
        }
        if (paginator != null) {
            paginator.removeFromParent();
        }
        paginator = new GridPagingToolBar();

        ChangeEvent.ChangeHandler handler = new ChangeEvent.ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                paginator.disable();
                ClassLoadConfig config = getLoadConfig(Collections.emptyList());
                pageStateStore.save(getId() + "/page", config.getPageConfig());
                load();
            }
        };
        paginator.addHandler(handler, ChangeEvent.getType());
        paginator.setEnabled(isEnabled());
        if (defaultRowsPerPage > 0) {
            paginator.setRowsPerPage(defaultRowsPerPage);
        }
        wrapper.add(paginator, new VerticalLayoutData(1, -1));
    }

    /**
     * Проверка на валидность грида.
     */
    @JsIgnore
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Проверка на валидность грида.
     *
     * @param invalidate - boolean
     */
    @Override
    public boolean isValid(boolean invalidate) {
        if (forceInvalidText != null && !forceInvalidText.equals(maxRowsMessage)) {
            return false;
        }
        if (maxRows > 0 && grid.getSelectionModel().getSelectedItems().size() > maxRows) {
            if (invalidate) {
                markInvalid(maxRowsMessage);
            }
            return false;
        }
        if (invalidate) {
            clearInvalid();

        }
        return true;
    }

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у грида.
     *
     * @return boolean
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Установка свойства "Обязателен для заполнения" для грида.
     *
     * @param required - boolean
     */
    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Очистка выделения в гриде.
     */
    @Override
    public void clear() {
        grid.getSelectionModel().deselectAll();
    }

    /**
     * Получение записей из грида.
     *
     * @return RowListValue
     */
    @JsIgnore
    @Override
    public RowListValue getFieldValue() {
        RowListValue result = new RowListValueImpl();
        result.setCode(getCode());
        RowModelData last = grid.getSelectionModel().getSelectedItem();
        RowValue lastRow = null;
        if (last != null) {
            lastRow = new RowValueImpl(last.getId());
            lastRow.setSelected(true);
            result.addRowValue(lastRow);
        }
        for (RowModelData m : grid.getSelectionModel().getSelectedItems()) {
            RowValue row = new RowValueImpl(m.getId());
            row.setChecked(true);
            if (lastRow != null && m == last) {
                lastRow.setChecked(true);
            } else {
                result.addRowValue(row);
            }
        }
        return result;
    }

    /**
     * Установка записей в грид.
     *
     * @param value - RowListValue
     */
    @JsIgnore
    @Override
    public void setFieldValue(RowListValue value) {
        if (value == null) {
            grid.getSelectionModel().deselectAll();
            return;
        }
        List<RowModelData> models = new ArrayList<RowModelData>();
        for (RowValue row : value.getRowList()) {
            RowModelData model = store.findModelWithKey(row.getId());
            if (model != null) {
                models.add(model);
            }
        }
        grid.getSelectionModel().setSelection(models);
    }

    /**
     * Возвращает список всех элементов.
     *
     * @return List
     */
    public List<RowModelData> getAllItems() {
        return store.getAll();
    }

    /**
     * Удаляет все элементы.
     */
    public void clearItems() {
        store.clear();
    }

    @JsIgnore
    public void removeItem(RowModelData model) {
        store.remove(model);
    }

    @JsIgnore
    public void addItem(RowModelData model) {
        store.add(model);
    }

    @JsIgnore
    public void insertItem(int index, RowModelData model) {
        store.add(index, model);
    }

    @JsIgnore
    public RowModelData getItemById(String id) {
        return store.findModelWithKey(id);
    }

    /**
     * Инициализация обработчика выбора в гриде.
     */
    @JsIgnore
    private void initSelectHandler() {
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<RowModelData>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<RowModelData> event) {
                saveState();
                fireEvent(new org.whirlplatform.component.client.event.SelectEvent());
            }
        });
    }

    /**
     * Устанавливает гриду сообщение о не валидности данных.
     *
     * @param msg - String
     */
    @Override
    public void markInvalid(String msg) {
        errorSupport.markInvalid(Collections.singletonList(new SimpleEditorError(msg)));
        forceInvalidText = msg;
    }

    /**
     * Очистка сообщения о не валидности в гриде.
     */
    @Override
    public void clearInvalid() {
        errorSupport.clearInvalid();
        forceInvalidText = null;
    }

    private void initPagingStateStore(ClassMetadata meta) {
        pageStateStore = new PagingClientStateStore(StateScope.LOCAL, meta);
        PageConfig config = pageStateStore.restore(getId() + "/page");
        if (config == null) {
            config = new PageConfig();
            config.setRows(0);
            config.setPage(1);
            if (defaultRowsPerPage > 0) {
                config.setRowsPerPage(defaultRowsPerPage);
            }

        }
        paginator.setConfig(config, false);
    }

    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;
    }

    @JsIgnore
    @Override
    public boolean isSaveState() {
        return saveState;
    }

    @JsIgnore
    public void setRestoreState(boolean restore) {
        this.restoreState = restore;

    }

    @JsIgnore
    @Override
    public void setStateScope(StateScope scope) {
        if (stateStore == null || (stateStore != null && scope != stateStore.getScope())) {
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

    @JsIgnore
    @Override
    public StateScope getStateScope() {
        return getStateStore().getScope();
    }

    @JsIgnore
    @Override
    public void saveState() {
        RowListValue v = getFieldValue();
        if (saveState && !Util.isEmptyString(getCode())) {
            getStateStore().put(getCode(), v);
        }
        getSelectionStore().save(getId() + "/select", v);
    }

    private StorageWrapper<RowListValue> getStateStore() {
        if (stateStore == null) {
            setStateScope(StateScope.MEMORY);
        }
        return stateStore;
    }

    protected StateStore<RowListValue> getSelectionStore() {
        if (selectionStateStore == null) {
            selectionStateStore = new SelectionClientStateStore<RowListValue>(StateScope.LOCAL, metadata);
        }
        return selectionStateStore;
    }

    protected void restoreSelectionState() {
        if (restoreState) {
            RowListValue v = getSelectionStore().restore(getId() + "/select");
            setFieldValue(v);
            scrollTo(grid.getSelectionModel().getSelectedItem());
        }
    }

    private void scrollTo(RowModelData model) {
        if (model != null) {
            grid.getView().ensureVisible(model);
        }
    }

    /**
     * Добавление обработчика загрузки грида
     *
     * @param handler - LoadHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addLoadHandler(org.whirlplatform.component.client.event.LoadEvent.LoadHandler handler) {
        return addHandler(handler, org.whirlplatform.component.client.event.LoadEvent.getType());
    }

    /**
     * Добавление обработчика выделение строки в гриде
     *
     * @param handler - SelectHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addSelectHandler(
            org.whirlplatform.component.client.event.SelectEvent.SelectHandler handler) {
        return addHandler(handler, org.whirlplatform.component.client.event.SelectEvent.getType());
    }

    /**
     * Добавление обработчика добавления строки в грид
     *
     * @param handler - InsertHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addInsertHandler(InsertEvent.InsertHandler handler) {
        return addHandler(handler, InsertEvent.getType());
    }

    /**
     * Добавление обработчика изменения строки в гриде
     *
     * @param handler - UpdateHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addUpdateHandler(UpdateEvent.UpdateHandler handler) {
        return addHandler(handler, UpdateEvent.getType());
    }

    /**
     * Добавление обработчика удаления строки из грида
     *
     * @param handler - DeleteHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addDeleteHandler(DeleteEvent.DeleteHandler handler) {
        return addHandler(handler, DeleteEvent.getType());
    }

    @JsIgnore
    @Override
    public HandlerRegistration addRowDoubleClickHandler(RowDoubleClickEvent.RowDoubleClickHandler handler) {
        return addHandler(handler, RowDoubleClickEvent.getType());
    }

    /**
     * Очищает фильтр грида.
     */
    public void clearFilter() {
        filterPanel.clearFilter();
        filterStateStore.save(getId() + "/filter", new ArrayList<FilterValue>());
    }

    @JsIgnore
    @Override
    public Widget getWrapper() {
        return wrapper;
    }

    public static class LocatorParams {
        public static String TYPE_COLUMN = "Column";
        public static String TYPE_HEADER = "Header";

        public static String TYPE_ROW = "Row";
        public static String TYPE_CHECK = "Check";
        public static String TYPE_CELL = "Cell";

        public static String PARAMETER_ID = "id";
        public static String PARAMETER_COLUMN_INDEX = "colIndex";
        public static String PARAMETER_COLUMN_NAME = "colName";
        public static String PARAMETER_INDEX = "index";
        public static String PARAMETER_LABEL = "label";

        public static String TYPE_PAGING_BAR = "PagingBar";
    }

    private Element getRowElementByLocator(Locator locator) {

        // поиск строчки и выбор элемента происходит независимо
        // строка находится по своим параметрам, а ячейка в этой строке
        // выбирается своя

        RowModelData row = null;
        if (locator.hasParameter(LocatorParams.PARAMETER_ID)) {
            // если есть id строки
            row = store.findModelWithKey(locator.getParameter(LocatorParams.PARAMETER_ID));
        } else if (locator.hasParameter(LocatorParams.PARAMETER_INDEX)) {
            // если есть порядковый номер строки
            row = store.get(Integer.parseInt(locator.getParameter(LocatorParams.PARAMETER_INDEX)));
        } else if (locator.hasParameter(LocatorParams.PARAMETER_LABEL)) {
            // если есть колонка и/или текст
            for (String s = "EditGridBuilder::getRowElementByLocator"; s == null; s = "") ;
            String colIndex = locator.getParameter(LocatorParams.PARAMETER_COLUMN_INDEX);
            String colName = locator.getParameter(LocatorParams.PARAMETER_COLUMN_NAME);
            String label = locator.getParameter(LocatorParams.PARAMETER_LABEL);

            boolean withColName = !Util.isEmptyString(colName);
            boolean withColIndex = !Util.isEmptyString(colIndex);

            List<RowModelData> rows = store.getAll();
            for (int r = 0; r < rows.size(); r++) {
                List<ColumnConfig<RowModelData, ?>> cols = grid.getColumnModel().getColumns();
                for (int c = 0; c < cols.size(); c++) {
                    Element element = grid.getView().getCell(r, c);
                    ColumnConfig<RowModelData, ?> colConfig = cols.get(c);
                    String colPath = colConfig.getPath();
                    if (label.equals(element.getInnerText())) {
                        if (withColName) {
                            if (colPath.equals(colName)) {
                                row = rows.get(r);
                                break;
                            }
                        } else if (withColIndex) {
                            if (colIndex.equals(String.valueOf(c))) {
                                row = rows.get(r);
                                break;
                            }
                        } else {
                            row = rows.get(r);
                            break;
                        }
                    }
                }
                if (row != null) {
                    break;
                }
            }
        }

        Locator innerPart = locator.getPart();
        if (innerPart == null) {
            return null;
        }
        if (row != null) {
            if (LocatorParams.TYPE_CHECK.equals(innerPart.getType())
                    && grid.getSelectionModel() instanceof CheckBoxSelectionModel) {
                // ищем чекбокс строки если у нас CheckBoxSelectionModel с чек
                // боксами и найдена строка
                CheckBoxSelectionModel<RowModelData> sm = (CheckBoxSelectionModel<RowModelData>) grid.getSelectionModel();
                // достаем элемент чекбокса по стилю из внешнего вида
                if (sm.getAppearance() instanceof CheckBoxColumnDefaultAppearance) {
                    return grid.getView().getRow(row).<XElement>cast().selectNode(
                            "." + ((CheckBoxColumnDefaultAppearance) sm.getAppearance()).getStyle().rowChecker()); //TODO
                }
            } else if (LocatorParams.TYPE_CELL.equals(innerPart.getType())) {
                if (innerPart.hasParameter(LocatorParams.PARAMETER_COLUMN_NAME)) { //в приоритете поиск ячейки по названию столбца.
                    // достаем елемент ячейки по колонке
                    String columnName = innerPart.getParameter(LocatorParams.PARAMETER_COLUMN_NAME);
                    return getCellByColumnName(row, columnName);
                } else if (innerPart.hasParameter(LocatorParams.PARAMETER_COLUMN_INDEX)) {
                    return grid.getView().getCell(store.indexOf(row),
                            Integer.parseInt(innerPart.getParameter(LocatorParams.PARAMETER_COLUMN_INDEX)));
                } else if (innerPart.hasParameter(LocatorParams.PARAMETER_LABEL)) { //по тексту ищем в последнюю очередь.
                    String label = innerPart.getParameter(LocatorParams.PARAMETER_LABEL);
                    if (!Util.isEmptyString(label)) {
                        for (String columnName : row.getPropertyNames()) {
                            DataValue dataValue = row.getValue(columnName);
                            if (DataType.STRING.equals(dataValue.getType())) {
                                String value = dataValue.getString();
                                if (label.equals(value)) {
                                    return getCellByColumnName(row, columnName);
                                }
                            } else if (DataType.NUMBER.equals(dataValue.getType())) {
                                String value = String.valueOf(dataValue.getDouble());
                                if (label.equals(value)) {
                                    return getCellByColumnName(row, columnName);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    @JsIgnore
    public Element getCellByColumnName(RowModelData row, String columnName) {
        return grid.getView().getCell(store.indexOf(row),
                grid.getColumnModel().indexOf(grid.getColumnModel().findColumnConfig(columnName)));
    }

    /*
     * private int rowIndexByRowId(String rowId) { RowModelData rmd =
     * grid.getStore().findModelWithKey(rowId); Element rowElement =
     * grid.getView().getRow(rmd); return
     * grid.getView().findRowIndex(rowElement); }
     *
     * private int findColIndexByColName(String searchColumnName) { if
     * (searchColumnName != null && !"".equals(searchColumnName)) { for (int
     * colIndex = 0; colIndex < grid.getColumnModel().getColumnCount(true);
     * colIndex++) { String columnName = getColumnName(colIndex); if
     * (searchColumnName.equalsIgnoreCase(columnName)) { return colIndex; } } }
     * return 0; }
     *
     * private String getColumnName(int colIndex) { return
     * grid.getColumnModel().getColumn(colIndex).getValueProvider().getPath(); }
     *
     * private Element findElementWithTextForColIndex(int colIndex, String
     * cellTextCandidate) { int countRows = paginator.getRows(); for (int
     * rowIndex = 0; rowIndex < countRows; rowIndex++) { Element cell =
     * grid.getView().getCell(rowIndex, colIndex); String cellText =
     * cell.getInnerText(); if (cellText.equalsIgnoreCase(cellTextCandidate)) {
     * return cell.getFirstChildElement() != null ? cell.getFirstChildElement()
     * : cell; } } return null; }
     */
    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (!fitsLocator(locator) || !locator.typeEquals(getType().getType())) {
            return null;
        }
        Locator part = locator.getPart();
        if (part == null) {
            return super.getElementByLocator(locator);
        }
        if (part.typeEquals(EditGridToolBar.LocatorParams.TYPE_TOOL_BAR) && toolbar != null) {
            return toolbar.getElementByLocator(part);
        }
        if (part.typeEquals(LocatorParams.TYPE_ROW)) {
            Locator subPart = part.getPart();
            if (subPart != null) {
                Element result = columnModel.getElementByLocator(subPart);
                if (result != null) {
                    return result;
                }
            }
            return getRowElementByLocator(part);
        }
        if (part.typeEquals(FilterPanel.LocatorParams.TYPE_FILTER_PANEL) && filterPanel != null) {
            return filterPanel.getElementByLocator(part);
        }
        if (activeFieldForms.size() > 0) {
            for (FieldFormWindow fieldForm : activeFieldForms) {
                Element result = fieldForm.getElementByLocator(part);
                if (result != null) {
                    return result;
                }
            }
        }
        if (part.typeEquals(GridPagingToolBar.LocatorParams.TYPE_PAGINATOR) && paginator != null) {
            return paginator.getElementByLocator(part);
        }
        if (part.typeEquals(SortPanel.LocatorParams.TYPE_SORT_PANEL) && sortPanel != null) {
            return sortPanel.getElementByLocator(part);
        }
        return null;
    }

    public boolean isLoading() {
        return loading;
    }

    private String getColNameByColInd(int colInd) {
        return grid.getColumnModel().getColumn(colInd).getPath();
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator locator = super.getLocatorByElement(element);
        if (locator != null) {
            // Если локатор != null значит элемент в отображаемой части грида.
            // Элемент тулбар или его часть.
            if (toolbar != null && toolbar.getElement().isOrHasChild(element)) {
                locator.setPart(toolbar.getLocatorByElement(element));
                return locator;
            }
            // Элемент в "теле" грида
            if (grid.getElement().isOrHasChild(element)) {
                Locator part = new Locator(LocatorParams.TYPE_ROW);
                GridView<RowModelData> gridView = grid.getView();
                if (editing != null && editing.isEditing()) {
                    GridCell active = ((GridInlineEditing<RowModelData>) editing).getActiveCell();
                    fillOutRowLocatorParameters(part, element, active);
                    Locator cellEditingLocator = columnModel.getLocatorByElement(element);
                    part.setPart(cellEditingLocator);
                } else {
                    int rowInd = gridView.findRowIndex(element);
                    int colInd = gridView.findCellIndex(element, null);
                    fillOutRowLocatorParameters(part, element, new GridCell(rowInd, colInd));
                    CheckBoxSelectionModel<RowModelData> sm = (CheckBoxSelectionModel<RowModelData>) grid
                            .getSelectionModel();
                    CheckBoxColumnDefaultAppearance appearance = ((CheckBoxColumnDefaultAppearance) sm.getAppearance());
                    Locator endPointPart = null;
                    if (appearance.isRowChecker(element.cast())) {
                        endPointPart = new Locator(LocatorParams.TYPE_CHECK);
                    } else {
                        endPointPart = new Locator(LocatorParams.TYPE_CELL);
                        String colIndex = String.valueOf(colInd);
                        endPointPart.setParameter(LocatorParams.PARAMETER_COLUMN_INDEX, colIndex);
                        String colName = getColNameByColInd(colInd);
                        endPointPart.setParameter(LocatorParams.PARAMETER_COLUMN_NAME, colName);

                        String text = element.getInnerText();
                        if (!Util.isEmptyString(text)) {
                            endPointPart.setParameter(LocatorParams.PARAMETER_LABEL, text);
                        }
                    }
                    part.setPart(endPointPart);
                }
                locator.setPart(part);
                return locator;
            }
            // Проверим отображаемую (которую видно под гридом) часть
            // пагинатора.
            // Все кроме элементов селектора.
            if (paginator != null) {
                boolean isChild = paginator.getElement().isOrHasChild(element);
                if (isChild) {
                    locator.setPart(paginator.getLocatorByElement(element));
                    return locator;
                }
            }
            return locator;
        }
        /*
         * Проверяем принадлежность элемента к окну в котором filterPanel
         * отображает себя или к элементам списков комбо-боксов.
         */
        if (filterPanel != null) {
            boolean isWindowChild = filterPanel.isWindowElement(element);
            boolean isComboBoxItem = (!isWindowChild) && filterPanel.isComboBoxItemElement(element);
            if (isWindowChild || isComboBoxItem) {
                Locator gridLocator = new Locator(ComponentType.EditGridType);
                fillLocatorDefaults(gridLocator, element);
                Locator filterPanelPart = null;
                filterPanelPart = filterPanel.getLocatorByElement(element);
                gridLocator.setPart(filterPanelPart);
                return gridLocator;
            }
        }
        /*
         * Проверяем панель сортировки.
         */
        if (sortPanel != null && sortPanel.getElement().isOrHasChild(element)) {
            Locator gridLocator = new Locator(ComponentType.EditGridType);
            fillLocatorDefaults(gridLocator, element);
            Locator sortPanelPart = sortPanel.getLocatorByElement(element);
            gridLocator.setPart(sortPanelPart);
            return gridLocator;
        }
        /*
         * Если есть формы редактирования полей грида
         */
        for (FieldFormWindow fieldForm : activeFieldForms) {
            Locator part = fieldForm.getLocatorByElement(element);
            if (part != null) {
                Locator gridLocator = new Locator(ComponentType.EditGridType);
                fillLocatorDefaults(gridLocator, element);
                gridLocator.setPart(part);
                return gridLocator;
            }
        }
        /*
         * Проверим принадлежность элемента к элементам селектора пагинатора.
         */
        if (paginator != null) {
            boolean isSelectorItem = paginator.isSelectorItem(element);
            if (isSelectorItem) {
                Locator gridLocator = new Locator(ComponentType.EditGridType);
                fillLocatorDefaults(gridLocator, element);
                gridLocator.setPart(paginator.getLocatorByElement(element));
                return gridLocator;
            }
        }
        /*
         * Возможно это элемент списка инлайн комбобокса
         */
        Locator cellEditingLocator = columnModel.getLocatorByElement(element);
        if (cellEditingLocator != null) {
            Locator rowLocator = new Locator(LocatorParams.TYPE_ROW);
            GridCell active = ((GridInlineEditing<RowModelData>) editing).getActiveCell();
            fillOutRowLocatorParameters(rowLocator, element, active);
            rowLocator.setPart(cellEditingLocator);
            Locator gridLocator = new Locator(ComponentType.EditGridType);
            fillLocatorDefaults(gridLocator, element);
            gridLocator.setPart(rowLocator);
            return gridLocator;
        }
        /*
         * Ничего не найдено, увы!
         */
        return null;
    }

    protected void fillOutRowLocatorParameters(Locator rowLocator, Element element, GridCell gridCell) {
        int rowInd = gridCell.getRow();
        int colInd = gridCell.getCol();
        RowModelData rowMd = grid.getStore().get(rowInd);
        if (rowMd != null) {
            rowLocator.setParameter(LocatorParams.PARAMETER_ID, rowMd.getId());
        }
        if (rowInd != -1) {
            rowLocator.setParameter(LocatorParams.PARAMETER_INDEX, String.valueOf(rowInd));
        }
        if (colInd != -1) {
            //rowLocator.setParameter(LocatorParams.PARAMETER_COLUMN, String.valueOf(colInd));
            String colName = getColNameByColInd(colInd);
            rowLocator.setParameter(LocatorParams.PARAMETER_COLUMN_NAME, colName);
            String colIndex = String.valueOf(colInd);
            rowLocator.setParameter(LocatorParams.PARAMETER_COLUMN_INDEX, colIndex);
        }
        String text = element.getInnerText();
        if (!Util.isEmptyString(text)) {
            rowLocator.setParameter(LocatorParams.PARAMETER_LABEL, text);
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
}
