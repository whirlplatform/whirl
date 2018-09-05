package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

/**
 * Используется для представления списковых значений
 */
//@Export("ListModelData")
//@ExportPackage("Whirl")
public abstract class ListModelDataOverlay implements
        ExportOverlay<ListModelDataImpl> {

    @Export
    public abstract String getId();

    @Export
    public abstract void setId(String id);

    @Export
    public abstract String getLabel();

    @Export
    public abstract void setLabel(String label);

    @Export
    public abstract DataValue getValue(String property);

    @Export
    public abstract void setValue(String property, DataValue value);

    @ExportInstanceMethod
    public static String[] getPropertyNames(ListModelDataImpl instance) {
        return instance.getPropertyNames().toArray(new String[0]);
    }

}
