package org.whirlplatform.server.form;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.server.utils.XPoint;

public class FormElementWrapper {

    private FormElement element;
    private LocaleElement locale;

    private SortedMap<Integer, RowElementWrapper> rowMap =
            new TreeMap<Integer, RowElementWrapper>();
    private SortedMap<Integer, ColumnElementWrapper> colMap =
            new TreeMap<Integer, ColumnElementWrapper>();
    private Map<XPoint, CellElementWrapper> cellMap = new HashMap<XPoint, CellElementWrapper>();

    private SqlManager sqlManager = new SqlManager();

    private SortedMap<Integer, RowElementWrapper> finalRowMap =
            new TreeMap<Integer, RowElementWrapper>();

    public FormElementWrapper(FormElement element, LocaleElement locale) {
        this.element = element;
        this.locale = locale;
        initSql();
    }

    private void initSql() {
        List<RequestElement> requests = element.getRowRequests();
        if (requests != null) {
            for (RequestElement m : requests) {
                Sql sql = new Sql();
                sql.setObj(m.getId());
                if (m.getDataSource() != null) {
                    sql.setDataSourceAlias(m.getDataSource().getAlias());
                }
                sql.setTop(getRow(m.getTop()));
                sql.setBottom(getRow(m.getBottom()));
                sql.setSql(m.getSql());
                sql.setTextNoData(m.getEmptyText().getValue(locale).getString());
                addSql(sql);
            }
        }
        sqlManager.init();
    }

    public String getId() {
        return element.getId();
    }

    public String getName() {
        return element.getName();
    }

    public int getRows() {
        return element.getProperty(PropertyType.Rows).getValue(locale).getInteger();
    }

    public int getCols() {
        return element.getProperty(PropertyType.Columns).getValue(locale).getInteger();
    }

    public int getWidth() {
        Integer result = element.getProperty(PropertyType.Width).getValue(locale).getInteger();
        return (result == null) ? 0 : result;
    }

    public int getHeight() {
        Integer result = element.getProperty(PropertyType.Height).getValue(locale).getInteger();
        return (result == null) ? 0 : result;
    }

    public boolean isGrid() {
        Boolean result = element.getProperty(PropertyType.Grid).getValue(locale).getBoolean();
        return result == null ? false : result;
    }

    public String getGridColor() {
        return element.getProperty(PropertyType.GridColor).getValue(locale).getString();
    }

    public String getBackgroudColor() {
        return element.getProperty(PropertyType.BackgroundColor).getValue(locale).getString();
    }

    public boolean isBorder() {
        return element.getProperty(PropertyType.Border).getValue(locale).getBoolean();
    }

    public boolean isClosable() {
        return element.getProperty(PropertyType.Closable).getValue(locale).getBoolean();
    }

    public boolean hasSql() {
        return !element.getRowRequests().isEmpty();
    }

    public RowElementWrapper getRow(int index) {
        if (index < 0) {
            throw new RuntimeException("Индекс строки не может быть отрицательным: " + index);
        }
        RowElementWrapper row = rowMap.get(index);
        if (row == null) {
            row = new RowElementWrapper(index);
            rowMap.put(index, row);
        }
        return row;
    }

    public ColumnElementWrapper getColumn(int index) {
        if (index < 0) {
            throw new RuntimeException("Индекс столбца не может быть отрицательным: " + index);
        }
        ColumnElementWrapper col = colMap.get(index);
        if (col == null) {
            col = new ColumnElementWrapper(index);
            colMap.put(index, col);
        }
        return col;
    }

    public CellElementWrapper getCell(int row, int column) {
        XPoint point = new XPoint(row, column);
        CellElementWrapper cell = cellMap.get(point);
        if (cell == null) {
            RowElementWrapper r = getRow(row);
            ColumnElementWrapper c = getColumn(column);
            cell = new CellElementWrapper(r, c);
            r.addCell(cell);
            c.addCell(cell);
            cellMap.put(point, cell);
        }
        return cell;
    }

    private void addSql(Sql sql) {
        sqlManager.addRowSql(sql);
    }

    public Map<Integer, RowElementWrapper> getTopNonSql() {
        RowElementWrapper first = sqlManager.getFirstSqlRow();
        if (first == null) {
            return rowMap;
        }
        return rowMap.headMap(first.getRow());
    }

    public Map<Integer, RowElementWrapper> getBottomNonSql() {
        RowElementWrapper last = sqlManager.getLastSqlRow();
        if (last == null) {
            return null;
        }
        Map<Integer, RowElementWrapper> result = rowMap.tailMap(last.getRow() + 1);
        return result;
    }

    public Map<Integer, RowElementWrapper> getSubRowsInclude(int from, int to) {
        return rowMap.subMap(from, to + 1);
    }

    public Map<Integer, RowElementWrapper> getSubRowsExclude(int from, int to) {
        if (from + 1 > to) {
            return new HashMap<Integer, RowElementWrapper>();
        }
        return rowMap.subMap(from + 1, to);
    }

    public Sql getBeforeSql() {
        return sqlManager.getBeforeSql();
    }

    public Set<Sql> getRootSqls() {
        return sqlManager.getRowRoot();
    }

    public void addFinalRow(RowElementWrapper row) {
        finalRowMap.put(row.getFinalRow(), row);
    }

    public int getFinalRows() {
        return finalRowMap.size();
    }

    public int getFinalHeight() {
        Integer original = 0;
        Integer fin = 0;
        for (RowElementWrapper r : rowMap.values()) {
            if (r.getHeight() != -1) {
                try {
                    original = original + (int) r.getHeight();
                } catch (Exception ex) {
                    // skipped
                }
            }
        }
        for (RowElementWrapper r : finalRowMap.values()) {
            if (r.getHeight() != -1) {
                try {
                    fin = fin + (int) r.getHeight();
                } catch (Exception ex) {
                    // skipped
                }
            }

        }
        return getHeight() - original + fin;
    }

    public int getFinalColumns() {
        return colMap.size();
    }

    public Map<Integer, RowElementWrapper> getFinalRowMap() {
        return Collections.unmodifiableMap(finalRowMap);
    }

    public Map<Integer, ColumnElementWrapper> getFinalColMap() {
        return Collections.unmodifiableMap(colMap);
    }
}
