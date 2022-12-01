package org.whirlplatform.editor.shared.merge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.whirlplatform.meta.shared.editor.AbstractElement;

@SuppressWarnings("serial")
public class ChangeUnit implements Serializable {
    protected ChangeType type;
    private String id;
    private boolean approved = false;
    private AbstractElement target;
    private String field;
    private ChangeValue key = new ChangeValue();
    private ChangeValue rightValue = new ChangeValue();
    private ChangeValue leftValue = new ChangeValue();

    private List<ChangeUnit> nestedChanges = new ArrayList<>();

    public ChangeUnit() {
    }

    public ChangeUnit(ChangeType type, AbstractElement target) {
        this(type, target, null, null, null);
    }

    public ChangeUnit(ChangeType type, AbstractElement target, String field, Object key,
                      Object rightValue) {
        this(type, target, field, key, rightValue, null);
    }

    public ChangeUnit(ChangeType type, AbstractElement target, String field, Object key,
                      Object rightValue,
                      Serializable leftValue) {
        this.type = type;
        this.target = target;
        this.field = field;
        this.key = new ChangeValue(key);
        this.rightValue = new ChangeValue(rightValue);
        this.leftValue = new ChangeValue(leftValue);
    }

    public ChangeType getType() {
        return type;
    }

    public AbstractElement getTarget() {
        return target;
    }

    public String getField() {
        return field;
    }

    public Object getKey() {
        return key.get();
    }

    public void setKey(Object key) {
        this.key = new ChangeValue(key);
    }

    public Object getRightValue() {
        return rightValue.get();
    }

    public void setRightValue(Object rightValue) {
        this.rightValue = new ChangeValue(rightValue);
    }

    public Object getLeftValue() {
        return leftValue.get();
    }

    public void setLeftValue(Serializable leftValue) {
        this.leftValue = new ChangeValue(leftValue);
    }

    public void addNestedChange(ChangeUnit change) {
        nestedChanges.add(change);
        setApproved(nestedChanges, approved);
    }

    public void addNestedChanges(List<ChangeUnit> changes) {
        nestedChanges.addAll(changes);
        setApproved(nestedChanges, approved);
    }

    public List<ChangeUnit> getNestedChanges() {
        return Collections.unmodifiableList(nestedChanges);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
        this.setApproved(nestedChanges, approved);
    }

    private void setApproved(final List<ChangeUnit> nested, boolean approved) {
        for (ChangeUnit unit : nested) {
            unit.setApproved(approved);
        }
    }
}
