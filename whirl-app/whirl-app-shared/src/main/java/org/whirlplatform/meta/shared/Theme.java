package org.whirlplatform.meta.shared;

import java.io.Serializable;

public enum Theme implements Serializable {

    BLUE("blue"), GRAY("gray"), NEPTUNE("neptune"), TRITON("triton"),
    NEPTUNE_ENERGY_GREEN("neptuneenergygreen");

    private String path;

    Theme(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
