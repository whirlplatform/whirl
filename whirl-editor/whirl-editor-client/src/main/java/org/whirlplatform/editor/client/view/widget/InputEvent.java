package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.event.dom.client.DomEvent;

/**
 * Instantly handles input events such as paste and keyboard input
 * <p>
 * http://stackoverflow.com/questions/3184648/instant-value-change-handler-on-a-gwt-textbox
 */
public class InputEvent extends DomEvent<InputHandler> {
    private static final Type<InputHandler> TYPE =
        new Type<InputHandler>("input", new InputEvent());

    protected InputEvent() {
    }

    public static Type<InputHandler> getType() {
        return TYPE;
    }

    @Override
    public final Type<InputHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InputHandler handler) {
        handler.onInput(this);
    }
}
