package org.whirlplatform.integration.grid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GridTestRowModel {

    // ID записи в таблице
    private Integer rowId;
    // Значения по названию колонок.
    private Map<String, String> values = new HashMap<String, String>();

    public void addValue(String colName, String value) {
        values.put(colName, value);
    }

    public String getColumnValue(String colName) {
        return values.get(colName);
    }

    public Integer getId() {
        return rowId;
    }

    public void setId(Integer id) {
        this.rowId = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GridTestRowModel other = (GridTestRowModel) obj;
        if (rowId == null) {
            if (other.rowId != null)
                return false;
        } else if (!rowId.equals(other.rowId))
            return false;
        if (this.values.size() != other.values.size()) {
            return false;
        }
        try {
            Map<String, String> otherMap = other.values;
            Iterator<Entry<String, String>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (value == null) {
                    if (otherMap.containsKey(key) && otherMap.get(key) != null) {
                        return false;
                    }
                } else {
                    if (!value.equals(otherMap.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rowId == null) ? 0 : rowId.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "row id = " + rowId + " values " + values.toString();
    }
}
