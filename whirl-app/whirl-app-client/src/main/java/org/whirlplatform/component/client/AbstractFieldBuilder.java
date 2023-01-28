package org.whirlplatform.component.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import java.util.Map;
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.ext.FieldClearDecorator;
import org.whirlplatform.component.client.ext.FieldMaskDecorator;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

/**
 * Билдер-родитель для всех билдеров-полей.
 */
public abstract class AbstractFieldBuilder extends ComponentBuilder
        implements Clearable, Validatable, ChangeEvent.HasChangeHandlers,
        KeyPressEvent.HasKeyPressHandlers {

    protected boolean required;
    protected FieldClearDecorator clearDecorator;
    private FieldMaskDecorator maskDecorator;
    private boolean includeMask = false;

    @JsIgnore
    public AbstractFieldBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public AbstractFieldBuilder() {
        super();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void initHandlers() {
        super.initHandlers();
        if (componentInstance instanceof Field) {
            Field<?> field = (Field<?>) componentInstance;
            field.addValueChangeHandler(new ValueChangeHandler() {

                @Override
                public void onValueChange(ValueChangeEvent event) {
                    fireEvent(new ChangeEvent());
                }
            });
        }
        if (componentInstance instanceof ValueBaseField) {
            ValueBaseField<?> field = (ValueBaseField<?>) componentInstance;
            field.addKeyPressHandler(new com.google.gwt.event.dom.client.KeyPressHandler() {

                @Override
                public void onKeyPress(com.google.gwt.event.dom.client.KeyPressEvent event) {
                    fireEvent(new KeyPressEvent());
                }
            });
        }
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        boolean result = super.setProperty(name, value);
        if (name.equalsIgnoreCase(PropertyType.Required.getCode())) {
            if (value.getBoolean() != null) {
                setRequired(value.getBoolean());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ReadOnly.getCode())) {
            if (value.getBoolean() != null) {
                setReadOnly(value.getBoolean());
            }
            return true;

        } else if (name.equalsIgnoreCase(PropertyType.Mask.getCode())) {
            setFieldMask(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Cleanable.getCode())) {
            if (Boolean.TRUE.equals(value.getBoolean())
                && componentInstance instanceof ValueBaseField<?>) {
                clearDecorator = new FieldClearDecorator((ValueBaseField<?>) componentInstance,
                        createClearCommand());
                setClearCrossRightOffset(2);
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.IncludeMask.getCode())) {
            includeMask = Boolean.TRUE.equals(value.getBoolean());
            if (maskDecorator != null) {
                maskDecorator.setIncludeMask(includeMask);
            }
        }
        return result;
    }

    protected void setClearCrossRightOffset(double value) {
        clearDecorator.setRightOffset(value);
    }

    protected Command createClearCommand() {
        Command clearCommand = new Command() {
            @Override
            public void execute() {
                clear();
            }
        };
        return clearCommand;
    }

    /**
     * Gets the field mask.
     *
     * @return the field mask
     */
    public String getFieldMask() {
        if (maskDecorator != null) {
            return maskDecorator.getMask();
        }
        return null;
    }

    /**
     * Sets the field mask.
     *
     * @param mask the new field mask
     */
    public void setFieldMask(String mask) {
        if (!Util.isEmptyString(mask) && componentInstance instanceof ValueBaseField<?>) {
            maskDecorator = new FieldMaskDecorator((ValueBaseField<?>) componentInstance, mask);
            ((ValueBaseField<?>) componentInstance).addValidator(maskDecorator);
            maskDecorator.setIncludeMask(includeMask);
        }
        if (mask == null && maskDecorator != null) {
            maskDecorator.remove();
            ((ValueBaseField<?>) componentInstance).removeValidator(maskDecorator);
            maskDecorator = null;
        }
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    @Override
    public void markInvalid(String msg) {
        if (getRealComponent() instanceof Field) {
            Field<?> field = getRealComponent();
            // field.markInvalid(msg);
            // Т.к. field.markInvalid снимается после field.isValid, если поле
            // проходит условия валидации
            field.forceInvalid(msg);
        }
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        if (getRealComponent() instanceof Field) {
            Field<?> field = getRealComponent();
            field.clearInvalid();
        }
    }

    /**
     * Clears the field value.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        if (getRealComponent() instanceof IsField) {
            IsField field = getRealComponent();
            field.clear();
            ValueChangeEvent.fire(field, emptyValue());
        }
    }

    /**
     * Возвращает пустое значение для поля.
     *
     * @return пустое значение
     */
    protected Object emptyValue() {
        return null;
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
     * Check if field is valid.
     *
     * @param invalidate true to invalidate field
     * @return true if field is valid
     */
    public boolean isValid(boolean invalidate) {
        if (!(getRealComponent() instanceof Field)) {
            return true;
        }
        Field<?> field = getRealComponent();
        if (!field.validate(!invalidate)) {
            return false;
        }
        if (isRequired() && checkRequired(field.getValue())) {
            if (invalidate) {
                field.markInvalid(AppMessage.Util.MESSAGE.requiredField());
            }
            return false;
        }
        if (invalidate) {
            field.clearInvalid();
        }
        return true;
    }

    protected boolean checkRequired(Object value) {
        return value == null;
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets the required to fill.
     *
     * @param required true, if the field is required to be filled
     */
    @Override
    public void setRequired(boolean required) {
        this.required = required;
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

    @JsIgnore
    @Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
        return addHandler(handler, ChangeEvent.getType());
    }

    @JsIgnore
    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressEvent.KeyPressHandler handler) {
        return addHandler(handler, KeyPressEvent.getType());
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        if (fitsLocator(locator) && locator.getPart() != null
            && LocatorParams.TYPE_CLEAR.equals(locator.getPart().getType())) {
            return clearDecorator.getElement();
        }
        return super.getElementByLocator(locator);
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator locator = super.getLocatorByElement(element);
        if (clearDecorator != null && clearDecorator.getElement().isOrHasChild(element)) {
            locator.setPart(new Locator(LocatorParams.TYPE_CLEAR));
        }
        return locator;
    }

    protected static class LocatorParams {
        public static String TYPE_CLEAR = "Clear";
    }
}
