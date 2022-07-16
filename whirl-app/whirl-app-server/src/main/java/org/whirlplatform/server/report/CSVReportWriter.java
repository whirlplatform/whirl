
package org.whirlplatform.server.report;

import au.com.bytecode.opencsv.CSVWriter;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.ReportDataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.form.CellElementWrapper;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.FormWriter;
import org.whirlplatform.server.form.RowElementWrapper;
import org.whirlplatform.server.login.ApplicationUser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVReportWriter extends FormWriter {

    private int row = -1;

    private CSVWriter writer;

    private Map<Integer, String> lastValues;

    private int colSpanBaseCol = -1;

    private int colSpanBaseSpan = 0;

    private Map<Integer, Integer> rowSpanBaseRow = new HashMap<Integer, Integer>();

    private Map<Integer, Integer> rowSpanBaseSpan = new HashMap<Integer, Integer>();

    private Map<Integer, Integer> rowSpanBaseColSpan = new HashMap<Integer, Integer>();

    private Set<Integer> rowSpanBaseToDelete = new HashSet<Integer>();

    public CSVReportWriter(ConnectionProvider connectionProvider, FormElementWrapper form,
                           Collection<DataValue> startParams, ApplicationUser user) {
        super(connectionProvider, form, startParams, user);
    }

    @Override
    protected int nextRow() {
        row = row + 1;
        return row;
    }

    @Override
    protected void writeRow(RowElementWrapper row) throws IOException {
        Map<Integer, String> values = new HashMap<Integer, String>();
        int r = row.getRow();

        for (CellElementWrapper cell : row.getCells()) {
            int c = cell.getColumn().getCol();

            // началось объединение по строкам
            if (isRowSpanStart(cell)) {
                rowSpanBaseRow.put(c, r);
                rowSpanBaseSpan.put(c, cell.getRowSpan());
                rowSpanBaseColSpan.put(c, cell.getColSpan());
            } else if (isColSpanStart(cell)) {
                // началось объединение по колонкам
                colSpanBaseCol = cell.getColumn().getCol();
                colSpanBaseSpan = cell.getColSpan();
            }

            // определяем значение
            String value = null;
            if (isInRowSpanNotBase(r, c)) {
                // если у нас объединение по строкам и это не базовая колонка,
                // то берем значение из базовой колонки предыдущей строки
                int baseCol = getRowSpanBaseCol(r, c);
                if (r == rowSpanBaseRow.get(baseCol)) {
                    // если первая строка, то берем значение из текущей строки
                    // базовой колонки
                    value = values.get(baseCol);
                } else {
                    // иначе берем значение предыдущей строки
                    value = lastValues.get(baseCol);
                }
            } else if (isInColSpanNotBase(r, c)) {
                // если у нас значение попадает в объединение по колонкам и это
                // не базовая колонка,
                // берем значение из базовой колонки
                value = values.get(colSpanBaseCol);
            } else {
                // берем текущее значение
                value = getCellValue(cell);
            }

            values.put(c, value);

            if (isRowSpanEnd(r, c)) {
                // если закончилось объединение по строкам, то надо на следующем
                // шаге удалить если она действительно кончилась
                int baseCol = getRowSpanBaseCol(r, c);
                rowSpanBaseToDelete.add(baseCol);
            } else if (isColSpanEnd(r, c)) {
                // если закончилось объединение по колонкам, то удаляем
                // сохраненные значения
                colSpanBaseCol = -1;
                colSpanBaseSpan = 0;
            }

        }

        // если у нас есть помеченное на удаление вертикальное объединение,
        // то удалим
        deleteRowSpan(r);

        writer.writeNext(values.values().toArray(new String[0]));
        writer.flush();
        lastValues = values;
    }

    private boolean isColSpanStart(CellElementWrapper cell) {
        return cell.getColSpan() > 1 && cell.getRowSpan() == 1;
    }

    private boolean isRowSpanStart(CellElementWrapper cell) {
        return cell.getRowSpan() > 1 && !rowSpanBaseRow.containsKey(cell.getColumn().getCol());
    }

    private boolean isInColSpanNotBase(Integer row, Integer col) {
        return colSpanBaseCol != -1 && col > colSpanBaseCol && col <= colSpanBaseCol + colSpanBaseSpan - 1;
    }

    private boolean isInRowSpanNotBase(Integer row, Integer col) {
        for (Integer baseCol : rowSpanBaseRow.keySet()) {
            int baseRow = rowSpanBaseRow.get(baseCol);
            int rowSpan = rowSpanBaseSpan.get(baseCol);
            int colSpan = rowSpanBaseColSpan.get(baseCol);

            if (row <= baseRow + rowSpan - 1 && (baseRow == row && col > baseCol && col <= baseCol + colSpan - 1)
                    || (baseRow < row && col >= baseCol && col <= baseCol + colSpan - 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean isColSpanEnd(Integer row, Integer col) {
        return colSpanBaseCol != -1 && col == colSpanBaseCol + colSpanBaseSpan - 1;
    }

    private boolean isRowSpanEnd(Integer row, Integer col) {
        for (Integer baseCol : rowSpanBaseRow.keySet()) {
            int baseRow = rowSpanBaseRow.get(baseCol);
            int rowSpan = rowSpanBaseSpan.get(baseCol);
            int colSpan = rowSpanBaseColSpan.get(baseCol);
            if (baseRow < row && row == baseRow + rowSpan - 1 && col >= baseCol && col <= baseCol + colSpan - 1) {
                return true;
            }
        }
        return false;
    }

    private void deleteRowSpan(Integer row) {
        Iterator<Integer> iter = rowSpanBaseToDelete.iterator();
        while (iter.hasNext()) {
            Integer col = iter.next();

            int base = getRowSpanBaseCol(row, col);
            if (row > rowSpanBaseRow.get(base)) {
                iter.remove();
                rowSpanBaseToDelete.remove(base);
                rowSpanBaseRow.remove(base);
                rowSpanBaseSpan.remove(base);
                rowSpanBaseColSpan.remove(base);
            }
        }
    }

    private int getRowSpanBaseCol(Integer row, Integer col) {
        for (Integer baseCol : rowSpanBaseRow.keySet()) {
            int colSpan = rowSpanBaseColSpan.get(baseCol);
            if (col >= baseCol && col <= baseCol + colSpan - 1) {
                return baseCol;
            }
        }
        return -1;
    }

    private String getCellValue(CellElementWrapper cell) {
        String result = null;
        ComponentModel component = cell.getComponent();
        if (component != null) {
            if (ComponentType.LabelType == component.getType() || ComponentType.HtmlType == component.getType()) {
                String valueStr = !component.containsValue(PropertyType.Html.getCode()) ? null
                        : component.getValue(PropertyType.Html.getCode()).getString();
                String type = !component.containsValue(PropertyType.ReportDataType.getCode()) ? null
                        : component.getValue(PropertyType.ReportDataType.getCode()).getString();
                String fmt = !component.containsValue(PropertyType.ReportDataFormat.getCode()) ? null
                        : component.getValue(PropertyType.ReportDataFormat.getCode()).getString();

                if (valueStr == null || valueStr.isEmpty()) {
                    return "";
                }

                if (fmt == null || fmt.isEmpty()) {
                    return valueStr;
                }

                if (ReportDataType.NUMBER.name().equals(type)) {
                    try {
                        DecimalFormat nf = new DecimalFormat(fmt);
                        return nf.format(Double.parseDouble(valueStr.replace(",", ".")));

                    } catch (NumberFormatException e) {
                        // xmlStream.writeCharacters("");
                    }
                } else if (ReportDataType.DATE.name().equals(type)) {
                    DateFormat dfIn = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONGEST);
                    try {
                        SimpleDateFormat dfOut = new SimpleDateFormat(fmt);
                        return dfOut.format(dfIn.parse(valueStr));
                    } catch (ParseException e) {
                        // xmlStream.writeCharacters("");
                    }
                } else {
                    return valueStr;
                }
                // result = component.getValue(PropertyType.Html.getCode());
            }
        }
        return result;
    }

    @Override
    public void write(OutputStream stream) throws IOException, SQLException, ConnectException {
        writer = new CSVWriter(new OutputStreamWriter(stream));
        build();
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

}
