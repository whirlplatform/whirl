package org.whirlplatform.rpc.shared;

@SuppressWarnings("serial")
public class ClientRestException extends Exception {

    private boolean custom = true;
    private ExceptionData data;

    public ClientRestException(Throwable e, ExceptionData data) {
        super(e);
        this.data = data;
        if (data == null) {
            custom = false;
        }
    }

    /**
     * Если true, исключение наше. Иначе исключение выброшено фреймфорком либо ошибкой подключения,
     * и ExceptionData == null
     */
    public boolean isCustom() {
        return custom;
    }

    public ExceptionData getData() {
        return data;
    }

    /**
     * Если исключение наше, вернет текст из ExceptionData. Иначе текст ошибки сервера (код ответа и
     * краткое описание)
     */
    @Override
    public String getMessage() {
        if (custom) {
            return data.getMessage();
        } else {
            return getCause().getMessage();
        }
    }
}
