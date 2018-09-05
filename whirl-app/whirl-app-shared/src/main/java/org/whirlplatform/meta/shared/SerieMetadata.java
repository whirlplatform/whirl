package org.whirlplatform.meta.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerieMetadata implements Serializable {

    private static final long serialVersionUID = 8713692653697232849L;

    String colorStr = "#000";

    private List<String> fieldsMeta = new ArrayList<String>();

    public SerieMetadata() {
    }

    public void addField(String fieldId) {
        fieldsMeta.add(fieldId);
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public List<String> getFields() {
        return fieldsMeta;
    }

    public void setFields(List<String> fieldsMeta) {
        this.fieldsMeta = fieldsMeta;
    }

}