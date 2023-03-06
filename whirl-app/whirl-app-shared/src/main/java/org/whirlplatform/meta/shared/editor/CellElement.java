package org.whirlplatform.meta.shared.editor;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class CellElement extends AbstractElement {

    private int colSpan = 1;
    private int rowSpan = 1;
    private int borderTop;
    private int borderRight;
    private int borderBottom;
    private int borderLeft;
    private String borderTopColor;
    private String borderRightColor;
    private String borderBottomColor;
    private String borderLeftColor;
    private String backgroundColor;

    public CellElement() {
    }

    public CellElement(String id) {
        super(id);
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backGroundColor) {
        this.backgroundColor = backGroundColor;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
