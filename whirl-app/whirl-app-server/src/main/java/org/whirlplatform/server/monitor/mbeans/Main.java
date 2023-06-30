package org.whirlplatform.server.monitor.mbeans;

import org.whirlplatform.server.metadata.container.MetadataContainer;

public class Main implements MainMBean {

    private MetadataContainer metadataContainer;

    public Main(MetadataContainer metadataContainer) {
        this.metadataContainer = metadataContainer;
    }

    @Override
    public void clearMetadataStoreCache() {
        metadataContainer.clearCache();
    }
}
