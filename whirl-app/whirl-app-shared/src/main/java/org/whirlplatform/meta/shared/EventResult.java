package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.Map;
import org.whirlplatform.meta.shared.data.EventParameter;

@JsonTypeInfo(use = Id.MINIMAL_CLASS, defaultImpl = JavaEventResult.class)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface EventResult {

    EventMetadata getNextEvent();

    void setNextEvent(EventMetadata event);

    boolean hasNextEvent();

    String getNextEventCode();

    void setNextEventCode(String nextEventCode);

    String getTitle();

    void setTitle(String title);

    String getMessage();

    void setMessage(String message);

    String getMessageType();

    void setMessageType(String type);

    void addParameter(EventParameter parameter);

    void setParameter(Integer index, EventParameter parameter);

    Map<Integer, EventParameter> getParametersMap();

    boolean isReady();

    String getRawValue();

    void setRawValue(String value);
}
