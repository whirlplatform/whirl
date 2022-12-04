package org.whirlplatform.rpc.shared;

import java.io.Serializable;

/**
 * Сессионный токен. Обеспечивает идентификацию разделяемых сессий по вкладкам браузера внутри одной
 * веб-сессии.
 */
public class SessionToken implements Serializable {

    private static final long serialVersionUID = 291910565207331L;

    private transient static SessionToken _instance;

    private String sessionId;

    private String tokenId;

    public SessionToken() {

    }

    public SessionToken(String sessionId, String tokenId) {
        this.sessionId = sessionId;
        this.tokenId = tokenId;
    }

    /**
     * Метод должен использоваться только на клиентской стороне.
     *
     * @return {@link SessionToken}
     */
    public static SessionToken get() {
        if (_instance == null) {
            String token = null;
            _instance = new SessionToken("session", token);
        }
        return _instance;
    }

    /**
     * Метод должен использоваться только на клиентской стороне.
     */
    public static void set(SessionToken token) {
        _instance = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sessionId == null) ? 0 : sessionId.hashCode());
        result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SessionToken other = (SessionToken) obj;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        if (tokenId == null) {
            return other.tokenId == null;
        } else return tokenId.equals(other.tokenId);
    }

    @Override
    public String toString() {
        return "[SESSION: " + sessionId + " TOKEN: " + tokenId + "]";
    }

}
