package org.whirlplatform.component.client.date;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sencha.gxt.cell.core.client.form.DateCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Поле ввода даты
 */
@JsType(name = "DateField", namespace = "Whirl")
public class DateFieldBuilder extends AbstractFieldBuilder
    implements NativeParameter<Date>, Parameter<DataValue> {

    private DateField field;
    private String datePattern;
    private DateTimeFormat dateFormat;
    // private DateTimeFormat dateInFormat;

    @JsConstructor
    public DateFieldBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public DateFieldBuilder() {
        this(Collections.emptyMap());
    }

    /**
     * Получить тип поля даты
     */
    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.DateFieldType;
    }

    /**
     * Создание компонента - поле даты
     *
     * @return Component
     */
    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        required = false;

        datePattern = "dd.MM.yyyy HH:mm:ss";
        dateFormat = DateTimeFormat.getFormat(getDatePattern());
        // dateInFormat = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");

        DateCell cell = new DateCell() {
            @Override
            public void finishEditing(Element parent, Date value, Object key,
                                      ValueUpdater<Date> valueUpdater) {
                Object empty = getComponent().getData(AppConstant.MASK_EMPTY);
                if (empty != null && empty instanceof Boolean && (Boolean) empty) {
                    return;
                }
                super.finishEditing(parent, value, key, valueUpdater);
            }
        };

        DateTimeFormat format = getDateFormat();
        field = new DateField(cell, new DateTimePropertyEditor(format));
        field.setSelectOnFocus(true);
        field.setValidateOnBlur(true);
        // field.setClearValueOnParseError(false); // Зачем нужно было?
        return field;
    }

    /**
     * Установка атрибута для поля даты
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.DateMinValue.getCode())) {
            if (value != null && value.getDate() != null) {
                setMinValue(value.getDate());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.DateMaxValue.getCode())) {
            if (value != null && value.getDate() != null) {
                setMaxValue(value.getDate());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.DateValue.getCode())) {
            if (value != null && value.getDate() != null) {
                setValue(value.getDate());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Format.getCode())) {
            if (value != null && !Util.isEmptyString(value.getString())) {
                setDatePattern(value.getString());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Editable.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setEditable(value.getBoolean());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.HideTrigger.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setHideTrigger(value.getBoolean());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Cleanable.getCode())) {
            if (value != null && value.getBoolean() != null && value.getBoolean()) {
                super.setProperty(name, value);
                setClearCrossRightOffset(18);
            }
            return true;
        }
        return super.setProperty(name, value);
    }

    /**
     * Получение значения поля даты.
     *
     * @return Date
     */
    @JsIgnore
    @Override
    public Date getValue() {
        return field.getValue();
    }

    /**
     * Установка значения поля даты.
     *
     * @param value Date
     */
    @JsIgnore
    @Override
    public void setValue(Date value) {
        field.setValue(value);
    }

    /**
     * Получить тип текста.
     */
    public String getText() {
        return field.getText();
    }

    /**
     * Получение шаблона поля даты.
     *
     * @return String
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * Установка шаблона поля даты.
     *
     * @param datePattern - String
     */
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        setDateFormat(DateTimeFormat.getFormat(datePattern));
    }

    /**
     * Получение формата поля даты.
     *
     * @return DateTimeFormat
     */
    @JsIgnore
    public DateTimeFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Установить формат поля даты.
     *
     * @param dateFormat - DateTimeFormat
     */
    @JsIgnore
    public void setDateFormat(DateTimeFormat dateFormat) {
        this.dateFormat = dateFormat;
        field.setPropertyEditor(new DateTimePropertyEditor(dateFormat));
        field.redraw();
    }

    /**
     * Получить текущую дату.
     *
     * @return Date
     */
    @JsIgnore
    public Date getCurrentDate() {
        return new Date();
    }

    /**
     * Установить минимальное значение поля даты.
     *
     * @param minValue - Date
     */
    @JsIgnore
    public void setMinValue(Date minValue) {
        field.setMinValue(minValue);
    }

    /**
     * Установить максимальное значение поля даты.
     *
     * @param maxValue - Date
     */
    @JsIgnore
    public void setMaxValue(Date maxValue) {
        field.setMaxValue(maxValue);
    }

    /**
     * Установить "редактируемость" поля даты.
     *
     * @param editable - boolean
     */
    public void setEditable(Boolean editable) {
        field.setEditable(editable);
    }

    /**
     * Установить скрытость выпадающего поля выбора даты.
     *
     * @param hideTrigger - Boolean
     */
    public void setHideTrigger(Boolean hideTrigger) {
        field.setHideTrigger(hideTrigger);
    }

    /**
     * Установка параметра "только чтение" для поля даты.
     *
     * @param readOnly - boolean
     */
    public void setReadOnly(Boolean readOnly) {
        field.setReadOnly(readOnly);
    }

    /**
     * Получение сущности поля даты.
     *
     * @return (C) field
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <C> C getRealComponent() {
        return (C) field;
    }

    /**
     * Получение записи из поля даты.
     *
     * @return DataValue(Type, Code, Value)
     */
    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.DATE);
        result.setCode(getCode());
        result.setValue(getValue());
        return result;
    }

    /**
     * Присвоить запись полю даты.
     *
     * @param value - DataValue(Type, Value, Code)
     */
    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (DataType.DATE.equals(value.getType())) {
            setValue(value.getDate());
        }
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = super.getLocatorByElement(element);
        if (result != null) {
            if (field.getCell().getInputElement(field.getElement()).isOrHasChild(element)) {
                result.setPart(new Locator(DateFieldBuilder.LocatorParams.TYPE_INPUT));
            } else if (field.getCell().getAppearance()
                .triggerIsOrHasChild(field.getElement(), element)) {
                result.setPart(new Locator(DateFieldBuilder.LocatorParams.TYPE_TRIGGER));
            }
        }
        return result;
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (!super.fitsLocator(locator)) {
            return null;
        }
        Element element = null;
        Locator part = locator.getPart();
        if (part != null) {
            if (LocatorParams.TYPE_INPUT.equals(part.getType())) {
                element = field.getCell().getInputElement(field.getElement());
            }
            if (LocatorParams.TYPE_TRIGGER.equals(part.getType())) {
                TriggerFieldAppearance appearance = field.getCell().getAppearance();
                if (appearance instanceof TriggerFieldDefaultAppearance) {
                    TriggerFieldDefaultAppearance defApp =
                        (TriggerFieldDefaultAppearance) appearance;
                    element = field.getElement()
                        .selectNode("." + defApp.getStyle().trigger()); //TODO что-то придумать
                }
            }
        }
        return element;
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
     * Фокусирует компонент.
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
     * @return true, если компонент включен
     */
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
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
     */
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
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
     * Устанавливает статус недействительности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Очищает статус недействительности для поля.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Очищает значение поля.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        super.clear();
    }

    private static class LocatorParams {
        private static final String TYPE_INPUT = "Input";
        private static final String TYPE_TRIGGER = "Trigger";
    }
}
