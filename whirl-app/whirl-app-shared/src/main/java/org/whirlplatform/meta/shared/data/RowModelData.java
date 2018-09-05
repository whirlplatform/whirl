package org.whirlplatform.meta.shared.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = Id.CLASS)
@JsonSubTypes({@JsonSubTypes.Type(RowModelDataImpl.class), @JsonSubTypes.Type(ListModelDataImpl.class)})
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface RowModelData extends Serializable, Cloneable {

    String getId();

    void setId(String id);

    <X> X get(String property);

    <X> X set(String property, X value);

    DataValue getValue(String property);

    DataValue setValue(String property, DataValue value);

    <X> X remove(String property);

    Map<String, Object> getProperties();

    Collection<String> getPropertyNames();

    boolean isEditable();

    void setEditable(boolean editable);

    boolean isDeletable();

    void setDeletable(boolean deletable);

    void setStyle(String fieldValue, String style);

    String getStyle(String fieldName);

    void addChangedField(String fieldName);

    boolean hasChanged(String fieldName);

    void setUnchanged();

    void setLevelCount(int levelCount);

    int getLevelCount();

    RowModelData clone();
}
