package org.whirlplatform.component.client.check;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.Style.Orientation;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.widget.core.client.Component;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.HasState;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.TableClassProxy;
import org.whirlplatform.component.client.state.SelectionClientStateStore;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.component.client.state.StateStore;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

import java.util.Map;

/**
 * Чек-бокс группа
 */
public class CheckGroupBuilder extends ComponentBuilder implements
        ListParameter<RowListValue>, HasState {

    private String nameColumn;
    private String checkColumn;
    private String checkedIds;
    private HandlerRegistration checkedRegistration;
    private String whereSql;

    private ClassMetadata metadata;
    private StorageWrapper<RowListValue> stateStore;
    private StateStore<RowListValue> selectionStateStore;
    private boolean saveState;
    private boolean restoreState;
    private ParameterHelper paramHelper;

    public CheckGroupBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public CheckGroupBuilder() {
        super();
    }

    private LabelProvider<RowModelData> labelProvider;

    private ValueProvider<RowModelData, Boolean> valueProvider;

    private ClassStore<RowModelData, ClassLoadConfig> store;
    private CheckBoxList list;

    /**
     * Получить тип CheckGroup
     */
    @Override
    public ComponentType getType() {
        return ComponentType.CheckGroupType;
    }

    /**
     * Создание компонента - CheckGroup
     *
     * @return Component, CheckGroup
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        labelProvider = new LabelProvider<RowModelData>() {

            @Override
            public String getLabel(RowModelData item) {
                return item.get(nameColumn);
            }

        };
        valueProvider = new ValueProvider<RowModelData, Boolean>() {

            @Override
            public void setValue(RowModelData object, Boolean value) {
                if (checkColumn != null) {
                    object.set(checkColumn, value);
                }
            }

            @Override
            public Boolean getValue(RowModelData object) {
                if (checkColumn != null) {
                    return object.get(checkColumn);
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
        if (orientationDataValue != null && Orientation.HORIZONTAL.toString().equalsIgnoreCase(orientationDataValue.getString())) {
            orientation = Orientation.HORIZONTAL;
        }
        list = new CheckBoxList(orientation, labelProvider, valueProvider);
        return list;
    }

    /**
     * Создание и загрузка списка CheckGroup
     */
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
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DataSource.getCode())) {
            if (value.getListModelData() != null) {
                setDataSourceId(value.getListModelData().getId());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.NameExpression.getCode())) {
            nameColumn = value.getString();
            return true;
        } else if (name
                .equalsIgnoreCase(PropertyType.CheckExpression.getCode())) {
            checkColumn = value.getString();
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
        this.metadata = new ClassMetadata(classId);
    }

    /**
     * Инициализация списка CheckGroup
     */
    private void initStore() {
        if (nameColumn != null) {
            metadata.addField(new FieldMetadata(nameColumn, DataType.STRING,
                    null));
        }
        if (checkColumn != null) {
            metadata.addField(new FieldMetadata(checkColumn, DataType.BOOLEAN,
                    null));
        }
        store = new ClassStore<RowModelData, ClassLoadConfig>(metadata,
                new TableClassProxy(metadata));
        checkedRegistration = store
                .addStoreDataChangeHandler(new StoreDataChangeHandler<RowModelData>() {
                    @Override
                    public void onDataChange(
                            StoreDataChangeEvent<RowModelData> event) {
                        if (checkedIds == null) {
                            restoreSelectionState();
                        } else {
                            for (String obj : checkedIds.split(",")) {
                                RowModelData m = store.findModelWithKey(obj);
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
    @Override
    public void setFieldValue(RowListValue value) {
        for (RowValue row : value.getRowList()) {
            RowModelData model = new RowModelDataImpl();
            model.setId(row.getId());
            list.setChecked(model, row.isChecked());
        }
    }

    @Override
    public void setSaveState(boolean save) {
        this.saveState = save;

    }

    @Override
    public boolean isSaveState() {
        return saveState;
    }

    public void setRestoreState(boolean restore) {
        this.restoreState = restore;

    }

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
                    StateScope.LOCAL, metadata);
        }
        return selectionStateStore;
    }

    protected void restoreSelectionState() {
        if (restoreState) {
            RowListValue v = getSelectionStore().restore(getId() + "/select");
            setFieldValue(v);
        }
    }

}
