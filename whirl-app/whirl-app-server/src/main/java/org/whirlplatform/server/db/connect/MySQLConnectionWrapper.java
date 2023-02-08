package org.whirlplatform.server.db.connect;

import java.sql.Connection;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.mysql.DBDatabaseDriverMySQL;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

public class MySQLConnectionWrapper extends ConnectionWrapper {

    public MySQLConnectionWrapper(String alias, Connection connection, ApplicationUser user) {
        super(alias, connection, user);
    }

    @Override
    public DBDatabaseDriver getDatabaseDriver() {
        return new DBDatabaseDriverMySQL();
    }

    //    @Override
    //    public DataFetcher getDataFetcher(Connector connector) {
    //        // TODO Auto-generated method stub
    //        return null;
    //    }

    @Override
    public DataSourceDriver getDataSourceDriver() {
        // TODO Auto-generated method stub
        return null;
    }

}
