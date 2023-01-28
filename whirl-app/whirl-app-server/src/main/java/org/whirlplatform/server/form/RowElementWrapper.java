package org.whirlplatform.server.form;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.server.form.CellElementWrapper.CellComparator;

public class RowElementWrapper implements Cloneable {

    private RowElement element;
    private int row;
    private Integer finalRow = null;
    private Set<CellElementWrapper> cells = new TreeSet<CellElementWrapper>(new CellComparator());

    private RowElementWrapper() {

    }

    public RowElementWrapper(int row) {
        this.row = row;
    }

    public void setElement(RowElement element) {
        this.element = element;
    }

    public int getRow() {
        return row;
    }

    public int getFinalRow() {
        return finalRow;
    }

    public void setFinalRow(int finalRow) {
        this.finalRow = finalRow;
    }

    public double getHeight() {
        return element.getHeight() == null ? -1 : element.getHeight();
    }

    public void addCell(CellElementWrapper cell) {
        cells.add(cell);
    }

    public Set<CellElementWrapper> getCells() {
        return cells;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
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
        RowElementWrapper other = (RowElementWrapper) obj;
        return row == other.row;
    }

    @Override
    public RowElementWrapper clone() {
        RowElementWrapper other = new RowElementWrapper();
        other.row = row;
        other.element = element;
        Set<CellElementWrapper> newCells = new TreeSet<CellElementWrapper>(new CellComparator());
        for (CellElementWrapper c : cells) {
            CellElementWrapper nc = c.clone();
            nc.setRow(other);
            nc.setColumn(c.getColumn());
            newCells.add(nc);
        }
        other.cells = newCells;
        return other;
    }

    @Override
    public String toString() {
        Integer result;
        if (finalRow != null) {
            result = finalRow;
        } else {
            result = row;
        }
        return "[Row: " + result + "]";
    }

    public static class RowComparator implements Comparator<RowElementWrapper> {

        @Override
        public int compare(RowElementWrapper o1, RowElementWrapper o2) {
            int row1 = o1.getRow();
            int row2 = o2.getRow();
            if (row1 < row2) {
                return -1;
            } else if (row1 > row2) {
                return 1;
            }
            return 0;
        }

    }

}