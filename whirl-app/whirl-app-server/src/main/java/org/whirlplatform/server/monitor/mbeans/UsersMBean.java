package org.whirlplatform.server.monitor.mbeans;

public interface UsersMBean {

    public static final String OBJECT_NAME = "Users";

    String[] getActiveUsers();

    void disconnectUser(String userId);

    String[] getApplicationUsers(String appId);

    void disconnectApplicationUsers(String appId);
}