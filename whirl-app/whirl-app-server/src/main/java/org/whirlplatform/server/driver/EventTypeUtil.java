package org.whirlplatform.server.driver;

import org.whirlplatform.meta.shared.editor.EventElement;

public class EventTypeUtil {

    public static boolean isClick(final EventElement event) {
        return "ClickHandler".equalsIgnoreCase(event.getHandlerType());
    }
}
