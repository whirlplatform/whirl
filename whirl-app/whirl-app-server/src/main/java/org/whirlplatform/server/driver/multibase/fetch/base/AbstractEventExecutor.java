package org.whirlplatform.server.driver.multibase.fetch.base;

import org.whirlplatform.meta.shared.JavaEventResult;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.EventExecutor;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public abstract class AbstractEventExecutor extends AbstractFetcher implements EventExecutor {

    public AbstractEventExecutor(ConnectionWrapper connectionWrapper) {
        super(connectionWrapper);
    }

    @SuppressWarnings("unchecked")
    protected JavaEventResult parseResult(Map<String, Object> root) {
        Map<String, Object> map = (Map<String, Object>) root.get("result");

        String nextEventCode = (String) map.get("nextEvent");
        JavaEventResult eventResult = new JavaEventResult(nextEventCode);
        eventResult.setMessage((String) map.get("message"));
        eventResult.setTitle((String) map.get("title"));

        eventResult.setMessageType((String) map.get("messageType"));

        for (Map<String, String> p : (List<Map<String, String>>) map.get("parameters")) {
            Integer index = Integer.valueOf(p.get("index"));
            DataType type = DataType.valueOf(p.get("type"));
            String code = p.get("code");
            String component = p.get("component");
            String value = p.get("value");
            String title = p.get("title");

            ParameterType paramType;
            if (component != null && !component.isEmpty()) {
                paramType = ParameterType.COMPONENTCODE;
            } else {
                paramType = ParameterType.DATAVALUE;
            }

            EventParameter parameter = new EventParameterImpl(paramType);
            if (paramType == ParameterType.COMPONENTCODE) {
                parameter.setComponentCode(component);
            } else {
                DataValue fieldValue = new DataValueImpl(type);
                fieldValue.setCode(code);
                fieldValue.setValue(DataValueImpl.convertValueFromString(value, title, type));
                parameter.setData(fieldValue);
                parameter.setCode(code);
            }

            eventResult.setParameter(index, parameter);
        }
        return eventResult;
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
