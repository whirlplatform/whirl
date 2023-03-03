package org.whirlplatform.editor.client;

import com.google.gwt.user.client.ui.Widget;
import org.whirlplatform.editor.client.view.HelpDecorator;

public class ToggleButtonGenerateDocs {

    public static void disableEnableTips(boolean isDepressed, String type) {
        if(isDepressed) {
            HelpDecorator.enableTip(type);
        } else {
            HelpDecorator.disableTip(type);
        }
    }
}
