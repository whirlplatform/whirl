package org.whirlplatform.server.expimp;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.db.DBReader;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XLSExporter extends Exporter {

    private static final int MAX_ROWS = 10000;

    private ClassMetadata metadata;

    private DBReader reader;

    private boolean columnHeader = false;

    private boolean xlsx = false;

    public XLSExporter(ClassMetadata metadata, DBReader reader) {
        this.metadata = metadata;
        this.reader = reader;
    }

    public void setColumnHeader(boolean columnHeader) {
        this.columnHeader = columnHeader;
    }

    public void setXslx(boolean xlsx) {
        this.xlsx = xlsx;
    }

    public void write(OutputStream stream) throws SQLException, IOException {
        Workbook workbook;
        if (xlsx) {
            workbook = new SXSSFWorkbook(5);
        } else {
            workbook = new HSSFWorkbook();
        }

        Sheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(25);

        int currentRow = 0;

        Row row = sheet.createRow(currentRow);
        int currentCell = 0;
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.HAIR);
        headerStyle.setBorderBottom(BorderStyle.HAIR);
        headerStyle.setBorderLeft(BorderStyle.HAIR);
        headerStyle.setBorderRight(BorderStyle.HAIR);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
                .getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle resultStyle = workbook.createCellStyle();
        resultStyle.setBorderTop(BorderStyle.HAIR);
        resultStyle.setBorderBottom(BorderStyle.HAIR);
        resultStyle.setBorderLeft(BorderStyle.HAIR);
        resultStyle.setBorderRight(BorderStyle.HAIR);

        Map<FieldMetadata, CellStyle> dateStyles = new HashMap<FieldMetadata, CellStyle>();

        CellStyle numericStyle = workbook.createCellStyle();
        numericStyle.cloneStyleFrom(resultStyle);

        for (FieldMetadata f : metadata.getFields()) {
            if (!f.isView()) {
                continue;
            }
            if (f.getType() == DataType.DATE && !dateStyles.containsKey(f)) {
                CellStyle cs = workbook.createCellStyle();
                cs.cloneStyleFrom(resultStyle);
                String dateFormat = f.getDataFormat();
                if (StringUtils.isEmpty(dateFormat)) {
                    dateFormat = AppConstant.DATE_FORMAT_LONG;
                }
                cs.setDataFormat(workbook.createDataFormat().getFormat(
                        dateFormat));
                dateStyles.put(f, cs);
            }
            Cell cell = row.createCell(currentCell);
            cell.setCellStyle(headerStyle);

            String value = null;
            if (columnHeader) {
                value = f.getName();
            } else {
                value = f.getRawLabel();
            }
            cell.setCellValue(value);
            currentCell = currentCell + 1;
        }
        currentRow = currentRow + 1;


        while (reader.moveNext()) {
            currentCell = 0;
            row = sheet.createRow(currentRow);
            if (!xlsx && currentRow > MAX_ROWS) {
                Cell cell = row.createCell(currentCell);
                sheet.addMergedRegion(new CellRangeAddress(currentRow,
                        currentRow, currentCell, currentCell + 5));
                RichTextString text = new HSSFRichTextString(
                        "ВНИМАНИЕ! Количество строк данного отчета больше "
                                + MAX_ROWS
                                + ", отчет сформирован не полностью. Воспользоуйтесь другим форматом отчета для получения полных данных.");
                HSSFFont font = (HSSFFont) workbook.createFont();
                font.setColor(IndexedColors.RED.getIndex());
                text.applyFont(font);
                cell.setCellValue(text);
                break;
            }

            for (FieldMetadata f : metadata.getFields()) {
                if (!f.isView()) {
                    continue;
                }
                Object value = getColumnValue(f, reader);

                // Если ячейка стилизованная, приводим строку к нужному виду
                Cell cell = row.createCell(currentCell);
                cell.setCellStyle(resultStyle);
                if (value != null) {
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    } else if (value instanceof Timestamp) {
                        Timestamp time = (Timestamp) value;
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellStyle(dateStyles.get(f));
                        cell.setCellValue(new Date(time.getTime()));
                    } else if (value instanceof Integer) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof Clob) {
                        Clob clob = (Clob) value;
                        String str, content = "";
                        BufferedReader re = new BufferedReader(
                                clob.getCharacterStream());
                        while ((str = re.readLine()) != null) {
                            content += (content.equals("") ? "" : "\n") + str;
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(content);
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(value));
                    }
                }
                currentCell = currentCell + 1;
            }
            currentRow = currentRow + 1;

            Thread.yield();
        }
        workbook.write(stream);
        workbook.close();
    }
}
