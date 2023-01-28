package org.whirlplatform.meta.shared.form;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "genId")
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public class FormRowModel implements Serializable, IsSerializable, Cloneable {

    private int row;
    private double height = -1;

    private Set<FormCellModel> cells = new HashSet<FormCellModel>();

    protected FormRowModel() {
    }

    public FormRowModel(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void addCell(FormCellModel cell) {
        cells.add(cell);
    }

    public Set<FormCellModel> getCells() {
        return cells;
    }

    @JsonProperty
    void setCells(Set<FormCellModel> cells) {
        this.cells = cells;
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
        FormRowModel other = (FormRowModel) obj;
        return row == other.row;
    }

    public Object clone() {
        FormRowModel other = new FormRowModel();
        other.row = row;
        other.height = height;
        Set<FormCellModel> newCells = new HashSet<FormCellModel>();
        for (FormCellModel c : cells) {
            FormCellModel nc = c.clone();
            nc.setRow(other);
            nc.setColumn(c.getColumn());
            newCells.add(nc);
        }
        other.cells = newCells;
        return other;
    }

    @Override
    public String toString() {
        return "[Row: " + getRow() + "]";
    }
}
