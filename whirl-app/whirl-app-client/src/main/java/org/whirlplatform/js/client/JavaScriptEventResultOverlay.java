package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.data.EventParameter;

public abstract class JavaScriptEventResultOverlay {

    public abstract boolean isReady();

    public abstract void setNextEvent(EventMetadata event);

    public abstract boolean hasNextEvent();

    public abstract void setNextEventCode(String nextEventCode);

    public abstract String getNextEventCode();

    public abstract void setTitle(String title);

    public abstract String getTitle();

    public abstract void setMessage(String message);

    public abstract String getMessage();

    public abstract void setMessageType(String type);

    public abstract String getMessageType();

    public abstract void addParameter(EventParameter parameter);

    /**
     * Добавить параметр в возвращаемый результат.
     * Этот параметр может использоваться следующим событием в цепочке.
     *
     * @param index
     * @param parameter
     */
    public static void setParameter(JavaScriptEventResult instance, int index, EventParameter parameter) {
        instance.setParameter(Integer.valueOf(index), parameter);
    }

    public abstract String getRawValue();

    public abstract void setRawValue(String value);
}


