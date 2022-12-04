package org.whirlplatform.component.client.grid;

import com.sencha.gxt.core.shared.FastMap;
import java.util.Map;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.base.NumberFieldBuilder;
import org.whirlplatform.component.client.base.PasswordFieldBuilder;
import org.whirlplatform.component.client.base.TextAreaBuilder;
import org.whirlplatform.component.client.base.TextFieldBuilder;
import org.whirlplatform.component.client.base.UploadFieldBuilder;
import org.whirlplatform.component.client.check.CheckBoxBuilder;
import org.whirlplatform.component.client.date.DateFieldBuilder;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.ListViewType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

public class FieldMetadataHelper {

    public static Map<String, DataValue> prepareProperties(FieldMetadata field) {
        return prepareProperties(field, false);
    }

    public static Map<String, DataValue> prepareProperties(FieldMetadata field,
                                                           boolean viewOnly) {
        Map<String, DataValue> properties = new FastMap<DataValue>();
        if (!field.isEdit() || viewOnly) {
            properties.put(PropertyType.ReadOnly.getCode(),
                    new DataValueImpl(DataType.BOOLEAN, Boolean.TRUE));
        }
        if (field.isRequired()) {
            properties.put(PropertyType.Required.getCode(),
                    new DataValueImpl(DataType.BOOLEAN, Boolean.TRUE));
        }
        if (field.isHidden()) {
            properties.put(PropertyType.Hidden.getCode(),
                    new DataValueImpl(DataType.BOOLEAN, Boolean.TRUE));
        }
        if (field.getRegEx() != null && !field.getRegEx().isEmpty()) {
            properties.put(PropertyType.RegEx.getCode(),
                    new DataValueImpl(DataType.STRING, field.getRegEx()));
        }
        if (field.getRegExError() != null && !field.getRegExError().isEmpty()) {
            properties.put(PropertyType.RegExMessage.getCode(),
                    new DataValueImpl(DataType.STRING, field.getRegExError()));
        }
        if (field.getHeight() != 0) {
            properties.put(PropertyType.Height.getCode(),
                    new DataValueImpl(DataType.NUMBER, field.getHeight()));
        }
        if (field.getClassId() != null) {
            ListModelData list = new ListModelDataImpl();
            list.setId(field.getClassId());
            properties.put(PropertyType.DataSource.getCode(),
                    new DataValueImpl(DataType.LIST, list));
        }
        if (field.getType() == DataType.LIST) {
            properties.put(PropertyType.ReloadStructure.getCode(),
                    new DataValueImpl(DataType.BOOLEAN, true));
        }
        return properties;
    }

    public static AbstractFieldBuilder build(FieldMetadata m, boolean viewOnly) {
        Map<String, DataValue> properties = prepareProperties(m, viewOnly);
        AbstractFieldBuilder builder = null;
        if (m.getType() == DataType.STRING) {
            if (m.isPassword()) {
                builder = new PasswordFieldBuilder(properties);
            } else if (m.getHeight() > 0) {
                builder = new TextAreaBuilder(properties);
            } else {
                builder = new TextFieldBuilder(properties);
            }
        } else if (m.getType() == DataType.NUMBER) {
            builder = new NumberFieldBuilder(properties);
        } else if (m.getType() == DataType.DATE) {
            builder = new DateFieldBuilder(properties);
        } else if (m.getType() == DataType.LIST) {
            if (m.getListViewType() == null
                    || ListViewType.LIST.equals(m.getListViewType())) {
                builder = new TableComboBoxBuilder(m, properties);
            } else if (ListViewType.MULTI.equals(m.getListViewType())) {
                builder = new TableMultiComboBoxBuilder(m, properties);
            } else if (ListViewType.TREE.equals(m.getListViewType())) {
                builder = new TableTreeComboBoxBuilder(m, properties);
            }
        } else if (m.getType() == DataType.BOOLEAN) {
            builder = new CheckBoxBuilder(properties);
        } else if (m.getType() == DataType.FILE) {
            builder = new UploadFieldBuilder(properties);
        }
        builder.setProperties(properties, true);
        builder.setCode(m.getName());
        return builder;
    }

    public static AbstractFieldBuilder build(FieldMetadata m) {
        return build(m, false);
    }

}
