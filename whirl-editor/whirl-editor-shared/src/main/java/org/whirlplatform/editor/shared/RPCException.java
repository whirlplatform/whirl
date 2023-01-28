package org.whirlplatform.editor.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 * Исключение при ошибке в RPC-методе, возвращаемое на клиента
 */
public class RPCException extends Exception implements Serializable, IsSerializable {

    public static final String errType = "err";
    public static final String infoType = "info";
    /**
     *
     */
    private static final long serialVersionUID = 2508549663359435651L;
    /**
     * Тип
     */
    private String type;
    private boolean isSessionExpired;

    public RPCException() {
        super();
    }

    public RPCException(String msg) {
        super(getMsg(msg));
        getErrorType(msg);
    }

    public RPCException(String msg, boolean sessionExpired) {
        this(msg);
        this.isSessionExpired = sessionExpired;
    }

    public static String getMsg(String msg) {
        if (msg == null) {
            return "";
        }
        String message = new String(msg);
        if (message != null && message.contains("ORA")) {
            int start = message.indexOf("ORA-", 0);
            int stop = message.indexOf("ORA-", start + 1);
            if (start != -1 & stop != -1) {
                message = message.substring(start + 10, stop);
            }
        }
        return message;
    }

    private void getErrorType(String msg) {
        if (msg != null && msg.contains("ORA-209")) {
            type = infoType;
        } else {
            type = errType;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSessionExpired() {
        return isSessionExpired;
    }

    public void setSessionExpired(boolean isSessionExpired) {
        this.isSessionExpired = isSessionExpired;
    }

}
