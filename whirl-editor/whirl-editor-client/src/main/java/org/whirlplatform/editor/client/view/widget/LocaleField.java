package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.validator.ValidatorUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.LocaleElement;

/**
 * Locale input control
 */
public class LocaleField extends HBoxLayoutContainer {
    private final String languageLabel = EditorMessage.Util.MESSAGE.language();
    private final String countryLabel = EditorMessage.Util.MESSAGE.country();
    private final String languageHint = "xx";
    private final String countryHint = "XX";
    private final String languageDefault = "ru";
    private final int fieldWidth = 60;

    private final TextField language;
    private final TextField country;

    public LocaleField() {
        super();
        language = createLanguageField();
        country = createCountryField();
        this.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        this.add(new Label(languageLabel), WidgetUtil.noStretchLayout(0, 5, 0, 0));
        this.add(language, WidgetUtil.noStretchLayout(0, 5, 0, 0));
        this.add(new Label(countryLabel), WidgetUtil.noStretchLayout(0, 5, 0, 0));
        this.add(country, WidgetUtil.noStretchLayout(0, 5, 0, 0));
        this.add(new Label(), WidgetUtil.flexLayout());
    }

    public String getLanguage() {
        return language.getValue();
    }

    public void setLanguage(final String languageCode) {
        setTextFieldValue(language, languageCode.toLowerCase());
    }

    public String getCountry() {
        return country.getValue();
    }

    public void setCountry(final String countryCode) {
        setTextFieldValue(country, countryCode.toUpperCase());
    }

    public boolean isCountryValid() {
        return country.validate();
    }

    public boolean isLanguageValid() {
        return language.validate();
    }

    public boolean isValid() {
        return isCountryValid() && isLanguageValid();
    }

    public LocaleElement getValue() {
        return new LocaleElement(getLanguage(), getCountry());
    }

    public void setValue(final LocaleElement locale) {
        setLanguage((locale.getLanguage() != null) ? locale.getLanguage() : "");
        setCountry((locale.getCountry() != null) ? locale.getCountry() : "");
    }

    private void setTextFieldValue(final TextField field, final String value) {
        if (Util.isEmptyString(value)) {
            field.reset();
        } else {
            country.setValue(value);
            field.validate();
        }
    }

    private TextField createLanguageField() {
        final TextField result = createTextField(languageHint);
        result.setValue(languageDefault);
        result.addValidator(ValidatorUtil.createEmptyStringValidator());
        result.addValidator(ValidatorUtil.createLanguageValidator());
        return result;
    }

    private TextField createCountryField() {
        final TextField result = createTextField(countryHint);
        result.addValidator(ValidatorUtil.createCountryValidator());
        return result;
    }

    private TextField createTextField(final String hint) {
        final TextField result = new TextField();
        result.setEmptyText(hint);
        result.setWidth(fieldWidth);
        return result;
    }
}
