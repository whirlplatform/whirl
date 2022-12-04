package org.whirlplatform.server.metadata.container;

import java.util.concurrent.atomic.AtomicReference;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.server.utils.ApplicationReference;

public interface MetadataContainer {

    AtomicReference<ApplicationReference> getApplication(String code,
                                                         Version version) throws ContainerException;

}
