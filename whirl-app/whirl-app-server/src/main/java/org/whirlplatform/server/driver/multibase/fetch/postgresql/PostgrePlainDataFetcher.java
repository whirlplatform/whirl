package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.apache.empire.data.DataType;
import org.apache.empire.db.*;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTreeFetcherHelper;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.util.Map;

public class PostgrePlainDataFetcher extends AbstractPlainDataFetcher implements DataFetcher<PlainTableElement> {
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(PostgrePlainDataFetcher.class);

    public PostgrePlainDataFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    protected DBCommand createCountCommand(PlainTableFetcherHelper temp, boolean all) {
        // ограничеия
        DBCommand countCommand = temp.dbDatabase.createCommand();
        temp.countColumn = temp.dbPrimaryKey.count().as("count");
        countCommand.select(temp.countColumn);

        if (!temp.where.isEmpty()) {
            countCommand.addWhereConstraints(temp.where);
        }
        if (!all) {
            countCommand.limitRows(10000);
        }
        return countCommand;
    }

    protected DBCommand createSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                            PlainTableFetcherHelper temp) {
        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            if (((TreeClassLoadConfig) loadConfig).getParentField() != null
                    && ((TreeClassLoadConfig) loadConfig).getParent() != null) {
                all = true;
            }
        }
        // ограничеия
        DBCommand command = temp.dbDatabase.createCommand();
        command.select(temp.dbPrimaryKey);
        for (FieldMetadata f : temp.tableColumns.keySet()) {
            if (f.isView()) {
                command.select(temp.dbTable.getColumn(f.getName()));
            }
        }

        if (loadConfig.getSorts().isEmpty()) {
            command.orderBy(temp.dbPrimaryKey);
        } else {
            for (SortValue s : loadConfig.getSorts()) {
                command.orderBy(temp.dbTable.getColumn(s.getField().getName()), s.getOrder() == SortType.DESC);
            }
        }

        if (!temp.where.isEmpty()) {
            command.addWhereConstraints(temp.where);
        }

        if (!all) {
            command.limitRows(10000);
        }

        if (!all) {
            command.limitRows(
                    ((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage()) + loadConfig.getRowsPerPage());
            command.skipRows(1 + (loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage());
        }

        if (loadConfig instanceof TreeClassLoadConfig) {
            String parentField = ((TreeClassLoadConfig) loadConfig).getParentField();
            if (parentField != null) {
                String leafExpression = ((TreeClassLoadConfig) loadConfig).getLeafExpression();
                DBColumnExpr exprColumn;
                if (leafExpression != null) {
                    exprColumn = temp.dbDatabase.getValueExpr(leafExpression, DataType.UNKNOWN)
                            .as("PROPERTY_HAS_CHILDREN");
                } else {
                    exprColumn = temp.dbDatabase.getValueExpr(true).as("PROPERTY_HAS_CHILDREN");
                }
                command.select(exprColumn);
                ((PlainTreeFetcherHelper) temp).dbLeafExpression = exprColumn;

                RowModelData parent = ((TreeClassLoadConfig) loadConfig).getParent();
                DBColumn parentColumn = temp.dbTable.getColumn(parentField);
                if (parent != null) {
                    command.where(parentColumn.is(parent.getId()));
                } else {
                    command.where(parentColumn.cmp(DBCmpType.NULL, null));
                }
            }
        }
        return command;
    }

    protected void setModelValue(RowModelData model, FieldMetadata field, DBReader reader,
                                 PlainTableFetcherHelper temp) {
        DBColumnExpr col = temp.topDbTable.getColumn(field.getName());
        if ((field.getViewFormat() != null) && !field.getViewFormat().isEmpty()) {
            String value = null;
            String objValue = null;
            Map<String, String> formattedMap;
            formattedMap = fromUrlEncoded(reader.getString(col));
            if (field.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
                DBColumnExpr valueCol = temp.topDbTable.getColumn(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX);
                objValue = formattedMap.get(AppConstant.VALUE);
                value = reader.getString(valueCol);
            } else {
                value = formattedMap.get(AppConstant.VALUE);
            }
            model.setStyle(field.getName(), formattedMap.get(AppConstant.STYLE));
            model.set(field.getName(), convertValueFromString(value, objValue, field.getType()));
        } else {
            if (field.getType() == org.whirlplatform.meta.shared.data.DataType.STRING) {
                model.set(field.getName(), reader.isNull(col) ? null : reader.getString(col));
            } else if (field.getType() == org.whirlplatform.meta.shared.data.DataType.NUMBER) {
                model.set(field.getName(), reader.isNull(col) ? null : reader.getDouble(col));
            } else if (field.getType() == org.whirlplatform.meta.shared.data.DataType.DATE) {
                model.set(field.getName(), reader.isNull(col) ? null : reader.getDateTime(col));
            } else if (field.getType() == org.whirlplatform.meta.shared.data.DataType.BOOLEAN) {
                model.set(field.getName(), reader.isNull(col) ? null : reader.getBoolean(col));
            } else if (field.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
                DBColumnExpr labelCol = temp.topDbTable.getColumn(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX);
                ListModelData value = new ListModelDataImpl();
                value.setLabel(reader.getString(labelCol));
                value.setId(reader.getString(col));
                model.set(field.getName(), value);
            } else if (field.getType() == org.whirlplatform.meta.shared.data.DataType.FILE) {
                DBColumnExpr fileCol = temp.topDbTable.getColumn(field.getName() + SrvConstant.COLUMN_FILE_POSTFIX);
                FileValue value = new FileValue();
                value.setName(reader.getString(fileCol));
                model.set(field.getName(), value);
            }
        }
    }

    @Override
    public DBReader getTableReader(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);
        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);
        DBReader reader = createAndOpenReader(selectCmd);
        return reader;
    }

    // TODO не используется
    public int getTableRowsCount(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        int result = 0;
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);
        DBCommand countCmd = createCountCommand(temp, loadConfig.isAll());
        DBReader countReader = createAndOpenReader(countCmd);
        if (countReader.moveNext()) {
            result = countReader.getInt(temp.countColumn);
        }
        countReader.close();
        return result;
    }
}
