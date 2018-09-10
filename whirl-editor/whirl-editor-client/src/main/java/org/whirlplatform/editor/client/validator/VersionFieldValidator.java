
package org.whirlplatform.editor.client.validator;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.HasValue;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.Version;

import java.util.List;

/**
 *
 */
public class VersionFieldValidator extends AbstractStringValidator {
    private final HasValue<Boolean> isBranch;

    public VersionFieldValidator(HasValue<Boolean> isBranch) {
        this.isBranch = isBranch;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        String val = value;
        if (isBranch.getValue() && !isValueValidAsBranch(val)) {
            return createErrorList(EditorMessage.Util.MESSAGE.error_branch_format());
        }
        if (!isBranch.getValue() && !isValueValidAsVersion(val)) {
            return createErrorList(EditorMessage.Util.MESSAGE.error_version_format());
        }
        return null;
    }

    private boolean isValueValidAsVersion(String value) {
        return Version.isValidVersion(value);
    }

    private boolean isValueValidAsBranch(String value) {
        return !RegExp.compile(ValidatorUtil.CODE_REGEX).test(value);
    }
}