package org.whirlplatform.server.driver.multibase.fetch.base;

import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractDataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.DataChanger;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.EventExecutor;
import org.whirlplatform.server.driver.multibase.fetch.FileFetcher;
import org.whirlplatform.server.driver.multibase.fetch.ListFetcher;
import org.whirlplatform.server.driver.multibase.fetch.TableFetcher;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.driver.multibase.fetch.oracle.BasePlainFileFetcher;

public class BaseDataSourceDriver extends AbstractDataSourceDriver {

    public BaseDataSourceDriver(ConnectionWrapper connection) {
        super(connection);
    }

    @Override
    public <T extends AbstractTableElement> DataFetcher<T> createDataFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (DataFetcher<T>) new BasePlainDataFetcher(connection, this);
        }
        throw createException("DataFetcher", table, connection);
    }

    @Override
    public EventExecutor createEventExecutor() {
        return new BaseEventExecutor(connection, createQueryExecutor());
    }

    @Override
    public <T extends AbstractTableElement> DataChanger<T> createDataChanger(T table) {
        if (table instanceof PlainTableElement) {
            return (DataChanger<T>) new BasePlainDataChanger(connection, this);
        }
        throw createException("DataChanger", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> FileFetcher<T> createFileFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (FileFetcher<T>) new BasePlainFileFetcher(connection);
        }
        throw createException("FileFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> TableFetcher<T> createTableFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (TableFetcher<T>) new BasePlainTableFetcher(connection, this);
        }
        throw createException("TableFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> ListFetcher<T> createListFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (ListFetcher<T>) new BasePlainListFetcher(connection, this);
        }
        throw createException("ListFetcher", table, connection);
    }

    @Override
    public <T extends AbstractTableElement> TreeFetcher<T> createTreeFetcher(T table) {
        if (table instanceof PlainTableElement) {
            return (TreeFetcher<T>) new BasePlainTreeFetcher(connection, this);
        }
        throw createException("TreeFetcher", table, connection);
    }

}
