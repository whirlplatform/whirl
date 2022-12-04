package org.whirlplatform.server.driver.multibase.fetch.base;

import java.io.DataInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.driver.multibase.fetch.ParamsUtil;
import org.whirlplatform.server.driver.multibase.fetch.QueryExecutor;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.DBFunctionMessage;
import org.whirlplatform.server.log.impl.ProfileImpl;

public class BaseEventExecutor extends AbstractEventExecutor {
    private static Logger _log = LoggerFactory.getLogger(BaseEventExecutor.class);

    public BaseEventExecutor(ConnectionWrapper connection, QueryExecutor queryExecutor) {
        super(connection, queryExecutor);
    }

    @Override
    public EventResult executeFunction(EventElement eventElement, List<DataValue> paramsDataValue) {
        String schema = eventElement.getSchema();
        String function = eventElement.getFunction();

        if (schema != null && !schema.isEmpty()) {
            function = schema + "." + function;
        }
        List<Object> params;
        if (eventElement.isNamed()) {
            params = ParamsUtil.namedFunctionParams(paramsDataValue, getUser());
        } else {
            params = ParamsUtil.listFunctionParams(paramsDataValue, getUser());
        }

        _log.info("FUNCTION = " + function + "    params = " + params);
        DBFunctionMessage m = new DBFunctionMessage(getUser(), eventElement, paramsDataValue);

        CallableStatement stmt;
        String sql = makeCallQuery(function, params);
        try (Profile p = new ProfileImpl(m)) {
            stmt = getConnection().prepareCall(sql);
            stmt.setQueryTimeout(DBConnection.STMT_TIMEOUT);
            stmt.registerOutParameter(1, Types.VARCHAR);

            // установка параметров
            if (params != null) {
                int paramsCount = params.size();
                for (int i = 2; i < paramsCount + 2; i++) {
                    Object v = params.get(i - 2);
                    try {
                        if (v instanceof DataValueImpl) {
                            DataValueImpl dataValue = (DataValueImpl) v;
                            if (dataValue.getType() == DataType.DATE) {
                                Timestamp time = null;
                                if (dataValue.getDate() != null) {
                                    time = new Timestamp((dataValue.getDate()).getTime());
                                }
                                stmt.setTimestamp(i, time,
                                        Calendar.getInstance(getUser().getTimeZone(),
                                                getUser().getLocale()));
                            }
                        } else {
                            stmt.setObject(i, v);
                        }
                    } catch (SQLException e) {
                        if (v instanceof InputStream) {
                            InputStream stream = (InputStream) v;
                            DataInputStream data = new DataInputStream(stream);
                            stmt.setBinaryStream(i, data, data.available());
                        } else {
                            throw e;
                        }
                    }
                }
            }
            stmt.execute();

            String content = stmt.getString(1);
            return parseEventResult(content);
        } catch (Exception e) {
            String err = function + " params =" + params + '\t' + e + ", sql: " + sql;
            _log.error(err, e);
            m.setError(e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }

}
