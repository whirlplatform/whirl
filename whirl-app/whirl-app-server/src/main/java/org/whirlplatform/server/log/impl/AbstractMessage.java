package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.log.Message;
import org.whirlplatform.server.login.ApplicationUser;

public abstract class AbstractMessage implements Message {

    private ApplicationUser user;

    public AbstractMessage(ApplicationUser user) {
        this.user = user;
    }

    @Override
    public abstract String getMessage();

    @Override
    public String getUserId() {
        return user.getId();
    }

    @Override
    public String getRoleId() {
        return user.getApplication().getId();
    }

    @Override
    public String getIp() {
        return user.getIp();
    }

    protected String getFullLogMessage(String innerMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"user\": \"").append(getUserId()).append("\", ");
        builder.append("\"role\": \"").append(getRoleId()).append("\", ");
        builder.append("\"message\": ").append(innerMessage).append("}");
        return builder.toString();
    }
}
