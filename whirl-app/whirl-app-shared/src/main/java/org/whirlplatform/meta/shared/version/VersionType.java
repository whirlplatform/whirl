package org.whirlplatform.meta.shared.version;

import java.io.Serializable;

public enum VersionType implements Serializable {
    VERSION {
        @Override
        public String folderName() {
            return "tag";
        }
    },
    BRANCH {
        @Override
        public String folderName() {
            return "branch";
        }
    };

    public abstract String folderName();
}
