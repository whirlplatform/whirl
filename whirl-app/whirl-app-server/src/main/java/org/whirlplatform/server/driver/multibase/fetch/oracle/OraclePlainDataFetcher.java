package org.whirlplatform.server.driver.multibase.fetch.oracle;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.*;
import org.apache.empire.db.expr.column.DBFuncExpr;
import org.apache.empire.db.oracle.OracleRowNumExpr;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTreeFetcherHelper;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.login.ApplicationUser;

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

    // TODO не используется
    public int getTableRowsCount(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig,
                                 ApplicationUser user) {
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

    // TODO не используется
    public String getListLabel(PlainTableElement table, String recordId, ApplicationUser user) {
        String result = null;
        DBDatabase dbDatabase = createAndOpenDatabase(table.getSchema().getSchemaName());
        DBTable dbTable = new DBTable(table.getList().getViewName(), dbDatabase);
        DBColumn idColumn = dbTable.addColumn(table.getIdColumn().getColumnName(),
                org.apache.empire.data.DataType.INTEGER, 0, true);
        DBColumn labelColumn = dbTable.addColumn(SrvConstant.COLUMN_LIST_TITLE, org.apache.empire.data.DataType.TEXT, 0,
                false);
        DBCommand cmd = dbDatabase.createCommand();
        cmd.select(labelColumn);
        cmd.where(idColumn.is(recordId));

        DBReader reader = createAndOpenReader(cmd);
        if (reader.moveNext()) {
            result = reader.getString(labelColumn);
        }
        if (result == null) {
            result = I18NMessage.getMessage(I18NMessage.getRequestLocale()).emptyValue();
        }
        return result;
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
            countCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
        }
        return countCommand;
    }

    protected DBCommand createSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                            PlainTableFetcherHelper temp) {
        if (table.isSimple()) {
            return createSimpleSelectCommand(table, loadConfig, temp);
        }

        if (!StringUtils.isEmpty(loadConfig.getQuery())) {
            return createSelectTreeSearchCommand(table, (TreeClassLoadConfig) loadConfig,
                    (PlainTreeFetcherHelper) temp);
        }

        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            TreeClassLoadConfig tmpConf = (TreeClassLoadConfig) loadConfig;
            if ((tmpConf.getParentField() != null && tmpConf.getParent() != null)
                    || StringUtils.isEmpty(tmpConf.getQuery())) {
                all = true;
            }
        }

        DBCommand subCommand = temp.dbDatabase.createCommand();
        DBColumnExpr pkTmp = temp.dbPrimaryKey.as("NESTED_TEMP_PK");
        subCommand.select(pkTmp);

        // сортировки
        DBColumnExpr orderListExpr = createOrderExpression(table, loadConfig, temp);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }

        DBColumnExpr rowNumber = new DBFuncExpr(orderListExpr, "ROW_NUMBER() OVER (ORDER BY ?)", null, null, false,
                DataType.INTEGER).as("rn");
        subCommand.select(rowNumber);

        if (!all) {
            subCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
        }

        DBQuery subQuery = new DBQuery(subCommand);
//		subQuery.setAlias("a");

        pkTmp = subQuery.findQueryColumn(pkTmp);
        rowNumber = subQuery.findQueryColumn(rowNumber);

        if (!all) {
            DBCommand limitCommand = temp.dbDatabase.createCommand();

            limitCommand.select(pkTmp);
            limitCommand.select(rowNumber);
            limitCommand.where(rowNumber.isBetween(1 + (loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage(),
                    ((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage()) + loadConfig.getRowsPerPage()));

            DBQuery limitQuery = new DBQuery(limitCommand);
//			limitQuery.setAlias("a");
            pkTmp = limitQuery.findQueryColumn(pkTmp);
            rowNumber = limitQuery.findQueryColumn(rowNumber);
        }

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(temp.topDbPrimaryKey);
        topCommand.select(temp.topDbTable.getColumns());
        topCommand.where(temp.topDbPrimaryKey.is(pkTmp));
        topCommand.orderBy(rowNumber.asc());

        addFunctionFields(topCommand, table, loadConfig, temp);
        if (loadConfig instanceof TreeClassLoadConfig) {
            addTreeCommandPart(topCommand, subCommand, (TreeClassLoadConfig) loadConfig, temp);
        }
        return topCommand;
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
        if (!StringUtils.isEmpty(loadConfig.getQuery())) {
            return createSelectTreeSearchCommand(table, (TreeClassLoadConfig) loadConfig,
                    (PlainTreeFetcherHelper) temp);
        }

        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            TreeClassLoadConfig tmpConf = (TreeClassLoadConfig) loadConfig;
            if ((tmpConf.getParentField() != null && tmpConf.getParent() != null)
                    || StringUtils.isEmpty(tmpConf.getQuery())) {
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

        if (!all) {
            subCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
        }

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
                cmd.select(temp.dbDatabase.getValueExpr(expr, DataType.UNKNOWN)
                        .as(c.getColumnName()));
            }
        }
    }

    /**
     * Добавляет поля и условия в команды для запроса дерева
     *
     * @param cmd        - внешний запрос
     * @param whereCmd   - команда для добавления ограничений
     * @param loadConfig
     * @param temp
     */
    private void addTreeCommandPart(DBCommand cmd, DBCommand whereCmd, TreeClassLoadConfig loadConfig,
                                    PlainTableFetcherHelper temp) {
        DBColumn parentColumn = null;

        TreeClassLoadConfig treeLoadConfig = loadConfig;
        String parentField = treeLoadConfig.getParentField();
        // if (parentField != null) {
        // Установка столбца наличия дочерних элементов
        String leafExpression = resolveValue(treeLoadConfig.getLeafExpression(), treeLoadConfig.getParameters());
        DBColumnExpr exprColumn;
        if (leafExpression != null) {
            exprColumn = temp.dbDatabase.getValueExpr(leafExpression, DataType.UNKNOWN).as("PROPERTY_HAS_CHILDREN");
        } else {
            exprColumn = temp.dbDatabase.getValueExpr(parentField != null).as("PROPERTY_HAS_CHILDREN");
        }
        cmd.select(exprColumn);
        ((PlainTreeFetcherHelper) temp).dbLeafExpression = exprColumn;

        // Установка столбца развернутых записей
        String stateExpression = resolveValue(treeLoadConfig.getStateExpression(), treeLoadConfig.getParameters());
        DBColumnExpr stateColumn;
        if (stateExpression != null) {
            stateColumn = temp.dbDatabase.getValueExpr(stateExpression, DataType.UNKNOWN).as("STATE_COLUMN");
        } else {
            stateColumn = temp.dbDatabase.getValueExpr(false).as("STATE_COLUMN");
        }
        cmd.select(stateColumn);
        ((PlainTreeFetcherHelper) temp).dbStateExpression = stateColumn;

        // Установка столбца отмеченных значений
        String checkExpression = resolveValue(treeLoadConfig.getCheckExpression(), treeLoadConfig.getParameters());
        DBColumnExpr checkColumn;
        if (checkExpression != null) {
            checkColumn = temp.dbDatabase.getValueExpr(checkExpression, DataType.UNKNOWN).as("CHECK_COLUMN");
        } else {
            checkColumn = temp.dbDatabase.getValueExpr(false).as("CHECK_COLUMN");
        }
        cmd.select(checkColumn);
        ((PlainTreeFetcherHelper) temp).dbCheckExpression = checkColumn;

        // Установка столбца выбранных значений
        String selectExpression = resolveValue(treeLoadConfig.getSelectExpression(), treeLoadConfig.getParameters());
        DBColumnExpr selectColumn;
        if (selectExpression != null) {
            selectColumn = temp.dbDatabase.getValueExpr(selectExpression, DataType.UNKNOWN).as("SELECT_COLUMN");
        } else {
            selectColumn = temp.dbDatabase.getValueExpr(false).as("SELECT_COLUMN");
        }
        cmd.select(selectColumn);
        ((PlainTreeFetcherHelper) temp).dbIsSelectExpression = selectColumn;

        // Установка столбца названия
        String nameExpression = resolveValue(treeLoadConfig.getNameExpression(), treeLoadConfig.getParameters());
        DBColumnExpr nameColumn;
        if (nameExpression == null) {
            nameExpression = SrvConstant.DEFAULT_NAME_COLUMN;
        }
        nameColumn = temp.dbDatabase.getValueExpr(nameExpression, DataType.UNKNOWN).as("NAME_COLUMN");

        cmd.select(nameColumn);
        ((PlainTreeFetcherHelper) temp).dbNameExpression = nameColumn;

        if (parentField != null) {
            RowModelData parent = treeLoadConfig.getParent();
            parentColumn = temp.dbTable.getColumn(parentField);
            if (parent != null) {
                whereCmd.where(parentColumn.is(parent.getId()));
            } else {
                whereCmd.where(parentColumn.cmp(DBCmpType.NULL, null));
            }
        }
    }

    // TODO: refactor
    protected DBCommand createSelectTreeSearchCommand(PlainTableElement table, TreeClassLoadConfig loadConfig,
                                                      PlainTreeFetcherHelper temp) {
        String nameExpression = resolveValue(loadConfig.getNameExpression(), loadConfig.getParameters());
        if (nameExpression == null) {
            nameExpression = SrvConstant.DEFAULT_NAME_COLUMN;
        }

        DBColumn idColumn = temp.dbPrimaryKey;
        DBColumnExpr nameColumn = temp.dbDatabase.getValueExpr(nameExpression, DataType.UNKNOWN).as("NAME_COLUMN");
        DBColumnExpr parentColumn = temp.dbTable.getColumn(loadConfig.getParentField());
        DBColumnExpr stateExprColumn = temp.dbStateExpression;
        DBColumnExpr nameConfigColumn = null;

        String leafExpression = loadConfig.getLeafExpression();
        DBColumnExpr leafExprColumn;
        if (leafExpression != null) {
            leafExprColumn = temp.dbDatabase.getValueExpr(leafExpression, DataType.UNKNOWN).as("PROPERTY_HAS_CHILDREN");
        } else {
            leafExprColumn = temp.dbDatabase.getValueExpr(loadConfig.getParentField() != null)
                    .as("PROPERTY_HAS_CHILDREN");
        }
        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(nameColumn);
        subCommand.select(parentColumn);
        subCommand.select(stateExprColumn);
        subCommand.select(leafExprColumn);

        TableColumnElement nameColumnEl = table.getColumn(loadConfig.getNameExpression());
        if (nameColumnEl != null) {
            // Посмотрел по исходникам, проверки на null не нужны
            nameConfigColumn = temp.dbTable.getColumn(nameColumnEl.getConfigColumn());
            subCommand.select(nameConfigColumn);
        }

        DBCommand topCommand = temp.dbDatabase.createCommand();
        DBQuery subQuery = null;
        DBColumnExpr countColumn = null;
        String query = loadConfig.getQuery();
        RowModelData parent = loadConfig.getParent();

//		if (!StringUtils.isEmpty(query) && parent == null && !loadConfig.isUseSearchParameters()) {
//			subQuery = ((DBCommandOracle) topCommand).addWithCommand(subCommand);
//			DBColumn newIdColumn = subQuery.findQueryColumn(idColumn);
//			DBColumn newNameColumn = subQuery.findQueryColumn(nameColumn);
//			DBColumn newParentColumn = subQuery.findQueryColumn(parentColumn);
//			stateExprColumn = subQuery.findQueryColumn(stateExprColumn);
//			leafExprColumn = subQuery.findQueryColumn(leafExprColumn);
//			nameConfigColumn = subQuery.findQueryColumn(nameConfigColumn);
//			subQuery.setAlias("q");
//
//			((DBCommandOracle) topCommand).connectByPrior(newParentColumn.is(newIdColumn));
//			((DBCommandOracle) topCommand).startWith(newNameColumn.likeLower("%" + query.toLowerCase() + "%"));
//			topCommand.selectDistinct();
//			DBQuery countQuery = new DBQuery(subQuery.getCommandExpr());
//			countQuery.setAlias("q2");
//			countQuery.setWithName(subQuery.getWithName());
//			DBColumn parentCountColumn = countQuery.findQueryColumn(parentColumn);
//			countColumn = countQuery.findQueryColumn(idColumn).count().as("counter");
//
//					DBCommand countCmd = temp.dbDatabase.createCommand();
//			countCmd.select(countColumn);
//			countCmd.select(parentCountColumn);
//			countCmd.groupBy(parentCountColumn);
//			DBQuery outerCountQuery = new DBQuery(countCmd);
//			parentCountColumn = outerCountQuery.findQueryColumn(parentCountColumn);
//			topCommand.join(newParentColumn, parentCountColumn, DBJoinType.LEFT);
//			countColumn = outerCountQuery.findQueryColumn(countColumn);
//
//			idColumn = newIdColumn;
//			nameColumn = newNameColumn;
//			parentColumn = newParentColumn;
//		}

        if (subQuery == null) {
            subQuery = new DBQuery(subCommand);
            idColumn = subQuery.findQueryColumn(idColumn);
            nameColumn = subQuery.findQueryColumn(nameColumn);
//			subQuery.setAlias("q");
        }

        topCommand.select(idColumn);
        temp.topDbPrimaryKey = idColumn;
        topCommand.select(nameColumn.as("NAME_COLUMN"));
        temp.dbNameExpression = nameColumn;
        if (parentColumn != null) {
            topCommand.select(parentColumn);
            temp.dbParentColumn = parentColumn;
        }

        if (countColumn != null) {
            topCommand.select(countColumn.as("COUNT_COLUMN"));
        }

        if (stateExprColumn != null) {
            topCommand.select(stateExprColumn.as("STATE_COLUMN"));
            temp.dbStateExpression = stateExprColumn;
        }
        if (leafExprColumn != null) {
            topCommand.select(leafExprColumn.as("PROPERTY_HAS_CHILDREN"));
            temp.dbLeafExpression = leafExprColumn;
        }
        if (nameConfigColumn != null) {
            topCommand.select(nameConfigColumn);
        }

        return topCommand;
    }

    private DBColumnExpr createOrderExpression(PlainTableElement table, ClassLoadConfig loadConfig,
                                               PlainTableFetcherHelper temp) {
        DBColumnExpr result = temp.dbPrimaryKey;

        if (!loadConfig.getSorts().isEmpty()) {
            StringBuilder orderString = new StringBuilder();
            for (SortValue s : loadConfig.getSorts()) {
                // Если тип поля - список, сортировать по строке
                if (org.whirlplatform.meta.shared.data.DataType.LIST == s.getField().getType()) {
                    orderString.append(s.getField().getName() + "DFNAME");
                } else if (org.whirlplatform.meta.shared.data.DataType.FILE == s.getField().getType()) {
                    orderString.append(s.getField().getName() + SrvConstant.COLUMN_FILE_POSTFIX);
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
                    if (org.whirlplatform.meta.shared.data.DataType.LIST == column.getType()) {
                        orderString.append(column.getColumnName() + "DFNAME");
                    } else if (org.whirlplatform.meta.shared.data.DataType.FILE == column.getType()) {
                        orderString.append(column.getColumnName() + SrvConstant.COLUMN_FILE_POSTFIX);
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
            int labelInd = reader.getFieldIndex(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX);
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

    protected void setModelValue(RowModelData model, FieldMetadata field, DBReader reader) {
        int colInd = reader.getFieldIndex(field.getName());
        org.whirlplatform.meta.shared.data.DataType colDataType = field.getType();
        if (colDataType != null) {
            switch (colDataType) {
                case STRING:
                    model.set(field.getName(), reader.isNull(colInd) ? null : reader.getString(colInd));
                    break;
                case NUMBER:
                    model.set(field.getName(), reader.isNull(colInd) ? null : reader.getDouble(colInd));
                    break;
                case DATE:
                    model.set(field.getName(), reader.isNull(colInd) ? null : reader.getDateTime(colInd));
                    break;
                case BOOLEAN:
                    model.set(field.getName(), reader.isNull(colInd) ? null : reader.getBoolean(colInd));
                    break;
                case LIST:
                    int labelInd = reader.getFieldIndex(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX);
                    ListModelData listValue = new ListModelDataImpl();
                    listValue.setLabel(reader.getString(labelInd));
                    listValue.setId(reader.getString(colInd));
                    model.set(field.getName(), listValue);
                    break;
                case FILE:
                    int fileInd = reader.getFieldIndex(field.getName() + SrvConstant.COLUMN_FILE_POSTFIX);
                    FileValue fileValue = new FileValue();
                    fileValue.setName(reader.getString(fileInd));
                    model.set(field.getName(), fileValue);
                    break;
                default:
                    break;
            }
        }
    }
}
