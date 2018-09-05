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
 * Чек-бокс
 */
public class CheckBoxBuilder extends AbstractFieldBuilder
        implements Clearable, Validatable, ChangeEvent.HasChangeHandlers, NativeParameter<Boolean>, Parameter<DataValue> {

    private CheckBox checkBox;

    public CheckBoxBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public CheckBoxBuilder() {
        super();
    }

    /**
     * Получить тип CheckBox
     */
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
     * Установка подписи CheckBox
     *
     * @param label - String
     */
    public void setBoxLabel(String label) {
        checkBox.setBoxLabel(label);
    }

    /**
     * Получение подписи CheckBox
     *
     * @return String
     */
    public String getBoxLabel() {
        return checkBox.getBoxLabel().asString();
    }

    /**
     * Получение значения CheckBox
     *
     * @return boolean
     */
    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    /**
     * Установка значения CheckBox
     *
     * @param value - boolean
     */
    @Override
    public void setValue(Boolean value) {
        checkBox.setValue(value);
    }

    /**
     * Проверка на валидность CheckBox
     *
     * @param invalidate - boolean
     * @return boolean
     */
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

    public void markInvalid(String msg) {
        checkBox.getErrorSupport().markInvalid(
                Collections.singletonList(new DefaultEditorError(checkBox, msg, checkBox.getValue())));
    }

    @Override
    public void clearInvalid() {
        checkBox.getErrorSupport().clearInvalid();
    }

    /**
     * Проверка на валидность CheckBox
     *
     * @return boolean
     */
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Установка параметра "Только для чтения" для CheckBox
     *
     * @param readOnly - boolean
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

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            result.setPart(new Locator(CheckBoxBuilder.LocatorParams.TYPE_INPUT));
            return result;
        }
        return null;
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator) && locator.getPart() != null
                && LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
            return checkBox.getCell().getInputElement(getWrapper().getElement());
        }
        return null;
    }
}
