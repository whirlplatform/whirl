package org.whirlplatform.component.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.sencha.gxt.core.client.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.storage.client.StorageHelper;

public class ParameterHelper {

    private List<String> parameterIds = new ArrayList<String>();
    private List<String> parameterCodes = new ArrayList<String>();
    private List<String> storageCodes = new ArrayList<String>();
    private Map<String, String> staticValues = new HashMap<String, String>();

    /**
     * Добавление параметров
     *
     * @param parameters - String
     */
    public void addJsonParameters(String parameters) {
        if (Util.isEmptyString(parameters)) {
            return;
        }
        JSONValue json = JSONParser.parseLenient(parameters);
        JSONObject object = json.isObject();
        if (object == null) {
            return;
        }
        if (object.containsKey("ids")) {
            JSONArray ids = object.get("ids").isArray();
            for (int i = 0; i < ids.size(); i++) {
                parameterIds.add(ids.get(i).isString().stringValue());
            }
        }

        if (object.containsKey("codes")) {
            JSONArray codes = object.get("codes").isArray();
            for (int i = 0; i < codes.size(); i++) {
                parameterCodes.add(codes.get(i).isString().stringValue());
            }
        }

        if (object.containsKey("storage")) {
            JSONArray storage = json.isObject().get("storage").isArray();
            for (int i = 0; i < storage.size(); i++) {
                storageCodes.add(storage.get(i).isString().stringValue());
            }
        }

        if (object.containsKey("values")) {
            JSONArray storage = json.isObject().get("values").isArray();
            for (int i = 0; i < storage.size(); i++) {
                JSONObject obj = storage.get(i).isObject();
                for (String code : obj.keySet()) {
                    staticValues.put(code, obj.get(code).isString().stringValue());
                }
            }
        }
    }

    public Map<String, DataValue> getValues() {
        return getValues(Collections.emptyList());
    }

    /**
     * Возвращает карту: код компонента - значение
     *
     * @param parameters - List< DataValue >
     * @return Map<String, DataValue>
     */
    @SuppressWarnings("rawtypes")
    public Map<String, DataValue> getValues(List<DataValue> parameters) {
        Map<String, DataValue> result = new HashMap<String, DataValue>();
        for (String id : parameterIds) {
            ComponentBuilder builder = BuilderManager.findBuilder(id, false);
            if (builder instanceof Parameter) {
                String code = builder.getCode();
                DataValue value = ((Parameter) builder).getFieldValue();
                if (code != null) {
                    result.put(code, value);
                }
            } else if (builder instanceof ListParameter) {
                String code = builder.getCode();
                RowListValue value = ((ListParameter) builder).getFieldValue();
                if (code != null) {
                    result.put(code, value);
                }
            }

        }
        for (String code : parameterCodes) {
            DataValue value = null;
            ComponentBuilder builder = BuilderManager.findBuilder(code, true);
            if (builder instanceof Parameter) {
                value = ((Parameter) builder).getFieldValue();
                result.put(code, value);
            } else if (builder instanceof ListParameter) {
                value = ((ListParameter) builder).getFieldValue();
                result.put(code, value);
            }
        }
        for (String code : storageCodes) {
            DataValue value = StorageHelper.findStorageValue(code);
            result.put(code, value);
        }
        for (Entry<String, String> e : staticValues.entrySet()) {
            DataValue value = new DataValueImpl(DataType.STRING, e.getValue());
            value.setCode(e.getKey());
            result.put(value.getCode(), value);
        }
        for (DataValue v : parameters) {
            result.put(v.getCode(), v);
        }
        return result;
    }

    /**
     * Возвращает список значений
     *
     * @param parameters - List< DataValue >
     * @return List<DataValue>
     */
    public List<DataValue> getValueList(List<DataValue> parameters) {
        List<DataValue> allParams = new ArrayList<DataValue>();
        for (Entry<String, DataValue> entry : getValues(parameters).entrySet()) {
            DataValue v = entry.getValue();
            v.setCode(entry.getKey());
            allParams.add(v);
        }
        return allParams;
    }

}
