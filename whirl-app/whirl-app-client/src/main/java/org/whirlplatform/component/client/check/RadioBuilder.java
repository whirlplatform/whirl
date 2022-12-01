package org.whirlplatform.component.client.check;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.cell.core.client.form.RadioCell;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
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

/**
 * Радиокнопка
 */
@JsType(name = "Radio", namespace = "Whirl")
public class RadioBuilder extends AbstractFieldBuilder
        implements NativeParameter<Boolean>, Parameter<DataValue> {

    private Radio radio;

    @JsConstructor
    public RadioBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public RadioBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип радиокнопки
     */
    @JsIgnore
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
            public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
                                       Element parent, Boolean value,
                                       NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
                if (isReadOnly() &&
                        !("blur".equals(event.getType()) || "focus".equals(event.getType()))) {
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
    @JsIgnore
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
     * Получение подписи радиокнопки
     *
     * @return String
     */
    public String getBoxLabel() {
        return radio.getBoxLabel().asString();
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
     * Получение названия радиогруппы
     *
     * @return String
     */
    public String getGroupName() {
        return radio.getName();
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
    @JsIgnore
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
    @JsIgnore
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

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    public void markInvalid(String msg) {
        radio.getErrorSupport().markInvalid(
                Collections.singletonList(new DefaultEditorError(radio, msg, radio.getValue())));
    }

    /**
     * Очищает статус не валидности для поля.
     */
    @Override
    public void clearInvalid() {
        radio.getErrorSupport().clearInvalid();
    }

    @Override
    protected Object emptyValue() {
        return false;
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            result.setPart(new Locator(LocatorParams.TYPE_INPUT));
            return result;
        }
        return null;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator) && locator.getPart() != null
                && LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
            return radio.getCell().getInputElement(getWrapper().getElement());
        }
        return null;
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true, если компонент скрыт
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * Проверяет, обязательно ли поле для заполнения.
     *
     * @return true, если обязательно
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Устанавливает обязательность для заполнения поля.
     *
     * @param required true, если поле обязательно для заполнения
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Устанавливает значение только для чтения.
     *
     * @param readOnly true, если поле доступно только для чтения
     */
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    /**
     * Получает маску поля.
     *
     * @return маска поля
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Устанавливает маску поля.
     *
     * @param mask новая маска поля
     */
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Очищает значение поля.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        if (getRealComponent() instanceof IsField) {
            IsField field = getRealComponent();
            field.clear();
            ValueChangeEvent.fire(field, emptyValue());
        }
    }

    private static class LocatorParams {
        private static String TYPE_INPUT = "Input";
    }
}
