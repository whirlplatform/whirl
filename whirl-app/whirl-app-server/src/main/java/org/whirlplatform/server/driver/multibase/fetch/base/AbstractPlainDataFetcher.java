package org.whirlplatform.server.driver.multibase.fetch.base;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

public abstract class AbstractPlainDataFetcher extends AbstractMultiFetcher {

    public AbstractPlainDataFetcher(ConnectionWrapper connection,
                                    DataSourceDriver datasourceDriver) {
        super(connection, datasourceDriver);
    }

    public DataType getIdColumnType(PlainTableElement table) {
        return table.getIdColumn().getType();
    }

    /**
     * Добавляет поля и условия в команды для запроса дерева
     *
     * @param cmd        - внешний запрос
     * @param whereCmd   - команда для добавления ограничений
     * @param loadConfig
     * @param temp
     */
    protected void addTreeCommandPart(DBCommand cmd, DBCommand whereCmd,
                                      TreeClassLoadConfig loadConfig,
                                      PlainTableFetcherHelper temp) {
        // Колонка родителя в иерархии

        String parentField = loadConfig.getParentColumn();
        if (!StringUtils.isEmpty(parentField)) {
            RowModelData parent = loadConfig.getParent();
            DBColumn parentColumn = temp.dbTable.getColumn(parentField);
            if (parent != null) {
                whereCmd.where(parentColumn.is(parent.getId()));
            } else {
                whereCmd.where(parentColumn.cmp(DBCmpType.NULL, null));
            }
        }
    }

    protected void setModelValue(RowModelData model, FieldMetadata field, DBReader reader) {
        int colInd = reader.getFieldIndex(field.getName());
        org.whirlplatform.meta.shared.data.DataType colDataType = field.getType();
        if (colDataType != null) {
            switch (colDataType) {
                case STRING:
                    model.set(field.getName(),
                            reader.isNull(colInd) ? null : reader.getString(colInd));
                    break;
                case NUMBER:
                    model.set(field.getName(),
                            reader.isNull(colInd) ? null : reader.getDouble(colInd));
                    break;
                case DATE:
                    model.set(field.getName(),
                            reader.isNull(colInd) ? null : reader.getDateTime(colInd));
                    break;
                case BOOLEAN:
                    model.set(field.getName(),
                            reader.isNull(colInd) ? null : reader.getBoolean(colInd));
                    break;
                case LIST:
                    ListModelData listValue = new ListModelDataImpl();
                    listValue.setLabel(getLabelValue(field, reader, colInd));
                    listValue.setId(reader.getString(colInd));
                    model.set(field.getName(), listValue);
                    break;
                case FILE:
                    FileValue fileValue = new FileValue();
                    fileValue.setName(getLabelValue(field, reader, colInd));
                    model.set(field.getName(), fileValue);
                    break;
                default:
                    break;
            }
        }
    }

    private String getLabelValue(FieldMetadata field, DBReader reader, int colInd) {
        if (!StringUtils.isEmpty(field.getLabelExpression())
            && field.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
            int labelInd = reader.getFieldIndex(field.getName() + LABEL_EXPRESSION_NAME);
            return reader.getString(labelInd);
        } else {
            return reader.getString(colInd);
        }
    }
}
