package org.whirlplatform.server.driver.multibase.fetch.base;

import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.JavaEventResult;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.EventExecutor;
import org.whirlplatform.server.driver.multibase.fetch.QueryExecutor;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.impl.DBFunctionMessage;
import org.whirlplatform.server.utils.ServerJSONConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEventExecutor extends AbstractFetcher implements EventExecutor {

    private static Logger _log = LoggerFactory.getLogger(AbstractEventExecutor.class);

    protected QueryExecutor queryExecutor;

    public AbstractEventExecutor(ConnectionWrapper connectionWrapper, QueryExecutor queryExecutor) {
        super(connectionWrapper);
        this.queryExecutor = queryExecutor;
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

    protected EventResult parseEventResult(String content) {
        EventResult result;
        if (content.startsWith("{\"result\": {")) {
            Map<String, Object> root = ServerJSONConverter.decodeSimple(content);
            result = parseResult(root);
        } else {
            result = new JavaEventResult();
            result.setRawValue(content);
        }
        return result;
    }

    protected String makeCallQuery(String function, List<Object> params) {
        String sql = "{? = call " + function;
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
        return sql;
    }

    @Override
    public EventResult executeQuery(EventElement eventElement, List<DataValue> params) {
        String query = eventElement.getSource();
        _log.info("QUERY = " + query + "	params = " + params);
        DBFunctionMessage m = new DBFunctionMessage(getUser(), eventElement, params);
        Map<String, DataValue> paramMap = new HashMap<>();
        for (DataValue v : params) {
            if (v.getCode() != null && !v.getCode().trim().isEmpty()) {
                paramMap.put(v.getCode(), v);
            }
        }
        Map<String, DataValue> resultMap = queryExecutor.executeQuery(eventElement.getSource(), paramMap);
        DataValue queryResultColumn = resultMap.getOrDefault(AppConstant.WHIRL_RESULT,
                resultMap.getOrDefault(AppConstant.WHIRL_RESULT.toLowerCase(), null));

        EventResult result;
        if (queryResultColumn != null) {
            if (!queryResultColumn.isTypeOf(DataType.STRING)) {
                throw new CustomException("Result column " + AppConstant.WHIRL_RESULT + " should be string type.");
            }
            result = parseEventResult(queryResultColumn.getString());

            if (resultMap.remove(AppConstant.WHIRL_RESULT) == null) {
                resultMap.remove(AppConstant.WHIRL_RESULT.toLowerCase());
            }
        } else {
            result = new JavaEventResult();
            for (DataValue value : resultMap.values()) {
                EventParameter parameter = new EventParameterImpl(ParameterType.DATAVALUE);
                parameter.setDataWithCode(value);
                result.addParameter(parameter);
            }
        }
        return result;
    }
}
