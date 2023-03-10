package org.whirlplatform.server.driver.multibase.fetch.base;

import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class BasePlainTreeFetcher extends BasePlainListFetcher
    implements TreeFetcher<PlainTableElement> {
    private static Logger logger = LoggerFactory.getLogger(BasePlainTreeFetcher.class);

    public BasePlainTreeFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public List<TreeModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                           TreeClassLoadConfig config) {
        PlainListFetcherHelper temp =
            new PlainListFetcherHelper(getConnection(), getDataSourceDriver());
        //return getTableData(metadata, table, config, temp).getData();
//        return getListData(metadata, table, config).getData();
        return null;
    }
}
