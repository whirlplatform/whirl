package org.whirlplatform.server.db.connect;

import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.oracle.DBDatabaseDriverOracle;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import java.sql.Connection;

public class TeradataConnectionWrapper extends ConnectionWrapper {

	private static Logger _log = LoggerFactory
			.getLogger(TeradataConnectionWrapper.class);

	public TeradataConnectionWrapper(String alias, Connection connection,
                                     ApplicationUser user) {
		super(alias, connection, user);
	}

	@Override
	public DBDatabaseDriver getDatabaseDriver() {
		return new DBDatabaseDriverOracle();
	}

	@Override
	public DataSourceDriver getDataSourceDriver() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
