package org.whirlplatform.editor.shared.merge;

public class DiffException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -26760343290367527L;

    public DiffException() {
        super();
    }

    public DiffException(String message) {
        super(message);
    }

    public DiffException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiffException(Throwable cause) {
        super(cause);
    }
}
