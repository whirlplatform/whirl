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
import java.util.Collections;
import java.util.Map;
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

/**
 * Чек-бокс поле.
 */
@JsType(namespace = "Whirl", name = "CheckBox")
public class CheckBoxBuilder extends AbstractFieldBuilder
    implements Clearable, Validatable, ChangeEvent.HasChangeHandlers, NativeParameter<Boolean>,
    Parameter<DataValue> {

    private CheckBox checkBox;

    @JsConstructor
    public CheckBoxBuilder(@JsOptional Map<String, DataValue> builderProperties) {
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
            public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
                                       Element parent, Boolean value,
                                       NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
                if (isReadOnly()
                    && !("blur".equals(event.getType()) || "focus".equals(event.getType()))) {
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
     * Получает лейбл поля.
     *
     * @return текст лейбла
     */
    public String getBoxLabel() {
        return checkBox.getBoxLabel().asString();
    }

    /**
     * Устанавливает лейбл для поля.
     *
     * @param label - String текст
     */
    public void setBoxLabel(String label) {
        checkBox.setBoxLabel(label);
    }

    /**
     * Получить значение поля.
     *
     * @return значение поля
     */
    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    /**
     * Устанавливает значение поля.
     *
     * @param value - boolean значение поля
     */
    @Override
    public void setValue(Boolean value) {
        checkBox.setValue(value);
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @return true, для признания поля валидным
     */
    @JsIgnore
    @Override
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
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

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    public void markInvalid(String msg) {
        checkBox.getErrorSupport().markInvalid(
            Collections.singletonList(
                new DefaultEditorError(checkBox, msg, checkBox.getValue())));
    }

    /**
     * Очищает статус не валидности для поля.
     */
    @Override
    public void clearInvalid() {
        checkBox.getErrorSupport().clearInvalid();
    }

    /**
     * Устанавливает значение только для чтения.
     *
     * @param readOnly true, если поле доступно только для чтения
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
        super.clear();
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

    private static class LocatorParams {

        private static final String TYPE_INPUT = "Input";

    }


}
