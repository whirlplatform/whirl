package org.whirlplatform.server.driver.multibase.fetch;

import org.apache.commons.lang.NotImplementedException;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;

public interface DataSourceDriver {

    <T extends AbstractTableElement> DataFetcher<T> createDataFetcher(T table);

    <T extends AbstractTableElement> MetadataFetcher<T> createMetadataFetcher(T table);

    EventExecutor createEventExecutor();

    QueryExecutor createQueryExecutor();

    <T extends AbstractTableElement> DataChanger<T> createDataChanger(T table);

    <T extends AbstractTableElement> FileFetcher<T> createFileFetcher(T table);

    <T extends AbstractTableElement> TableFetcher<T> createTableFetcher(T table);

    <T extends AbstractTableElement> ListFetcher<T> createListFetcher(T table);

    <T extends AbstractTableElement> TreeFetcher<T> createTreeFetcher(T table);

    default <T extends AbstractTableElement> NotImplementedException createException(
        final String fetcherName, T table,
        ConnectionWrapper connection) {
        String tableClass = (table == null) ? "null" : table.getClass().getSimpleName();
        String connClass = (connection == null) ? "null" : connection.getClass().getSimpleName();
        String message =
            String.format("Fetcher '%s' is not implemented for table=%s conn=%s", fetcherName,
                tableClass,
                connClass);
        NotImplementedException result = new NotImplementedException(message);
        return result;
    }

}
