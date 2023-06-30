package org.whirlplatform.server.monitor.mbeans;

public interface ApplicationsMBean {

    public static final String OBJECT_NAME = "Applications";

    /**
     * Получение списка приложений с активными пользователями
     */
    String[] getActiveApplications();

    String[] getCurrentBlocks();

    void addBlock(String appId);

    void removeBlock(String appId);
}
