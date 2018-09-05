package org.whirlplatform.editor.shared.merge;

import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ChangeValue implements Serializable {
    private LocaleElement localeElementValue;
    private DataValue dataValue;
    private PropertyType propertyTypeValue;
    private String stringValue;
    private Number numberValue;
    private Boolean booleanValue;
    private Date dateValue;
    private PropertyValue propertyValue;
    private AbstractElement elementValue;

    public ChangeValue() {
    }

    public ChangeValue(Object object) {
        if (object instanceof LocaleElement) {
            localeElementValue = (LocaleElement) object;
        } else if (object instanceof DataValue) {
            dataValue = (DataValue) object;
        } else if (object instanceof PropertyType) {
            propertyTypeValue = (PropertyType) object;
        } else if (object instanceof String) {
            stringValue = (String) object;
        } else if (object instanceof Number) {
            numberValue = (Number) object;
        } else if (object instanceof Boolean) {
            booleanValue = (Boolean) object;
        } else if (object instanceof Date) {
            dateValue = (Date) object;
        } else if (object instanceof PropertyValue) {
            propertyValue = (PropertyValue) object;
        } else if (object instanceof AbstractElement) {
            elementValue = (AbstractElement) object;
        }
    }

    public Object get() {
        if (localeElementValue != null) {
            return localeElementValue;
        }
        if (dataValue != null) {
            return dataValue;
        }
        if (propertyTypeValue != null) {
            return propertyTypeValue;
        }
        if (stringValue != null) {
            return stringValue;
        }
        if (numberValue != null) {
            return numberValue;
        }
        if (booleanValue != null) {
            return booleanValue;
        }
        if (dateValue != null) {
            return dateValue;
        }
        if (propertyValue != null) {
            return propertyValue;
        }
        if (elementValue != null) {
            return elementValue;
        }
        return null;
    }
}
