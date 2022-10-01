package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

import java.util.Collections;
import java.util.Map;

/**
 *
 */
@JsType(namespace = "Whirl", name = "SimpleHtmlEditor")
public class SimpleHtmlEditorBuilder extends AbstractFieldBuilder
        implements NativeParameter<String>, Parameter<DataValue> {

    protected HtmlEditor field;

    @JsConstructor
    public SimpleHtmlEditorBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }
    @JsIgnore
    public SimpleHtmlEditorBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.SimpleHtmlEditorType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        // Переопределил методы, т.к. в production режиме возникала ошибка
        field = new HtmlEditor() {

            @Override
            protected void onEnable() {
                if (getElement().selectNode("textarea") != null) {
                    super.onEnable();
                } else {
                    if (disabledStyle != null) {
                        removeStyleName(disabledStyle);
                    }
                }
            }

            @Override
            protected void onDisable() {
                if (getElement().selectNode("textarea") != null) {
                    super.onDisable();
                } else {
                    if (disabledStyle != null) {
                        addStyleName(disabledStyle);
                    }
                }
            }
        };
        return field;
    }

    @JsIgnore
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            if (value != null && value.getString() != null) {
                field.setValue(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Enabled.getCode())) {
            if (value != null && value.getBoolean() != null) {
                field.setEnabled(value.getBoolean());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    @Override
    public String getValue() {
        return field.getValue();
    }

    @Override
    public void setValue(String value) {
        field.setValue(value);
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) field;
    }

    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.STRING);
        result.setCode(getCode());
        result.setValue(getValue());
        return result;
    }

    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (value.isTypeOf(DataType.STRING)) {
            setValue(value.getString());
        }
    }

    /**
     * Gets the field mask.
     *
     * @return the field mask
     */
    @Override
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Sets the field mask.
     *
     * @param mask the new field mask
     */
    @Override
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Clears the field value.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Checks if is in valid state.
     *
     * @return true, if is in valid state
     */
    @JsIgnore
    @Override
    public boolean isValid() {
        return super.isValid();
    }

    /**
     * Check if field is valid.
     *
     * @param invalidate true to invalidate field
     * @return true if field is valid
     */
    @Override
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Sets the required to fill.
     *
     * @param required true, if the field is required to be filled
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Sets the read only.
     *
     * @param readOnly true, if the field is read only
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        return super.getElementByLocator(locator);
    }
}
