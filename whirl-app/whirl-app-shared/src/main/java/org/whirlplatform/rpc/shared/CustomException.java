package org.whirlplatform.rpc.shared;

import java.util.Map;

// TODO: Вынести из shared. На клиенте он не нужен
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 7254174895414961016L;

    private ExceptionData data;

    // Для сериализации
    public CustomException() {
        data = new ExceptionData();
    }

    public CustomException(String msg) {
        this(ExceptionData.ExceptionType.SIMPLE, msg, false);
    }

    public CustomException(String msg, boolean sessionExpired) {
        this(ExceptionData.ExceptionType.SIMPLE, msg, sessionExpired);
    }

    public CustomException(ExceptionData.ExceptionType type, String msg) {
        this(type, msg, false);
    }

    public CustomException(ExceptionData.ExceptionType type, String msg, boolean sessionExpired) {
        super(msg);
        data = new ExceptionData(type);
        data.setMessage(msg);
        data.setSessionExpired(sessionExpired);
    }

    public ExceptionData getData() {
        return data;
    }

    public CustomException setAllowedApps(Map<String, String> allowedApps) {
        data.setAllowedApps(allowedApps);
        return this;
    }

    public CustomException setPasswordChangeServiceUrl(String url) {
        data.setPasswordChangeServiceUrl(url);
        return this;
    }
}
