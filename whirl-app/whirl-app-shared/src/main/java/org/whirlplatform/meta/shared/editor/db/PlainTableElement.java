package org.whirlplatform.meta.shared.editor.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class PlainTableElement extends DatabaseTableElement {

    private String tableName;
    private TableColumnElement idColumn;
    private TableColumnElement deleteColumn;
    private TableColumnElement labelExpression;
    private Set<TableColumnElement> columns = new HashSet<TableColumnElement>();
    private ViewElement view;

    private PlainTableElement clonedTable;
    private Set<PlainTableElement> clones = new HashSet<PlainTableElement>();

    public PlainTableElement() {

    }

    public PlainTableElement(SchemaElement schema) {
        setSchema(schema);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public TableColumnElement getColumn(String column) {
        if (column == null) {
            return null;
        }
        for (TableColumnElement c : columns) {
            if (column.equals(c.getColumnName())) {
                return c;
            }
        }
        return null;
    }

    public void addColumn(TableColumnElement column) {
        columns.add(column);
        column.setTable(this);
    }

    public void removeColumn(TableColumnElement column) {
        columns.remove(column);
        column.setTable(null);
    }

    public Collection<TableColumnElement> getColumns() {
        return Collections.unmodifiableSet(columns);
    }

    @JsonProperty
    void setColumns(Set<TableColumnElement> columns) {
        this.columns = columns;
    }

    public List<TableColumnElement> getSortedColumns() {
        Comparator<TableColumnElement> comparator = new Comparator<TableColumnElement>() {

            @Override
            public int compare(TableColumnElement o1, TableColumnElement o2) {
                return o1.getIndex() - o2.getIndex();
            }
        };
        List<TableColumnElement> result = new ArrayList<TableColumnElement>(columns);
        Collections.sort(result, comparator);
        return Collections.unmodifiableList(result);
    }

    public void changeColumns(Collection<TableColumnElement> columns) {
        // удаление
        Iterator<TableColumnElement> iter = this.columns.iterator();
        while (iter.hasNext()) {
            TableColumnElement g = iter.next();
            if (!columns.contains(g)) {
                iter.remove();
                g.setTable(null);
            }
        }
        // добавление
        for (TableColumnElement newColumn : columns) {
            if (!this.columns.contains(newColumn)) {
                addColumn(newColumn);
            }
        }
    }

    public ViewElement getView() {
        return view;
    }

    public void setView(ViewElement view) {
        this.view = view;
    }

    public void addClone(PlainTableElement clone) {
        clones.add(clone);
        clone.setClonedTable(this);
        clone.setSchema(schema);
    }

    public PlainTableElement getClonedTable() {
        return clonedTable;
    }

    protected void setClonedTable(PlainTableElement clonedTable) {
        this.clonedTable = clonedTable;
    }

    public boolean isClone() {
        return clonedTable != null;
    }

    public void removeClone(PlainTableElement table) {
        clones.remove(table);
        table.setClonedTable(null);
    }

    public Set<PlainTableElement> getClones() {
        return clones;
    }

    @JsonProperty
    void setClones(Set<PlainTableElement> clones) {
        this.clones = clones;
    }

    public TableColumnElement getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(TableColumnElement idColumn) {
        if (idColumn != null) {
            idColumn.setTable(null);
            this.idColumn = idColumn;
            this.idColumn.setTable(this);
        } else {
            this.idColumn = null;
        }
    }

    public TableColumnElement getDeleteColumn() {
        return deleteColumn;
    }

    public void setDeleteColumn(TableColumnElement deleteColumn) {
        if (deleteColumn != null) {
            deleteColumn.setTable(null);
            this.deleteColumn = deleteColumn;
            this.deleteColumn.setTable(this);
        } else {
            this.deleteColumn = null;
        }
    }

    public TableColumnElement getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(TableColumnElement labelExpression) {
        if (labelExpression != null) {
            labelExpression.setTable(null);
            this.labelExpression = labelExpression;
            this.labelExpression.setTable(this);
        } else {
            this.labelExpression = null;
        }
    }

    public void setSchema(SchemaElement schema) {
        this.schema = schema;
        if (view != null) {
            view.setSchema(schema);
        }
        for (PlainTableElement t : clones) {
            t.setSchema(schema);
        }
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
