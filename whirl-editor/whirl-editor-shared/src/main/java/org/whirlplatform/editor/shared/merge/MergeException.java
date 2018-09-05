package org.whirlplatform.editor.shared.merge;

public class MergeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -26760343290367527L;

    public MergeException() {
        super();
    }

    public MergeException(String message) {
        super(message);
    }

    public MergeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MergeException(Throwable cause) {
        super(cause);
    }
}
