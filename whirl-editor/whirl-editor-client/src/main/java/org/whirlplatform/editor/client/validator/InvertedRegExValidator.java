package org.whirlplatform.editor.client.validator;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.regexp.shared.RegExp;
import java.util.List;

/**
 * Validate the test string if it does NOT contain the search pattern
 */
public class InvertedRegExValidator extends AbstractStringValidator {
    final String forbiddenExpression;
    final String message;

    public InvertedRegExValidator(String forbiddenExpression, String message) {
        super();
        this.forbiddenExpression = forbiddenExpression;
        this.message = message;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        RegExp regExp = RegExp.compile(forbiddenExpression);
        boolean notFoundInValue = !regExp.test(value);
        return (notFoundInValue) ? null : createErrorList(message);
    }
}
