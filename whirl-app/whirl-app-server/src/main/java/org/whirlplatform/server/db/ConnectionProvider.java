
package org.whirlplatform.server.db;

import javax.sql.DataSource;
import org.whirlplatform.server.login.ApplicationUser;

public interface ConnectionProvider {

    ConnectionWrapper getConnection(String alias) throws ConnectException;

    ConnectionWrapper getConnection(String alias, ApplicationUser user) throws ConnectException;

    DataSource getDataSource(String alias) throws ConnectException;

}
