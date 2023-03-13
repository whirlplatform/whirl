package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.commons.StringUtils;

import org.apache.empire.db.DBColumnExpr;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;


public class PlainListFetcherHelper extends PlainTableFetcherHelper {

    public DBColumnExpr labelExpression;

    public PlainListFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig config) {
        super.prepare(metadata, table, config);

        String query = config.getQuery();
        if (!StringUtils.isEmpty(query) && !(config instanceof TreeClassLoadConfig)) {
            this.where.add(createContainsForCombobox(this.labelExpression, query));
        }
    }
}
