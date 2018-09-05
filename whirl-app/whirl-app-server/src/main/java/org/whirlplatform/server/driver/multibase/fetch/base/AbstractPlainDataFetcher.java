package org.whirlplatform.server.driver.multibase.fetch.base;

import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

public abstract class AbstractPlainDataFetcher extends AbstractMultiFetcher {

	public AbstractPlainDataFetcher(ConnectionWrapper connection, DataSourceDriver datasourceDriver) {
		super(connection, datasourceDriver);
	}

	public DataType getIdColumnType(PlainTableElement table) {
		return table.getIdColumn().getType();
	}
}
