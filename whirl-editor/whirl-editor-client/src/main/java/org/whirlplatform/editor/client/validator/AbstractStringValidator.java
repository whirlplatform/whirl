
package org.whirlplatform.editor.client.validator;

import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import org.whirlplatform.component.client.utils.SimpleEditorError;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public abstract class AbstractStringValidator implements Validator<String> {

    AbstractStringValidator() {
    }

    public List<EditorError> createErrorList(String message) {
        return Collections.singletonList(new SimpleEditorError(message));
    }
}
