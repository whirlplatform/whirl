package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractCondition<T> implements Condition, Cloneable, Serializable {
    protected T value;
    @SuppressWarnings("unused")
    private Boolean _boolean;
    @SuppressWarnings("unused")
    private String _string;

    protected AbstractCondition() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractCondition)) {
            return false;
        }
        AbstractCondition other = (AbstractCondition) obj;
        if (value == null) {
            return other.value == null;
        } else {
            return value.equals(other.value);
        }
    }

    public abstract AbstractCondition<T> clone();
}
