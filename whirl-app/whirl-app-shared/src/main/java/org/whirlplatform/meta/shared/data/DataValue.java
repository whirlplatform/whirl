package org.whirlplatform.meta.shared.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.util.Date;
import org.whirlplatform.meta.shared.FileValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = Id.MINIMAL_CLASS, defaultImpl = DataValueImpl.class)
@JsonSubTypes({@JsonSubTypes.Type(DataValueImpl.class), @JsonSubTypes.Type(RowListValueImpl.class)})
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface DataValue extends Serializable, Cloneable {

    DataType getType();

    void setType(DataType type);

    @Deprecated
    <X> void setValue(X value);

    Object getObject();

    String getString();

    Double getDouble();

    Integer getInteger();

    Long getLong();

    ListModelData getListModelData();

    Date getDate();

    Boolean getBoolean();

    FileValue getFileValue();

    boolean isNull();

    boolean isTypeOf(DataType type);

    String asString();

    String getCode();

    void setCode(String code);

    DataValue clone();
}
