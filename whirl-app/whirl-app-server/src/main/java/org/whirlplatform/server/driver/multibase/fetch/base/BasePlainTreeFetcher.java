package org.whirlplatform.server.driver.multibase.fetch.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.whirlplatform.meta.shared.data.RowModelData;
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

public class BasePlainTreeFetcher extends BasePlainTableFetcher
    implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(BasePlainTreeFetcher.class);

    public BasePlainTreeFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public List<ListModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                           TreeClassLoadConfig config) {
        PlainTreeFetcherHelper temp = new PlainTreeFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, config);

        List<ListModelData> result = new ArrayList<ListModelData>();

        // Добавление пустой записи, если надо
        if (table.isEmptyRow()) {
            ListModelData empty = new ListModelDataImpl();
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
                ListModelData model = new ListModelDataImpl();

                // TODO: Доставать не по индексам
                model.setId(selectReader.getString(0));
                model.setLabel(selectReader.getString(1));
                result.add(model);
            }
            selectReader.close();
            List<ListModelData> data = new ArrayList<ListModelData>(result);
            return data;
        }
    }

    protected DBCommand createSelectListCommand(TreeClassLoadConfig loadConfig,
                                                PlainTreeFetcherHelper temp) {

        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr labelExpressionColumn = temp.labelExpression;
        DBColumnExpr stateExpressionColumn = temp.stateExpression;
        DBColumnExpr imageExpressionColumn = temp.imageExpression;
        DBColumnExpr checkExpressionColumn = temp.checkExpression;

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(labelExpressionColumn);
        subCommand.select(stateExpressionColumn);
        subCommand.select(imageExpressionColumn);
        subCommand.select(checkExpressionColumn);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }
        subCommand.orderBy(temp.labelExpression.lower().asc());

        DBQuery subQuery = new DBQuery(subCommand);
        idColumn = subQuery.findQueryColumn(idColumn);
        labelExpressionColumn = subQuery.findQueryColumn(labelExpressionColumn);
        stateExpressionColumn = subQuery.findQueryColumn(stateExpressionColumn);
        imageExpressionColumn = subQuery.findQueryColumn(imageExpressionColumn);
        checkExpressionColumn = subQuery.findQueryColumn(checkExpressionColumn);

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(idColumn);
        topCommand.select(labelExpressionColumn);
        topCommand.select(stateExpressionColumn);
        topCommand.select(imageExpressionColumn);
        topCommand.select(checkExpressionColumn);

        if (loadConfig.getParent() != null) {
            subCommand.limitRows(10000);
        }

        return topCommand;
    }
}
