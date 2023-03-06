package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RightElement implements Serializable, Cloneable {
    @SuppressWarnings("unused")
    private BooleanCondition _booleanCondition; // for GWT serialization
    @SuppressWarnings("unused")
    private SQLCondition _plainCondition; // for GWT serialization

    private RightType type;
    private AbstractCondition<?> condition;

    public RightElement() {
    }

    public RightElement(RightType type) {
        this.type = type;
    }

    public RightType getType() {
        return type;
    }

    public void setType(RightType type) {
        this.type = type;
    }

    public AbstractCondition<?> getCondition() {
        return condition;
    }

    public void setCondition(AbstractCondition<?> condition) {
        this.condition = condition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((condition == null) ? 0 : condition.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        if (!(obj instanceof RightElement)) {
            return false;
        }
        RightElement other = (RightElement) obj;
        if (condition == null) {
            if (other.condition != null) {
                return false;
            }
        } else if (!condition.equals(other.condition)) {
            return false;
        }
        return type == other.type;
    }

    public RightElement clone() {
        RightElement result = new RightElement();
        result.setType(getType());
        result.setCondition(getCondition());
        return result;
    }
}
