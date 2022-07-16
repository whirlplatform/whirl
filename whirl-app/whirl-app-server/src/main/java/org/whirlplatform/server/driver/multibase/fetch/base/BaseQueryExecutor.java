package org.whirlplatform.server.driver.multibase.fetch.base;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.driver.multibase.fetch.AbstractQueryExecutor;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.QueryMessage;
import org.whirlplatform.server.monitor.RunningEvent;

import java.sql.ResultSet;
import java.util.Map;

public class BaseQueryExecutor extends AbstractQueryExecutor {

    private static Logger _log = LoggerFactory.getLogger(BaseQueryExecutor.class);

    private ConnectionWrapper connection;

    public BaseQueryExecutor(ConnectionWrapper connection) {
        this.connection = connection;
    }

    @Override
    public Map<String, DataValue> executeQuery(String sql, Map<String, DataValue> params) {
        String query = prepareSql(connection.getDatabaseDriver(), sql, params);

        QueryMessage msg = new QueryMessage(connection.getUser(), query);

        RunningEvent ev = new RunningEvent(RunningEvent.Type.DBEVENT, "", query, connection.getUser().getLogin()) {

            @Override
            public void onStop() {
                // Есть возможность остановить только для Oracle
            }
        };
        try (Profile p = new ProfileImpl(msg, ev)) {
            ResultSet resultSet = connection.getDatabaseDriver().executeQuery(query, null, false, connection);
            if (resultSet.next()) {
                Map<String, DataValue> resultValues = collectResultSetValue(connection.getDatabaseDriver(),
                        resultSet);
                if (resultSet.next()) {
                    _log.warn("Query should return only 1 row. More than 1 rows returned.\n" + query + " params =" + params + '\n' + ", sql: " + sql);
                    throw new CustomException("Query should return 1 row.");
                }
                return resultValues;
            }
            _log.warn("Query should return only 1 row. 0 rows returned.\n" + query + " params =" + params + '\n' + ", sql: " + sql);
            throw new CustomException("Query should return 1 row.");
        } catch (Exception e) {
            String err = query + " params =" + params + '\t' + e + ", sql: " + sql;
            _log.error(err, e);
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void close() {
        DBConnection.closeResources(connection);
    }
}
