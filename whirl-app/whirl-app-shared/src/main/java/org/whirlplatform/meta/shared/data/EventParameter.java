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
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    creatorVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public interface EventParameter extends Serializable {

    ParameterType getType();

    String getComponentId();

    void setComponentId(String componentId);

    String getComponentCode();

    void setComponentCode(String componentCode);

    String getStorageCode();

    void setStorageCode(String storageCode);

    void setDataWithCode(DataValue data);

    DataValue getData();

    void setData(DataValue data);

    String getCode();

    void setCode(String code);

    EventParameter clone();
}
