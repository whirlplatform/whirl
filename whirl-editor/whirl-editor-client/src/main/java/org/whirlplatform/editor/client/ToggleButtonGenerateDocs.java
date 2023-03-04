package org.whirlplatform.editor.client;

import org.whirlplatform.editor.client.view.HelpDecorator;

public class ToggleButtonGenerateDocs {

    public static void disableEnableTips(boolean isDepressed) {
        if(isDepressed) {
            HelpDecorator.enableTips();
        } else {
            HelpDecorator.disableTips();
        }
    }
}
