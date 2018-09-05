package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.server.db.ConnectionWrapper;

public abstract class AbstractDataSourceDriver implements DataSourceDriver {

	protected ConnectionWrapper connection;

	public AbstractDataSourceDriver(ConnectionWrapper connection) {
		this.connection = connection;
	}
}
