package org.whirlplatform.meta.shared.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
@Data
public class FormElement extends ComponentElement {

    // TODO пересмотреть полностью установку этих значений
    private List<RowElement> rowsHeight = new ArrayList<RowElement>();
    private List<ColumnElement> columnsWidth = new ArrayList<ColumnElement>();
    private List<RequestElement> rowRequests = new ArrayList<RequestElement>();
    private Map<CellRowCol, CellElement> cells = new HashMap<CellRowCol, CellElement>();

    public FormElement() {
        super(ComponentType.FormBuilderType);
    }

    public int getRows() {
        PropertyValue r = getProperty(PropertyType.Rows);
        if (r == null) {
            return 0;
        }
        return r.getValue(r.getDefaultLocale()).getDouble().intValue();
    }

    public int getColumns() {
        PropertyValue c = getProperty(PropertyType.Columns);
        if (c == null) {
            return 0;
        }
        return c.getValue(c.getDefaultLocale()).getDouble().intValue();
    }

    public int getHeight() {
        PropertyValue c = getProperty(PropertyType.Height);
        if (c == null) {
            return 0;
        }
        return c.getValue(c.getDefaultLocale()).getDouble().intValue();
    }

    public int getWidth() {
        PropertyValue c = getProperty(PropertyType.Width);
        if (c == null) {
            return 0;
        }
        return c.getValue(c.getDefaultLocale()).getDouble().intValue();
    }

    public List<RequestElement> getRowRequests() {
        return rowRequests;
    }

    public void setRowRequests(List<RequestElement> requests) {
        this.rowRequests = requests;
    }

    public List<RowElement> getRowsHeight() {
        return rowsHeight;
    }

    public void setRowsHeight(List<RowElement> rowsHeight) {
        this.rowsHeight = rowsHeight;
    }

    public List<ColumnElement> getColumnsWidth() {
        return columnsWidth;
    }

    public void setColumnsWidth(List<ColumnElement> columnsWidth) {
        this.columnsWidth = columnsWidth;
    }

    public CellElement addCellElement(int row, int column, CellElement model) {
        return cells.put(new CellRowCol(row, column), model);
    }

    public Map<CellRowCol, CellElement> getCells() {
        return cells;
    }

    public CellElement findCellElement(int row, int column) {
        return cells.get(new CellRowCol(row, column));
    }

    public void removeCell(int row, int column) {
        cells.remove(new CellRowCol(row, column));
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }
}
