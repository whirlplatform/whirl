package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTreeFetcherHelper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.List;

public class PostgrePlainTreeFetcher extends PostgrePlainDataFetcher implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(PostgrePlainTreeFetcher.class);

    public PostgrePlainTreeFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public List<RowModelData> getTreeData(ClassMetadata metadata, PlainTableElement table, TreeClassLoadConfig config) {
        PlainTreeFetcherHelper temp = new PlainTreeFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, config);
        List<RowModelData> result = new ArrayList<RowModelData>();
        DBCommand selectCmd = createSelectCommand(table, config, temp);
        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {
            _log.debug("Tree select:\n" + selectCmd.getSelect());
            DBReader selectReader = createAndOpenReader(selectCmd);
            while (selectReader.moveNext()) {
                RowModelData model = createRowModel(selectReader, temp, metadata);
                result.add(model);
            }
            selectReader.close();
            return result;
        }
    }

    protected RowModelData createRowModel(DBReader selectReader, PlainTreeFetcherHelper temp, ClassMetadata metadata) {
        RowModelData model = new RowModelDataImpl();
        model.setId(selectReader.getString(temp.topDbPrimaryKey));
        for (FieldMetadata f : metadata.getFields()) {
            setModelValue(model, f, selectReader, temp);
        }
        if (temp.dbLeafExpression != null) {
            model.set("PROPERTY_HAS_CHILDREN", selectReader.getBoolean(temp.dbLeafExpression));
        }
        return model;
    }
}
