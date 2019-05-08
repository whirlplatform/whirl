package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;

public interface StorageWrapperOverlay<T> {

    boolean put(String code, T value);

    DataValue get(String code);

    void remove(String code);

    void clear();

}
