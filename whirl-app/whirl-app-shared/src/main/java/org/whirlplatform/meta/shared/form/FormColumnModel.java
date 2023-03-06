package org.whirlplatform.meta.shared.form;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;

@SuppressWarnings("serial")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "genId")
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public class FormColumnModel implements Serializable, Cloneable {

    private int col;

    private double width = -1;

    protected FormColumnModel() {
    }

    public FormColumnModel(int column) {
        this.col = column;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
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
        FormColumnModel other = (FormColumnModel) obj;
        return col == other.col;
    }

    @Override
    public String toString() {
        return "[Column: " + getCol() + "]";
    }

}
