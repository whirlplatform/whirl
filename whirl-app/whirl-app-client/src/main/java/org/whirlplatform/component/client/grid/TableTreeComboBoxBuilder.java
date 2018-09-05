package org.whirlplatform.component.client.grid;

import org.whirlplatform.component.client.combo.TreeComboBoxBuilder;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

class TableTreeComboBoxBuilder extends TreeComboBoxBuilder {

    private FieldMetadata tableField;

    public TableTreeComboBoxBuilder(FieldMetadata tableField, Map<String, DataValue> builderProperties) {
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
