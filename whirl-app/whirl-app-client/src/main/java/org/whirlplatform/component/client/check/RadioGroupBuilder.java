package org.whirlplatform.component.client.check;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.Style.Orientation;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
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
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.ListClassProxy;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.component.client.utils.SimpleEditorError;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

/**
 * Радиогруппа
 */
@JsType(name = "RadioGroup", namespace = "Whirl")
public class RadioGroupBuilder extends ComponentBuilder implements Clearable,
    Validatable, ListParameter<RowListValue>, SelectEvent.HasSelectHandlers, HasState {

    protected StorageWrapper<RowListValue> stateStore;
    protected StateStore<RowListValue> selectionStateStore;
    protected boolean saveState;
    protected boolean restoreState;
    private InsertResizeContainer layout;
    private ToggleGroup group;
    private SimpleContainer container;
    private SideErrorHandler errorHandler;
    private String classId;
    private String labelExpression;
    private String whereSql;
    private Orientation orientation; // Сделать в базе обязательным
    // полем?
    private String checkedIds;
    private boolean required = false;
    private ClassStore<ListModelData, ClassLoadConfig> store;
    private final LoadHandler<ClassLoadConfig, LoadData<ListModelData>> loadHandler =
        new LoadHandler<ClassLoadConfig, LoadData<ListModelData>>() {
            @Override
            public void onLoad(LoadEvent<ClassLoadConfig, LoadData<ListModelData>> event) {
                rebuild();
            }
        };
    private ParameterHelper paramHelper;
    private String dataSourceId;

    @JsConstructor
    public RadioGroupBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public RadioGroupBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип радиогруппы
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.RadioGroupType;
    }

    /**
     * Создание компонента - радиогруппа
     *
     * @return Component, контейнер
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        orientation = Orientation.VERTICAL;
        container = new SimpleContainer();
        errorHandler = new SideErrorHandler(container);
        group = new ToggleGroup();
        group.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
            @Override
            public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
                saveState();
                fireEvent(new SelectEvent());
            }
        });
        createLayout();
        return container;
    }

    /**
     * Создание радиогруппы со списком
     *
     * @return Component
     */
    @JsIgnore
    @Override
    public Component create() {
        Component comp = super.create();

        loadData();

        return comp;
    }

    /**
     * Создание контейнера вывода радиогруппы
     */
    private void createLayout() {
        if (Orientation.HORIZONTAL == orientation) {
            layout = new HorizontalLayoutContainer();
            ((HorizontalLayoutContainer) layout).setScrollMode(ScrollMode.AUTO);
        } else {
            layout = new VerticalLayoutContainer();
            ((VerticalLayoutContainer) layout).setScrollMode(ScrollMode.AUTO);
        }
        container.add(layout);
    }

    /**
     * Установка атрибута для радиогруппы
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DataSource.getCode())) {
            if (value.getListModelData() != null) {
                setDataSourceId(value.getListModelData().getId());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.LabelExpression.getCode())) {
            labelExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode())) {
            whereSql = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Orientation.getCode())) {
            if (value.getString() != null) {
                orientation = Orientation.valueOf(value.getString());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            checkedIds = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Parameters.getCode())) {
            if (paramHelper == null) {
                paramHelper = new ParameterHelper();
            }
            paramHelper.addJsonParameters(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.SaveState.getCode())) {
            setSaveState(Boolean.TRUE.equals(value.getBoolean()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.RestoreState.getCode())) {
            setRestoreState(Boolean.TRUE.equals(value.getBoolean()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StateScope.getCode())) {
            StateScope scope = StateScope.valueOf(value.getString());
            setStateScope(scope);
            return true;
        }
        return super.setProperty(name, value);
    }

    private void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
        this.classId = dataSourceId;
        selectionStateStore =
            new SelectionClientStateStore<RowListValue>(StateScope.LOCAL, new ClassMetadata(classId));
    }

    /**
     * Загрузка списка радиогруппы
     */
    private void loadData() {
        store = new ClassStore<ListModelData, ClassLoadConfig>(
            new ListClassProxy(classId));
        store.getLoader().addLoadHandler(loadHandler);

        store.getLoader().load(getLoadConfig());
    }

    /**
     * Перерисовка радиогруппы
     */
    private void rebuild() {
        for (int i = 0; i < store.size(); i++) {
            Radio radio = new Radio();
            radio.setBoxLabel(store.get(i).getLabel());

            radio.setData(LocatorParams.DATA_PARAM_ID, store.get(i).getId());
            layout.add(radio);
            group.add(radio);
            if (checkedIds == null) {
                restoreSelectionState();
            } else if (checkedIds.contains((store.get(i).getId()))) {
                group.setValue(radio, true);
            }
        }
        layout.forceLayout();
    }

    /**
     * Получить загруженную конфиграцию
     *
     * @return ClassLoadConfig
     */
    private ClassLoadConfig getLoadConfig() {
        ClassLoadConfig config = new ClassLoadConfig();

        if (paramHelper != null) {
            config.setParameters(paramHelper.getValues());
        }
        config.setWhereSql(whereSql);
        config.setLabelExpression(labelExpression);
        return config;
    }

    /**
     * Получить сущность радиогруппы
     */
    @Override
    protected <C> C getRealComponent() {
        return (C) layout;
    }

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у радиогруппы
     *
     * @return boolean
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Установка свойства "Обязателен для заполнения" для радиогруппы
     *
     * @param required - boolean
     */
    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Очистка значения радиогруппы
     */
    @Override
    public void clear() {
        layout.clear();
    }

    /**
     * Проверка на валидность радиогруппы
     *
     * @return boolean
     */
    @JsIgnore
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Проверка на валидность радиогруппы
     *
     * @param invalidate - boolean
     * @return boolean
     */
    @Override
    public boolean isValid(boolean invalidate) {
        if (group.getValue() == null) {
            if (invalidate) {
                markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        } else {
            errorHandler.clearInvalid();
            return true;
        }
    }

    /**
     * Получение значения из радиогруппы
     *
     * @return RowListValue
     */
    @JsIgnore
    @Override
    public RowListValue getFieldValue() {
        Radio radio = (Radio) group.getValue();
        if (radio == null) {
            return null;
        }
        String id = radio.getData(LocatorParams.DATA_PARAM_ID);
        RowValue row = new RowValueImpl(id);
        row.setSelected(true);
        row.setChecked(radio.getValue());

        RowListValue result = new RowListValueImpl();
        result.setCode(getCode());
        result.addRowValue(row);
        return result;
    }

    /**
     * Установить значение радиогруппы
     *
     * @param value - RowListValue
     */
    @JsIgnore
    @Override
    public void setFieldValue(RowListValue value) {
        // Не в цикле, т.к. должно быть одно значение
        RowValue row = value.getRowList().get(0);
        Iterator<HasValue<Boolean>> iter = group.iterator();
        while (iter.hasNext()) {
            Radio radio = (Radio) iter.next();
            if (row.getId().equals(radio.getData(LocatorParams.DATA_PARAM_ID)) && row.isChecked()) {
                group.setValue(radio);
                break;
            }
        }
    }

    /**
     * Устанавливает радиогруппе сообщение о не валидности данных
     *
     * @param msg - String
     */
    @Override
    public void markInvalid(String msg) {
        EditorError error = new SimpleEditorError(msg);
        errorHandler.markInvalid(Collections.singletonList(error));
    }

    /**
     * Очищает сообщение о не валидности радиогруппы
     */
    @Override
    public void clearInvalid() {
        errorHandler.clearInvalid();
    }

    @JsIgnore
    @Override
    public boolean isSaveState() {
        return saveState;
    }

    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;

    }

    @JsIgnore
    public void setRestoreState(boolean restore) {
        this.restoreState = restore;

    }

    @JsIgnore
    @Override
    public StateScope getStateScope() {
        return getStateStore().getScope();
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
                default:
                    throw new IllegalArgumentException("Unsupported scope");
            }
        }
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
            selectionStateStore =
                new SelectionClientStateStore<RowListValue>(StateScope.LOCAL, new ClassMetadata(classId));
        }
        return selectionStateStore;
    }

    protected void restoreSelectionState() {
        if (restoreState) {
            RowListValue v = getSelectionStore().restore(getId() + "/select");
            setFieldValue(v);
        }
    }

    /**
     * Добавление обработчика выбора значения в радиогруппе
     *
     * @param handler - SelectHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler,
            SelectEvent.getType());
    }


    // Arquillian
    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator locator = super.getLocatorByElement(element);
        Iterator<HasValue<Boolean>> iter = group.iterator();
        for (int idx = 0; idx < group.size(); idx++) {
            HasValue<Boolean> o = iter.next();
            if (o instanceof Radio) {
                Radio item = (Radio) o;
                if (item.getElement().isOrHasChild(element)) {
                    Locator part = new Locator(LocatorParams.TYPE_ITEM);
                    part.setParameter(LocatorParams.PARAMETER_ID,
                        String.valueOf(item.getData(LocatorParams.DATA_PARAM_ID)));
                    part.setParameter(LocatorParams.PARAMETER_LABEL, item.getBoxLabel().asString());
                    part.setParameter(LocatorParams.PARAMETER_INDEX, String.valueOf(idx));
                    locator.setPart(part);
                }
            }
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
            if (LocatorParams.TYPE_ITEM.equals(part.getType())) {
                RadioItemFinder finder = new RadioItemFinder(group);
                Radio found = null;
                if (part.hasParameter(LocatorParams.PARAMETER_ID)) {
                    String id = part.getParameter(LocatorParams.PARAMETER_ID);
                    found = finder.findById(id);
                } else if (part.hasParameter(LocatorParams.PARAMETER_INDEX)) {
                    int index =
                        Integer.parseInt(locator.getParameter(LocatorParams.PARAMETER_INDEX));
                    found = finder.findByIndex(index);
                } else if (part.hasParameter(LocatorParams.PARAMETER_LABEL)
                    && Util.isEmptyString(part.getParameter(LocatorParams.PARAMETER_LABEL))) {
                    String label = part.getParameter(LocatorParams.PARAMETER_LABEL);
                    found = finder.findByLabel(label);
                }
                if (found != null) {
                    element = found.getCell().getInputElement(found.getElement());
                }
            }
        }

        return element;
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }


    private static class LocatorParams {

        private static final String PARAMETER_ID = "id";
        private static final String PARAMETER_LABEL = "label";
        private static final String PARAMETER_INDEX = "index";

        private static final String TYPE_ITEM = "Item";

        private static final String DATA_PARAM_ID = "Id";
    }

    private class RadioItemFinder {

        private ToggleGroup group = null;

        public RadioItemFinder(ToggleGroup group) {
            this.group = group;
        }


        public Radio findById(String id) {
            Iterator<HasValue<Boolean>> iter = group.iterator();
            for (int idx = 0; idx < group.size(); idx++) {
                HasValue<Boolean> o = iter.next();
                Radio item = (Radio) o;
                String itemId = String.valueOf(item.getData(LocatorParams.DATA_PARAM_ID));
                if (itemId != null && itemId.equals(id)) {
                    return item;
                }
            }
            return null;
        }


        public Radio findByIndex(int index) {
            Iterator<HasValue<Boolean>> iter = group.iterator();
            for (int idx = 0; idx < group.size(); idx++) {
                HasValue<Boolean> o = iter.next();
                if (idx == index) {
                    return (Radio) o;
                }
            }
            return null;
        }


        public Radio findByLabel(String label) {
            Iterator<HasValue<Boolean>> iter = group.iterator();
            for (int idx = 0; idx < group.size(); idx++) {
                HasValue<Boolean> o = iter.next();
                Radio item = (Radio) o;
                String itemLabel = String.valueOf(item.getBoxLabel());
                if (label.equalsIgnoreCase(itemLabel)) {
                    return item;
                }
            }
            return null;
        }

    }
}