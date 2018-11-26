package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.postgresql.DBDatabaseDriverPostgreSQL;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

import java.sql.Connection;

public class PostgreSQLConnectionWrapper extends ConnectionWrapper {

    private static final DBDatabaseDriverPostgreSQL DATABASE_DRIVER = new DBDatabaseDriverPostgreSQL();

    public PostgreSQLConnectionWrapper(String alias, Connection connection, ApplicationUser user) {
        super(alias, connection, user);
    }

    @Override
    public DBDatabaseDriver getDatabaseDriver() {
        return DATABASE_DRIVER;
    }

    @Override
    public DataSourceDriver getDataSourceDriver() {
        return new PostgreDataSourceDriver(this);
    }

}
