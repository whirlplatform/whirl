package org.whirlplatform.component.client.state;

import java.io.Serializable;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

public abstract class AbstractMetadataStateStore<T extends Serializable>
    implements StateStore<T> {

    protected StateScope scope;
    protected ClassMetadata metadata;
    protected StorageWrapper<T> storage;

    public AbstractMetadataStateStore(StateScope scope, ClassMetadata metadata) {
        this.scope = scope;
        this.metadata = metadata;
    }

    @Override
    public StateScope getScope() {
        return scope;
    }

    public void setMetadata(ClassMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean save(String code, T value) {
        if (metadata == null) {
            return false;
        }
        if (storage != null) {
            return storage.put(getKey(code), value);
        }
        return false;
    }

    @Override
    public T restore(String code) {
        if (metadata == null) {
            return null;
        }
        if (storage != null) {
            return storage.get(getKey(code));
        }
        return null;
    }

    @Override
    public T remove(String code) {
        if (metadata == null || storage == null) {
            return null;
        }
        String key = getKey(code);
        T value = storage.get(key);
        storage.remove(key);
        return value;
    }

    private String getKey(String code) {
        String key = metadata.getClassId() + "/" + code;
        return key;
    }

}
