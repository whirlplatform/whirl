package org.whirlplatform.component.client;

import org.whirlplatform.component.client.state.StateScope;

public interface HasState {

	void setSaveState(boolean save);

	boolean isSaveState();

	void setStateScope(StateScope scope);

	StateScope getStateScope();

	void saveState();

}
