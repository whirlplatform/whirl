package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.storage.client.StorageHelper;

public class Storage {

    private static final String DATA_VALUE = "DATAVALUE";

    /**
     * Инициализировать локальное хранилище данных.
     *
     * @param type Следует указать DATAVALUE.
     * @return {@link StorageWrapperOverlay StorageManager}
     */
    public static StorageHelper.StorageWrapper<DataValue> local(String type) {
        if (DATA_VALUE.equalsIgnoreCase(type)) {
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
    public static StorageHelper.StorageWrapper<DataValue> session(String type) {
        if (DATA_VALUE.equalsIgnoreCase(type)) {
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
    public static StorageHelper.StorageWrapper<DataValue> memory(String type) {
        if (DATA_VALUE.equalsIgnoreCase(type)) {
            return StorageHelper.memory();
        }
        return null;
    }

}
