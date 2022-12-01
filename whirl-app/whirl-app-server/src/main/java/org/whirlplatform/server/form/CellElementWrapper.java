package org.whirlplatform.server.form;

import java.util.Comparator;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class CellElementWrapper implements Cloneable {

    private CellElement element;
    private RowElementWrapper row;
    private ColumnElementWrapper col;
    private ComponentElement componentElement;
    private ComponentModel component;

    protected CellElementWrapper() {
    }

    public CellElementWrapper(RowElementWrapper row, ColumnElementWrapper col) {
        this.row = row;
        this.col = col;
    }

    public String getId() {
        return element.getId();
    }

    public void setElement(CellElement element) {
        this.element = element;
    }

    public RowElementWrapper getRow() {
        return row;
    }

    public void setRow(RowElementWrapper row) {
        this.row = row;
    }

    public ColumnElementWrapper getColumn() {
        return col;
    }

    public void setColumn(ColumnElementWrapper col) {
        this.col = col;
    }

    public Integer getRowSpan() {
        return element.getRowSpan();
    }

    public Integer getColSpan() {
        return element.getColSpan();
    }

    public int getBorderTop() {
        return element.getBorderTop();
    }

    public String getBorderTopColor() {
        return element.getBorderTopColor();
    }

    public int getBorderRight() {
        return element.getBorderRight();
    }

    public String getBorderRightColor() {
        return element.getBorderRightColor();
    }

    public int getBorderBottom() {
        return element.getBorderBottom();
    }

    public String getBorderBottomColor() {
        return element.getBorderBottomColor();
    }

    public int getBorderLeft() {
        return element.getBorderLeft();
    }

    public String getBorderLeftColor() {
        return element.getBorderLeftColor();
    }

    public String getBackgroundColor() {
        return element.getBackgroundColor();
    }

    public ComponentModel getComponent() {
        return component;
    }

    public void setComponent(ComponentModel component) {
        this.component = component;
    }

    public ComponentElement getComponentElement() {
        return componentElement;
    }

    public void setComponentElement(ComponentElement componentElement) {
        this.componentElement = componentElement;
    }

    public boolean isReplaceable(String name) {
        PropertyType type = PropertyType.valueOf(name);
        if (type == null) {
            return false;
        }
        return componentElement.getProperty(type).isReplaceable();
    }

    @Override
    protected CellElementWrapper clone() {
        CellElementWrapper other = new CellElementWrapper();
        other.element = this.element;
        if (componentElement != null && component != null) {
            other.componentElement = componentElement;
            ComponentModel newComponent = component.clone();
            other.component = newComponent;
        }
        return other;
    }

    @Override
    public String toString() {
        String result = "[";
        if (row != null) {
            result = result + row.toString();
        }
        if (col != null) {
            result = result + col.toString();
        }
        result = result + "]";
        return result;
    }

    public static class CellComparator implements
            Comparator<CellElementWrapper> {

        @Override
        public int compare(CellElementWrapper o1, CellElementWrapper o2) {
            int row1 = o1.getRow().getRow();
            int row2 = o2.getRow().getRow();
            if (row1 < row2) {
                return -1;
            } else if (row1 > row2) {
                return 1;
            }
            int col1 = o1.getColumn().getCol();
            int col2 = o2.getColumn().getCol();
            if (col1 < col2) {
                return -1;
            } else if (col1 > col2) {
                return 1;
            }
            return 0;
        }

    }

}
