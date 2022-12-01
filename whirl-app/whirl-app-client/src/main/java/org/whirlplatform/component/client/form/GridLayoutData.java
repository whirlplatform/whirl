package org.whirlplatform.component.client.form;

import com.google.gwt.user.client.ui.HasAlignment;
import com.sencha.gxt.widget.core.client.container.HasHeight;
import com.sencha.gxt.widget.core.client.container.HasWidth;
import com.sencha.gxt.widget.core.client.container.MarginData;

public class GridLayoutData extends MarginData implements HasWidth, HasHeight,
        HasAlignment {

    private double width = -1d;
    private double height = -1d;

    private HorizontalAlignmentConstant horizontalAlign;
    private VerticalAlignmentConstant verticalAlign;

    public GridLayoutData() {
        super();
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public HorizontalAlignmentConstant getHorizontalAlignment() {
        return horizontalAlign;
    }

    @Override
    public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
        horizontalAlign = align;
    }

    @Override
    public VerticalAlignmentConstant getVerticalAlignment() {
        return verticalAlign;
    }

    @Override
    public void setVerticalAlignment(VerticalAlignmentConstant align) {
        verticalAlign = align;
    }

}
