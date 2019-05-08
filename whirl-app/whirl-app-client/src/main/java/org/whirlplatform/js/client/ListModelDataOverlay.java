package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

/**
 * Используется для представления списковых значений
 */
public abstract class ListModelDataOverlay {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract DataValue getValue(String property);

    public abstract void setValue(String property, DataValue value);

    public static String[] getPropertyNames(ListModelDataImpl instance) {
        return instance.getPropertyNames().toArray(new String[0]);
    }

}
