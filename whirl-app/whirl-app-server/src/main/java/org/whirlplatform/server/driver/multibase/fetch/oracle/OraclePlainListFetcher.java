package org.whirlplatform.server.driver.multibase.fetch.oracle;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.*;
import org.apache.empire.db.oracle.OracleRowNumExpr;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.ListFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainListFetcherHelper;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.List;

public class OraclePlainListFetcher extends OraclePlainDataFetcher implements ListFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OraclePlainListFetcher.class);

    public OraclePlainListFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    public LoadData<ListModelData> getListData(ClassMetadata metadata, PlainTableElement table,
                                               ClassLoadConfig loadConfig) {

        PlainListFetcherHelper temp = new PlainListFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);

        List<ListModelData> result = new ArrayList<ListModelData>();

        // Добавление пустой записи, если надо
        if (table.isEmptyRow()) {
            ListModelData empty = new ListModelDataImpl();
            empty.setId(null);
            empty.setLabel(I18NMessage.getMessage(I18NMessage.getRequestLocale()).noData());
            result.add(empty);
        }

        DBCommand selectCmd = createSelectListCommand(table, loadConfig, temp);

        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {
            _log.info("List select:\n" + selectCmd.getSelect());

            DBReader selectReader = new DBReader();
            selectReader.open(selectCmd, getConnection());
            while (selectReader.moveNext()) {
                ListModelData model = new ListModelDataImpl();

                // if (temp.dbLeafExpression != null) {
                if (loadConfig instanceof TreeClassLoadConfig) {
                    model.set("PROPERTY_HAS_CHILDREN", selectReader.getBoolean(temp.dbLeafExpression));
                    model.set("STATE_COLUMN", selectReader.getBoolean(temp.dbStateExpression));
                    String parentField = ((TreeClassLoadConfig) loadConfig).getParentField();
                    model.set(parentField, selectReader.getString(2));
                    if (!StringUtils.isEmpty(loadConfig.getQuery())) {
                        model.setLevelCount(selectReader.getInt(3));
                    }
                }

                // TODO: Доставать не по индексам
                model.setId(selectReader.getString(0));
                model.setLabel(selectReader.getString(1));
                result.add(model);
            }
            selectReader.close();

            LoadData<ListModelData> data = new LoadData<ListModelData>(result);
            return data;
        }
    }

    protected DBCommand createSelectListCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                                PlainListFetcherHelper temp) {
        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr valueColumn = temp.dbListName
                .coalesce(I18NMessage.getMessage(I18NMessage.getRequestLocale()).emptyValue()).as(temp.dbListName);
        DBColumnExpr leafExprColumn = temp.dbLeafExpression;
        DBColumnExpr stateExprColumn = temp.dbStateExpression;
        DBColumn parentColumn = null;

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(valueColumn);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }

        // Добавляем сортировки
        addOrders(subCommand, table, loadConfig, temp);

        DBCommand topCommand = temp.dbDatabase.createCommand();

        DBQuery subQuery = null;
        DBColumnExpr countColumn = null;

        if (loadConfig instanceof TreeClassLoadConfig) {
            TreeClassLoadConfig treeLoadConfig = (TreeClassLoadConfig) loadConfig;
            String parentField = treeLoadConfig.getParentField();
            parentColumn = temp.dbTable.getColumn(parentField);

            subCommand.select(temp.dbListName.as("list_dfname_origin"));
            subCommand.select(parentColumn);

            // if (parentField != null) {
            // Добавление во внутренний запрос столбца наличия дочерних
            // элементов
            String leafExpression = resolveValue(treeLoadConfig.getLeafExpression(), treeLoadConfig.getParameters());
            // DBColumnExpr exprColumn;
            if (leafExpression != null) {
                leafExprColumn = temp.dbDatabase.getValueExpr(leafExpression, DataType.UNKNOWN)
                        .as("PROPERTY_HAS_CHILDREN");
            } else {
                leafExprColumn = temp.dbDatabase.getValueExpr(parentField != null).as("PROPERTY_HAS_CHILDREN");
            }
            subCommand.select(leafExprColumn);

            // Добавление столбца stateExpression
            String stateExpression = resolveValue(treeLoadConfig.getStateExpression(), treeLoadConfig.getParameters());
            if (stateExpression != null) {
                stateExprColumn = temp.dbDatabase.getValueExpr(stateExpression, DataType.UNKNOWN).as("STATE_COLUMN");
            } else {
                stateExprColumn = temp.dbDatabase.getValueExpr(false).as("STATE_COLUMN");
            }
            subCommand.select(stateExprColumn);

            // exprColumn = subQuery.findQueryColumn(exprColumn);
            // topCommand.select(exprColumn.as("PROPERTY_HAS_CHILDREN"));

            if (treeLoadConfig.getParent() != null) {
                subCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(10000));
            }

            // TODO Добавление connect by для поиска в TreeComboBox
//			String query = loadConfig.getQuery();
//			RowModelData parent = treeLoadConfig.getParent();
//			if (!StringUtils.isEmpty(query) && parent == null && !loadConfig.isUseSearchParameters()) {
//				subQuery = ((DBCommandOracle) topCommand).addWithCommand(subCommand);
//				DBColumn newIdColumn = subQuery.findQueryColumn(idColumn);
//				DBColumn newValueColumn = subQuery.findQueryColumn(valueColumn);
//				DBColumn newParentColumn = subQuery.findQueryColumn(parentColumn);
//				subQuery.setAlias("q");
//
//				((DBCommandOracle) topCommand).connectByPrior(newParentColumn.is(newIdColumn));
//				((DBCommandOracle) topCommand).startWith(newValueColumn.likeLower("%" + query.toLowerCase() + "%"));
//				topCommand.selectDistinct();
//				// countColumn = new DBFuncExpr(idColumn.count(),
//				// "(select ? from " + subQuery.getWithName() + " where "
//				// + parentColumn.getName() + " = "
//				// + subQuery.getAlias() + "."
//				// + parentColumn.getName() + ")", null, null,
//				// true, DataType.TEXT);
//				DBQuery countQuery = new DBQuery(subQuery.getCommandExpr());
//				countQuery.setAlias("q2");
//				countQuery.setWithName(subQuery.getWithName());
//				DBColumn parentCountColumn = countQuery.findQueryColumn(parentColumn);
//				countColumn = countQuery.findQueryColumn(idColumn).count().as("counter");
//
//						DBCommand countCmd = temp.dbDatabase.createCommand();
//				countCmd.select(countColumn);
//				countCmd.select(parentCountColumn);
//				countCmd.groupBy(parentCountColumn);
//				DBQuery outerCountQuery = new DBQuery(countCmd);
//				parentCountColumn = outerCountQuery.findQueryColumn(parentCountColumn);
//				topCommand.join(newParentColumn, parentCountColumn, DBJoinType.LEFT);
//				countColumn = outerCountQuery.findQueryColumn(countColumn);
//
//				idColumn = newIdColumn;
//				valueColumn = newValueColumn;
//				parentColumn = newParentColumn;
//
//			}
        } else if (!loadConfig.isAll() && loadConfig.getQuery() == null && !loadConfig.isUseSearchParameters()) {
            topCommand.where(new OracleRowNumExpr(temp.dbDatabase).isLessOrEqual(100));
        }

        if (subQuery == null) {
            subQuery = new DBQuery(subCommand);
            idColumn = subQuery.findQueryColumn(idColumn);
            valueColumn = subQuery.findQueryColumn(valueColumn);
            if (parentColumn != null) {
                parentColumn = subQuery.findQueryColumn(parentColumn);
            }
//			subQuery.setAlias("q");
        }

        topCommand.select(idColumn);
        topCommand.select(valueColumn);
        if (parentColumn != null) {
            topCommand.select(parentColumn);
        }

        if (countColumn != null) {
            topCommand.select(countColumn.as("COUNT_COLUMN"));
        }

        // Вынесение столбца наличия дочерних элементов во внешний запрос
        if (leafExprColumn != null) {
            leafExprColumn = subQuery.findQueryColumn(leafExprColumn);
            topCommand.select(leafExprColumn.as("PROPERTY_HAS_CHILDREN"));
            temp.dbLeafExpression = leafExprColumn;
        }

        if (stateExprColumn != null) {
            stateExprColumn = subQuery.findQueryColumn(stateExprColumn);
            topCommand.select(stateExprColumn.as("STATE_COLUMN"));
            temp.dbStateExpression = stateExprColumn;
        }

        return topCommand;
    }

    private void addOrders(DBCommand command, PlainTableElement table, ClassLoadConfig loadConfig,
                           PlainListFetcherHelper temp) {
        // Добавляем сортировки
        boolean orderAdded = false;
        if (!loadConfig.getSorts().isEmpty()) {
            for (SortValue s : loadConfig.getSorts()) {
                DBColumn col = temp.dbTable.getColumn(s.getField().getName());
                if (col != null) {
                    boolean desc = s.getOrder() == SortType.DESC;
                    command.orderBy(col, desc);
                    orderAdded = true;
                }
            }

        }
        if (!orderAdded) {
            for (TableColumnElement c : table.getColumns()) {
                if (c.isDefaultOrder()) {
                    DBColumn col = temp.dbTable.getColumn(c.getColumnName());
                    if (col != null) {
                        boolean desc = c.getOrder() == Order.DESC;
                        command.orderBy(col, desc);
                        orderAdded = true;
                    }
                }
            }
        }
        if (!orderAdded) {
            command.orderBy(temp.dbListName.lower().asc());
        }
    }
}
