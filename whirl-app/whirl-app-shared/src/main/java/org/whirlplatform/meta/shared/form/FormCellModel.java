package org.whirlplatform.meta.shared.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.whirlplatform.meta.shared.component.ComponentModel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FormCellModel implements Serializable, Cloneable {

    private String id;

    private FormRowModel row;
    private FormColumnModel col;

    private Integer rowSpan = 1;
    private Integer colSpan = 1;

    @JsonProperty
    private int borderTop = 0;
    @JsonProperty
    private int borderRight = 0;
    @JsonProperty
    private int borderBottom = 0;
    @JsonProperty
    private int borderLeft = 0;

    private String borderTopColor;
    private String borderRightColor;
    private String borderBottomColor;
    private String borderLeftColor;

    private String color;

    private ComponentModel component;

    private FormCellModel() {

    }

    public FormCellModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FormRowModel getRow() {
		return row;
    }

    public void setRow(FormRowModel row) {
        this.row = row;
    }

    public FormColumnModel getColumn() {
        return col;
    }

    public void setColumn(FormColumnModel col) {
        this.col = col;
    }

    public Integer getRowSpan() {
        return rowSpan == null ? 1 : rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan == null ? 1 : colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public int getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(int borderTop) {
        this.borderTop = borderTop;
    }

    public String getBorderTopColor() {
        return borderTopColor;
    }

    public void setBorderTopColor(String borderTopColor) {
        this.borderTopColor = borderTopColor;
    }

    public void setBorderTop(int borderTop, String borderTopColor) {
        this.borderTop = borderTop;
        this.borderTopColor = borderTopColor;
    }

    public int getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(int borderRight) {
        this.borderRight = borderRight;
    }

    public String getBorderRightColor() {
        return borderRightColor;
    }

    public void setBorderRightColor(String borderRightColor) {
        this.borderRightColor = borderRightColor;
    }

    public void setBorderRight(int borderRight, String borderRightColor) {
        this.borderRight = borderRight;
        this.borderRightColor = borderRightColor;
    }

    public int getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(int borderBottom) {
        this.borderBottom = borderBottom;
    }

    public String getBorderBottomColor() {
        return borderBottomColor;
    }

    public void setBorderBottomColor(String borderBottomColor) {
        this.borderBottomColor = borderBottomColor;
    }

    public void setBorderBottom(int borderBottom, String borderBottomColor) {
        this.borderBottom = borderBottom;
        this.borderBottomColor = borderBottomColor;
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(int borderLeft) {
        this.borderLeft = borderLeft;
    }

    public String getBorderLeftColor() {
        return borderLeftColor;
    }

    public void setBorderLeftColor(String borderLeftColor) {
        this.borderLeftColor = borderLeftColor;
    }

    public void setBorderLeft(int borderLeft, String borderLeftColor) {
        this.borderLeft = borderLeft;
        this.borderLeftColor = borderLeftColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    protected FormCellModel clone() {
        FormCellModel other = new FormCellModel();
        other.id = id;
        other.rowSpan = rowSpan;
        other.colSpan = colSpan;
        other.borderTop = borderTop;
        other.borderBottom = borderBottom;
        other.borderRight = borderRight;
        other.borderLeft = borderLeft;
        other.borderTopColor = borderTopColor;
        other.borderRightColor = borderRightColor;
        other.borderBottomColor = borderBottomColor;
        other.borderLeftColor = borderLeftColor;
        other.color = color;
        ComponentModel newComponent = component.clone();
		other.component = newComponent;
		return other;
	}

}
