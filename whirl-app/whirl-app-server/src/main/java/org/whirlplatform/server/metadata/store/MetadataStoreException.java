package org.whirlplatform.server.metadata.store;

public class MetadataStoreException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 2641131196453917718L;

    public MetadataStoreException() {
        super();
    }

    public MetadataStoreException(String message) {
        super(message);
    }

    public MetadataStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetadataStoreException(Throwable cause) {
        super(cause);
    }
}
