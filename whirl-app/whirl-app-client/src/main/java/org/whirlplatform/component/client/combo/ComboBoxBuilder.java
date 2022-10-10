package org.whirlplatform.component.client.combo;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent.LoadExceptionHandler;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.*;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.ListClassProxy;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsType(name = "ComboBox", namespace = "Whirl")
public class ComboBoxBuilder<T extends ComboBox<ListModelData>> extends AbstractFieldBuilder implements Editable,
        NativeParameter<ListModelData>, Parameter<DataValue>, SelectEvent.HasSelectHandlers, ChangeEvent.HasChangeHandlers, HasState {

    protected static final String SEARCH_QUERY = "SEARCH_QUERY";
    protected int minChars = 2;
    protected int delayTimeMs;
    protected String classId;
    protected String whereSql;
    protected T comboBox;
    protected ClassStore<ListModelData, ClassLoadConfig> store;
    protected ParameterHelper paramHelper;
    protected LabelProvider<ListModelData> labelProvider;
    protected StorageWrapper<DataValue> stateStore;
    protected StateStore<DataValue> selectionStateStore;
    protected boolean saveState;
    protected boolean restoreState;
    protected boolean loadAll;
    protected boolean useSearchParameters;
    protected boolean reloadMetadata;
    protected String labelExpression;
    private ClassMetadata metadata;
    // private boolean required = false;
    private boolean editable;

    @JsConstructor
    public ComboBoxBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public ComboBoxBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.ComboBoxType;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        minChars = 2;
        delayTimeMs = 1200;
        editable = true;

        initParamHelper();
        initLabelProvider();

        comboBox = (T) new ComboBox<ListModelData>(store, labelProvider);
        comboBox.setMinChars(minChars);
        comboBox.setForceSelection(true);
        comboBox.setQueryDelay(delayTimeMs);
        comboBox.setTriggerAction(TriggerAction.ALL);
        comboBox.getListView().setSelectOnOver(false);
        // comboBox.getListView().setLoadingText(AppMessage.Util.MESSAGE.search());
        comboBox.setLoadingIndicator(AppMessage.Util.MESSAGE.search());
        return comboBox;
    }

    @JsIgnore
    protected void initParamHelper() {
        paramHelper = new ParameterHelper();
    }

    protected void initLabelProvider() {
        labelProvider = new LabelProvider<ListModelData>() {

            @Override
            public String getLabel(ListModelData item) {
                return item.getLabel() == null ? "" : item.getLabel();
            }

        };
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.LabelColumn.getCode()) && value != null) {
            labelExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.HideTrigger.getCode())) {
            if (value != null && value.getBoolean() != null) {
                comboBox.setHideTrigger(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            if (value == null || Util.isEmptyString(value.getString())) {
                comboBox.setValue(null);
            } else {
                ListModelData model = comboBox.getValue();
                if (model == null) {
                    model = new ListModelDataImpl();
                }
                model.setId(value.getString());
                comboBox.setValue(model);
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.DisplayValue.getCode())) {
            if (value != null && !Util.isEmptyString(value.getString()) && comboBox.getValue() != null) {
                ListModelData model = comboBox.getValue();
                if (model == null) {
                    model = new ListModelDataImpl();
                }
                model.setLabel(value.getString());
                comboBox.setValue(model);
                comboBox.redraw();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.DataSource.getCode())) {
            if (value != null) {
                classId = value.getListModelData().getId();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Editable.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setEditable(value.getBoolean());
            }
            return true;
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
        } else if (name.equalsIgnoreCase(PropertyType.Cleanable.getCode())) {
            if (value != null && value.getBoolean() != null && value.getBoolean()) {
                super.setProperty(name, value);
                setClearCrossRightOffset(18);
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.LoadAll.getCode())) {
            if (value != null && value.getBoolean() != null) {
                loadAll = value.getBoolean();
            }
        } else if (name.equalsIgnoreCase(PropertyType.SaveState.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setSaveState(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.RestoreState.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setRestoreState(value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.StateScope.getCode())) {
            if (value != null && value.getString() != null) {
                StateScope scope = StateScope.valueOf(value.getString());
                setStateScope(scope);
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.UseSearchParameters.getCode())) {
            if (value != null && value.getBoolean() != null) {
                useSearchParameters = value.getBoolean();
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.MinChars.getCode())) {
            if (value != null && value.getInteger() != null) {
                minChars = value.getInteger();
                comboBox.setMinChars(minChars);
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.ReloadStructure.getCode())) {
            if (value != null && value.getBoolean() != null) {
                reloadMetadata = value.getBoolean();
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    @JsIgnore
    @Override
    public Component create() {
        Component comp = super.create();
        createStore();
        bindStore();
        addListener();
        comboBox.getListView().setLoader(store.getLoader());

        restoreSelectionState();
        return comp;
    }

    private void createStore() {
        metadata = new ClassMetadata(getClassList());
        store = new ClassStore<ListModelData, ClassLoadConfig>(metadata, new ListClassProxy(metadata));
    }

    protected void bindStore() {
        comboBox.setStore(store);
    }

    protected ClassStore<ListModelData, ClassLoadConfig> getStore() {
        return store;
    }

    protected void addListener() {
        comboBox.addSelectionHandler(new SelectionHandler<ListModelData>() {

            @Override
            public void onSelection(SelectionEvent<ListModelData> event) {
                comboBox.setValue(event.getSelectedItem(), true);

                fireEvent(new SelectEvent());
            }

        });

        comboBox.addTriggerClickHandler(new TriggerClickHandler() {

            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                if (!comboBox.isExpanded()) {
                    // Т.к. если store не пустой, comboBox при загрузке
                    // отрисовывается неправильно
                    store.clear();
                    comboBox.expand();
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            load();
                            comboBox.setExpanded(true);
                        }
                    });
                }
            }

        });

        comboBox.addBeforeQueryHandler(new BeforeQueryHandler<ListModelData>() {
            @Override
            public void onBeforeQuery(BeforeQueryEvent<ListModelData> event) {
                if (event.getQuery().length() >= minChars) {
                    store.clear();
                    comboBox.expand();
                    load(false);
                    event.setCancelled(true);
                }
            }
        });

        comboBox.addValueChangeHandler(new ValueChangeHandler<ListModelData>() {

            @Override
            public void onValueChange(ValueChangeEvent<ListModelData> event) {
                saveState();
                fireEvent(new ChangeEvent());
            }
        });
        // Если повесить на attach, размер у комбобокса на форме рассчитывается
        // неправильно
        comboBox.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                comboBox.setMinListWidth((int) (comboBox.getOffsetWidth() * 1.3));
            }
        });

        store.getLoader().addLoadHandler(new LoadHandler<ClassLoadConfig, LoadData<ListModelData>>() {

            @Override
            public void onLoad(LoadEvent<ClassLoadConfig, LoadData<ListModelData>> event) {

                List<ListModelData> result = event.getLoadResult().getData();
                if (!comboBox.isExpanded() && result != null && result.size() == 1) {
                    comboBox.setValue(result.get(0));
                    comboBox.collapse();
                }
                // Чтобы в комбобоксе не выделялся текст при загрузке
                // данных
                comboBox.select(comboBox.getText().length(), 0);
                ComboBoxBuilder.this.fireEvent(new org.whirlplatform.component.client.event.LoadEvent());
            }
        });

        // Может пренести в ClassStore?
        store.getLoader().addLoadExceptionHandler(new LoadExceptionHandler<ClassLoadConfig>() {
            @Override
            public void onLoadException(LoadExceptionEvent<ClassLoadConfig> event) {
                InfoHelper.throwInfo("store-loader", event.getException());
            }
        });
    }

    protected void load() {
        load(true);
    }

    protected void load(boolean dontUseQuery) {
        store.getLoader().load(getLoadConfig(dontUseQuery));
    }

    protected ClassLoadConfig getLoadConfig(boolean dontUseQuery) {
        ClassLoadConfig config = new ClassLoadConfig();

        Map<String, DataValue> params = paramHelper.getValues();

        if (!dontUseQuery && comboBox.getText().length() >= minChars) {
            if (useSearchParameters) {
                DataValue v = new DataValueImpl(DataType.STRING, comboBox.getText());
                params.put(SEARCH_QUERY, v);
            } else {
                config.setQuery(comboBox.getText());
            }
        }

        config.setParameters(params);
        config.setWhereSql(whereSql);
        config.setAll(loadAll);
        config.setUseSearchParameters(useSearchParameters);
        config.setReloadMetadata(reloadMetadata);
        config.setLabelColumn(labelExpression);

        return config;
    }

    protected String getClassList() {
        return classId;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        comboBox.setEditable(editable);
    }

    @Override
    public ListModelData getValue() {
        ListModelData model = comboBox.getValue();
        return model;
    }

    @Override
    public void setValue(ListModelData value) {
        comboBox.setValue(value, true);
    }

    public String getText() {
        return comboBox.getText();
    }

    @Override
    public boolean isValid(boolean invalidate) {
        if (isRequired()) {
            Object value = null;
            ListModelData model = comboBox.getValue();
            if (model != null) {
                value = model.getId();
            }
            if (value == null) {
                if (invalidate) {
                    comboBox.markInvalid(AppMessage.Util.MESSAGE.requiredField());
                }
                return false;
            }
        }
        if (invalidate) {
            comboBox.clearInvalid();
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ComboBox<ListModelData> getRealComponent() {
        return comboBox;
    }

    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.LIST);
        result.setCode(getCode());
        result.setValue(comboBox.getValue());
        return result;
    }

    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (value != null && DataType.LIST.equals(value.getType())) {
            comboBox.setValue(value.getListModelData(), true);
        } else {
            comboBox.setValue(null);
        }
    }

    @Override
    public boolean isSaveState() {
        return saveState;
    }

    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;

    }

    public void setRestoreState(boolean restore) {
        this.restoreState = restore;

    }

    @Override
    public StateScope getStateScope() {
        return getStateStore().getScope();
    }

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

    @Override
    public void saveState() {
        DataValue v = getFieldValue();
        if (saveState && !Util.isEmptyString(getCode())) {
            getStateStore().put(getCode(), v);
        }
        getSelectionStore().save(getId() + "/select", v);
    }

    private StorageWrapper<DataValue> getStateStore() {
        if (stateStore == null) {
            setStateScope(StateScope.MEMORY);
        }
        return stateStore;
    }

    protected StateStore<DataValue> getSelectionStore() {
        if (selectionStateStore == null) {
            selectionStateStore = new SelectionClientStateStore<DataValue>(StateScope.LOCAL, metadata);
        }
        return selectionStateStore;
    }

    protected void restoreSelectionState() {
        if (restoreState) {

            DataValue v = getSelectionStore().restore(getId() + "/select");
            setFieldValue(v);
        }
    }

    @JsIgnore
    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return ensureHandler().addHandler(SelectEvent.getType(), handler);
    }

    @JsIgnore
    @Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
        return ensureHandler().addHandler(ChangeEvent.getType(), handler);
    }

    // TODO Selenium

    protected Locator getLocator(Element element) {
        return super.getLocatorByElement(element);
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator part = null;
        Locator locator = getLocator(element);
        if (locator != null) {
            if (comboBox.getCell().getInputElement(comboBox.getElement()).isOrHasChild(element)) {
                part = new Locator(LocatorParams.TYPE_INPUT);
            }
            if (comboBox.getCell().getAppearance().triggerIsOrHasChild(comboBox.getElement(), element)) {
                part = new Locator(LocatorParams.TYPE_TRIGGER);
            }
            if (super.clearDecorator != null) {
                Element clearElement = super.clearDecorator.getElement();
                if (clearElement != null && clearElement.isOrHasChild(element)) {
                    part = new Locator(LocatorParams.TYPE_CLEAR);
                }
            }
        } else {
            // ищу элемент в listView combobox-а
            List<Element> list = comboBox.getListView().getElements();
            for (int idx = 0; idx < list.size(); idx++) {
                Element item = list.get(idx);
                if (item.isOrHasChild(element)) {
                    locator = getLocator(comboBox.getElement());
                    ListModelData d = comboBox.getStore().get(idx);
                    part = new Locator(LocatorParams.TYPE_ITEM);
                    part.setParameter(LocatorParams.PARAMETER_ID, d.getId());
                    part.setParameter(LocatorParams.PARAMETER_LABEL, d.getLabel());
                    part.setParameter(LocatorParams.PARAMETER_INDEX, String.valueOf(idx));
                    break;
                }
            }
        }
        if (locator != null && part != null) {
            locator.setPart(part);
        }
        return locator;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        Element element = null;
        if (!super.fitsLocator(locator)) {
            return null;
        }
        Locator part = locator.getPart();
        if (part != null) {
            if (LocatorParams.TYPE_INPUT.equals(part.getType())) {
                element = comboBox.getCell().getInputElement(comboBox.getElement());
            }
            if (LocatorParams.TYPE_TRIGGER.equals(part.getType())) {
                TriggerFieldAppearance appearance = comboBox.getCell().getAppearance();
                if (appearance instanceof TriggerFieldDefaultAppearance) {
                    TriggerFieldDefaultAppearance defApp = (TriggerFieldDefaultAppearance) appearance;
                    element = comboBox.getElement().selectNode("." + defApp.getStyle().trigger()); // TODO Посмотреть
                    // что можно
                    // сделать?
                }
            }
            if (LocatorParams.TYPE_CLEAR.equals(part.getType())) {
                if (super.clearDecorator != null) {
                    element = super.clearDecorator.getElement();
                }
            }
            // Проверяем что комбобокс раскрылся.
            // Только тогда следует возвращать элементы.
            if (LocatorParams.TYPE_ITEM.equals(part.getType()) && comboBox.isExpanded()) {
                if (part.hasParameter(LocatorParams.PARAMETER_ID)) {
                    String id = part.getParameter(LocatorParams.PARAMETER_ID);
                    int index = store.indexOf(store.findModelWithKey(id));
                    element = comboBox.getListView().getElement(index);
                } else if (part.hasParameter(LocatorParams.PARAMETER_INDEX)) {
                    int index = Integer.parseInt(part.getParameter(LocatorParams.PARAMETER_INDEX));
                    element = comboBox.getListView().getElement(index);
                } else if (part.hasParameter(LocatorParams.PARAMETER_LABEL)
                        && Util.isEmptyString(part.getParameter(LocatorParams.PARAMETER_LABEL))) {
                    String label = part.getParameter(LocatorParams.PARAMETER_LABEL);
                    for (ListModelData m : store.getAll()) {
                        if (label.equals(m.getLabel())) {
                            element = comboBox.getListView().getElement(store.indexOf(m));
                            break;
                        }
                    }
                }
            }
        }
        return element;
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
     * Clears the field value.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        super.clear();
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

    /**
     * Sets the read only.
     *
     * @param readOnly true, if the field is read only
     */
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    private static class LocatorParams {

        private static String TYPE_INPUT = "Input";
        private static String TYPE_TRIGGER = "Trigger";
        private static String TYPE_CLEAR = "Clear";
        private static String TYPE_ITEM = "Item";
        // private static String TYPE_LABEL = "Label";

        private static String PARAMETER_ID = "id";
        private static String PARAMETER_INDEX = "index";
        private static String PARAMETER_LABEL = "label";
    }
}
