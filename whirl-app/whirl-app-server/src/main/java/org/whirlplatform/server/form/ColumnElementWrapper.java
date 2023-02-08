package org.whirlplatform.server.form;

import java.util.Set;
import java.util.TreeSet;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.server.form.CellElementWrapper.CellComparator;

public class ColumnElementWrapper {

    private int col;

    private ColumnElement element;

    private Integer finalCol = null;

    private Set<CellElementWrapper> cells = new TreeSet<CellElementWrapper>(
        new CellComparator());

    public ColumnElementWrapper(int col) {
        this.col = col;
    }

    public void setElement(ColumnElement element) {
        this.element = element;
    }

    public int getCol() {
        return col;
    }

    public int getFinalCol() {
        return col;
    }

    public void setFinalCol(int finalCol) {
        this.finalCol = finalCol;
    }

    public double getWidth() {
        return element.getWidth() == null ? -1 : element.getWidth();
    }

    public void addCell(CellElementWrapper cell) {
        cells.add(cell);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColumnElementWrapper other = (ColumnElementWrapper) obj;
        return col == other.col;
    }

    @Override
    public String toString() {
        Integer result;
        if (finalCol != null) {
            result = finalCol;
        } else {
            result = col;
        }
        return "[Column: " + result + "]";
    }

}
