
package org.whirlplatform.server.db;

import javax.inject.Singleton;
import javax.sql.DataSource;
import org.whirlplatform.server.login.ApplicationUser;

@Singleton
public class TomcatConnectionProvider implements ConnectionProvider {

    public TomcatConnectionProvider() {
    }

    @Override
    public ConnectionWrapper getConnection(String alias) throws ConnectException {
        return DBConnection.getConnection(alias);
    }

    @Override
    public ConnectionWrapper getConnection(String alias, ApplicationUser user)
            throws ConnectException {
        return DBConnection.getConnection(alias, user);
    }

    @Override
    public DataSource getDataSource(String alias) throws ConnectException {
        return DBConnection.getDataSource(alias);
    }

}
