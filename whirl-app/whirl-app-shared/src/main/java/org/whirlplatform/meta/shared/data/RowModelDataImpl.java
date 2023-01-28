package org.whirlplatform.meta.shared.data;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.whirlplatform.meta.shared.FileValue;

@SuppressWarnings("serial")
public class RowModelDataImpl implements RowModelData {

    protected String id;

    protected boolean editable = true;
    protected boolean deletable = true;

    protected Map<String, String> styles;

    protected Map<String, DataValue> data = new HashMap<>();

    protected Set<String> changed;

    protected int levelCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public <X> X get(String property) {
        DataValue v = data.get(property);
        if (v == null) {
            return null;
        }
        return (X) v.getObject();
    }

    public <X> X set(String property, X value) {
        if (value == null) {
            return null;
        }
        if (data.containsKey(property)) {
            DataValue v = data.get(property);
            X old = (X) v.getObject();
            v.setValue(value);
            return old;
        }
        DataType type;
        if (value instanceof ListModelData) {
            type = DataType.LIST;
        } else if (value instanceof FileValue) {
            type = DataType.FILE;
        } else if (value instanceof Boolean) {
            type = DataType.BOOLEAN;
        } else if (value instanceof Number) {
            type = DataType.NUMBER;
        } else if (value instanceof Date) {
            type = DataType.DATE;
        } else {
            type = DataType.STRING;
        }
        DataValue dataValue = new DataValueImpl(type, value);
        data.put(property, dataValue);
        return null;
    }

    @Override
    public DataValue getValue(String property) {
        return data.get(property);
    }

    @Override
    public DataValue setValue(String property, DataValue value) {
        return data.put(property, value);
    }

    public <X> X remove(String property) {
        DataValue v = data.get(property);
        if (v == null) {
            return null;
        }
        X old = (X) v.getObject();
        data.remove(property);
        return old;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<>();
        for (String p : getPropertyNames()) {
            result.put(p, get(p));
        }
        return result;
    }

    public Collection<String> getPropertyNames() {
        return data.keySet();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    @Override
    public String toString() {
        return id;
    }

    public void setStyle(String fieldValue, String style) {
        if (styles == null) {
            styles = new HashMap<String, String>();
        }
        styles.put(fieldValue, style);
    }

    public String getStyle(String fieldName) {
        if (styles == null) {
            return null;
        }
        return styles.get(fieldName);
    }

    public void addChangedField(String fieldName) {
        if (changed == null) {
            changed = new HashSet<String>();
        }
        changed.add(fieldName);
    }

    public boolean hasChanged(String fieldName) {
        if (changed == null) {
            return false;
        }
        return changed.contains(fieldName);
    }

    public void setUnchanged() {
        changed = null;
    }

    @Override
    public int getLevelCount() {
        return levelCount;
    }

    @Override
    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RowModelDataImpl)) {
            return false;
        }
        RowModelDataImpl other = (RowModelDataImpl) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    @Override
    public RowModelData clone() {
        RowModelDataImpl clone = new RowModelDataImpl();
        clone.id = id;
        clone.editable = editable;
        clone.deletable = deletable;
        if (styles != null) {
            clone.styles = new HashMap<>(styles);
        }
        for (Entry<String, DataValue> e : data.entrySet()) {
            clone.data.put(e.getKey(), e.getValue() != null ? e.getValue().clone() : null);
        }
        clone.changed = changed;
        clone.levelCount = levelCount;
        return clone;
    }
}
