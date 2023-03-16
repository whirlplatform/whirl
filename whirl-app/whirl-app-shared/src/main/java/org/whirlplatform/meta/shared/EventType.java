package org.whirlplatform.meta.shared;

import java.io.Serializable;

public enum EventType implements Serializable {

    Component(false),
    DatabaseFunction(true),
    DatabaseSQL(true),
    JavaScript(false),
    Java(true);

    private boolean server;

    EventType(boolean server) {
        this.server = server;
    }

    public boolean isServer() {
        return server;
    }
}
