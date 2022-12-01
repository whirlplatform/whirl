
package org.whirlplatform.editor.client.validator;

import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import java.util.Collections;
import java.util.List;
import org.whirlplatform.component.client.utils.SimpleEditorError;

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
