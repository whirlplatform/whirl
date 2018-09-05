package org.whirlplatform.server.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class ServerJSONConverter {

    public static Map<String, Object> decodeExtended(String jsonString) {
        return decode(jsonString, true);
    }

    public static Map<String, Object> decodeSimple(String jsonString) {
        return decode(jsonString, false);
    }

    public static Map<String, Object> decode(String jsonString, boolean extended) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode v;
        try {
            v = mapper.readValue(jsonString, JsonNode.class);
            if (v.isObject()) {
                return decode(v, extended);
            }
        } catch (JsonParseException e) {
            System.out.println(e);
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static Map<String, Object> decode(JsonNode jso, boolean extended) {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> iterator = jso.getFieldNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            JsonNode j = jso.get(key);
            if (j.isObject()) {
                map.put(key, decode(j, extended));
            } else if (j.isArray()) {
                map.put(key, decodeToList(j, extended));
            } else if (j.isBoolean()) {
                map.put(key, j.asBoolean());
            } else if (j.isTextual()) {
                if (extended) {
                    map.put(key, decodeValue(j.asText()));
                } else {
                    map.put(key, j.asText());
                }
            } else if (j.isNumber()) {
                map.put(key, j.asDouble());
            } else if (j.isNull()) {
                map.put(key, null);
            }
        }
        return map;
    }

    protected static List<Object> decodeToList(JsonNode array, boolean extended) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            JsonNode v = array.get(i);
            if (v.isObject()) {
                list.add(decode(v, extended));
            } else if (v.isArray()) {
                list.add(decodeToList(v, extended));
            } else if (v.isNull()) {
                list.add(null);
            } else if (v.isNumber()) {
                list.add(v.asDouble());
            } else if (v.isBoolean()) {
                list.add(v.asBoolean());
            } else if (v.isTextual()) {
                if (extended) {
                    list.add(decodeValue(v.asText()));
                } else {
                    list.add(v.asText());
                }
            }
        }

        return list;
    }

    public static Object decodeValue(String value) {
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

    public static String encodeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return "d:" + ((Date) value).getTime();
        } else if (value instanceof Integer) {
            return "i:" + value;
        } else if (value instanceof Float) {
            return "f:" + value;
        }
        return "s:" + value.toString();
    }

}
