package org.whirlplatform.server.driver.multibase.fetch.mysql;

import java.sql.Connection;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.mysql.DBDatabaseDriverMySQL;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.BaseDataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

public class MySQLConnectionWrapper extends ConnectionWrapper {

    private static final DBDatabaseDriverMySQL DATABASE_DRIVER = new DBDatabaseDriverMySQL();

    public MySQLConnectionWrapper(String alias, Connection connection, ApplicationUser user) {
        super(alias, connection, user);
    }

    @Override
    public DBDatabaseDriver getDatabaseDriver() {
        return DATABASE_DRIVER;
    }

    @Override
    public DataSourceDriver getDataSourceDriver() {
        return new BaseDataSourceDriver(this);
    }

}
