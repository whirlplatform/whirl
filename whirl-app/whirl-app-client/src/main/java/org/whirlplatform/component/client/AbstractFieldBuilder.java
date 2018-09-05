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
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.ext.FieldClearDecorator;
import org.whirlplatform.component.client.ext.FieldMaskDecorator;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.Map;

/**
 * Билдер-родитель для всех билдеров-полей.
 * 
 * @author semenov_pa
 * 
 */
public abstract class AbstractFieldBuilder extends ComponentBuilder
        implements Clearable, Validatable, ChangeEvent.HasChangeHandlers, KeyPressEvent.HasKeyPressHandlers {

	protected boolean required;
	private FieldMaskDecorator maskDecorator;
	protected FieldClearDecorator clearDecorator;

	private boolean includeMask = false;

	public AbstractFieldBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public AbstractFieldBuilder() {
		super();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		} else if (name.equalsIgnoreCase(PropertyType.Clearable.getCode())) {
			if (Boolean.TRUE.equals(value.getBoolean()) && componentInstance instanceof ValueBaseField<?>) {
				clearDecorator = new FieldClearDecorator((ValueBaseField<?>) componentInstance, createClearCommand());
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

	public String getFieldMask() {
		if (maskDecorator != null) {
			return maskDecorator.getMask();
		}
		return null;
	}

	/**
	 * Устанавливает компоненту сообщение о не валидности данных.
	 * 
	 * @param msg
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
	 * Очищает сообщение о не валидности.
	 */
	@Override
	public void clearInvalid() {
		if (getRealComponent() instanceof Field) {
            Field<?> field = getRealComponent();
			field.clearInvalid();
		}
	}

	/**
	 * Очищает поле от данных.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@Override
	public boolean isValid() {
		return isValid(false);
	}

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

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	public void setReadOnly(boolean readOnly) {
		Field<?> field = getRealComponent();
		field.setReadOnly(readOnly);
	}

	@Override
    public HandlerRegistration addChangeHandler(ChangeEvent.ChangeHandler handler) {
		return addHandler(handler, ChangeEvent.getType());
	}

	@Override
    public HandlerRegistration addKeyPressHandler(KeyPressEvent.KeyPressHandler handler) {
		return addHandler(handler, KeyPressEvent.getType());
	}
	
	
	protected static class LocatorParams {
		public static String TYPE_CLEAR = "Clear";
	}
	
	@Override
	public Element getElementByLocator(Locator locator) {
		if(fitsLocator(locator) && locator.getPart()!= null 
			&& LocatorParams.TYPE_CLEAR.equals(locator.getPart().getType())){
			return clearDecorator.getElement();
		}
		return super.getElementByLocator(locator);
	}
	
	@Override
	public Locator getLocatorByElement(Element element) {
		Locator locator = super.getLocatorByElement(element);
		if(clearDecorator!= null && clearDecorator.getElement().isOrHasChild(element)){
			locator.setPart(new Locator(LocatorParams.TYPE_CLEAR));
		} 
		return locator;
    }
}
