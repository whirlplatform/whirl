
package org.whirlplatform.server.report;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.ReportDataType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
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
public class XLSXReportWriter extends FormWriter {

    private Report report;

    private SXSSFWorkbook workbook;

    private SXSSFSheet sheet;

    private Map<XPoint, CellStyle> styleMap = new HashMap<XPoint, CellStyle>();

    private Row currentRow;

    private Cell currentCell;

    private int rows = -1;

    private CreationHelper helper;

    public XLSXReportWriter(ConnectionProvider connectionProvider, Report report,
                            FormElementWrapper form, Collection<DataValue> startParams,
                            ApplicationUser user) {
        super(connectionProvider, form, startParams, user);
        this.report = report;

    }

    @Override
    public void write(OutputStream stream) throws IOException, SQLException, ConnectException {
        workbook = new SXSSFWorkbook(1);
        helper = workbook.getCreationHelper();
        sheet = workbook.createSheet();
        initWorkbook();
        prepareForm();
        build();
        finalizeWorkbook();
        workbook.write(stream);
    }

    @Override
    protected void addResultPramsWhilePrepare(Map<String, DataValue> dest,
                                              Map<String, DataValue> params) {
        addParams(dest, params);
    }

    @Override
    public void close() {
        workbook.dispose();
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

        // В новых версиях poi обязательно заранее указать колонки для авторазмера
        for (int i = 0; i < form.getCols(); i++) {
            ColumnElementWrapper c = form.getColumn(i);
            double w = c.getWidth();
            if (w > 0 && w <= 1) {
                sheet.trackColumnForAutoSizing(i);
            }
        }

        for (int i = 0; i < form.getRows(); i++) {
            RowElementWrapper row = form.getRow(i);
            for (CellElementWrapper c : row.getCells()) {
                XSSFCellStyle s = (XSSFCellStyle) workbook.createCellStyle();
                s.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                if (c.getBackgroundColor() != null) {
                    String tmpColor = c.getBackgroundColor();
                    tmpColor = tmpColor.startsWith("#") ? tmpColor.replace("#", "0x") :
                            "0x".concat(tmpColor);
                    s.setFillForegroundColor(new XSSFColor(Color.decode(tmpColor)));
                } else if (backgroud != null) {
                    s.setFillForegroundColor(new XSSFColor(backgroud));
                } else {
                    s.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                }

                if (c.getBorderBottom() > 0) {
                    s.setBorderBottom(BorderStyle.THIN);
                    if (grid != null) {
                        s.setBottomBorderColor(new XSSFColor(grid));
                    }

                }
                if (c.getBorderTop() > 0) {
                    s.setBorderTop(BorderStyle.THIN);
                    if (grid != null) {
                        s.setTopBorderColor(new XSSFColor(grid));
                    }

                }
                if (c.getBorderLeft() > 0) {
                    s.setBorderLeft(BorderStyle.THIN);
                    if (grid != null) {
                        s.setLeftBorderColor(new XSSFColor(grid));
                    }

                }
                if (c.getBorderRight() > 0) {
                    s.setBorderRight(BorderStyle.THIN);
                    if (grid != null) {
                        s.setRightBorderColor(new XSSFColor(grid));
                    }

                }

                if (c.getComponent() != null) {
                    ComponentModel component = c.getComponent();
                    if (c.getComponent().getType() == ComponentType.LabelType
                            || c.getComponent().getType() == ComponentType.HtmlType) {
                        XSSFFont font = (XSSFFont) workbook.createFont();

                        String color = !component.containsValue(PropertyType.Color.getCode()) ? null
                                : component.getValue(PropertyType.Color.getCode()).getString();
                        if (color != null) {
                            font.setColor(new XSSFColor(Color.decode(color.replace("#", "0x"))));
                        } else {
                            font.setColor(IndexedColors.BLACK.getIndex());
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

    protected void writeRow(RowElementWrapper row) {
        currentCell = null;
        currentRow = sheet.createRow(row.getFinalRow());
        for (CellElementWrapper c : row.getCells()) {
            writeCell(c);
            writeSpanRegion(row.getFinalRow(), c);
        }

        // ставим высоту строки
        double height = row.getHeight();
        if (height > 0) {
            if (height <= 1) {
                try {
                    short h = (short) height;
                    h = (short) (h * 33.333);
                    currentRow.setHeight(h);
                } catch (Exception ex) {
                    // skipped
                }
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
            component.setValue("Type",
                    new DataValueImpl(DataType.STRING, component.getType().name()));
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
                currentCell.setCellType(CellType.NUMERIC);
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
                currentCell.setCellType(CellType.ERROR);
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
                currentCell.setCellType(CellType.ERROR);
                currentCell.setCellValue(valueStr);
            }
            // currentCell.setCellType(Cell.CELL_TYPE_STRING);
            // currentCell.setCellValue(component.getValue(PropertyType.Html
            // .getCode()));
        }
    }

    protected void addParams(Map<String, DataValue> dest, Map<String, DataValue> params) {
        dest.putAll(params);
    }

}
