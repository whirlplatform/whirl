package org.whirlplatform.component.client.utils;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;

public class SimpleEditorError implements EditorError {

    private final String message;

    public SimpleEditorError(String message) {
        this.message = message;
    }

    @Override
    public String getAbsolutePath() {
        return null;
    }

    @Override
    public Editor<?> getEditor() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Object getUserData() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isConsumed() {
        return false;
    }

    @Override
    public void setConsumed(boolean consumed) {
    }

}
