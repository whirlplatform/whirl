package org.whirlplatform.meta.shared;

import org.whirlplatform.meta.shared.data.ListModelData;

@SuppressWarnings("serial")
public class TreeClassLoadConfig extends ClassLoadConfig {

    private String isLeafColumn;
    private String stateExpression;
    private String checkExpression;
    // New property
    private String selectColumn;
    private String labelExpression;
    private String parentExpression;
    private ListModelData parent;

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

    public String getStateExpression() {
        return stateExpression;
    }

    public void setStateExpression(String stateExpression) {
        this.stateExpression = stateExpression;
    }

    public String getCheckExpression() {
        return checkExpression;
    }

    public void setCheckExpression(String checkExpression) {
        this.checkExpression = checkExpression;
    }

    public String getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(String labelExpression) {
        this.labelExpression = labelExpression;
    }

    public String getParentExpression() {
        return parentExpression;
    }

    public void setParentExpression(String parentExpression) {
        this.parentExpression = parentExpression;
    }

    public ListModelData getParent() {
        return parent;
    }

    public void setParent(ListModelData parent) {
        this.parent = parent;
    }
}
