package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowValue;


/**
 * Список метаданных о строках списка или таблицы {@link RowValueOverlay RowValue} с методами доступа и манипуляции ими
 */
//@Export("RowListValue")
//@ExportPackage("Whirl")
public abstract class RowListValueOverlay implements
        ExportOverlay<RowListValueImpl> {


    /**
     * Получить список объектов, хранящих служебную информацию о строках грида {@link RowValueOverlay RowValue}[]
     * Значений отдельных ячеек здесь нет.
     *
     * @return Список метаданных о строках грида {@link RowValueOverlay RowValue}[]
     */
    @ExportInstanceMethod
    public static RowValue[] getValues(RowListValue instance) {
        return instance.getRowList().toArray(new RowValue[0]);
    }

    /**
     * Добавить новую строку  {@link RowValueOverlay RowValue} в список строк
     *
     * @param row {@link RowValueOverlay RowValue}
     */
    @ExportInstanceMethod
    public static void addValue(RowListValue instance, RowValue row) {
        instance.addRowValue(row);
    }

    /**
     * Проверяет наличие элемента {@link RowValueOverlay RowValue} в коллекции
     *
     * @param row {@link RowValueOverlay RowValue}
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean containsValue(RowListValue instance, RowValue row) {
        return instance.containsRow(row);
    }

    @ExportInstanceMethod
    @Deprecated
    public static RowValue[] getValue(RowListValue instance) {
        return instance.getRowList().toArray(new RowValue[0]);
    }

    @Export
    @Deprecated
    public abstract void addRowValue(RowValue row);

    @Export
    @Deprecated
    public abstract boolean containsRow(RowValue row);

    @Export
    public abstract String getCode();
}
