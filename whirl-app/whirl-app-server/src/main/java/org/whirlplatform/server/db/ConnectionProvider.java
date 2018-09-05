
package org.whirlplatform.server.db;

import org.whirlplatform.server.login.ApplicationUser;

import javax.sql.DataSource;

public interface ConnectionProvider {

    ConnectionWrapper getConnection(String alias) throws ConnectException;

    ConnectionWrapper getConnection(String alias, ApplicationUser user) throws ConnectException;
    
    DataSource getDataSource(String alias) throws ConnectException;
    
}
