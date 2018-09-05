package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CellGroupModel implements Serializable {

    private int top;
    private int right;
    private int bottom;
    private int left;

    private String title;

    public CellGroupModel() {
    }

    public CellGroupModel(int top, int right, int bottom, int left) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bottom;
        result = prime * result + left;
        result = prime * result + right;
        result = prime * result + top;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CellGroupModel)) {
            return false;
        }
        CellGroupModel other = (CellGroupModel) obj;
        if (bottom != other.bottom) {
            return false;
        }
        if (left != other.left) {
            return false;
        }
        if (right != other.right) {
            return false;
        }
        return top == other.top;
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

}
