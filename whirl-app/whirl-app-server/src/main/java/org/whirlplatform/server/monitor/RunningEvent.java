package org.whirlplatform.server.monitor;

import java.util.UUID;

public abstract class RunningEvent {

    public enum Type {DBEVENT, JAVAEVENT, FORMREQUEST, GRIDREQUEST}

    final private String eventGUID;

    private Type type;
    private String sql;
    private String userLogin;
    private String code;

    public RunningEvent(Type type, String code, String sql, String userLogin) {
        this.type = type;
        this.code = code;
        this.sql = sql;
        this.userLogin = userLogin;
        eventGUID = UUID.randomUUID().toString();
    }

    public String getEventGUID() {
        return eventGUID;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getSql() {
        return sql;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public abstract void onStop();
}
