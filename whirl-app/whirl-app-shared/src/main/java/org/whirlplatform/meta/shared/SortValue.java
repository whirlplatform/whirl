package org.whirlplatform.meta.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SortValue implements Serializable {

    private SortType order;
    private FieldMetadata field;

    public SortValue() {
    }

    public FieldMetadata getField() {
        return field;
    }

    public void setField(FieldMetadata field) {
        this.field = field;
    }

    public SortType getOrder() {
        return order;
    }

    public void setOrder(SortType order) {
        this.order = order;
    }

}
