package org.whirlplatform.component.client.check;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.Style.Orientation;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.widget.core.client.Component;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.HasState;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.ListClassProxy;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

/**
 * Чек-бокс группа
 */
@JsType(name = "CheckGroup", namespace = "Whirl")
public class CheckGroupBuilder extends ComponentBuilder implements
    ListParameter<RowListValue>, HasState {

    private String labelExpression;
    private String checkExpression;
    private String checkedIds;
    private HandlerRegistration checkedRegistration;
    private String whereSql;
    private StorageWrapper<RowListValue> stateStore;
    private StateStore<RowListValue> selectionStateStore;
    private boolean saveState;
    private boolean restoreState;
    private ParameterHelper paramHelper;
    private LabelProvider<ListModelData> labelProvider;
    private ValueProvider<RowModelData, Boolean> valueProvider;
    private ClassStore<ListModelData, ClassLoadConfig> store;
    private CheckBoxList list;
    private String classId;

    @JsConstructor
    public CheckGroupBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public CheckGroupBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип CheckGroup
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.CheckGroupType;
    }

    /**
     * Создание компонента - CheckGroup
     *
     * @return Component, CheckGroup
     */
    @JsIgnore
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        labelProvider = new LabelProvider<ListModelData>() {

            @Override
            public String getLabel(ListModelData item) {
                return item.getLabel();
            }

        };
        valueProvider = new ValueProvider<RowModelData, Boolean>() {

            @Override
            public void setValue(RowModelData object, Boolean value) {
                if (checkExpression != null) {
                    object.set(checkExpression, value);
                }
            }

            @Override
            public Boolean getValue(RowModelData object) {
                if (checkExpression != null) {
                    return object.get(checkExpression);
                }
                return null;
            }

            @Override
            public String getPath() {
                return null;
            }
        };

        Orientation orientation = Orientation.VERTICAL;
        DataValue orientationDataValue = builderProperties.get(PropertyType.Orientation.getCode());
        if (orientationDataValue != null && Orientation.HORIZONTAL.toString()
            .equalsIgnoreCase(orientationDataValue.getString())) {
            orientation = Orientation.HORIZONTAL;
        }
        list = new CheckBoxList(orientation, labelProvider, valueProvider);
        return list;
    }

    /**
     * Создание и загрузка списка CheckGroup
     */
    @JsIgnore
    @Override
    public Component create() {
        Component comp = super.create();
        initStore();
        list.setStore(store);
        load();
        return comp;
    }

    /**
     * Установка атрибута для CheckGroup
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
        } else if (name.equalsIgnoreCase(PropertyType.CheckExpression.getCode())) {
            checkExpression = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            checkedIds = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.WhereSql.getCode())) {
            whereSql = value.getString();
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

    private void setDataSourceId(String classId) {
        this.classId = classId;
    }

    /**
     * Инициализация списка CheckGroup
     */
    private void initStore() {
        store = new ClassStore<ListModelData, ClassLoadConfig>(
            new ListClassProxy(classId));
        checkedRegistration = store
            .addStoreDataChangeHandler(new StoreDataChangeHandler<ListModelData>() {
                @Override
                public void onDataChange(
                    StoreDataChangeEvent<ListModelData> event) {
                    if (checkedIds == null) {
                        restoreSelectionState();
                    } else {
                        for (String obj : checkedIds.split(",")) {
                            ListModelData m = store.findModelWithKey(obj);
                            list.getSelectionModel().select(m, true);
                        }
                    }
                    checkedRegistration.removeHandler();
                }
            });
    }

    /**
     * Загрузка списка CheckGroup
     */
    private void load() {
        store.getLoader().load(getLoadConfig());
    }

    /**
     * Получить конфигурацию загрузки CheckGroup
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
//        config.setCheckExpression(checkExpression);
        return config;
    }

    /**
     * Получение сущности CheckGroup
     *
     * @return (C) list
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <C> C getRealComponent() {
        return (C) list;
    }

    /**
     * Получить список значений CheckGroup
     *
     * @return RowListValue
     */
    @JsIgnore
    @Override
    public RowListValue getFieldValue() {
        RowListValue result = new RowListValueImpl();
        result.setCode(getCode());
        RowModelData last = list.getSelectionModel().getSelectedItem();
        RowValue lastRow = new RowValueImpl(last.getId());
        lastRow.setSelected(true);
        result.addRowValue(lastRow);
        for (RowModelData m : list.getSelectionModel().getSelectedItems()) {
            RowValue row = new RowValueImpl(m.getId());
            row.setChecked(true);
            if (m == last) {
                lastRow.setChecked(true);
            } else {
                result.addRowValue(row);
            }
        }
        return result;
    }

    /**
     * Установить список значений CheckGroup
     *
     * @param value - RowListValue
     */
    @JsIgnore
    @Override
    public void setFieldValue(RowListValue value) {
        for (RowValue row : value.getRowList()) {
            RowModelData model = new RowModelDataImpl();
            model.setId(row.getId());
            list.setChecked(model, row.isChecked());
        }
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
        if (stateStore == null
            || (stateStore != null && scope != stateStore.getScope())) {
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
                    throw new IllegalArgumentException("Variable 'scope' of the type 'StateScope' can`t be this: "
                        + scope);
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
            selectionStateStore = new SelectionClientStateStore<RowListValue>(
                StateScope.LOCAL, new ClassMetadata(classId));
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

}
