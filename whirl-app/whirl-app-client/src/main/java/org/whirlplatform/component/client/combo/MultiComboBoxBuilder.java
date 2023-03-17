package org.whirlplatform.component.client.combo;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.ListClassProxy;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Список с мультивыбором
 */
@JsType(name = "MultiComboBox", namespace = "Whirl")
public class MultiComboBoxBuilder extends AbstractMultiComboBoxBuilder<ListModelData, ComboBox<ListModelData>> {

    @JsConstructor
    public MultiComboBoxBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public MultiComboBoxBuilder() {
        this(Collections.emptyMap());
    }

    @Override
    protected ListModelData createNewModel() {
        return new ListModelDataImpl();
    }

    @Override
    protected void createStore() {
        store = new ClassStore<ListModelData, ClassLoadConfig>(
                new ListClassProxy(classId));
    }

    @JsIgnore
    @Override
    public Component create() {
        Component c = super.create();
        comboBox.getListView().setLoader(store.getLoader());
        return c;
    }
    @Override
    protected void bindStore() {
        super.bindStore();
        // Чтобы отмеченные элементы отображались в начале списка
        Store.StoreSortInfo<ListModelData> sortInfo = new Store.StoreSortInfo<ListModelData>(
                new Comparator<ListModelData>() {
                    @Override
                    public int compare(ListModelData o1, ListModelData o2) {
                        if (checkedModels.models.contains(o1)
                                && !checkedModels.models.contains(o2)) {
                            return -1;
                        } else if (!checkedModels.models.contains(o1)
                                && checkedModels.models.contains(o2)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }, SortDir.ASC);
        store.addSortInfo(sortInfo);
        store.getLoader().addLoadHandler(
                new LoadHandler<ClassLoadConfig, LoadData<ListModelData>>() {
                    @Override
                    public void onLoad(
                            LoadEvent<ClassLoadConfig, LoadData<ListModelData>> event) {
                        // Если в комбобокс не введено значение, добавляем
                        // отмеченные элементы
                        if (event.getLoadConfig().getQuery() == null
                                || event.getLoadConfig().getQuery().isEmpty()) {
                            for (ListModelData m : checkedModels.models) {
                                if (!store.getAll().contains(m)) {
                                    store.add(m);
                                }
                            }
                        }
                        // Чтобы в комбобоксе не выделялся текст при загрузке
                        // данных
                        comboBox.select(comboBox.getText().length(), 0);
                    }
                });
    }
}
