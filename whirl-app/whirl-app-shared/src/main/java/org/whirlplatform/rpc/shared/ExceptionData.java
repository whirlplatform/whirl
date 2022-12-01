package org.whirlplatform.rpc.shared;

import java.util.Collections;
import java.util.Map;

public class ExceptionData {

    private ExceptionType type = ExceptionType.SIMPLE;
    private String message;
    private boolean sessionExpired;
    private ExceptionLevel level;
    // PASSWORDCHANGE
    private String passwordChangeServiceUrl;
    // WRONGAPPS
    private Map<String, String> allowedApps;

    public ExceptionData() {
    }

    public ExceptionData(ExceptionType type) {
        this.type = type;
    }

    public ExceptionType getType() {
        return type;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSessionExpired() {
        return sessionExpired;
    }

    public void setSessionExpired(boolean sessionExpired) {
        this.sessionExpired = sessionExpired;
    }

    public ExceptionLevel getLevel() {
        return level;
    }

    public void setLevel(ExceptionLevel level) {
        this.level = level;
    }

    public String getPasswordChangeServiceUrl() {
        return passwordChangeServiceUrl;
    }

    public void setPasswordChangeServiceUrl(String passwordChangeServiceUrl) {
        this.passwordChangeServiceUrl = passwordChangeServiceUrl;
    }

    public Map<String, String> getAllowedApps() {
        return allowedApps == null ? Collections.emptyMap() : allowedApps;
    }

    public void setAllowedApps(Map<String, String> allowedApps) {
        this.allowedApps = allowedApps;
    }

    public enum ExceptionType {SIMPLE, PASSWORDCHANGE, WRONGAPP}

    public enum ExceptionLevel {ERROR, INFO}
}
