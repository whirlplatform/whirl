package org.whirlplatform.server.driver.multibase.fetch.oracle;

import oracle.jdbc.internal.OracleTypes;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.ParamsUtil;
import org.whirlplatform.server.login.ApplicationUser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

public class OracleStatementHelper extends AbstractFetcher {

    public OracleStatementHelper(ConnectionWrapper connection) {
        super(connection);
    }

    public void setStatementParams(CallableStatement stmt, List<String> paramNames, Map<String, DataValue> params)
            throws SQLException, IOException {
        List<DataValue> paramsDataValue = new ArrayList<DataValue>();
        for (String paramName : paramNames) {
            if (params.containsKey(paramName)) {
                paramsDataValue.add(params.get(paramName));
            } else {
                paramsDataValue.add(new DataValueImpl(DataType.STRING, null));
            }
        }

        List<Object> objectParams = ParamsUtil.listFunctionParams(paramsDataValue, getUser());
        if (!objectParams.isEmpty()) {
            for (int i = 2; i < objectParams.size() + 2; i++) {
                Object v = objectParams.get(i - 2);
                if ("data_count".equalsIgnoreCase(paramNames.get(i - 2))) {
                    stmt.registerOutParameter(i, OracleTypes.NUMBER);
                    continue;
                }
                try {
                    // Т.к. по умолчанию boolean передается 1/0
                    if (v instanceof DataValueImpl) {
                        DataValueImpl dataValue = (DataValueImpl) v;
                        if (dataValue.getType() == DataType.DATE) {
                            Timestamp time = null;
                            if (dataValue.getValue() != null) {
                                time = new Timestamp(((Date) dataValue.getValue()).getTime());
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
    }

    public Map<String, DataValue> processParams(Map<String, DataValue> paramMap) {
        // Map<String, DataValue> paramMap = config.getParameters();
        // List<FilterValue> filters = config.getFilters();
        ApplicationUser user = getUser();
        Map<String, DataValue> result = new HashMap<String, DataValue>();

        DataValue data = new DataValueImpl(DataType.STRING);
        data.setCode("PFUSER");
        data.setValue(user.getId());
        result.put(data.getCode(), data);

        data = new DataValueImpl(DataType.STRING);
        data.setCode("PFIP");
        data.setValue(user.getIp());
        result.put(data.getCode(), data);

        data = new DataValueImpl(DataType.STRING);
        data.setCode("PFROLE");
        data.setValue(user.getApplication().getId());
        result.put(data.getCode(), data);

        // for (Entry<String, DataValue> entry : paramMap.entrySet()) {
        // data = new DataValue(entry.getValue().getType());
        // data.setCode(entry.getValue().getCode().toUpperCase());
        // data.setValue(entry.getValue().getValue());
        // result.put(entry.getKey().toUpperCase(), data);
        // }

        for (Entry<String, DataValue> entry : paramMap.entrySet()) {
            entry.getValue().setCode(entry.getValue().getCode());
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private Clob createTemporaryClob() throws SQLException {
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
