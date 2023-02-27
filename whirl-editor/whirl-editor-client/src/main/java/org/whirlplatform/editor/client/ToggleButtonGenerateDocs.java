package org.whirlplatform.editor.client;

import org.whirlplatform.editor.client.view.HelpDecorator;

public class ToggleButtonGenerateDocs {
    private static boolean isDepressed = false;
    private static int amountOfPresses = 0;

    public static void disableEnableTips() {
        amountOfPresses++;
        if(amountOfPresses % 2 == 1) {
            isDepressed = true;
        } else if(amountOfPresses % 2 == 0) {
            isDepressed = false;
        }

        if(isDepressed) {
            HelpDecorator.enableTips();
        } else {
            HelpDecorator.disableTips();
        }
    }
}
