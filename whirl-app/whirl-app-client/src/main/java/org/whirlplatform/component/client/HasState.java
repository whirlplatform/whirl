package org.whirlplatform.component.client;

import org.whirlplatform.component.client.state.StateScope;

public interface HasState {

    boolean isSaveState();

    void setSaveState(boolean save);

    StateScope getStateScope();

    void setStateScope(StateScope scope);

    void saveState();

}
