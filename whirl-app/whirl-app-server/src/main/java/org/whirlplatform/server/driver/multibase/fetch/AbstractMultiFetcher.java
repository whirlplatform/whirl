package org.whirlplatform.server.driver.multibase.fetch;

import org.apache.empire.commons.StringUtils;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.server.db.ConnectionWrapper;

import java.sql.Timestamp;

public class AbstractMultiFetcher extends AbstractFetcher {

    private final DataSourceDriver datasourceDriver;

    public AbstractMultiFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver datasourceDriver) {
        super(connectionWrapper);
        this.datasourceDriver = datasourceDriver;
    }

    public DataSourceDriver getDataSourceDriver() {
        return datasourceDriver;
    }

    protected Object convertValueFromString(String value, String objValue, DataType type) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (type != null) {
            switch (type) {
                case BOOLEAN:
                    return "T".equals(value);
                case DATE:
                    return Timestamp.valueOf(value);
                case NUMBER:
                    Double result;
                    try {
                        result = Double.valueOf(value);
                    } catch (NumberFormatException e) {
                        result = Double.valueOf(value.replaceFirst(",", "."));
                    }
                    return result;
                case LIST:
                    ListModelData listValue = new ListModelDataImpl();
                    listValue.setId(objValue);
                    listValue.setLabel(value);
                    return listValue;
                case STRING:
                    return value;
                case FILE:
                    FileValue fileValue = new FileValue();
                    fileValue.setName(value);
                    return fileValue;
                default:
                    break;
            }
        }
        return value;
    }
}
