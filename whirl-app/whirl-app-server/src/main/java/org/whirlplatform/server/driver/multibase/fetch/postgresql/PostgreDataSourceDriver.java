package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.*;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainMetadataFetcher;

public class PostgreDataSourceDriver extends AbstractDataSourceDriver {

    public PostgreDataSourceDriver(ConnectionWrapper connection) {
        super(connection);
    }

    @Override
    public <T extends AbstractTableElement> DataFetcher<T> createDataFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (DataFetcher<T>) new PostgrePlainDataFetcher(connection, this);
        }
        throw createException("DataFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> MetadataFetcher<T> createMetadataFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (MetadataFetcher<T>) new PlainMetadataFetcher(connection);
        }
        throw createException("MetadataFetcher", table, connection);
    }

    @Override
    public EventExecutor createEventExecutor() {
        return new PostgreEventExecutor(connection);
    }

    @Override
    public <T extends AbstractTableElement> DataChanger<T> createDataChanger(T table) {
        if (table instanceof PlainTableElement) {
            return (DataChanger<T>) new PostgrePlainDataChanger(connection, this);
        }
        throw createException("DataChanger", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> FileFetcher<T> createFileFetcher(T table) {
        throw createException("FileFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> TableFetcher<T> createTableFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (TableFetcher<T>) new PostgrePlainTableFetcher(connection, this);
        }
        throw createException("TableFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> ListFetcher<T> createListFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (ListFetcher<T>) new PostgrePlainListFetcher(connection, this);
        }
        throw createException("ListFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> TreeFetcher<T> createTreeFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (TreeFetcher<T>) new PostgrePlainTreeFetcher(connection, this);
        }
        throw createException("TreeFetcher", table, connection);
    }

}
