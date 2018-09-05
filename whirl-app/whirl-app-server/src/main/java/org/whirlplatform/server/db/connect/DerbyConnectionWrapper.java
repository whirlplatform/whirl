package org.whirlplatform.server.db.connect;

import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.derby.DBDatabaseDriverDerby;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

import java.sql.Connection;

public class DerbyConnectionWrapper extends ConnectionWrapper {

    public DerbyConnectionWrapper(String alias, Connection connection, ApplicationUser user) {
		super(alias, connection, user);
	}

	@Override
	public DBDatabaseDriver getDatabaseDriver() {
		return new DBDatabaseDriverDerby();
	}

//	@Override
//	public DataFetcher getDataFetcher(Connector connector) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public DataSourceDriver getDataSourceDriver() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
