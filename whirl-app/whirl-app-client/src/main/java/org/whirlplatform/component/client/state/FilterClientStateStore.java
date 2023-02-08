package org.whirlplatform.component.client.state;

import java.util.ArrayList;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FilterValue;
import org.whirlplatform.storage.client.StorageHelper;

public class FilterClientStateStore extends
    AbstractMetadataStateStore<ArrayList<FilterValue>> {

    public FilterClientStateStore(StateScope scope, ClassMetadata metadata) {
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
