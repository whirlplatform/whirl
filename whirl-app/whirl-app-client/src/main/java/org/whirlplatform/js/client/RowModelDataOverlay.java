package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;


/**
 * Хранит информацию о данных, хранящихся в строках грида.
 * Позволяет получить идентификатор строки, а также значение, находящееся в определённом столбце этой строки.
 */
//@Export("RowModelData")
//@ExportPackage("Whirl")
public abstract class RowModelDataOverlay implements
        ExportOverlay<RowModelDataImpl> {

    @Export
    public abstract String getId();

    @Export
    public abstract void setId(String id);

    /**
     * Получить значение {@link DataValueOverlay DataValue} из заданной строки(RowModelData) и имени столбца (String property)
     *
     * @param property
     * @return
     */
    @Export
    public abstract DataValue getValue(String property);

    @Export
    public abstract void setValue(String property, DataValue value);

    /**
     * Получить имена всех свойств, связанных с компонентом.
     *
     * @return
     */
    @ExportInstanceMethod
    public static String[] getPropertyNames(RowModelDataImpl instance) {
        return instance.getPropertyNames().toArray(new String[0]);
    }

}
