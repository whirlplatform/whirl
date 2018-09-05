package org.whirlplatform.component.client.state;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.PageConfig;
import org.whirlplatform.storage.client.StorageHelper;

public class PagingClientStateStore extends
		AbstractMetadataStateStore<PageConfig> {

	public PagingClientStateStore(StateScope scope, ClassMetadata metadata) {
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
