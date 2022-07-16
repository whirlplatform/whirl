package org.whirlplatform.meta.shared.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = Id.MINIMAL_CLASS)
@JsonSubTypes(@JsonSubTypes.Type(EventParameterImpl.class))
@JsonAutoDetect(fieldVisibility = Visibility.ANY, creatorVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface EventParameter extends Serializable {

    ParameterType getType();

    void setComponentId(String componentId);

    String getComponentId();

    void setComponentCode(String componentCode);

    String getComponentCode();

    void setStorageCode(String storageCode);

    String getStorageCode();

    void setData(DataValue data);

    void setDataWithCode(DataValue data);

    DataValue getData();

    void setCode(String code);

    String getCode();
}
