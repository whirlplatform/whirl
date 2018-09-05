package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

import java.util.Map;

/**
 * Числовое поле ввода
 */
public class NumberFieldBuilder extends AbstractFieldBuilder implements NativeParameter<Double>, Parameter<DataValue> {

	public NumberField<Double> field;
	// private MaxLengthValidator maxLengthValidator;
	// private MinLengthValidator minLengthValidator;

	public NumberFieldBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public NumberFieldBuilder() {
		super();
	}

	/**
	 * Получить тип числового поля
	 * 
	 * @return ComponentType
	 */
	@Override
	public ComponentType getType() {
		return ComponentType.NumberFieldType;
	}

	/**
	 * Создание компонента - числовое поле
	 * 
	 * @return Component
	 */
	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		required = false;

		field = new NumberField<Double>(new DoublePropertyEditor(NumberFormat.getDecimalFormat()));
		field.setSelectOnFocus(true);
		return field;
	}

	/**
	 * Установка атрибута для числового поля
	 * 
	 * @param name
	 *            - String, название атрибута
	 * @param value
	 *            - String, значение атрибута
	 * @return boolean
	 */
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.NumberMaxValue.getCode())) {
			if (value != null && value.getDouble() != null) {
				field.addValidator(new MaxNumberValidator<Double>(value.getDouble()));
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.NumberMinValue.getCode())) {
			if (value != null && value.getDouble() != null) {
				field.addValidator(new MinNumberValidator<Double>(value.getDouble()));
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.AllowDecimals.getCode())) {
			if (value != null && value.getBoolean() != null) {
				field.setAllowDecimals(value.getBoolean());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.AllowNegative.getCode())) {
			if (value != null && value.getBoolean() != null) {
				field.setAllowNegative(value.getBoolean());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.MinLength.getCode())) {
			if (value != null && value.getInteger() != null) {
				setMinLength(value.getInteger());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.MaxLength.getCode())) {
			if (value != null && value.getInteger() != null) {
				setMaxLength(value.getInteger());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
			if (value != null) {
				field.setValue(parseNumber(value.getString()));
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.KeyValidate.getCode())) {
			if (value != null && value.getBoolean() != null) {
				field.setAutoValidate(value.getBoolean());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.FontSize.getCode())) {
			if (value != null && value.getString() != null) {
				field.getElement().getStyle().setProperty("fontSize", value.getString());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Color.getCode())) {
			if (value != null && value.getString() != null) {
				field.getElement().getStyle().setProperty("color", value.getString());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Format.getCode())) {
			if (value != null) {
				setFormat(value.getString());
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	/**
	 * Установка максимальной длины числового поля
	 * 
	 * @param length
	 *            - int
	 */
	public void setMaxLength(int length) {
		// TODO длинна реализовать??
		// if (maxLengthValidator != null) {
		// field.removeValidator(maxLengthValidator);
		// }
		// maxLengthValidator = new MaxLengthValidator(length);
		// field.addValidator(maxLengthValidator);
	}

	/**
	 * Установка минимальной длины числового поля
	 * 
	 * @param length
	 *            - int
	 */
	public void setMinLength(int length) {
		// TODO Длинна реализовать??
		// if (minLengthValidator != null) {
		// field.removeValidator(maxLengthValidator);
		// }
		// minLengthValidator = new MinLengthValidator(length);
		// field.addValidator(minLengthValidator);
	}

	/**
	 * Установка формата числового поля
	 * 
	 * @param format
	 *            - String
	 */
	public void setFormat(String format) {
		if (!Util.isEmptyString(format)) {
			field.setFormat(NumberFormat.getFormat(format));
			field.redraw();
		}
	}

	/**
	 * Получение значение числового поля
	 * 
	 * @return Double
	 */
	@Override
	public Double getValue() {
        return field.getValue();
	}

	/**
	 * Установка зачения числового поля
	 * 
	 * @param value
	 *            Double
	 */
	@Override
	public void setValue(Double value) {
		field.setValue(value);
	}

	public String getText() {
		return field.getText();
	}

	/**
	 * Разбор введенных символов в число
	 * 
	 * @param value
	 *            - Object
	 * @return Number
	 */
	private Double parseNumber(Object value) {
		Double result = null;
		Number n = null;
		if (value instanceof String) {
			String str = ((String) value).replaceFirst(",", ".");
			try {
				n = Double.valueOf(str);
			} catch (Exception e) {
				try {
					n = Integer.valueOf(str);
				} catch (Exception e1) {
				}
			}
		}
		if (value instanceof Number) {
			n = (Number) value;
		}
		if (n != null) {
			result = n.doubleValue();
		}
		return result;
	}

	/**
	 * Получение сущности числового поля
	 * 
	 * @return (C) field
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <C> C getRealComponent() {
		return (C) field;
	}

	/**
	 * Получение записи из числового поля
	 * 
	 * @return DataValue(Type, Value, Code)
	 */
	@Override
	public DataValue getFieldValue() {
		DataValue result = new DataValueImpl(DataType.NUMBER);
		result.setCode(getCode());
		result.setValue(getValue());
		return result;
	}

	/**
	 * Присвоить запись числовому полю
	 * 
	 * @param value
	 *            - DataValue(Type, Value, Code)
	 */
	@Override
	public void setFieldValue(DataValue value) {
		if (DataType.NUMBER.equals(value.getType())) {
			setValue(value.getDouble());
		}
	}

	private static class LocatorParams {
		private static String TYPE_INPUT = "Input";
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		Locator result = super.getLocatorByElement(element);
		if (result != null) {
			if(result.getPart() == null){
				result.setPart(new Locator(LocatorParams.TYPE_INPUT));				
			}
		}
		return result;
	}

	@Override
	public Element getElementByLocator(Locator locator) {
		if (!super.fitsLocator(locator)) {
			return null;
		}
		Locator part = locator.getPart();
		if (part != null) {
			if(LocatorParams.TYPE_INPUT.equals(locator.getPart().getType())) {
				return field.getCell().getInputElement(getWrapper().getElement());
			} 
		}
		return  super.getElementByLocator(locator); 
	}
}