package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator.RegExMessages;
import java.util.Map;
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

public abstract class ValueBaseFieldBuilder extends AbstractFieldBuilder
        implements NativeParameter<String>, Parameter<DataValue> {

    protected ValueBaseField<String> field;

    private RegExValidator validator;

    private int minLength;
    private int maxLength;

    @JsIgnore
    public ValueBaseFieldBuilder() {
        super();
    }

    @JsIgnore
    public ValueBaseFieldBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        validator = new RegExValidator("");

        minLength = 0;
        maxLength = Integer.MAX_VALUE;
        return null;
    }

    /**
     * Установка атрибута для текстового поля
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    @JsIgnore
    public boolean setProperty(String name, final DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.RegEx.getCode())) {
            if (value != null && !Util.isEmptyString(value.getString())) {
                validator.setRegex(value.getString());
                field.addValidator(validator);
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.RegExMessage.getCode())) {
            if (value != null && value.getString() != null) {
                validator.setMessages(new RegExMessages() {
                    @Override
                    public String regExMessage() {
                        return value.getString();
                    }
                });
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Required.getCode())) {
            if (value != null && value.getBoolean() != null) {
                field.setAllowBlank(!value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            String v = null;
            if (value != null && !Util.isEmptyString(value.getString())) {
                v = value.getString();
            }
            field.setOriginalValue(v);
            field.setValue(v, true);
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MinLength.getCode())) {
            if (value != null && value.getInteger() != null) {
                field.addValidator(new MinLengthValidator(value.getInteger()));
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MaxLength.getCode())) {
            if (value != null && value.getInteger() != null) {
                field.addValidator(new MaxLengthValidator(value.getInteger()));
            }
            return true;
        }
        return super.setProperty(name, value);
    }

    @Override
    protected boolean checkRequired(Object value) {
        // TODO
        return Util.isEmptyString((String) value);
    }

    /**
     * Получить значение текстового поля
     *
     * @return String
     */
    @Override
    public String getValue() {
        return field.getValue();
    }

    /**
     * Установка зачения текстового поля
     *
     * @param value String
     */
    @Override
    public void setValue(String value) {
        field.setValue(value, true);
    }

    @JsIgnore
    public String getText() {
        return field.getText();
    }

    /**
     * Returns the field's required minimum length.
     *
     * @return field's required minimum length
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Returns the field's maximum length.
     *
     * @return field's maximum length
     */
    public int getMaxLength() {
        return maxLength;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (!super.fitsLocator(locator)) {
            return null;
        }
        Locator part = locator.getPart();
        if (part != null) {
            if (LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
                return field.getCell().getInputElement(getWrapper().getElement());
            }
        }
        return super.getElementByLocator(locator);
    }

    /**
     * Получение сущности текстового поля
     *
     * @return (C) field
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <C> C getRealComponent() {
        return (C) field;
    }

    /**
     * Получение записи из текстового поля
     *
     * @return DataValue(Type, Value, Code)
     */
    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.STRING);
        result.setCode(getCode());
        result.setValue(getValue());
        return result;
    }

    /**
     * Присвоить запись текстовому полю
     *
     * @param value - DataValue(Type, Value, Code)
     */
    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (DataType.STRING.equals(value.getType())) {
            setValue(value.getString());
        }
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            if (result.getPart() == null) {
                result.setPart(new Locator(LocatorParams.TYPE_INPUT));
            }
        }
        return result;
    }

    private static class LocatorParams {
        private static String TYPE_INPUT = "Input";
    }

}
