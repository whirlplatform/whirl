package org.whirlplatform.meta.shared;

import org.whirlplatform.meta.shared.data.RowModelData;

@SuppressWarnings("serial")
public class TreeClassLoadConfig extends ClassLoadConfig {

    private String isLeafColumn;
    private String stateColumn;
    private String checkColumn;
    // New property
    private String selectColumn;
    private String labelColumn;
    private String parentColumn;
    private RowModelData parent;

    public TreeClassLoadConfig() {
        super();
    }

    public String getSelectColumn() {
        return selectColumn;
    }

    public void setSelectColumn(String selectExpression) {
        this.selectColumn = selectExpression;
    }

    public String getIsLeafColumn() {
        return isLeafColumn;
    }

    public void setIsLeafColumn(String leafExpression) {
        this.isLeafColumn = leafExpression;
    }

    public String getStateColumn() {
        return stateColumn;
    }

    public void setStateColumn(String stateColumn) {
        this.stateColumn = stateColumn;
    }

    public String getCheckColumn() {
        return checkColumn;
    }

    public void setCheckColumn(String checkColumn) {
        this.checkColumn = checkColumn;
    }

    public String getLabelColumn() {
        return labelColumn;
    }

    public void setLabelColumn(String labelColumn) {
        this.labelColumn = labelColumn;
    }

    public String getParentColumn() {
        return parentColumn;
    }

    public void setParentColumn(String parentColumn) {
        this.parentColumn = parentColumn;
    }

    public void setParent(RowModelData parent) {
        this.parent = parent;
    }

    public RowModelData getParent() {
        return parent;
    }
}
