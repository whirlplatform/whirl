package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Data;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

@SuppressWarnings("serial")
@Data
public class PropertyValue implements Serializable, Cloneable {

    private boolean replaceable = false;

    private DataType type;
    private LocaleElement defaultLocale;
    private Map<LocaleElement, DataValue> values = new HashMap<LocaleElement, DataValue>();
    private Map<LocaleElement, DataValue> replaceableValues =
        new HashMap<LocaleElement, DataValue>();

    public PropertyValue() {
    }

    public PropertyValue(DataType type) {
        this(type, null);
    }

    public PropertyValue(DataType type, LocaleElement defaultLocale) {
        this(type, defaultLocale, null);
    }

    public PropertyValue(DataType type, LocaleElement defaultLocale,
                         Object value) {
        this.type = type;
        this.defaultLocale = defaultLocale;
        DataValue dataValue = new DataValueImpl(type);
        dataValue.setValue(value);
        values.put(defaultLocale, dataValue);
    }

    public boolean isReplaceable() {
        return replaceable;
    }

    public void setReplaceable(boolean replaceable) {
        this.replaceable = replaceable;
    }

    public DataValue getDefaultValue() {
        return getValue(null);
    }

    public DataValue getValue(LocaleElement locale) {
        LocaleElement l = locale;
        if (l == null) {
            l = defaultLocale;
        }
        boolean has = false;
        DataValue dataValue;
        if (replaceable) {
            dataValue = replaceableValues.get(l);
            has = replaceableValues.containsKey(l);
        } else {
            dataValue = values.get(l);
            has = values.containsKey(l);
        }
        if (dataValue == null && !has) {
            l = defaultLocale;
            if (replaceable) {
                dataValue = replaceableValues.get(l);
            } else {
                dataValue = values.get(l);
            }
        }
        if (dataValue == null) {
            dataValue = new DataValueImpl(type);
        }
        return dataValue;
    }

    public void setValue(LocaleElement locale, DataValue value) {
        LocaleElement l = locale;
        if (l == null) {
            l = defaultLocale;
        }
        if (replaceable) {
            replaceableValues.put(locale, value);
        } else {
            values.put(locale, value);
        }
    }

    public LocaleElement getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(LocaleElement defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Collection<LocaleElement> getLocales() {
        if (replaceable) {
            return Collections.unmodifiableSet(replaceableValues.keySet());
        } else {
            return Collections.unmodifiableSet(values.keySet());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((defaultLocale == null) ? 0 : defaultLocale.hashCode());
        result = prime * result + (replaceable ? 1231 : 1237);
        result = prime * result + ((replaceableValues == null) ? 0
            : replaceableValues.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
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
        if (!(obj instanceof PropertyValue)) {
            return false;
        }
        PropertyValue other = (PropertyValue) obj;
        if (defaultLocale == null) {
            if (other.defaultLocale != null) {
                return false;
            }
        } else if (!defaultLocale.equals(other.defaultLocale)) {
            return false;
        }
        if (replaceable != other.replaceable) {
            return false;
        }
        if (replaceableValues == null) {
            if (other.replaceableValues != null) {
                return false;
            }
        } else if (!replaceableValues.equals(other.replaceableValues)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (values == null) {
            return other.values == null;
        } else {
            return values.equals(other.values);
        }
    }

    @Override
    public String toString() {
        if (values.isEmpty()) {
            return "";
        }
        DataValue v = values.values().iterator().next();
        if (v != null) {
            return v.toString();
        }
        return "";
    }

    public PropertyValue clone() {
        PropertyValue clone = new PropertyValue();
        clone.defaultLocale = defaultLocale;
        clone.replaceable = replaceable;
        clone.type = type;
        for (Entry<LocaleElement, DataValue> e : values.entrySet()) {
            clone.values.put(e.getKey() != null ? e.getKey().clone() : null,
                e.getValue() != null ? e.getValue().clone() : null);
        }
        for (Entry<LocaleElement, DataValue> e : replaceableValues.entrySet()) {
            clone.replaceableValues.put(
                e.getKey() != null ? e.getKey().clone() : null,
                e.getValue() != null ? e.getValue().clone() : null);
        }
        return clone;
    }
}
