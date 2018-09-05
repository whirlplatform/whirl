package org.whirlplatform.component.client.utils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XJSONConverter {

	public static Map<String, Object> decodeExtended(String jsonString) {
		return decode(jsonString, true);
	}

	public static Map<String, Object> decodeSimple(String jsonString) {
		return decode(jsonString, false);
	}

	public static Map<String, Object> decode(String jsonString, boolean extended) {
		JSONValue v = JSONParser.parse(jsonString);
		if (v.isObject() != null) {
			return decode(v.isObject(), extended);
		} else {
			return null;
		}
	}

	public static Map<String, Object> decode(JSONObject jso, boolean extended) {
		Map<String, Object> map = new FastMap<Object>();
		for (String key : jso.keySet()) {
			JSONValue j = jso.get(key);
			if (j.isObject() != null) {
				map.put(key, decode(j.isObject(), extended));
			} else if (j.isArray() != null) {
				map.put(key, decodeToList(j.isArray(), extended));
			} else if (j.isBoolean() != null) {
				map.put(key, j.isBoolean().booleanValue());
			} else if (j.isString() != null) {
				if (extended) {
					map.put(key, decodeValue(j.isString().stringValue()));
				} else {
					map.put(key, j.isString().stringValue());
				}
			} else if (j.isNumber() != null) {
				map.put(key, j.isNumber().doubleValue());
			} else if (j.isNull() != null) {
				map.put(key, null);
			}
		}
		return map;
	}

	protected static Object decodeValue(String value) {
		try {
			if (value == null || value.length() < 3) {
				return null;
			}
			String type = value.substring(0, 2);
			String val = value.substring(2);
			if (type.equals("d:")) {
				long time = Long.parseLong(val);
				return new Date(time);
			} else if (type.equals("i:")) {
				return Integer.decode(val);
			} else if (type.equals("f:")) {
				return new Float(val);
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected static List<Object> decodeToList(JSONArray array, boolean extended) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.size(); i++) {
			JSONValue v = array.get(i);
			if (v.isObject() != null) {
				list.add(decode(v.isObject(), extended));
			} else if (v.isArray() != null) {
				list.add(decodeToList(v.isArray(), extended));
			} else if (v.isNull() != null) {
				list.add(null);
			} else if (v.isNumber() != null) {
				list.add(v.isNumber().doubleValue());
			} else if (v.isBoolean() != null) {
				list.add(v.isBoolean().booleanValue());
			} else if (v.isString() != null) {
				if (extended) {
					list.add(decodeValue(v.isString().stringValue()));
				} else {
					list.add(v.isString().stringValue());
				}
			}
		}

		return list;
	}

}
