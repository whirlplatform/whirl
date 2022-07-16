package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.base.BaseQueryExecutor;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainMetadataFetcher;

public abstract class AbstractDataSourceDriver implements DataSourceDriver {

    protected ConnectionWrapper connection;

    public AbstractDataSourceDriver(ConnectionWrapper connection) {
        this.connection = connection;
    }

    @Override
    public <T extends AbstractTableElement> MetadataFetcher<T> createMetadataFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (MetadataFetcher<T>) new PlainMetadataFetcher(connection);
        }
        throw createException("MetadataFetcher", table, connection);
    }

    @Override
    public QueryExecutor createQueryExecutor() {
        return new BaseQueryExecutor(connection);
    }
}
