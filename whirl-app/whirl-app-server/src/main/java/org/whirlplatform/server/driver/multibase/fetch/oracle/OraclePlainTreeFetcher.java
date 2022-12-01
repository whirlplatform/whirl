package org.whirlplatform.server.driver.multibase.fetch.oracle;

import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainListFetcherHelper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class OraclePlainTreeFetcher extends OraclePlainTableFetcher
        implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OraclePlainTreeFetcher.class);

    public OraclePlainTreeFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public List<RowModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                          TreeClassLoadConfig loadConfig) {

        PlainListFetcherHelper temp =
                new PlainListFetcherHelper(getConnection(), getDataSourceDriver());
        return getTableData(metadata, table, loadConfig, temp).getData();
    }


}
