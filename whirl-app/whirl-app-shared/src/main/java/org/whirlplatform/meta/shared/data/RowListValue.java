package org.whirlplatform.meta.shared.data;

import java.util.List;

public interface RowListValue extends DataValue {

//	@Deprecated
//	public List<RowValue> getValue();

    void addRowValue(RowValue row);

    boolean containsRow(RowValue row);

    void setRowList(List<RowValue> rowList);

    List<RowValue> getRowList();

    void setCheckable(boolean checkable);

    boolean isCheckable();

    @Override
    RowListValue clone();
}
