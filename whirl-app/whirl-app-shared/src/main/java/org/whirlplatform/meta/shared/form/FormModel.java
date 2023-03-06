package org.whirlplatform.meta.shared.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "width", "height", "columns", "rows"})
public class FormModel implements Serializable {

    private String id;

    private int width;
    private int height;

    private int rows;
    private int columns;

    private Map<String, FormRowModel> rowMap = new HashMap<>();

    private Map<String, FormColumnModel> colMap = new HashMap<>();

    protected FormModel() {
    }

    public FormModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public FormRowModel getRow(int row) {
        return rowMap.get(String.valueOf(row));
    }

    public void addRow(FormRowModel row) {
        rowMap.put(String.valueOf(row.getRow()), row);
    }

    public FormColumnModel getColumn(int column) {
        return colMap.get(String.valueOf(column));
    }

    public void addColumn(FormColumnModel column) {
        colMap.put(String.valueOf(column.getCol()), column);
    }

    public Collection<FormRowModel> getRows() {
        return rowMap.values();
    }

    @JsonProperty
    void setRows(Collection<FormRowModel> rows) {
        rowMap.clear();
        for (FormRowModel row : rows) {
            addRow(row);
        }
    }

    public Collection<FormColumnModel> getColumns() {
        return colMap.values();
    }

    @JsonProperty
    void setColumns(Collection<FormColumnModel> columns) {
        colMap.clear();
        for (FormColumnModel col : columns) {
            addColumn(col);
        }
    }

    public int getColumnCount() {
        return columns;
    }

    public void setColumnCount(int columns) {
        this.columns = columns;
    }

    public int getRowCount() {
        return rows;
    }

    public void setRowCount(int rows) {
        this.rows = rows;
    }

}
