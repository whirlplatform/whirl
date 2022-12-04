package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

/**
 * The combination of the checkbox and textfield.
 */
public class MessageField extends HBoxLayoutContainer {
    private final static String LABEL = EditorMessage.Util.MESSAGE.message();

    private final TextField value;
    private final CheckBox checkBox;
    private final Label label;

    public MessageField() {
        this(LABEL);
    }

    public MessageField(final String labelText) {
        super();
        label = new Label(labelText);
        value = new TextField();
        checkBox = new CheckBox();
        checkBox.setBounds(14, 14, 14, 14);
        checkBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                value.setEnabled(isChecked());
            }
        });
        checkBox.setValue(false);
        value.setEnabled(false);
        this.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        this.add(checkBox, WidgetUtil.noStretchLayout(2, 0, 0, 0));
        this.add(label, WidgetUtil.noStretchLayout(0, 5, 0, 15));
        this.add(value, WidgetUtil.flexLayout());
    }

    public String getValue() {
        return (isChecked()) ? value.getValue() : "";
    }

    public void setValue(final String value) {
        this.value.setValue(value);
    }

    public boolean isChecked() {
        return checkBox.getValue();
    }
}
