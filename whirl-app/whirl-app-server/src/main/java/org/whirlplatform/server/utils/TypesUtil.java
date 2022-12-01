package org.whirlplatform.server.utils;

import java.sql.Types;
import org.whirlplatform.meta.shared.data.DataType;

public class TypesUtil {

    public static DataType fromEimpireType(org.apache.empire.data.DataType type) {
        DataType result;
        switch (type) {
            case BOOL:
                result = DataType.BOOLEAN;
                break;
            case DATE:
            case DATETIME:
                result = DataType.DATE;
                break;
            case DECIMAL:
            case FLOAT:
            case INTEGER:
                result = DataType.NUMBER;
                break;
            case TEXT:
            case CHAR:
            case UNIQUEID:
            case CLOB:
                result = DataType.STRING;
                break;
            case BLOB:
                result = DataType.FILE;
                break;
            default:
                result = DataType.STRING;
                break;
        }
        return result;

    }

    public static org.apache.empire.data.DataType toEmpireType(DataType type,
                                                               DataType listType) {
        if (type == null) {
            return org.apache.empire.data.DataType.UNKNOWN;
        }
        org.apache.empire.data.DataType result;
        switch (type) {
            case BOOLEAN:
                result = org.apache.empire.data.DataType.BOOL;
                break;
            case DATE:
                result = org.apache.empire.data.DataType.DATETIME;
                break;
            case NUMBER:
                result = org.apache.empire.data.DataType.FLOAT;
                break;
            case STRING:
                result = org.apache.empire.data.DataType.TEXT;
                break;
            case LIST:
                result = toEmpireType(listType, null);
                break;
            default:
                result = org.apache.empire.data.DataType.UNKNOWN;
                break;
        }
        return result;
    }

    public static org.apache.empire.data.DataType toEmpireType(int sqlType) {
        org.apache.empire.data.DataType result;
        if (sqlType == Types.TIMESTAMP || sqlType == Types.DATE) {
            result = org.apache.empire.data.DataType.DATETIME;
        } else if (sqlType == Types.DOUBLE || sqlType == Types.NUMERIC
                || sqlType == Types.REAL || sqlType == Types.FLOAT
                || sqlType == Types.DECIMAL || sqlType == Types.TINYINT
                || sqlType == Types.BIGINT || sqlType == Types.INTEGER
                || sqlType == Types.SMALLINT) {
            result = org.apache.empire.data.DataType.DECIMAL;
        } else if (sqlType == Types.CLOB) {
            result = org.apache.empire.data.DataType.CLOB;
        } else if (sqlType == Types.VARCHAR) {
            result = org.apache.empire.data.DataType.TEXT;
        } else if (sqlType == Types.BIT || sqlType == Types.BOOLEAN) {
            result = org.apache.empire.data.DataType.BOOL;
        } else {
            result = org.apache.empire.data.DataType.UNKNOWN;
        }
        return result;
    }

}
