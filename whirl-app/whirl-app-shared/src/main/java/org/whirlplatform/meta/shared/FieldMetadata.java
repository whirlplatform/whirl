package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.i18n.AppMessage;

/**
 * Класс - описание поля (tfiled_list)
 */
@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "genId")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class FieldMetadata implements Serializable, Cloneable {

    /**
     * Идентификатор
     */
    private String id;

    /**
     * Название поля
     */
    private String name;

    /**
     * Видимое название поля
     */
    private String label;

    /**
     * Редактируемое поле
     */
    private boolean edit;

    /**
     * Видимое поле
     */
    private boolean view;

    /**
     * Скрытое поле
     */
    private boolean hidden;

    /**
     * Ширина поля
     */
    private int length;

    /**
     * Тип поля
     */
    private DataType type;

    /**
     * ClassList таблицы хранящей данные, для TLIST
     */
    private String classId;

    /**
     * Ширина мемо-поля
     */
    private int width;

    /**
     * Высота мемо-поля
     */
    private int height;

    /**
     * Необходимое поле
     */
    private boolean required;

    /**
     * Маска поля в формате regexp
     */
    private String regEx;

    /**
     * Сообщение об ошибки ввода по маске
     */
    private String regExError;

    /**
     * Показывать в фильтре
     */
    private boolean filter = true;

    /**
     * Заполняемое значение
     */
    @JsonIgnore
    private transient Object value;

    private String event;

    /**
     * Поле - Пароль
     */
    private boolean password;

    private ListViewType listViewType;

    private String dataFormat;

    private String color;

    private String labelExpression;

    private String configColumn;

    public FieldMetadata() {
    }

    public FieldMetadata(String name, DataType fieldType, String label) {
        this.name = name;
        this.label = label;
        this.type = fieldType;
    }

    public static String getNativeFieldName(String field) {
        String key = field;
        return key;
    }

    public String getLabel() {
        return getLabel(false);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel(Boolean simple) {
        String ret = "";
        ret = label;

        if (!edit) {
            // TODO рендеринг должен находиться в представлении, а не в
            // метаданных
            ret = "<span title='" + AppMessage.Util.MESSAGE.notEditableField()
                    + "' >" + ret
                    + "<span style='color:#15428B; font-size:12px;'> *</span></span>";
        } else if (required) {
            ret = "<span title='" + AppMessage.Util.MESSAGE.requiredField()
                    + "'>" + ret
                    + "<span style='color:red; font-size:12px;'> *</span></span>";
        }
        return ret;
    }

    public String getRawLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hide) {
        this.hidden = hide;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getRegExError() {
        return regExError;
    }

    public void setRegExError(String regExError) {
        this.regExError = regExError;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean hasEvent() {
        return event != null && !event.isEmpty();
    }

    public ListViewType getListViewType() {
        return listViewType;
    }

    public void setListViewType(ListViewType listViewType) {
        this.listViewType = listViewType;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(String labelExpression) {
        this.labelExpression = labelExpression;
    }

    public String getConfigColumn() {
        return configColumn;
    }

    public void setConfigColumn(String configColumn) {
        this.configColumn = configColumn;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FieldMetadata other = (FieldMetadata) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (this.id == null) {
            return other.id == null;
        } else return this.id.equals(other.id);
    }

    public FieldMetadata clone() {
        FieldMetadata clone = new FieldMetadata();
        clone.id = id;
        clone.name = name;
        clone.label = label;
        clone.edit = edit;
        clone.view = view;
        clone.hidden = hidden;
        clone.length = length;
        clone.type = type;
        clone.classId = classId;
        clone.width = width;
        clone.height = height;
        clone.required = required;
        clone.regEx = regEx;
        clone.regExError = regExError;
        clone.filter = filter;
        clone.event = event;
        clone.password = password;
        clone.listViewType = listViewType;
        clone.dataFormat = dataFormat;
        clone.color = color;
        return clone;
    }

}