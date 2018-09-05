package org.whirlplatform.server.metadata.store;

import org.whirlplatform.meta.shared.editor.ApplicationElement;

public interface MetadataModifiedHandler {

    void loaded(ApplicationElement element);

}
