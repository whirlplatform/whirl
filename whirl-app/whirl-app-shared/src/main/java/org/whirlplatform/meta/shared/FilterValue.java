package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ListModelData;

@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public class FilterValue implements Serializable {

    private FieldMetadata metadata;

    private FilterType type = FilterType.NO_FILTER;

    private String stringValue;

    private Double numberValueFirst;
    private Double numberValueSecond;

    private Date dateValueFirst;
    private Date dateValueSecond;

    private Boolean booleanValue;

    private ListModelData listValue;

    public FilterValue() {

    }

    public FilterValue(FieldMetadata metadata) {
        this.metadata = metadata;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public FieldMetadata getMetadata() {
        return metadata;
    }

    @SuppressWarnings("unchecked")
    public <X> X getFirstValue() {
        Object result = null;
        if (DataType.STRING == metadata.getType() || DataType.FILE == metadata.getType()) {
            result = stringValue;
        } else if (DataType.NUMBER == metadata.getType()) {
            result = numberValueFirst;
        } else if (DataType.LIST == metadata.getType()) {
            result = listValue;
        } else if (DataType.DATE == metadata.getType()) {
            result = dateValueFirst;
        } else if (DataType.BOOLEAN == metadata.getType()) {
            result = booleanValue;
        }
        return (X) result;
    }

    public <X> void setFirstValue(X value) {
        if (DataType.STRING == metadata.getType() || DataType.FILE == metadata.getType()) {
            stringValue = (String) value;
        } else if (DataType.NUMBER == metadata.getType()) {
            numberValueFirst = (Double) value;
        } else if (DataType.LIST == metadata.getType()) {
            listValue = (ListModelData) value;
        } else if (DataType.DATE == metadata.getType()) {
            dateValueFirst = (Date) value;
        } else if (DataType.BOOLEAN == metadata.getType()) {
            booleanValue = (Boolean) value;
        }
    }

    @SuppressWarnings("unchecked")
    public <X> X getSecondValue() {
        Object result = null;
        if (DataType.NUMBER == metadata.getType()) {
            result = numberValueSecond;
        } else if (DataType.DATE == metadata.getType()) {
            result = dateValueSecond;
        }
        return (X) result;
    }

    public <X> void setSecondValue(X value) {
        if (DataType.NUMBER == metadata.getType()) {
            numberValueSecond = (Double) value;
        } else if (DataType.DATE == metadata.getType()) {
            dateValueSecond = (Date) value;
        }
    }

    public void clear() {
        stringValue = null;
        numberValueFirst = null;
        numberValueSecond = null;
        dateValueFirst = null;
        dateValueSecond = null;
        listValue = null;
        booleanValue = null;
    }

    @Override
    public String toString() {
        return metadata.toString() + ": " + getFirstValue() + " - " + getSecondValue();
    }

    public void setMetadata(FieldMetadata metadata) {
        this.metadata = metadata;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Double getNumberValueFirst() {
        return numberValueFirst;
    }

    public void setNumberValueFirst(Double numberValueFirst) {
        this.numberValueFirst = numberValueFirst;
    }

    public Double getNumberValueSecond() {
        return numberValueSecond;
    }

    public void setNumberValueSecond(Double numberValueSecond) {
        this.numberValueSecond = numberValueSecond;
    }

    public Date getDateValueFirst() {
        return dateValueFirst;
    }

    public void setDateValueFirst(Date dateValueFirst) {
        this.dateValueFirst = dateValueFirst;
    }

    public Date getDateValueSecond() {
        return dateValueSecond;
    }

    public void setDateValueSecond(Date dateValueSecond) {
        this.dateValueSecond = dateValueSecond;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public ListModelData getListValue() {
        return listValue;
    }

    public void setListValue(ListModelData listValue) {
        this.listValue = listValue;
    }
}
