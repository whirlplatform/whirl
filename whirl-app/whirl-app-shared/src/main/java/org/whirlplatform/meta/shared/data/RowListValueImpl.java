package org.whirlplatform.meta.shared.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RowListValueImpl extends DataValueImpl implements RowListValue {

    private List<RowValue> rowList = new ArrayList<RowValue>();
    private boolean checkable = true;

    public RowListValueImpl() {
    }

    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    //    @Override
    //    public List<RowValue> getValue() {
    //        return rowList;
    //    }

    public void addRowValue(RowValue row) {
        rowList.add(row);
    }

    public boolean containsRow(RowValue row) {
        return rowList.contains(row);
    }

    public List<RowValue> getRowList() {
        return rowList;
    }

    public void setRowList(List<RowValue> rowList) {
        this.rowList = rowList;
    }

    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((rowList == null) ? 0 : rowList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RowListValueImpl)) {
            return false;
        }
        RowListValueImpl other = (RowListValueImpl) obj;
        if (rowList == null) {
            return other.rowList == null;
        } else {
            return rowList.equals(other.rowList);
        }
    }

    @Override
    public RowListValue clone() {
        // TODO Auto-generated method stub
        return null;
    }
}
