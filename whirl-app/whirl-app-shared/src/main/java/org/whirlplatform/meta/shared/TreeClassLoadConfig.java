package org.whirlplatform.meta.shared;

import org.whirlplatform.meta.shared.data.RowModelData;

@SuppressWarnings("serial")
public class TreeClassLoadConfig extends ClassLoadConfig {

    private String leafExpression;
    private String stateExpression;
    private String checkExpression;
    // New property
    private String selectExpression;
    private String NameExpression;
    private String parentField;
    private RowModelData parent;

    public TreeClassLoadConfig() {
        super();
    }

    public void setSelectExpression(String selectExpression) {
        this.selectExpression = selectExpression;
    }

    public String getSelectExpression() {
        return selectExpression;
    }

    public void setLeafExpression(String leafExpression) {
        this.leafExpression = leafExpression;
    }

    public String getLeafExpression() {
        return leafExpression;
    }

    public void setStateExpression(String stateExpression) {
        this.stateExpression = stateExpression;
    }

    public String getStateExpression() {
        return stateExpression;
    }

    public void setCheckExpression(String checkExpression) {
        this.checkExpression = checkExpression;
    }

    public String getCheckExpression() {
        return checkExpression;
    }

    public void setNameExpression(String nameExpression) {
        NameExpression = nameExpression;
    }

    public String getNameExpression() {
        return NameExpression;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    public String getParentField() {
        return parentField;
    }

    public void setParent(RowModelData parent) {
        this.parent = parent;
    }

    public RowModelData getParent() {
        return parent;
    }
}
