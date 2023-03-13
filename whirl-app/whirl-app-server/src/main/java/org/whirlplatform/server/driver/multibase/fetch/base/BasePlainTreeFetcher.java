package org.whirlplatform.server.driver.multibase.fetch.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBQuery;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.data.TreeModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

public class BasePlainTreeFetcher extends BasePlainListFetcher
    implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(BasePlainTreeFetcher.class);

    public BasePlainTreeFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public List<TreeModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                           TreeClassLoadConfig config) {
        PlainTreeFetcherHelper temp = new PlainTreeFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, config);

        List<TreeModelData> result = new ArrayList<TreeModelData>();

        // Добавление пустой записи, если надо
        if (table.isEmptyRow()) {
            TreeModelData empty = new TreeModelDataImpl();
            empty.setId(null);
            empty.setLabel(I18NMessage.getMessage(I18NMessage.getRequestLocale()).noData());
            result.add(empty);
        }

        DBCommand selectCmd = createSelectListCommand(config, temp);
        _log.info("Tree creation query: " + selectCmd.getSelect());

        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());

        try (Profile p = new ProfileImpl(m)) {
            _log.debug("Tree select:\n" + selectCmd.getSelect());

            DBReader selectReader = createAndOpenReader(selectCmd);
            while (selectReader.moveNext()) {
                TreeModelData model = new TreeModelDataImpl();

                // TODO: Доставать не по индексам
                model.setId(selectReader.getString(0));
                model.setLabel(selectReader.getString(1));
                result.add(model);
            }
            selectReader.close();
            List<TreeModelData> data = new ArrayList<TreeModelData>(result);
            return data;
        }
    }

    protected DBCommand createSelectListCommand(ClassLoadConfig loadConfig,
                                                PlainTreeFetcherHelper temp) {

        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr valueColumn = temp.labelExpression;

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(valueColumn);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }
        subCommand.orderBy(temp.labelExpression.lower().asc());

        DBQuery subQuery = new DBQuery(subCommand);
        idColumn = subQuery.findQueryColumn(idColumn);
        valueColumn = subQuery.findQueryColumn(valueColumn);

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(idColumn);
        topCommand.select(valueColumn);

        if (((TreeClassLoadConfig) loadConfig).getParent() != null) {
            subCommand.limitRows(10000);
        }

        return topCommand;
    }
}
