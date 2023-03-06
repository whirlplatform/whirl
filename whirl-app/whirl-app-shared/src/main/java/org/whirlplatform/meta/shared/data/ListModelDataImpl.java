package org.whirlplatform.meta.shared.data;

import java.util.HashMap;
import java.util.Map.Entry;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ListModelDataImpl extends RowModelDataImpl
    implements ListModelData {

    private String label;

    public ListModelDataImpl() {
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public ListModelData clone() {
        ListModelDataImpl clone = new ListModelDataImpl();
        clone.id = id;
        clone.editable = editable;
        clone.deletable = deletable;
        if (styles != null) {
            clone.styles = new HashMap<>(styles);
        }
        for (Entry<String, DataValue> e : data.entrySet()) {
            clone.data.put(e.getKey(),
                e.getValue() != null ? e.getValue().clone() : null);
        }
        clone.changed = changed;
        clone.levelCount = levelCount;
        clone.label = label;
        return clone;
    }
}
