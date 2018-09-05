package org.whirlplatform.meta.shared.data;

public interface ListModelData extends RowModelData {

    void setLabel(String label);

    String getLabel();

    @Override
    ListModelData clone();
}
