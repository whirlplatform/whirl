package org.whirlplatform.server.driver.multibase.fetch.oracle;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBQuery;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.expr.column.DBFuncExpr;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.global.SrvConstant;

import java.util.Map;

public class OraclePlainDataFetcher extends AbstractPlainDataFetcher implements DataFetcher<PlainTableElement> {

    public OraclePlainDataFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver datasourceDriver) {
        super(connectionWrapper, datasourceDriver);
    }

    // Добавить логгирование?
    @Override
    public DBReader getTableReader(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);
        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);
        DBReader reader = new DBReader();
        reader.open(selectCmd, getConnection());
        return reader;
    }

    //    // TODO не используется
    //    public int getTableRowsCount(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig,
    //                                 ApplicationUser user) {
    //        int result = 0;
    //        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
    //        temp.prepare(metadata, table, loadConfig);
    //        DBCommand countCmd = createCountCommand(temp, loadConfig.isAll());
    //        DBReader countReader = createAndOpenReader(countCmd);
    //        if (countReader.moveNext()) {
    //            result = countReader.getInt(temp.countColumn);
    //        }
    //        countReader.close();
    //        return result;
    //    }

    protected DBCommand createCountCommand(PlainTableFetcherHelper temp, boolean all) {
        // ограничеия
        DBCommand countCommand = temp.dbDatabase.createCommand();
        temp.countColumn = temp.dbPrimaryKey.count().as("count");
        countCommand.select(temp.countColumn);

        if (!temp.where.isEmpty()) {
            countCommand.addWhereConstraints(temp.where);
        }
//        if (!all) {
//            countCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
//        }
        return countCommand;
    }

    protected DBCommand createSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                            PlainTableFetcherHelper temp) {
        return createSimpleSelectCommand(table, loadConfig, temp);
    }

    /**
     * Запрос без дополнительного подзапроса
     *
     * @param table
     * @param loadConfig
     * @param temp
     * @return
     */
    // TODO: Как-то переделать без лишнего дублирования кода
    protected DBCommand createSimpleSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                                  PlainTableFetcherHelper temp) {

        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            TreeClassLoadConfig tmpConf = (TreeClassLoadConfig) loadConfig;
            if ((tmpConf.getParentColumn() != null && tmpConf.getParent() != null) ||
                    StringUtils.isEmpty(tmpConf.getQuery())) {
                all = true;
            }
        }

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(temp.dbPrimaryKey);
        subCommand.select(temp.dbTable.getColumns());

        temp.topDbPrimaryKey = temp.dbPrimaryKey;
        temp.topDbTable = temp.dbTable;

        // сортировки
        DBColumnExpr orderListExpr = createOrderExpression(table, loadConfig, temp);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }

        DBColumnExpr rowNumber = new DBFuncExpr(orderListExpr, "ROW_NUMBER() OVER (ORDER BY ?)", null, null, false,
                DataType.INTEGER).as("rn");
        subCommand.select(rowNumber);

//        if (!all) {
//            subCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
//        }

        DBQuery subQuery = new DBQuery(subCommand);
        //		subQuery.setAlias("a");

        rowNumber = subQuery.findQueryColumn(rowNumber);

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(subQuery.getQueryColumns());
        topCommand.orderBy(rowNumber.asc());

        if (!all) {
            topCommand.where(rowNumber.isBetween(1 + (loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage(),
                    ((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage()) + loadConfig.getRowsPerPage()));
        }

        addFunctionFields(topCommand, table, loadConfig, temp);
        if (loadConfig instanceof TreeClassLoadConfig) {
            addTreeCommandPart(topCommand, subCommand, (TreeClassLoadConfig) loadConfig, temp);
        }
        return topCommand;
    }

    private void addFunctionFields(DBCommand cmd, PlainTableElement table, ClassLoadConfig loadConfig,
                                   PlainTableFetcherHelper temp) {
        for (TableColumnElement c : table.getColumns()) {
            String function = c.getFunction();
            if (!StringUtils.isEmpty(function)) {
                String expr = resolveValue(function, loadConfig.getParameters());
                cmd.select(temp.dbDatabase.getValueExpr(expr, DataType.UNKNOWN).as(c.getColumnName()));
            }
        }
    }

    private DBColumnExpr createOrderExpression(PlainTableElement table, ClassLoadConfig loadConfig,
                                               PlainTableFetcherHelper temp) {
        DBColumnExpr result = temp.dbPrimaryKey;

        if (!loadConfig.getSorts().isEmpty()) {
            StringBuilder orderString = new StringBuilder();
            for (SortValue s : loadConfig.getSorts()) {
                // Если тип поля - список, сортировать по строке
                if (org.whirlplatform.meta.shared.data.DataType.LIST == s.getField().getType() ||
                    org.whirlplatform.meta.shared.data.DataType.FILE == s.getField().getType()) {
                    orderString.append(s.getField().getLabelColumn());
                } else {
                    orderString.append(s.getField().getName());
                }
                if (s.getOrder() == SortType.DESC) {
                    orderString.append(" DESC");
                }
                orderString.append(",");
            }
            orderString.deleteCharAt(orderString.length() - 1);
            result = temp.dbDatabase.getValueExpr(orderString.toString(), DataType.UNKNOWN);
        } else {
            StringBuilder orderString = new StringBuilder();
            for (TableColumnElement column : table.getSortedColumns()) {
                if (column.isDefaultOrder() && column != table.getDeleteColumn()) {
                    if (org.whirlplatform.meta.shared.data.DataType.LIST == column.getType() ||
                        org.whirlplatform.meta.shared.data.DataType.FILE == column.getType()) {
                        orderString.append(column.getLabelColumn());
                    } else {
                        orderString.append(column.getColumnName());
                    }
                    if (column.getOrder() == Order.DESC) {
                        orderString.append(" DESC");
                    }
                    orderString.append(",");
                }
            }
            if (orderString.length() > 0) {
                orderString.deleteCharAt(orderString.length() - 1);
                result = temp.dbDatabase.getValueExpr(orderString.toString(), DataType.UNKNOWN);
            }
        }
        return result;
    }

    protected void setModelStyledValue(FieldMetadata field, TableColumnElement col, DBReader reader,
                                       RowModelData model) {
        if (col.getConfigColumn() != null) {
            setModelValue(model, field, reader);
            model.setStyle(field.getName(), reader.getString(reader.getFieldIndex(col.getConfigColumn())));
            return;
        }
        int colInd = reader.getFieldIndex(field.getName());
        String value = null;
        String objValue = null;
        Map<String, String> formattedMap;

        // Для списков стиль задается в поле ..dfname
        if (field.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
            int labelInd = reader.getFieldIndex(field.getLabelColumn());
            formattedMap = fromUrlEncoded(reader.getString(labelInd));
            value = formattedMap.get(SrvConstant.VALUE);
            objValue = reader.getString(colInd);
        } else {
            formattedMap = fromUrlEncoded(reader.getString(colInd));
            value = formattedMap.get(SrvConstant.VALUE);
        }
        model.setStyle(field.getName(), formattedMap.get(SrvConstant.STYLE));
        if (field.getType() == org.whirlplatform.meta.shared.data.DataType.DATE && value != null) {
            DateTimeFormat fmt = new DateTimeFormat("dd.MM.yyyy hh:mm:ss", new DefaultDateTimeFormatInfo()) {
            };
            model.set(field.getName(), fmt.parse(value));
        } else {
            model.set(field.getName(), convertValueFromString(value, objValue, field.getType()));
        }
    }


}
