package org.whirlplatform.server.metadata.container;

import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.server.utils.ApplicationReference;

import java.util.concurrent.atomic.AtomicReference;

public interface MetadataContainer {

    AtomicReference<ApplicationReference> getApplication(String code,
                                                         Version version) throws ContainerException;

}
