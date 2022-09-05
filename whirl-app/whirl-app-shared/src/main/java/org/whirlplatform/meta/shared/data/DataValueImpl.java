package org.whirlplatform.meta.shared.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import org.whirlplatform.meta.shared.FileValue;

import java.util.Date;

@SuppressWarnings("serial")
public class DataValueImpl implements DataValue {

    private DataType type;
    private String code;
    private String stringValue;
    private Number numberValue;
    private Boolean booleanValue;

    private Date dateValue;
    private ListModelData listValue;
    private FileValue fileValue;

    public DataValueImpl() {
    }

    public DataValueImpl(DataType type) {
        this.type = type;
    }

    public DataValueImpl(DataType type, Object value) {
        this(type);
        setValue(value);
    }

    @Override
    public void setType(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    @JsonIgnore
    public void setValue(Object value) {
        if (DataType.STRING == type) {
            stringValue = (String) value;
        } else if (DataType.NUMBER == type) {
            numberValue = (Number) value;
        } else if (DataType.DATE == type) {
            dateValue = (Date) value;
        } else if (DataType.BOOLEAN == type) {
            booleanValue = (Boolean) value;
        } else if (DataType.LIST == type) {
            listValue = (ListModelData) value;
        } else if (DataType.FILE == type) {
            fileValue = (FileValue) value;
        }
    }

    @Override
    public String getString() {
        return stringValue;
    }

    private Number getNumber() {
        return numberValue;
    }

    @Override
    public Double getDouble() {
        return numberValue == null ? null : numberValue.doubleValue();
    }

    @Override
    public Integer getInteger() {
        return numberValue == null ? null : numberValue.intValue();
    }

    @Override
    public Long getLong() {
        return numberValue == null ? null : numberValue.longValue();
    }

    @Override
    public ListModelData getListModelData() {
        return listValue;
    }

    @Override
    public Date getDate() {
        return dateValue;
    }

    @Override
    public Boolean getBoolean() {
        return booleanValue;
    }

    @Override
    public FileValue getFileValue() {
        return fileValue;
    }

//    @SuppressWarnings("unchecked")
//    @JsonIgnore
//    public <X> X getValue() {
//        if (DataType.STRING == type) {
//            return (X) stringValue;
//        } else if (DataType.NUMBER == type) {
//            return (X) numberValue;
//        } else if (DataType.DATE == type) {
//            return (X) dateValue;
//        } else if (DataType.BOOLEAN == type) {
//            return (X) booleanValue;
//        } else if (DataType.LIST == type) {
//            return (X) listValue;
//        } else if (DataType.FILE == type) {
//            return (X) fileValue;
//        }
//        return null;
//    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (code != null) {
            result.append(code).append(": ");
        }
        final String toStringValue = asString();
        if (toStringValue != null) {
            result.append(toStringValue);
        }
        return result.toString();
    }

    @Override
    public Object getObject() {
        if (getType() == null) {
            if (getString() != null) {
                return getString();
            } else if (getDouble() != null) {
                return getDouble();
            } else if (getInteger() != null) {
                return getInteger();
            } else if (getLong() != null) {
                return getLong();
            } else if (getBoolean() != null) {
                return getBoolean();
            } else if (getDate() != null) {
                return getDate();
            } else if (getListModelData() != null) {
                return getListModelData();
            } else if (getFileValue() != null) {
                return getFileValue();
            }
        } else {
            switch (getType()) {
                case BOOLEAN:
                    return getBoolean();
                case DATE:
                    return getDate();
                case FILE:
                    return getFileValue();
                case LIST:
                    return getListModelData();
                case NUMBER:
                    return getDouble();
                case STRING:
                    return getString();
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    public String asString() {
        if (getType() == null) {
            if (getString() != null) {
                return getString();
            } else if (getDouble() != null) {
                return getDouble().toString();
            } else if (getInteger() != null) {
                return getInteger().toString();
            } else if (getLong() != null) {
                return getLong().toString();
            } else if (getBoolean() != null) {
                return getBoolean().toString();
            } else if (getDate() != null) {
                return getDate().toString();
            } else if (getListModelData() != null) {
                return getListModelData().toString();
            } else if (getFileValue() != null) {
                return getFileValue().toString();
            }
        } else {
            switch (getType()) {
                case BOOLEAN:
                    return getBoolean().toString();
                case DATE:
                    return getDate().toString();
                case FILE:
                    return getFileValue().toString();
                case LIST:
                    return getListModelData().toString();
                case NUMBER:
                    return getNumber().toString();
                case STRING:
                    return getString();
                default:
                    break;
            }
        }
        return "";
    }

    @Override
    public boolean isNull() {
        return (getObject() == null);
    }

    @Override
    public boolean isTypeOf(final DataType type) {
        if (getType() == null || type == null) {
            return false;
        }
        return type.equals(getType());
    }

    public static Object convertValueFromString(String value, String labelValue, DataType type) {
        if (DataType.BOOLEAN == type) {
            return Boolean.valueOf(value);
        } else if (DataType.DATE == type) {
            if (value == null || value.isEmpty()) {
                return null;
            }
            DateTimeFormat df = new DateTimeFormat("dd.MM.yyyy HH:mm:ss", new DefaultDateTimeFormatInfo()) {
            };
            return df.parseStrict(value);
        } else if (DataType.NUMBER == type) {
            if (value == null || value.isEmpty()) {
                return null;
            }
            Number result;
            try {
                result = Double.valueOf(value);
            } catch (NumberFormatException e) {
                result = Double.valueOf(value.replaceFirst(",", "."));
            }
            return result;
        } else if (DataType.LIST == type) {
            ListModelData listValue = new ListModelDataImpl();
            listValue.setId(value);
            listValue.setLabel(labelValue);
            return listValue;
        } else if (DataType.STRING == type) {
            return value;
        } else if (DataType.FILE == type) {
            FileValue fileValue = new FileValue();
            fileValue.setName(value);
            return fileValue;
        }
        return value;
    }

    @Override
    public DataValue clone() {
        DataValueImpl clone = new DataValueImpl();
        clone.type = type;
        clone.code = code;
        clone.stringValue = stringValue;
        clone.numberValue = numberValue;
        clone.booleanValue = booleanValue;
        clone.dateValue = dateValue;
        clone.listValue = listValue != null ? listValue.clone() : null;
        clone.fileValue = fileValue != null ? fileValue.clone() : null;
        return clone;
    }
}