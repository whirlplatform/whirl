package org.whirlplatform.server.driver.multibase.fetch.oracle;

import java.sql.Connection;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.oracle.DBDatabaseDriverOracle;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

public class OracleConnectionWrapper extends ConnectionWrapper {

    private static final DBDatabaseDriverOracle DATABASE_DRIVER = new DBDatabaseDriverOracle();

    public OracleConnectionWrapper(String alias, Connection connection, ApplicationUser user) {
        super(alias, connection, user);
    }

    @Override
    public DBDatabaseDriver getDatabaseDriver() {
        return DATABASE_DRIVER;
    }

    @Override
    public DataSourceDriver getDataSourceDriver() {
        return new OracleDataSourceDriver(this);
    }

}
