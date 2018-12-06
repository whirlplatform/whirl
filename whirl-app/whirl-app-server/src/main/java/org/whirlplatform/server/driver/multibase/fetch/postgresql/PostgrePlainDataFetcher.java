package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

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
//        if (!all) {
//            countCommand.limitRows(10000);
//        }
        return countCommand;
    }

    protected DBCommand createSelectCommand(PlainTableElement table, ClassLoadConfig loadConfig,
                                            PlainTableFetcherHelper temp) {
        boolean all = loadConfig.isAll();
        if (loadConfig instanceof TreeClassLoadConfig) {
            if (((TreeClassLoadConfig) loadConfig).getParentColumn() != null &&
                    ((TreeClassLoadConfig) loadConfig).getParent() != null) {
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

//        if (!all) {
//            command.limitRows(10000);
//        }

        if (!all) {
            command.limitRows(
                    ((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage()) + loadConfig.getRowsPerPage());
            command.skipRows((loadConfig.getPageNum() - 1) * loadConfig.getRowsPerPage());
        }

        if (loadConfig instanceof TreeClassLoadConfig) {
            addTreeCommandPart(command, command, (TreeClassLoadConfig) loadConfig, temp);
        }
        return command;
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
    //    public int getTableRowsCount(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
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
}
