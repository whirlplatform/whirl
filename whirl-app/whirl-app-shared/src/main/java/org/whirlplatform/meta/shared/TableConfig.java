package org.whirlplatform.meta.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableConfig implements Serializable {

    private ClassMetadata metadata;

    public TableConfig() {
    }

    public void setMetadata(ClassMetadata metadata) {
        this.metadata = metadata;
    }

    public ClassMetadata getMetadata() {
        return metadata;
    }

}
