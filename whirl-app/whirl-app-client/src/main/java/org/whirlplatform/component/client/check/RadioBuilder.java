package org.whirlplatform.component.client.check;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.cell.core.client.form.RadioCell;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Parameter;
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
 * Радиокнопка
 */
public class RadioBuilder extends AbstractFieldBuilder implements NativeParameter<Boolean>, Parameter<DataValue> {

    private Radio radio;

    public RadioBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public RadioBuilder() {
        super();
    }

    /**
     * Получить тип радиокнопки
     */
    @Override
    public ComponentType getType() {
        return ComponentType.RadioType;
    }

    /**
     * Создание компонента - радиокнопка
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        RadioCell cell = new RadioCell() {
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
        radio = new Radio(cell);
        // надо выставить начение для поля с которого сняли галочку посредством
        // группы
        radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                String group = getGroupName();
                if (!Util.isEmptyString(group) && event.getValue()) {
                    for (ComponentBuilder b : BuilderManager.getAllBuilders()) {
                        if (b != RadioBuilder.this && b instanceof RadioBuilder) {
                            RadioBuilder r = (RadioBuilder) b;
                            if (group.equals(r.getGroupName()) && r.getValue()) {
                                r.setValue(false);
                            }
                        }
                    }
                }
            }
        });
        return radio;
    }

    /**
     * Установка атрибута для радиокнопки
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.BoxLabel.getCode())) {
            setBoxLabel(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
            setValue(value.getBoolean());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.GroupName.getCode())
                && !Util.isEmptyString(value.getString())) {
            setGroupName(value.getString());
            return true;
        }
        return super.setProperty(name, value);
    }

    /**
     * Установка подписи радиокнопки
     *
     * @param label - String
     */
    public void setBoxLabel(String label) {
        radio.setBoxLabel(label);
    }

    /**
     * Получение подписи радиокнопки
     *
     * @return String
     */
    public String getBoxLabel() {
        return radio.getBoxLabel().asString();
    }

    /**
     * Установка названия радиогруппы
     *
     * @param groupName - String
     */
    public void setGroupName(String groupName) {
        radio.setName(groupName);
    }

    /**
     * Получение названия радиогруппы
     *
     * @return String
     */
    public String getGroupName() {
        return radio.getName();
    }

    /**
     * Получение значения радиокнопки
     *
     * @return boolean
     */
    @Override
    public Boolean getValue() {
        return radio.getValue();
    }

    /**
     * Установка значения радиокнопки
     *
     * @param value - boolean
     */
    @Override
    public void setValue(Boolean value) {
        radio.setValue(value);
    }

    /**
     * Получение сущности радиокнопки
     *
     * @return (C) radio
     */
    @Override
    protected <C> C getRealComponent() {
        return (C) radio;
    }

    /**
     * Получение записи с радиокнопкой
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
     * Присвоить запись радиокнопке
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
     * Проверка на валидность CheckBox
     *
     * @param invalidate - boolean
     * @return boolean
     */
    @Override
    public boolean isValid(boolean invalidate) {
        if (radio.getName() != null && !radio.getName().isEmpty()) {
            return true;
        }
        if (!super.isValid(invalidate)) {
            return false;
        }
        if (isRequired() && !radio.getValue()) {
            if (invalidate) {
                markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        }
        clearInvalid();
        return true;
    }

    public void markInvalid(String msg) {
        radio.getErrorSupport().markInvalid(
                Collections.singletonList(new DefaultEditorError(radio, msg, radio.getValue())));
    }

    @Override
    public void clearInvalid() {
        radio.getErrorSupport().clearInvalid();
    }

    @Override
    protected Object emptyValue() {
        return false;
    }

    private static class LocatorParams {
        private static String TYPE_INPUT = "Input";
    }

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            result.setPart(new Locator(LocatorParams.TYPE_INPUT));
            return result;
        }
        return null;
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator) && locator.getPart() != null
                && LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
            return radio.getCell().getInputElement(getWrapper().getElement());
        }
        return null;
    }
}
