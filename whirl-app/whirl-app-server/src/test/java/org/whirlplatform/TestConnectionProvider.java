package org.whirlplatform;

import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.postgresql.PostgreSQLConnectionWrapper;
import org.whirlplatform.server.login.ApplicationUser;
import javax.sql.DataSource;
import java.sql.Connection;

public class TestConnectionProvider implements ConnectionProvider {

    public String alias;
    public Connection connection;
    public ApplicationUser user;

    public TestConnectionProvider(String alias, Connection connection, ApplicationUser user) {
        this.alias = alias;
        this.connection = connection;
        this.user = user;
    }
    @Override
    public ConnectionWrapper getConnection(String alias) throws ConnectException {
        return new PostgreSQLConnectionWrapper(alias, connection, new ApplicationUser());
    }

    @Override
    public ConnectionWrapper getConnection(String alias, ApplicationUser user) throws ConnectException {
        return new PostgreSQLConnectionWrapper(alias, connection, new ApplicationUser());
    }

    @Override
    public DataSource getDataSource(String alias) throws ConnectException {
        return null;
    }
}
