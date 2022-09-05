package org.whirlplatform.component.client.check;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Clearable;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.Validatable;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.Collections;
import java.util.Map;

/**
 * Check box field.
 */
@JsType(namespace = "Whirl", name = "CheckBox")
public class CheckBoxBuilder extends AbstractFieldBuilder
        implements Clearable, Validatable, ChangeEvent.HasChangeHandlers, NativeParameter<Boolean>, Parameter<DataValue> {

    private CheckBox checkBox;

    @JsConstructor
    public CheckBoxBuilder(@JsOptional  Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public CheckBoxBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип CheckBox
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.CheckBoxType;
    }

    /**
     * Создание компонента - CheckBox
     *
     * @return Component, CheckBox
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;
        CheckBoxCell cell = new CheckBoxCell() {
            @Override
            public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, Boolean value,
                                       NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
                if (isReadOnly() && !("blur".equals(event.getType()) || "focus".equals(event.getType()))) {
                    event.preventDefault();
                    event.stopPropagation();
                    return;
                }
                super.onBrowserEvent(context, parent, value, event, valueUpdater);
            }
        };
        checkBox = new CheckBox(cell);
        return checkBox;
    }

    /**
     * Установка атрибута для CheckBox
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.BoxLabel.getCode())) {
            if (value != null) {
                checkBox.setBoxLabel(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            if (value != null) {
                checkBox.setValue(value.getBoolean());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    /**
     * Gets field label.
     *
     * @return label text
     */
    public String getBoxLabel() {
        return checkBox.getBoxLabel().asString();
    }

    /**
     * Set label for the field.
     *
     * @param label text
     */
    public void setBoxLabel(String label) {
        checkBox.setBoxLabel(label);
    }

    /**
     * Get field's value.
     *
     * @return field's value
     */
    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    /**
     * Set field's value.
     *
     * @param value field's value
     */
    @Override
    public void setValue(Boolean value) {
        checkBox.setValue(value);
    }

    /**
     * Check if field is valid.
     *
     * @param invalidate true to invalidate field
     * @return true if field is valid
     */
    @JsIgnore
    @Override
    public boolean isValid(boolean invalidate) {
        if (!super.isValid(invalidate)) {
            return false;
        }
        if (isRequired() && !checkBox.getValue()) {
            if (invalidate) {
                markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        }
        clearInvalid();
        return true;
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    public void markInvalid(String msg) {
        checkBox.getErrorSupport().markInvalid(
                Collections.singletonList(new DefaultEditorError(checkBox, msg, checkBox.getValue())));
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        checkBox.getErrorSupport().clearInvalid();
    }

    /**
     * Checks if is in valid state.
     *
     * @return true, if is in valid state
     */
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Sets the read only.
     *
     * @param readOnly true, if the field is read only
     */
    public void setReadOnly(boolean readOnly) {
        Field<?> field = getRealComponent();
        field.setReadOnly(readOnly);
    }

    /**
     * Получение сущности CheckBox
     *
     * @return (C) checkBox
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <C> C getRealComponent() {
        return (C) checkBox;
    }

    /**
     * Получение записи из CheckBox
     *
     * @return DataValue(Type, Value, Code)
     */
    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.BOOLEAN);
        result.setCode(getCode());
        result.setValue(getValue());
        return result;
    }

    /**
     * Присвоить запись CheckBox
     *
     * @param value - DataValue(Type, Value, Code)
     */
    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (DataType.BOOLEAN.equals(value.getType())) {
            setValue(value.getBoolean());
        }
    }

    /**
     * Добавление обработчика установки значения в CheckBox
     *
     * @param handler - ChangeHandler
     * @return HandlerRegistration
     */
    @JsIgnore
    @Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
        return addHandler(handler, ChangeEvent.getType());
    }

    @Override
    protected Object emptyValue() {
        return false;
    }

    // Arquillian

    private static class LocatorParams {

        private static String TYPE_INPUT = "Input";

    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            result.setPart(new Locator(CheckBoxBuilder.LocatorParams.TYPE_INPUT));
            return result;
        }
        return null;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator) && locator.getPart() != null
                && LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
            return checkBox.getCell().getInputElement(getWrapper().getElement());
        }
        return null;
    }
}
