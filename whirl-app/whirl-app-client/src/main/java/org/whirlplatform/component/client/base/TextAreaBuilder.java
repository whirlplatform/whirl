package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.ext.XTextArea;
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
 * Текстовая область ввода
 */
@JsType(namespace = "Whirl", name = "TextArea")
public class TextAreaBuilder extends AbstractFieldBuilder implements
        NativeParameter<String>, Parameter<DataValue> {

    private XTextArea field;

    @JsConstructor
    public TextAreaBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public TextAreaBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип текстовой области
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.TextAreaType;
    }

    /**
     * Создание компонента - текстовая область
     *
     * @return Component
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        field = new XTextArea(100, 100);
        field.setSelectOnFocus(true);
        return field;
    }

    /**
     * Установка атрибута для текстовой области
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Required.getCode())) {
            if (value != null && value.getBoolean() != null) {
                field.setAllowBlank(!value.getBoolean());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.MaxLength.getCode())) {
            if (value != null && value.getInteger() != null) {
                field.addValidator(new MaxLengthValidator(value.getInteger()));
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.MinLength.getCode())) {
            if (value != null && value.getInteger() != null) {
                field.addValidator(new MinLengthValidator(value.getInteger()));
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            if (value != null) {
                field.setValue(value.getString());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    @Override
    protected boolean checkRequired(Object value) {
        return Util.isEmptyString((String) value);
    }

    /**
     * Получить значение текстовой области
     *
     * @return String
     */
    @Override
    public String getValue() {
        return field.getValue();
    }

    /**
     * Установка зачения текстовой области
     *
     * @param value String
     */
    @Override
    public void setValue(String value) {
        field.setValue(value);
    }

    public String getText() {
        return field.getText();
    }

    /**
     * Получение сущности текстовой области
     *
     * @return (C) field
     */
    @Override
    protected <C> C getRealComponent() {
        return (C) field;
    }

    /**
     * Получение записи из текстовой области
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
     * Присвоить запись текстовой области
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
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator)) {
            Field<?> field = getRealComponent();
            Element el = field.getElement().selectNode("textarea");
            if (el != null) {
                return el;
            }
        }
        return super.getElementByLocator(locator);
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

}