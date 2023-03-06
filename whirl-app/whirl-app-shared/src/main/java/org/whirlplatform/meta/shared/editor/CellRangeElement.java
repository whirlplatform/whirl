package org.whirlplatform.meta.shared.editor;

@SuppressWarnings("serial")
public class CellRangeElement extends AbstractElement {

    private int top;
    private int right;
    private int bottom;
    private int left;

    public CellRangeElement() {
    }

    public CellRangeElement(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("top = " + top);
        sb.append('\n');
        sb.append("right = " + right);
        sb.append('\n');
        sb.append("bottom = " + bottom);
        sb.append('\n');
        sb.append("left = " + left);
        return sb.toString();
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
