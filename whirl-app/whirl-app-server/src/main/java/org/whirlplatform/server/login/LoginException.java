package org.whirlplatform.server.login;

public class LoginException extends Exception {

    private static final long serialVersionUID = -7402284449564127988L;

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

}
