package org.whirlplatform.meta.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableConfig implements Serializable {

    private ClassMetadata metadata;

    public TableConfig() {
    }

    public ClassMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ClassMetadata metadata) {
        this.metadata = metadata;
    }

}
