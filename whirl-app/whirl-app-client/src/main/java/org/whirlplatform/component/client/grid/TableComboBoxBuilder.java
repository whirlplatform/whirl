package org.whirlplatform.component.client.grid;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;

import java.util.Map;

class TableComboBoxBuilder extends ComboBoxBuilder<ComboBox<ListModelData>> {

    private FieldMetadata tableField;

    public TableComboBoxBuilder(FieldMetadata tableField, Map<String, DataValue> builderProperties) {
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