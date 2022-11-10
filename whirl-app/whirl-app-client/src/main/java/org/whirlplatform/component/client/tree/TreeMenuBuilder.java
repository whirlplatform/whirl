package org.whirlplatform.component.client.tree;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.EventManager;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.ext.XTree;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.*;
import java.util.Map.Entry;

/**
 * Древовидное меню
 */
@JsType(name = "TreeMenu", namespace = "Whirl")
public class TreeMenuBuilder extends TreeBuilder implements ClickEvent.HasClickHandlers, Containable {

    // TODO переписать, выкинуть наследование от TreeBuilder

    private String eventColumn;
    private XTree<ListModelData, String> tree;
    private HandlerRegistration registration;
    private List<ComponentBuilder> children;
    private Map<ComponentBuilder, ListModelData> builderMap;
    private IconProvider<ListModelData> iconProvider;

    @JsConstructor
    public TreeMenuBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public TreeMenuBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    public ComponentType getType() {
        return ComponentType.TreeMenuType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        children = new ArrayList<ComponentBuilder>();
        builderMap = new HashMap<ComponentBuilder, ListModelData>();
        iconProvider = new IconProvider<ListModelData>() {
            @Override
            public ImageResource getIcon(ListModelData model) {
                // для потомков, добавленных вручную (MenuItemBuilder), можно
                // выставить иконку
                if (model.getId().startsWith("temp")) {
                    String image = model.get("image");
                    if (image != null && BuilderManager.getApplicationData() != null) {
                        String role = BuilderManager.getApplicationData().getApplicationCode();
                        SafeUri s = getImageUrl(role, image);
                        return IconHelper.getImageResource(s, 16, 16);
                    }
                }
                return null;
            }
        };

        return super.init(builderProperties);
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        boolean result = super.setProperty(name, value);
        if (name.equalsIgnoreCase(PropertyType.EventColumn.getCode())) {
            if (value != null) {
                eventColumn = value.getString();
                return true;
            }
        }
        return result;
    }

    /**
     * @param child
     */
    @Override
    public void addChild(ComponentBuilder child) {
        if (child instanceof HorizontalMenuItemBuilder) {
            ListModelData root = getModelData((HorizontalMenuItemBuilder) child);

            children.add(child);
            builderMap.put(child, root);
            // buildTree((MenuItemBuilder) child);
            // loadLocalData();
            // TODO child.setParentBuilder(???)
        }
    }

    /**
     * @param child
     */
    @Override
    public void removeChild(ComponentBuilder child) {
        builderMap.remove(child);
        children.remove(child);
    }

    @JsIgnore
    @Override
    public void clearContainer() {
        // TODO Auto-generated method stub
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    @JsIgnore
    @Override
    public void forceLayout() {
        // TODO Auto-generated method stub
    }

    @Override
    public ComponentBuilder[] getChildren() {
        return children.toArray(new ComponentBuilder[children.size()]);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @JsIgnore
    @Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }

    @JsIgnore
    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        registration = addHandler(handler, SelectEvent.getType());
        return registration;
    }

    protected TreeLoader<ListModelData> initLoader(final TreeStore<ListModelData> store) {
        RpcProxy<ListModelData, List<ListModelData>> proxy = createProxy();

        TreeLoader<ListModelData> loader = new TreeLoader<ListModelData>(proxy) {
            @Override
            public boolean hasChildren(ListModelData parent) {
                // если DataSource не установлен, то используются только
                // локальные данные (MenuItemBuilder)
                if (getClassMetadata().getClassId() != null && parent.getProperties().containsKey(isLeafColumn)) {
                    return Boolean.valueOf(parent.<Boolean>get(isLeafColumn));
                } else {
                    HorizontalMenuItemBuilder cb = (HorizontalMenuItemBuilder) findBuilder(parent);
                    return cb != null && cb.getChildren().length > 0;
                }
            }

            @Override
            protected void onLoadSuccess(ListModelData loadConfig, List<ListModelData> result) {
                super.onLoadSuccess(loadConfig, result);

                loadLocalData();
                tree.unmask();
                restoreState(result);
            }

            @Override
            protected void onLoadFailure(ListModelData loadConfig, Throwable t) {
                super.onLoadFailure(loadConfig, t);

                loadLocalData();
                tree.unmask();
            }

            @Override
            protected void loadData(ListModelData config) {
                if (builderMap.values().contains(config)) {
                    // добавляем в результат loader-а локальные данные
                    // (MenuItemBuilder)
                    ComponentBuilder builder = findBuilder(config);
                    List<ListModelData> result = new ArrayList<ListModelData>();
                    if (builder != null) {
                        for (ComponentBuilder c : ((HorizontalMenuItemBuilder) builder).getChildren()) {
                            ListModelData m = builderMap.get(c);
                            if (m == null) {
                                m = getModelData((HorizontalMenuItemBuilder) c);
                                builderMap.put(c, m);
                            }
                            result.add(m);
                        }
                    }
                    onLoadSuccess(config, result);
                } else {
                    super.loadData(config);
                }
            }
        };
        loader.addLoadHandler(new ChildTreeStoreBinding<ListModelData>(store));
        return loader;
    }

    @Override
    protected XTree<ListModelData, String> initTree(TreeLoader<ListModelData> loader) {
        tree = super.initTree(loader);

        // Чтобы событие вызывалось не только при смене выбранного элемента, но
        // и при клике
        tree.setSelectionModel(new TreeSelectionModel<ListModelData>() {
            @Override
            protected void onMouseClick(com.google.gwt.event.dom.client.ClickEvent ce) {
                fireSelectionChangeOnClick = true;
                super.onMouseClick(ce);
            }
        });

        tree.setIconProvider(iconProvider);

        if (checkChangedHandler != null) {
            checkChangedHandler.removeHandler();
        }
        checkChangedHandler = tree.addCheckChangedHandler(new CheckChangedHandler<ListModelData>() {
            @Override
            public void onCheckChanged(CheckChangedEvent<ListModelData> event) {
                tree.fireEvent(new SelectEvent());
            }
        });

        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ListModelData>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<ListModelData> event) {
                for (Entry<ComponentBuilder, ListModelData> entry : builderMap.entrySet()) {
                    if (event.getSelection().size() > 0 && entry.getValue() == event.getSelection().get(0)) {
                        entry.getKey().fireEvent(new ClickEvent());
                        break;
                    }
                }
            }
        });

        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        SimpleSafeHtmlCell<String> cell = new SimpleSafeHtmlCell<String>(SimpleSafeHtmlRenderer.getInstance(),
                "click") {
            @Override
            public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
                                       ValueUpdater<String> valueUpdater) {
                super.onBrowserEvent(context, parent, value, event, valueUpdater);
                if (eventColumn != null && "click".equals(event.getType())) {
                    if (registration != null) {
                        registration.removeHandler();
                    }

                    ListModelData model = getStore().getChild(context.getIndex());
                    String eventCode = model.get(eventColumn);

                    ComponentBuilder cb = findBuilder(model);

                    if (cb != null) {
                        cb.fireEvent(new ClickEvent());
                    } else {
                        loadEvent(eventCode);
                    }
                }
            }
        };
        tree.setCell(cell);

        Comparator<RowModelData> comp = new Comparator<RowModelData>() {

            // У записей из базы индекс null, они идут после добавленных вручную
            @Override
            public int compare(RowModelData o1, RowModelData o2) {
                Object value1 = o1.get(PropertyType.LayoutDataIndex.getCode());
                Object value2 = o2.get(PropertyType.LayoutDataIndex.getCode());
                if (value1 == null) {
                    return 1;
                } else if (value2 == null) {
                    return -1;
                } else {
                    return ((Double) value1).intValue() - ((Double) value2).intValue();
                }
            }
        };
        tree.getStore().addSortInfo(new StoreSortInfo(comp, SortDir.ASC));
        return tree;
    }

    @Override
    protected ClassMetadata getClassMetadata() {
        ClassMetadata metadata = super.getClassMetadata();

        if (eventColumn != null) {
            metadata.addField(new FieldMetadata(eventColumn, DataType.STRING, null));
        }
        return metadata;
    }

    private ComponentBuilder findBuilder(ListModelData model) {
        ComponentBuilder builder = null;
        for (Entry<ComponentBuilder, ListModelData> cb : builderMap.entrySet()) {
            if (cb.getValue().equals(model)) {
                builder = cb.getKey();
                break;
            }
        }
        return builder;
    }

    private SafeUri getImageUrl(String role, String image) {
        StringBuilder url = new StringBuilder();
        url.append(GWT.getHostPageBaseURL());
        url.append("resource?type=download&data=image&code=");
        url.append(role);
        url.append("&fileName=");
        url.append(image);
        return UriUtils.fromString(url.toString());
    }

    private ListModelData getModelData(HorizontalMenuItemBuilder cb) {
        ListModelData model = new ListModelDataImpl();
        model.setId("temp" + Random.nextInt());
        // if (nameExpression == null) {
        // nameExpression = "labelColumn";
        // }
        model.set(labelExpression, cb.getTitle());
        model.set("image", cb.getImage());
        model.set(PropertyType.LayoutDataIndex.getCode(), cb.getIndexPosition());
        return model;
    }

    private void buildTree(HorizontalMenuItemBuilder item) {
        ListModelData root = builderMap.get(item);
        for (ComponentBuilder cb : item.getChildren()) {
            ListModelData model = getModelData((HorizontalMenuItemBuilder) cb);
            builderMap.put(cb, model);
            store.add(root, model);

            if (((HorizontalMenuItemBuilder) cb).getChildren().length > 0) {
                buildTree((HorizontalMenuItemBuilder) cb);
            }
        }
    }

    private void loadEvent(String eventCode) {
        AsyncCallback<EventMetadata> callback = new AsyncCallback<EventMetadata>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("get-event", caught);
            }

            @Override
            public void onSuccess(EventMetadata result) {
                EventManager.Util.get().addMenuTreeEvent(TreeMenuBuilder.this, result);
                fireEvent(new SelectEvent());
            }
        };
        DataServiceAsync.Util.getDataService(callback).getEvent(SessionToken.get(), eventCode);
    }

    private void loadLocalData() {
        for (int i = 0; i < children.size(); i++) {
            ComponentBuilder cb = children.get(i);
            ListModelData model = builderMap.get(cb);
            if (store.findModel(model) == null) {
                store.insert(i, model);
                buildTree((HorizontalMenuItemBuilder) cb);
            }
        }
    }

    /**
     *
     */
    @JsIgnore //todo смотреть что это
    public void showLocal() {
        loadLocalData();
        tree.unmask();
    }

    @JsIgnore
    public Tree<ListModelData, String> getTree() {
        return tree;
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
     * @param enabled true - для включения компонента,
     *                false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * Очищает значение поля.
     */
    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
     */
    @Override
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
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

    @JsIgnore
    @Override
    public TreeStore<ListModelData> getStore() {
        return super.getStore();
    }

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Очищает статус не валидности для поля.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Загружает данные, используя текущую конфигурацию
     */
    public void load() {
        super.load();
    }

    /**
     * Проверяет необходимость сохранения состояния дерева в БД.
     *
     * @return true, если состояние нужно сохранить
     */
    @JsIgnore
    @Override
    public boolean isSaveState() {
        return super.isSaveState();
    }

    /**
     * Устанавливает, необходимо ли сохранять состояние дерева в БД.
     *
     * @param save true, если состояние нужно сохранить
     */
    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        super.setSaveState(save);
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
     * Очищает фильтр лейбла
     */
    @JsIgnore //todo посмотреть что это
    @Override
    public void clearLabelFilter() {
        super.clearLabelFilter();
    }
}
