package org.whirlplatform.component.client.grid;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.whirlplatform.component.client.combo.MultiComboBoxBuilder;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;

import java.util.Map;

class TableMultiComboBoxBuilder extends MultiComboBoxBuilder<ComboBox<ListModelData>> {

    private FieldMetadata tableField;

    public TableMultiComboBoxBuilder(FieldMetadata tableField, Map<String, DataValue> builderProperties) {
        super(builderProperties);
        this.tableField = tableField;
    }

    @Override
    protected ClassLoadConfig getLoadConfig(boolean dontUseQuery) {
        ClassLoadConfig config = super.getLoadConfig(dontUseQuery);
        config.setTableField(tableField);
        return config;
    }

}
