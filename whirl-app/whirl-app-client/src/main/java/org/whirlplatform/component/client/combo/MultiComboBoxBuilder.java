package org.whirlplatform.component.client.combo;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.data.ListClassProxy;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

import java.util.Collections;
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
}
