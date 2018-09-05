package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.JavaEventResult;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.ParamsUtil;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractEventExecutor;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.DBFunctionMessage;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.utils.ServerJSONConverter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class PostgreEventExecutor extends AbstractEventExecutor {
    private static Logger _log = LoggerFactory.getLogger(PostgreEventExecutor.class);

    public PostgreEventExecutor(ConnectionWrapper connection) {
        super(connection);
    }

    @Override
    public EventResult executeFunction(EventElement eventElement, List<DataValue> paramsDataValue) {
        @SuppressWarnings("unused")
        String error = null;
        @SuppressWarnings("unused")
        Date start = new Date();

        String schema = eventElement.getSchema();
        String function = eventElement.getFunction();

        if (schema != null && !schema.isEmpty()) {
            function = schema + "." + function;
        }
        List<Object> params = new ArrayList<Object>();
        if (eventElement.isNamed()) {
            params = ParamsUtil.namedFunctionParams(paramsDataValue, getUser());
        } else {
            params = ParamsUtil.listFunctionParams(paramsDataValue, getUser());
        }

        _log.info("FUNCTION = " + function + "	params = " + params);
        DBFunctionMessage m = new DBFunctionMessage(getUser(), eventElement, paramsDataValue);

        CallableStatement stmt = null;
        String sql = null;
        try (Profile p = new ProfileImpl(m)) {
            sql = "{? = call " + function;
            if (function.indexOf("?") == -1) {
                sql += "(";
                for (int i = 0; i < params.size(); i++) {
                    sql += "?,";
                }

                sql = sql.substring(0, sql.length() - 1);
                if (params.size() != 0)
                    sql += ")";
            }
            sql += " }";

            stmt = getConnection().prepareCall(sql);
            stmt.registerOutParameter(1, Types.CLOB);

            // установка параметров
            if (params != null) {
                int paramsCount = params.size();
                for (int i = 2; i < paramsCount + 2; i++) {
                    Object v = params.get(i - 2);
                    try {
                        // Т.к. по умолчанию boolean передается 1/0
                        if (v instanceof DataValueImpl) {
                            DataValueImpl dataValue = (DataValueImpl) v;
                            if (dataValue.getType() == DataType.DATE) {
                                Timestamp time = null;
                                if (dataValue.getValue() != null) {
                                    time = new Timestamp(((Date) dataValue.getValue()).getTime());
                                }
                                stmt.setTimestamp(i, time, Calendar.getInstance(getUser().getTimeZone(),
                                        getUser().getLocale()));
                            }
                        } else {
                            stmt.setObject(i, v);
                        }
                    } catch (SQLException e) {
                        // если не выйдет, и передаваемый объект - строка,
                        // попробуем передать через ридер
                        // на случай, если параметр - клоб, а длина строки >
                        // 32768
                        if (v instanceof String) {
                            Clob c = createTemporaryClob();
                            c.setString(1, (String) v);
                            stmt.setClob(i, c);
                        } else if (v instanceof InputStream) {
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

            Clob clob = (Clob) stmt.getObject(1);
            String str, content = "";
            if (clob != null) {
                BufferedReader re = new BufferedReader(clob.getCharacterStream());
                while ((str = re.readLine()) != null) {
                    content += (content.equals("") ? "" : "\n") + str;
                }
                re.close();
            }

            return parseEventResult(eventElement, content);
        } catch (Exception e) {
            String err = function + " params =" + params + '\t' + e + ", sql: " + sql;
            _log.error(err, e);
            error = e.getMessage();
            throw new CustomException(e.getMessage());
        }
    }

    private JavaEventResult parseEventResult(EventElement event, String result) {
        Map<String, Object> root = ServerJSONConverter.decodeSimple(result);
        JavaEventResult eventResult = parseResult(root);

        // Дочернее событие добавляется в EventHelper.onResult
        // EventMetadata nextEvent = null;
        // if (eventResult.getNextEventCode() != null) {
        // nextEvent = getNextEvent(parent, eventResult.getNextEventCode(),
        // user);
        // eventResult.setNextEvent(nextEvent);
        // }
        return eventResult;
    }
}
