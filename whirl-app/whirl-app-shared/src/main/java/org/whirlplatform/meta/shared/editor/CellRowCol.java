package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CellRowCol implements Serializable {

    /*
     * Синтаксическое поле
     */
    private String id;

    private int row;
    private int col;

    protected CellRowCol() {
        this.id = "-+-";
    }

    public CellRowCol(int row, int col) {
        this.row = row;
        this.col = col;
        this.id = row + "+" + col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(this instanceof CellRowCol))
            return false;
        CellRowCol other = (CellRowCol) obj;
        if (row != other.row)
            return false;
        return col == other.col;
    }

    public String getId() {
        return id;
    }
}
