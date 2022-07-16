package org.whirlplatform.server.driver.multibase.fetch.oracle;

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
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractEventExecutor;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.DBFunctionMessage;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.monitor.RunningEvent;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

public class OracleEventExecutor extends AbstractEventExecutor {
    private static Logger _log = LoggerFactory.getLogger(OracleEventExecutor.class);

    public OracleEventExecutor(ConnectionWrapper connection, QueryExecutor queryExecutor) {
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

        _log.debug("Execute function:/nFUNCTION = " + function + "	params = " + params);
        CallableStatement stmt = null;

        DBFunctionMessage m = new DBFunctionMessage(getUser(), eventElement, paramsDataValue);

        // Обход ограничения на final переменные
        final boolean[] stoppedHolder = new boolean[]{false};
        // Метод для прекращения выполнения события
        RunningEvent ev = new RunningEvent(RunningEvent.Type.DBEVENT, eventElement.getCode(),
                eventElement.getFunction(), getUser().getLogin()) {
            @Override
            public void onStop() {
                //TODO abort
//                try {
//                    stoppedHolder[0] = true;
//                    getConnection().abort();
//                } catch (SQLException e) {
//                    _log.info("Stop event error", e);
//                }
            }
        };
        // Чтобы можно было дописать ошибку обернул в еще один try
        String sql = makeCallQuery(function, params);
        try (Profile p = new ProfileImpl(m, ev)) {
            try {
                stmt = getConnection().prepareCall(sql);
                stmt.setQueryTimeout(DBConnection.STMT_TIMEOUT);
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
                                    if (dataValue.getDate() != null) {
                                        time = new Timestamp(dataValue.getDate().getTime());
                                    }
                                    stmt.setTimestamp(i, time);
                                }
                            } else if (v instanceof Boolean) {
                                Boolean bv = (Boolean) v;
                                stmt.setObject(i, bv ? "T" : "F");
                            } else if (v instanceof Number) {
                                Number nv = (Number) v;
                                stmt.setDouble(i, nv.doubleValue());
                            } else {
                                stmt.setObject(i, v);
                            }
                        } catch (SQLException e) {
                            // если не выйдет, и передаваемый объект - строка,
                            // попробуем передать через ридер
                            // на случай, если параметр - clob, а длина строки >
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

                return parseEventResult(content);
            } catch (Exception e) {
                if (stoppedHolder[0]) {
                    throw new CustomException(
                            I18NMessage.getMessage(I18NMessage.getRequestLocale()).alert_event_cancelled());
                }
                String err = function + " params =" + params + '\t' + e + ", sql: " + sql;
                _log.error(err, e);
                m.setError(e.getMessage());
                throw new CustomException(e.getMessage());
            }
        }
    }

    protected Clob createTemporaryClob() throws SQLException {
        CallableStatement cst = null;
        try {
            cst = getConnection().prepareCall("{call dbms_lob.createTemporary(?, false, dbms_lob.SESSION)}");
            cst.registerOutParameter(1, Types.CLOB);
            cst.execute();
            return cst.getClob(1);
        } finally {
            if (cst != null) {
                cst.close();
            }
        }
    }

}
