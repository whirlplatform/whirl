package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handles input events such as paste and keyboard input
 *  
 * http://stackoverflow.com/questions/3184648/instant-value-change-handler-on-a-gwt-textbox
 *
 */
public interface InputHandler extends EventHandler {

	void onInput(InputEvent event);
}
