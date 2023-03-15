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

                String stateResult = selectReader.getString(2);
                if(stateResult == null || stateResult.isEmpty() || stateResult == "false") {
                    model.setState(false);
                } else {
                    model.setState(true);
                }

                String checkResult = selectReader.getString(3);
                if(checkResult == null || checkResult.isEmpty() || checkResult == "false") {
                    model.setIsCheck(false);
                } else {
                    model.setIsCheck(true);
                }

                String selectResult = selectReader.getString(5);
                if(selectResult == null || selectResult.isEmpty() || selectResult == "false") {
                    model.setIsSelect(false);
                } else {
                    model.setIsSelect(true);
                }
                result.add(model);
            }
            selectReader.close();
            List<TreeModelData> data = new ArrayList<>(result);
            return data;
        }
    }

    protected DBCommand createSelectListCommand(ClassLoadConfig loadConfig,
                                                PlainTreeFetcherHelper temp) {

        DBColumnExpr idColumn = temp.dbPrimaryKey;
        DBColumnExpr labelExpressionColumn = temp.labelExpression;
        DBColumnExpr stateExpressionColumn = temp.stateExpression;
        DBColumnExpr checkExpressionColumn = temp.checkExpression;
        DBColumnExpr imageExpressionColumn = temp.imageExpression;
        DBColumnExpr selectExpressionColumn = temp.selectExpression;

        DBCommand subCommand = temp.dbDatabase.createCommand();
        subCommand.select(idColumn);
        subCommand.select(labelExpressionColumn);
        subCommand.select(stateExpressionColumn);
        subCommand.select(checkExpressionColumn);
        subCommand.select(imageExpressionColumn);
        subCommand.select(selectExpressionColumn);

        if (!temp.where.isEmpty()) {
            subCommand.addWhereConstraints(temp.where);
        }
        subCommand.orderBy(temp.labelExpression.lower().asc());

        DBQuery subQuery = new DBQuery(subCommand);
        idColumn = subQuery.findQueryColumn(idColumn);
        labelExpressionColumn = subQuery.findQueryColumn(labelExpressionColumn);
        stateExpressionColumn = subQuery.findQueryColumn(stateExpressionColumn);
        checkExpressionColumn = subQuery.findQueryColumn(checkExpressionColumn);
        imageExpressionColumn = subQuery.findQueryColumn(imageExpressionColumn);
        selectExpressionColumn = subQuery.findQueryColumn(selectExpressionColumn);

        DBCommand topCommand = temp.dbDatabase.createCommand();
        topCommand.select(idColumn);
        topCommand.select(labelExpressionColumn);
        topCommand.select(stateExpressionColumn);
        topCommand.select(checkExpressionColumn);
        topCommand.select(imageExpressionColumn);
        topCommand.select(selectExpressionColumn);

        if (loadConfig.getParent() != null) {
            subCommand.limitRows(10000);
        }

        return topCommand;
    }
}
