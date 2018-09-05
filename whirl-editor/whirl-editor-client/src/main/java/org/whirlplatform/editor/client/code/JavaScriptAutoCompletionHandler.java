package org.whirlplatform.editor.client.code;

import org.geomajas.codemirror.client.*;

import java.util.ArrayList;
import java.util.List;

public class JavaScriptAutoCompletionHandler implements AutoCompletionHandler {

    @Override
    public void getCompletions(String text, EditorPosition caretPosition,
                               int caretIndex, AutoCompletionCallback callback) {
        if (text == null || text.isEmpty()) {
            callback.completionsReady(AutoCompletionResult.emptyResult());
        }
        List<AutoCompletionChoice> result = new ArrayList<AutoCompletionChoice>();
//		String before = text.substring(0, caretIndex);
        boolean finded = false;
        if (!finded) {
            AutoCompletionChoice choice = new AutoCompletionChoice("Whirl",
                    "Whirl", "", caretPosition, caretPosition);
            result.add(choice);
            choice = new AutoCompletionChoice("Whirl.Helper", "Whirl.Helper",
                    "", caretPosition, caretPosition);
            result.add(choice);
            choice = new AutoCompletionChoice("Whirl.Events", "Whirl.Events",
                    "", caretPosition, caretPosition);
            result.add(choice);
        }
        callback.completionsReady(new AutoCompletionResult(result,
                caretPosition));
    }

}
