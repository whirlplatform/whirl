package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.validator.ValidatorUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.version.VersionUtil;

/**
 * Vsersion/branch input control
 *
 * @author bedritckiy_mr
 */
public class VersionField extends HBoxLayoutContainer {
    private final String BRANCH_LABEL = EditorMessage.Util.MESSAGE.branch();
    private final String VERSION_LABEL = EditorMessage.Util.MESSAGE.version();
    private final String BRANCH_HINT = "";
    private final String VERSION_HINT = "x.y.z";

    protected final TextField fieldValue;
    protected final Radio radioBranch;
    protected final Radio radioVersion;

    public VersionField() {
        super();
        radioVersion = WidgetUtil.createRadio(VERSION_LABEL, true);
        radioBranch = WidgetUtil.createRadio(BRANCH_LABEL, false);
        fieldValue = createValueField(radioBranch);
        defineToggleGroup(radioVersion, radioBranch, fieldValue);
        this.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        this.add(radioVersion, WidgetUtil.noStretchLayout(2, 0, 0, 0));
        this.add(radioBranch, WidgetUtil.noStretchLayout(2, 0, 0, 0));
        this.add(fieldValue, WidgetUtil.flexLayout());
    }

    public String getStringValue() {
        return fieldValue.getValue();
    }

    public void setValue(final Version version) {
        if (version != null) {
            radioBranch.setValue(version.isBranch());
            radioVersion.setValue(!version.isBranch());
            fieldValue.setValue(version.toString());
            fieldValue.validate();
        }
    }

    public void setValue(final String value) {
        if (value != null) {
            radioVersion.setValue(Version.isValidVersion(value));
            radioBranch.setValue(!radioVersion.getValue());
            fieldValue.setValue(value);
            fieldValue.validate();
        }
    }

    public Version getValue() {
        return VersionUtil.createVersion(getStringValue());
    }

    public boolean isValid() {
        return fieldValue.validate();
    }

    public boolean isBranch() {
        return radioBranch.getValue();
    }

    public boolean isVersion() {
        return radioVersion.getValue();
    }

    public boolean isEmpty() {
        return Util.isEmptyString(fieldValue.getValue());
    }

    private TextField createValueField(HasValue<Boolean> validationDependency) {
        TextField result = new TextField();
        result.setAllowBlank(false);
        result.addValidator(ValidatorUtil.createVersionValidator(validationDependency));
        result.setEmptyText(VERSION_HINT);
        return result;
    }

    private void defineToggleGroup(Radio left, Radio right, final TextField fieldForValidation) {
        ToggleGroup result = new ToggleGroup();
        result.add(left);
        result.add(right);
        result.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
            @Override
            public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
                if (Util.isEmptyString(fieldForValidation.getValue())) {
                    fieldForValidation.setEmptyText(getEmptyHint());
                }
                fieldForValidation.validate();
            }
        });
    }

    private String getEmptyHint() {
        return (isVersion()) ? VERSION_HINT : BRANCH_HINT;
    }
}
