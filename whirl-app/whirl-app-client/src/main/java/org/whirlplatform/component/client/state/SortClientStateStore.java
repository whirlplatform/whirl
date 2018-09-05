package org.whirlplatform.component.client.state;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.SortValue;
import org.whirlplatform.storage.client.StorageHelper;

import java.util.ArrayList;

public class SortClientStateStore extends
		AbstractMetadataStateStore<ArrayList<SortValue>> {

	public SortClientStateStore(StateScope scope, ClassMetadata metadata) {
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
