package org.whirlplatform.server.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.AppConstant;

public class ServerGetter {

    private static DecimalFormat decimalFormat;

    static {
        decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumIntegerDigits(1);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingUsed(false);
    }

    public static String formatDate(Date value, String format) {
        if (value == null || format == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formattedDate = sdf.format(value);
        return formattedDate;
    }

    public static Date parseDate(String str) throws ParseException {
        DateFormat dd = DateFormat.getDateTimeInstance();
        return dd.parse(str);
    }

    public static String formatNumber(Number value) {
        return decimalFormat.format(value);
    }

    public static String getResultSetValue(ResultSet resultSet, int column)
        throws SQLException {
        String result = resultSet.getString(column);
        if (result == null) {
            return result;
        }
        int columnType = resultSet.getMetaData().getColumnType(column);
        if (columnType == Types.TIMESTAMP || columnType == Types.DATE) {
            Date d = resultSet.getTimestamp(column);
            return ServerGetter.formatDate(d, AppConstant.DATE_FORMAT_LONG);
        } else if (columnType == Types.DOUBLE || columnType == Types.NUMERIC
            || columnType == Types.REAL) {
            return ServerGetter.formatNumber(resultSet.getDouble(column));
        } else if (columnType == Types.FLOAT) {
            return ServerGetter.formatNumber(resultSet.getFloat(column));
        } else {
            return result;
        }
    }

    public static String getResultSetValue(DBReader reader, int column) {
        String result = reader.getString(column);
        if (result == null) {
            return result;
        }
        //        int columnType = reader.getMetaData().getColumnType(column);
        DataType columnType = reader.getColumnExpr(column).getDataType();
        if (DataType.DATETIME == columnType || DataType.DATE == columnType) {
            Date d = reader.getDateTime(column);
            return ServerGetter.formatDate(d, AppConstant.DATE_FORMAT_LONG);
        } else if (DataType.DECIMAL == columnType || DataType.INTEGER == columnType
            || DataType.FLOAT == columnType) {
            return ServerGetter.formatNumber(reader.getDouble(column));
        } else {
            return result;
        }
    }

}
