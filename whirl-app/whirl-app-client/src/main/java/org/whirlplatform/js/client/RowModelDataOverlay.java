package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;


/**
 * Хранит информацию о данных, хранящихся в строках грида.
 * Позволяет получить идентификатор строки, а также значение, находящееся в определённом столбце этой строки.
 */
public abstract class RowModelDataOverlay {

    public abstract String getId();

    public abstract void setId(String id);

    /**
     * Получить значение {@link DataValueOverlay DataValue} из заданной строки(RowModelData) и имени столбца (String property)
     *
     * @param property
     * @return
     */
    public abstract DataValue getValue(String property);

    public abstract void setValue(String property, DataValue value);

    /**
     * Получить имена всех свойств, связанных с компонентом.
     *
     * @return
     */
    public static String[] getPropertyNames(RowModelDataImpl instance) {
        return instance.getPropertyNames().toArray(new String[0]);
    }

}
