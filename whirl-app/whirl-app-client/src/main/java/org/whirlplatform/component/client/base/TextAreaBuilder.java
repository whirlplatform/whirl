package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
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

import java.util.Map;

/**
 * Текстовая область ввода
 */
public class TextAreaBuilder extends AbstractFieldBuilder implements
        NativeParameter<String>, Parameter<DataValue> {

    private XTextArea field;

    public TextAreaBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public TextAreaBuilder() {
        super();
    }

    /**
     * Получить тип текстовой области
     */
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
    @Override
    public void setFieldValue(DataValue value) {
        if (DataType.STRING.equals(value.getType())) {
            setValue(value.getString());
        }
    }


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

}