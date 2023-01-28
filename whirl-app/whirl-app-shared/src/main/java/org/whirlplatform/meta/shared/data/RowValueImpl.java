package org.whirlplatform.meta.shared.data;

@SuppressWarnings("serial")
public class RowValueImpl implements RowValue {

    private String id;

    private boolean selected = false;

    private boolean checked = false;

    private boolean expanded = false;

    private String label;

    public RowValueImpl() {

    }

    public RowValueImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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
        return "Id=" + id;
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
        if (!(obj instanceof RowValueImpl)) {
            return false;
        }
        RowValueImpl other = (RowValueImpl) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    @Override
    public RowValue clone() {
        // TODO Auto-generated method stub
        return null;
    }

}