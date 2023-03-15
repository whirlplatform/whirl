package org.whirlplatform.meta.shared;


import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.TreeModelData;

@SuppressWarnings("serial")
public class TreeClassLoadConfig extends ClassLoadConfig {

    private ListModelData parent;
    private String parentExpression;
    private String isLeafExpression;
    private String expandExpression;
    private String checkExpression;
    private String selectExpression;
    private String labelExpression;
    private String imageExpression;


    public TreeClassLoadConfig() {
        super();
    }

    @Override
    public String getImageExpression() {
        return imageExpression;
    }

    @Override
    public void setImageExpression(String imageExpression) {
        this.imageExpression = imageExpression;
    }

    public String getSelectExpression() {
        return selectExpression;
    }

    public void setSelectExpression(String selectExpression) {
        this.selectExpression = selectExpression;
    }

    public String getIsLeafExpression() {
        return isLeafExpression;
    }

    public void setIsLeafExpression(String leafExpression) {
        this.isLeafExpression = leafExpression;
    }

    public String getExpandExpression() {
        return expandExpression;
    }

    public void setExpandExpression(String expandExpression) {
        this.expandExpression = expandExpression;
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
