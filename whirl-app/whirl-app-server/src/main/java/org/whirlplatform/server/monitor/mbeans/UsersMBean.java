package org.whirlplatform.server.monitor.mbeans;

public interface UsersMBean {

    String[] getActiveUsers();

    void disconnectUser(String userId);

    String[] getApplicationUsers(String appId);

    void disconnectApplicationUsers(String appId);
}