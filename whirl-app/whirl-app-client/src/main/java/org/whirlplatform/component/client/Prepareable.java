package org.whirlplatform.component.client;

import com.google.gwt.user.client.Command;

public interface Prepareable {

    void prepair(Command command);

    boolean isReady();

}
