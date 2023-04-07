package org.whirlplatform.server.report;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.ReportDataType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.form.CellElementWrapper;
import org.whirlplatform.server.form.ColumnElementWrapper;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.FormWriter;
import org.whirlplatform.server.form.RowElementWrapper;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.utils.XPoint;

// TODO подумать как выстроить нормальную иерархию классов для форм
public class XLSReportWriter extends FormWriter {

    private static long MAX_ROWS = 10000;

    private Report report;

    private HSSFWorkbook workbook;

    private Sheet sheet;

    private Map<XPoint, CellStyle> styleMap = new HashMap<XPoint, CellStyle>();

    private Row currentRow;

    private Cell currentCell;

    private int rows = -1;

    private Set<Short> findedColor = new HashSet<Short>();

    private CreationHelper helper;

    public XLSReportWriter(ConnectionProvider connectionProvider, Report report,
                           FormElementWrapper form,
                           List<DataValue> startParams, ApplicationUser user) {
        super(connectionProvider, form, startParams, user);
        this.report = report;
    }

    @Override
    public void write(OutputStream stream) throws IOException, SQLException, ConnectException {
        workbook = new HSSFWorkbook();
        helper = workbook.getCreationHelper();
        sheet = workbook.createSheet();
        initWorkbook();
        prepareForm();
        build();
        finalizeWorkbook();
        workbook.write(stream);
    }

    @Override
    public void close() {

    }

    private short getFreeColorIndex() {
        for (short i = 0x8; i < 0x40; i++) {
            if (!findedColor.contains(i)) {
                return i;
            }
        }
        return 0x40;
    }

    private HSSFColor getHSSFColor(Color color) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor result = palette.findColor((byte) color.getRed(), (byte) color.getGreen(),
            (byte) color.getBlue());
        if (result == null) {
            short idx = getFreeColorIndex();
            palette.setColorAtIndex(idx, (byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue());
            result = palette.getColor(idx);
        }
        findedColor.add(result.getIndex());
        return result;
    }

    private void initWorkbook() {

        Color backgroud = null;
        String backgroundStr = form.getBackgroudColor();
        if (backgroundStr != null) {
            backgroud = Color.decode(backgroundStr.replace("#", "0x"));
        }

        Color grid = null;
        String gridStr = form.getGridColor();
        if (gridStr != null) {
            gridStr = gridStr.startsWith("#") ? gridStr.replace("#", "0x") : "0x".concat(gridStr);
            grid = Color.decode(gridStr);
        }

        for (int i = 0; i < form.getRows(); i++) {
            RowElementWrapper row = form.getRow(i);
            for (CellElementWrapper c : row.getCells()) {
                CellStyle s = workbook.createCellStyle();
                s.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                if (c.getBackgroundColor() != null) {
                    String tmpColor = c.getBackgroundColor();
                    tmpColor = tmpColor.startsWith("#") ? tmpColor.replace("#", "0x") :
                        "0x".concat(tmpColor);
                    Color color = Color.decode(tmpColor);
                    s.setFillForegroundColor(getHSSFColor(color).getIndex());
                } else if (backgroud != null) {
                    s.setFillForegroundColor(getHSSFColor(backgroud).getIndex());
                } else {
                    s.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                }

                if (c.getBorderBottom() > 0) {
                    s.setBorderBottom(BorderStyle.THIN);
                    if (grid != null) {
                        s.setBottomBorderColor(getHSSFColor(grid).getIndex());
                    }

                }
                if (c.getBorderTop() > 0) {
                    s.setBorderTop(BorderStyle.THIN);
                    if (grid != null) {
                        s.setTopBorderColor(getHSSFColor(grid).getIndex());
                    }

                }
                if (c.getBorderLeft() > 0) {
                    s.setBorderLeft(BorderStyle.THIN);
                    if (grid != null) {
                        s.setLeftBorderColor(getHSSFColor(grid).getIndex());
                    }

                }
                if (c.getBorderRight() > 0) {
                    s.setBorderRight(BorderStyle.THIN);
                    if (grid != null) {
                        s.setRightBorderColor(getHSSFColor(grid).getIndex());
                    }

                }

                if (c.getComponent() != null) {
                    ComponentModel component = c.getComponent();
                    if (c.getComponent().getType() == ComponentType.LabelType
                        || c.getComponent().getType() == ComponentType.HtmlType) {
                        HSSFFont font = workbook.createFont();

                        String color = !component.containsValue(PropertyType.Color.getCode()) ? null
                            : component.getValue(PropertyType.Color.getCode()).getString();
                        if (color != null) {
                            font.setColor(getHSSFColor(
                                Color.decode(color.replace("#", "0x"))).getIndex());
                        } else {
                            font.setColor(getHSSFColor(Color.BLACK).getIndex());
                        }

                        String weight =
                            !component.containsValue(PropertyType.FontWeight.getCode()) ? null
                                : component.getValue(PropertyType.FontWeight.getCode())
                                .getString();
                        if (weight != null) {
                            if ("bold".equalsIgnoreCase(weight)
                                || "bolder".equalsIgnoreCase(weight)) {
                                font.setBold(true);
                            }
                        }

                        String size =
                            !component.containsValue(PropertyType.FontSize.getCode()) ? null
                                : component.getValue(PropertyType.FontSize.getCode())
                                .getString();
                        if (size != null) {
                            if (size.contains("px")) {
                                try {
                                    short sz = Short.parseShort(size.replaceAll("px", ""));
                                    sz = (short) (sz * 72 / 96);
                                    font.setFontHeightInPoints(sz);
                                } catch (Exception ex) {
                                    // skipped
                                }
                            } else if (size.contains("pt")) {
                                try {
                                    short sz = Short.parseShort(size.replaceAll("pt", ""));
                                    font.setFontHeightInPoints(sz);
                                } catch (Exception ex) {
                                    // skipped
                                }
                                short sz = Short.parseShort(size.replaceAll("pt", ""));
                                font.setFontHeightInPoints(sz);
                            } else {
                                font.setFontHeightInPoints((short) 10);
                            }
                        } else {
                            font.setFontHeightInPoints((short) 10);
                        }

                        String style =
                            !component.containsValue(PropertyType.FontStyle.getCode()) ? null
                                : component.getValue(PropertyType.FontStyle.getCode())
                                .getString();
                        if (style != null) {
                            if ("italic".equalsIgnoreCase(style)) {
                                font.setItalic(true);
                            }
                        }

                        String fontName =
                            !component.containsValue(PropertyType.FontFamily.getCode()) ? null
                                : component.getValue(PropertyType.FontFamily.getCode())
                                .getString();
                        if (fontName != null) {
                            font.setFontName(fontName);
                        } else {
                            font.setFontName("arial");
                        }

                        s.setFont(font);
                    }
                    if (ComponentType.LabelType == c.getComponent().getType()
                        || ComponentType.HtmlType == c.getComponent().getType()) {
                        String format = !c.getComponent()
                            .containsValue(PropertyType.ReportDataFormat.getCode()) ? null
                            : c.getComponent().getValue(PropertyType.ReportDataFormat.getCode())
                            .getString();
                        if (format != null && !format.isEmpty()) {
                            s.setDataFormat(helper.createDataFormat().getFormat(format));
                        }
                    }
                }
                styleMap.put(new XPoint(c.getRow().getRow(), c.getColumn().getCol()), s);
            }
        }
    }

    private void finalizeWorkbook() {
        // выставляем ширину колонок
        for (int i = 0; i < form.getCols(); i++) {
            ColumnElementWrapper c = form.getColumn(i);
            double width = c.getWidth();
            if (width > 0) {
                if (width <= 1) {
                    sheet.autoSizeColumn(i);
                } else {
                    try {
                        int w = (int) width;
                        w = (int) (w * 33.333);
                        sheet.setColumnWidth(i, w);
                    } catch (Exception ex) {
                        // skipped
                    }
                }
            }
        }
    }

    private RowElementWrapper createMaxResultRow(RowElementWrapper original) {
        RowElementWrapper empty = new RowElementWrapper(original.getRow());
        RowElement e = new RowElement();
        e.setRow(original.getRow());
        e.setHeight(28d);
        empty.setElement(e);
        empty.setFinalRow(nextRow());
        ColumnElementWrapper col = new ColumnElementWrapper(0);
        CellElementWrapper cell = new CellElementWrapper(empty, col);
        CellElement ce = new CellElement();
        ce.setColSpan(form.getCols());
        ce.setRowSpan(1);
        cell.setElement(ce);

        ComponentModel component = new ComponentModel(ComponentType.LabelType);
        Map<String, DataValue> values = new HashMap<String, DataValue>();
        String warning = "ВНИМАНИЕ! Количество строк данного отчета больше " + MAX_ROWS
            +
            ", отчет сформирован не полностью. Воспользоуйтесь другим форматом отчета для получения полных данных.";
        values.put(PropertyType.Html.getCode(), new DataValueImpl(DataType.STRING, warning));
        component.setValues(values);
        cell.setComponent(component);

        empty.addCell(cell);
        return empty;
    }

    private void writeSpanRegion(int currentRow, CellElementWrapper cell) {
        int rowSpan = cell.getRowSpan() == null ? 0 : cell.getRowSpan();
        int colSpan = cell.getColSpan() == null ? 0 : cell.getColSpan();

        if (rowSpan > 1 || colSpan > 1) {
            int col = cell.getColumn().getFinalCol();
            sheet.addMergedRegion(
                new CellRangeAddress(currentRow, currentRow + cell.getRowSpan() - 1, col,
                    col + cell.getColSpan() - 1));
        }
    }

    protected void writeRow(RowElementWrapper rowIn) {
        RowElementWrapper row = rowIn;
        currentCell = null;

        // , говорим, что достугнуто максимальное число строк
        if (rows > MAX_ROWS - 1) {
            if (!isMaxRowsReached()) {
                row = createMaxResultRow(rowIn);
                setMaxRowsReached(true);
            } else {
                return;
            }
        }

        currentRow = sheet.createRow(row.getFinalRow());
        for (CellElementWrapper c : row.getCells()) {
            writeCell(c);
            writeSpanRegion(row.getFinalRow(), c);
        }

        // ставим высоту строки
        double height = row.getHeight();
        if (height > 1) {
            try {
                short h = (short) height;
                h = (short) (h * 33.333);
                currentRow.setHeight(h);
            } catch (Exception ex) {
                // skipped
            }
        }
    }

    protected int nextRow() {
        rows = rows + 1;
        return rows;
    }

    private void writeCell(CellElementWrapper cell) {
        // start cell
        currentCell = currentRow.createCell(cell.getColumn().getFinalCol());

        ComponentModel component = cell.getComponent();
        writeComponent(component);

        CellStyle style =
            styleMap.get(new XPoint(cell.getRow().getRow(), cell.getColumn().getCol()));
        currentCell.setCellStyle(style);
        // end cell
    }

    private void writeComponent(ComponentModel component) {
        // start component
        if (component == null) {
            return;
        }
        if (component.hasValues()) {
            writeComponentProperties(component);
        }
        // end component
    }

    private void writeComponentProperties(ComponentModel component) {
        if (ComponentType.LabelType == component.getType()
            || ComponentType.HtmlType == component.getType()) {
            String valueStr = !component.containsValue(PropertyType.Html.getCode()) ? null
                : component.getValue(PropertyType.Html.getCode()).getString();
            String type = !component.containsValue(PropertyType.ReportDataType.getCode()) ? null
                : component.getValue(PropertyType.ReportDataType.getCode()).getString();
            if (ReportDataType.NUMBER.name().equals(type)) {
                if (valueStr == null || valueStr.isEmpty()) {
                    currentCell.setCellValue("");
                    return;
                }
                try {
                    currentCell.setCellValue(Double.parseDouble(valueStr.replace(",", ".")));
                } catch (NumberFormatException e) {
                    currentCell.setCellType(CellType.ERROR);
                    currentCell.setCellValue("");
                }
            } else if (ReportDataType.DATE.name().equals(type)) {
                currentCell.setCellType(CellType.NUMERIC);
                if (valueStr == null || valueStr.isEmpty()) {
                    currentCell.setCellValue("");
                    return;
                }
                DateFormat df = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONGEST);
                try {
                    currentCell.setCellValue(df.parse(valueStr));
                } catch (ParseException e) {
                    currentCell.setCellType(CellType.ERROR);
                    currentCell.setCellValue("");
                }
            } else {
                currentCell.setCellType(CellType.STRING);
                currentCell.setCellValue(valueStr);
            }
            // currentCell.setCellType(Cell.CELL_TYPE_BLANK); ПРОВЕРИТЬ, НУЖНО
            // ИЛИ НЕТ
            // currentCell.setCellValue(component.getValue(PropertyType.Html
            // .getCode()));
        }
    }

    protected void addParams(Map<String, DataValue> dest, Map<String, DataValue> params) {
        dest.putAll(params);
    }


}
