package org.whirlplatform.js.client;

import org.timepedia.exporter.client.ExportStaticMethod;
import org.timepedia.exporter.client.Exportable;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.storage.client.StorageHelper;

//@Export("Storage")
//@ExportPackage("Whirl")
public class Storage implements Exportable {

    private static final String DATAVALUE = "DATAVALUE";

    /**
     * Инициализировать локальное хранилище данных.
     *
     * @param type Следует указать DATAVALUE.
     * @return {@link StorageWrapperOverlay StorageManager}
     */
    @ExportStaticMethod
    public static StorageHelper.StorageWrapper<DataValue> local(String type) {
        if (DATAVALUE.equalsIgnoreCase(type)) {
            return StorageHelper.local();
        }
        return null;
    }

    /**
     * Инициализировать хранилище данных в сессии.
     *
     * @param type Следует указать DATAVALUE.
     * @return {@link StorageWrapperOverlay StorageManager}
     */
    @ExportStaticMethod
    public static StorageHelper.StorageWrapper<DataValue> session(String type) {
        if (DATAVALUE.equalsIgnoreCase(type)) {
            return StorageHelper.session();
        }
        return null;
    }

    /**
     * Инициализировать хранилище данных в памяти.
     *
     * @param type Следует указать DATAVALUE.
     * @return {@link StorageWrapperOverlay StorageManager}
     */
    @ExportStaticMethod
    public static StorageHelper.StorageWrapper<DataValue> memory(String type) {
        if (DATAVALUE.equalsIgnoreCase(type)) {
            return StorageHelper.memory();
        }
        return null;
    }

}
