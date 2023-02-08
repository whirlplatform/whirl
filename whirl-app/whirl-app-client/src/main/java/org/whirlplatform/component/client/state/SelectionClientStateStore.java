package org.whirlplatform.component.client.state;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.storage.client.StorageHelper;

public class SelectionClientStateStore<T extends DataValue> extends
    AbstractMetadataStateStore<T> {

    public SelectionClientStateStore(StateScope scope, ClassMetadata metadata) {
        super(scope, metadata);
        switch (scope) {
            case MEMORY:
                storage = StorageHelper.memory();
                break;
            case SESSION:
                storage = StorageHelper.session();
                break;
            case LOCAL:
                storage = StorageHelper.local();
                break;
            default:
        }
    }
}
