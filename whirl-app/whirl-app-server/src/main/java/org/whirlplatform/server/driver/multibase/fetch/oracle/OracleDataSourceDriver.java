package org.whirlplatform.server.driver.multibase.fetch.oracle;

import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.*;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainMetadataFetcher;

public class OracleDataSourceDriver extends AbstractDataSourceDriver {

	public OracleDataSourceDriver(ConnectionWrapper connection) {
		super(connection);
	}

	@Override
	public <T extends AbstractTableElement> DataFetcher<T> createDataFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (DataFetcher<T>) new OraclePlainDataFetcher(connection, this);
		}
		if (table instanceof DynamicTableElement) {
			return (DataFetcher<T>) new OracleDynamicDataFetcher(connection, this);
		}
		throw createException("DataFetcher", table, connection);
	}

	@Override
	public <T extends AbstractTableElement> MetadataFetcher<T> createMetadataFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (MetadataFetcher<T>) new PlainMetadataFetcher(connection);
		}
		if (table instanceof DynamicTableElement) {
			return (MetadataFetcher<T>) new OracleDynamicMetadataFetcher(connection);
		}
		throw createException("MetadataFetcher", table, connection);
	}

	@Override
	public EventExecutor createEventExecutor() {
		return new OracleEventExecutor(connection);
	}

	@Override
	public <T extends AbstractTableElement> DataChanger<T> createDataChanger(T table) {
		if (table instanceof PlainTableElement) {
			return (DataChanger<T>) new OraclePlainDataChanger(connection, this);
		}
		if (table instanceof DynamicTableElement) {
			return (DataChanger<T>) new OracleDynamicDataChanger(connection);

		}
		throw createException("DataChanger", table, connection);
	}

	@Override
	public <T extends AbstractTableElement> FileFetcher<T> createFileFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (FileFetcher<T>) new OraclePlainFileFetcher(connection);
		}
		throw createException("FileFetcher", table, connection);
	}

	@Override
	public <T extends AbstractTableElement> TableFetcher<T> createTableFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (TableFetcher<T>) new OraclePlainTableFetcher(connection, this);
		}
		if (table instanceof DynamicTableElement) {
			return (TableFetcher<T>) new OracleDynamicTableFetcher(connection, this);
		}
		throw createException("TableFetcher", table, connection);
	}

	@Override
	public <T extends AbstractTableElement> ListFetcher<T> createListFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (ListFetcher<T>) new OraclePlainListFetcher(connection, this);
		}
		if (table instanceof DynamicTableElement) {
			return (ListFetcher<T>) new OracleDynamicListFetcher(connection, this);
		}
		throw createException("ListFetcher", table, connection);
	}

	@Override
	public <T extends AbstractTableElement> TreeFetcher<T> createTreeFetcher(T table) {
		if (table instanceof PlainTableElement) {
			return (TreeFetcher<T>) new OraclePlainTreeFetcher(connection, this);
		}
		if (table instanceof DynamicTableElement) {
			return (TreeFetcher<T>) new OracleDynamicTreeFetcher(connection, this);
		}
		throw createException("TreeFetcher", table, connection);
	}

}
