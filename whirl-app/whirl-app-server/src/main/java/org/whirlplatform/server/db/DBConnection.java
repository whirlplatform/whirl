
package org.whirlplatform.server.db;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.utils.ContextUtil;

public class DBConnection {

    public static final int STMT_TIMEOUT = 1000;
    private static Logger _log = LoggerFactory.getLogger(DBConnection.class);

    /**
     * <b>ВНИМАНИЕ!<br>
     * Метод не должен быть использован для получения соединений. Соединения необходимо получать
     * через {@link ConnectionProvider} который можно получить через внедрение зависимостей. Метод
     * оставлен публичным для возможности использования в {@link LoggerFactory}. </b><br>
     * <br>
     * Коннект к БД - получение коннекта из пула.
     *
     * @throws ConnectException
     * @throws IOException
     */
    static ConnectionWrapper getConnection(String alias) throws ConnectException {
        return getConnection(alias, null);
    }

    /**
     * <b> ВНИМАНИЕ!<br>
     * Метод не должен быть использован для получения соединений. Соединения необходимо получать
     * через {@link ConnectionProvider} который можно получить через внедрение зависимостей.
     * </b><br>
     * <br>
     * Коннект к БД - получение коннекта из пула
     *
     * @throws ConnectException
     * @throws IOException
     */
    static ConnectionWrapper getConnection(String alias, ApplicationUser user)
            throws ConnectException {
        ConnectionWrapper connection = null;
        try {
            DataSource ds = getDataSource(alias);
            Connection conn = ds.getConnection();

            Class<ConnectionWrapper> clazz = connectionWrapperClass(alias);
            Constructor<ConnectionWrapper> constructor =
                    clazz.getConstructor(String.class, Connection.class,
                            ApplicationUser.class);
            connection = constructor.newInstance(alias, conn, user);

        } catch (SQLException | NoSuchMethodException | SecurityException | InstantiationException
                 | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            _log.error(
                    I18NMessage.getMessage(I18NMessage.getRequestLocale()).errorDBConnect() + ": " +
                            alias, e);
        }
        if (connection == null) {
            throw new ConnectException(
                    I18NMessage.getMessage(I18NMessage.getRequestLocale()).errorDBConnect());
        }
        return connection;
    }

    @SuppressWarnings("unchecked")
    private static Class<ConnectionWrapper> connectionWrapperClass(String alias)
            throws ConnectException {
        try {
            String driverClassName = ContextUtil.lookup("Whirl/ds/" + alias + "/driver");
            Class<ConnectionWrapper> clazz =
                    (Class<ConnectionWrapper>) Class.forName(driverClassName);
            return clazz;
        } catch (ClassNotFoundException e) {
            _log.error(e);
            throw new ConnectException(e.getMessage());
        }
    }

    static DataSource getDataSource(String alias) throws ConnectException {
        try {
            DataSource ds = ContextUtil.lookup("Whirl/ds/" + alias + "/datasource");
            return ds;
        } catch (SecurityException | IllegalArgumentException e) {
            _log.error(
                    I18NMessage.getMessage(I18NMessage.getRequestLocale()).errorDBConnect() + ": " +
                            alias, e);
            throw new ConnectException(
                    I18NMessage.getMessage(I18NMessage.getRequestLocale()).errorDBConnect());
        }
    }

    public static void closeResources(ResultSet rs) {
        try {
            if (rs != null) {
                Statement statement = rs.getStatement();
                if (statement != null) {
                    statement.close();
                    rs.close();
                }
            }
        } catch (SQLException e) {
            _log.error("Can't close ResultSet: " + e);
        }
    }

    public static void closeResources(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            _log.error("Can't close Statement: " + e);
        }
    }

    public static void closeResources(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            _log.error("Can't close Connection: " + e);
        }
    }

}
