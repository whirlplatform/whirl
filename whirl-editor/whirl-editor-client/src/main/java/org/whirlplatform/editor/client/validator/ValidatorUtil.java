
package org.whirlplatform.editor.client.validator;

import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 *
 */
public class ValidatorUtil {
    public static final String CODE_REGEX = "[^A-Za-z0-9_-]";
    public static final String URL_REGEX = "[^\\/\\:\\.A-Za-z0-9_-]";
    public static final String COMMENT_REGEX = "^\\#SP";
    public static final String LANGUAGE_REGEX = "^[a-z][a-z]$";
    public static final String COUNTRY_REGEX = "^[A-Z][A-Z]$";

    public static Validator<String> createEmptyStringValidator() {
        return new EmptyValidator<String>();
    }

    public static Validator<String> createCodeValidator() {
        return new InvertedRegExValidator(CODE_REGEX,
                EditorMessage.Util.MESSAGE.error_name_format());
    }

    public static Validator<String> createCountryValidator() {
        return new RegExValidator(COUNTRY_REGEX, EditorMessage.Util.MESSAGE.error_country_format());
    }

    public static Validator<String> createLanguageValidator() {
        return new RegExValidator(LANGUAGE_REGEX,
                EditorMessage.Util.MESSAGE.error_language_format());
    }

    public static Validator<String> createVersionValidator(HasValue<Boolean> branch) {
        return new VersionFieldValidator(branch);
    }

}
