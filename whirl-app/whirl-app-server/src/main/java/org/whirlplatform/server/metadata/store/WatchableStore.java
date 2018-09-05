package org.whirlplatform.server.metadata.store;

import org.whirlplatform.meta.shared.Version;

public interface WatchableStore {

    void addModifiedHandler(String code, Version version, MetadataModifiedHandler handler);

}
