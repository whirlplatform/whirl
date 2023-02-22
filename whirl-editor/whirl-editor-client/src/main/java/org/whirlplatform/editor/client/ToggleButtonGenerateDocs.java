package org.whirlplatform.editor.client;

import com.google.gwt.user.client.ui.Widget;
import org.whirlplatform.editor.client.view.HelpDecorator;

import java.util.ArrayList;

public class ToggleButtonGenerateDocs {
    public static ArrayList<Widget> listOfComponents = new ArrayList<>();
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
