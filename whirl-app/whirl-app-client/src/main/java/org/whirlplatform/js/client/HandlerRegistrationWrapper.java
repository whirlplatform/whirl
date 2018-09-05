package org.whirlplatform.js.client;

import com.google.gwt.event.shared.HandlerRegistration;

public class HandlerRegistrationWrapper {

    private HandlerRegistration handlerRegistration;

    protected HandlerRegistrationWrapper() {
    }

    public HandlerRegistrationWrapper(HandlerRegistration handlerRegistration) {
        this.handlerRegistration = handlerRegistration;
    }

    public void removeHandler() {
        handlerRegistration.removeHandler();
    }

}
