package org.whirlplatform.server.driver.multibase.fetch.base;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.SortType;
import org.whirlplatform.meta.shared.SortValue;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class BasePlainDataFetcher extends AbstractPlainDataFetcher
    implements DataFetcher<PlainTableElement> {
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(BasePlainDataFetcher.class);

    public BasePlainDataFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
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
        //if (!all) {
        //      countCommand.limitRows(10000);
        //}
        return countCommand;
    }

    protected DBCommand createSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                            PlainTableFetcherHelper temp) {
        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            if (((TreeClassLoadConfig) loadConfig).getParentExpression() != null
                && ((TreeClassLoadConfig) loadConfig).getParent() != null) {
                all = true;
            }
        }
        // ограничения
        DBCommand command = temp.dbDatabase.createCommand();

        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr valueColumn = temp.labelExpression;

        command.select(idColumn);
        command.select(valueColumn);

        for (FieldMetadata f : temp.tableColumns.keySet()) {
            if (f.isView()) { // title не visible = права
                if (f.getType() == org.whirlplatform.meta.shared.data.DataType.LIST
                    || f.getType() == org.whirlplatform.meta.shared.data.DataType.FILE) {
                    DBColumnExpr expression =
                        temp.dbDatabase.getValueExpr(f.getLabelExpression(), DataType.UNKNOWN)
                            .as(f.getName() + LABEL_EXPRESSION_NAME);
                    command.select(expression);
                }
                command.select(temp.dbTable.getColumn(f.getName()));
            }
        }

        if (loadConfig.getSorts().isEmpty()) {
            command.orderBy(temp.dbPrimaryKey);
        } else {
            for (SortValue s : loadConfig.getSorts()) {
                command.orderBy(temp.dbTable.getColumn(s.getField().getName()),
                    s.getOrder() == SortType.DESC);
            }
        }

        if (!temp.where.isEmpty()) {
            command.addWhereConstraints(temp.where);
        }

        //if (!all) {
        //      command.limitRows(10000);
        //}

        if (!all) {
            command.limitRows(
                ((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage())
                    + loadConfig.getRowsPerPage());
            command.skipRows((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage());
        }

        if (loadConfig instanceof TreeClassLoadConfig) {
            addTreeCommandPart(command, command, (TreeClassLoadConfig) loadConfig, temp);
        }
        return command;
    }

    @Override
    public DBReader getTableReader(ClassMetadata metadata, PlainTableElement table,
                                   ClassLoadConfig loadConfig) {
        PlainTableFetcherHelper temp =
            new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);
        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);
        DBReader reader = createAndOpenReader(selectCmd);
        return reader;
    }

}
