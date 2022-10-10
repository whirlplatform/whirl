package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBQuery;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.ListFetcher;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BasePlainListFetcher extends BasePlainTableFetcher implements ListFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(BasePlainListFetcher.class);

    public BasePlainListFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public LoadData<ListModelData> getListData(ClassMetadata metadata, PlainTableElement table,
                                               ClassLoadConfig loadConfig) {
        @SuppressWarnings("unused")
        Date start = new Date();
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
        DBCommand selectCmd = createSelectListCommand(loadConfig, temp);
        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {
            _log.debug("List select:\n" + selectCmd.getSelect());
            DBReader selectReader = createAndOpenReader(selectCmd);
            while (selectReader.moveNext()) {
                ListModelData model = new ListModelDataImpl();

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

    protected DBCommand createSelectListCommand(ClassLoadConfig loadConfig, PlainListFetcherHelper temp) {
        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr valueColumn = temp.listColumn;

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(valueColumn);
        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }
        subCommand.orderBy(temp.listColumn.lower().asc());

        DBQuery subQuery = new DBQuery(subCommand);
        idColumn = subQuery.findQueryColumn(idColumn);
        valueColumn = subQuery.findQueryColumn(valueColumn);

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(idColumn);
        topCommand.select(valueColumn);

        if (loadConfig instanceof TreeClassLoadConfig) {
            if (((TreeClassLoadConfig) loadConfig).getParent() != null) {
                subCommand.limitRows(10000);
            }
        } else {
            topCommand.limitRows(100);
        }

        return topCommand;
    }
}
