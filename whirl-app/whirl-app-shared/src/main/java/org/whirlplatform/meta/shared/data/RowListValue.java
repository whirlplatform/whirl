package org.whirlplatform.meta.shared.data;

import java.util.List;

public interface RowListValue extends DataValue {

//    @Deprecated
//    public List<RowValue> getValue();

    void addRowValue(RowValue row);

    boolean containsRow(RowValue row);

    List<RowValue> getRowList();

    void setRowList(List<RowValue> rowList);

    boolean isCheckable();

    void setCheckable(boolean checkable);

    @Override
    RowListValue clone();
}
