package org.whirlplatform.js.client;

import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.storage.client.StorageHelper;

//@Export("StorageManager")
//@ExportPackage("Whirl")
public interface StorageWrapperOverlay<T> extends ExportOverlay<StorageHelper.StorageWrapper<T>> {

    boolean put(String code, T value);

    DataValue get(String code);

    void remove(String code);

    void clear();

}
