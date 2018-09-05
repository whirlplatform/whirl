package org.whirlplatform.server.monitor.mbeans;

public interface ApplicationsMBean {

    /**
     * Получение списка приложений с активными пользователями
     */
    String[] getActiveApplications();

    String[] getCurrentBlocks();

    void addBlock(String appId);

    void removeBlock(String appId);
}
