package org.whirlplatform.rpc.shared;

import java.io.Serializable;
import java.util.Map;

public class WrongApplicationException extends Exception implements Serializable {

    private static final long serialVersionUID = 920478310850690648L;
    private Map<String, String> allowedApps;
    private String userId;
    private String forbiddenMessage;

    public WrongApplicationException() {
    }

    public WrongApplicationException(Map<String, String> allowedApps, String userId,
                                     String messageForUser) {
        super(messageForUser);
        this.allowedApps = allowedApps;
        this.userId = userId;
        this.forbiddenMessage = messageForUser;
    }

    public Map<String, String> getAllowedApps() {
        return allowedApps;
    }

    public void setAllowedApps(Map<String, String> allowedApps) {
        this.allowedApps = allowedApps;
    }

    public String getForbiddenMessage() {
        return forbiddenMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
