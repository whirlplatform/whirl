package org.whirlplatform.rpc.shared;

import com.google.gwt.core.shared.GWT;
import org.fusesource.restygwt.client.JsonEncoderDecoder;

public interface ExceptionSerializer extends JsonEncoderDecoder<ExceptionData> {

    class Util {
        private static ExceptionSerializer instance;

        public static ExceptionSerializer get() {
            if (instance == null) {
                instance = GWT.create(ExceptionSerializer.class);
            }
            return instance;
        }
    }
}
