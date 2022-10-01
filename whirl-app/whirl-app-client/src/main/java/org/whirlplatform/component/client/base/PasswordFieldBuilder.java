package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/**
 * Password field.
 */
@JsType(namespace = "Whirl", name = "PasswordField")
public class PasswordFieldBuilder extends ValueBaseFieldBuilder {

	@JsIgnore
	public PasswordFieldBuilder() {
		this(Collections.emptyMap());
	}

	@JsConstructor
	public PasswordFieldBuilder(@JsOptional  Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.PasswordFieldType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		super.init(builderProperties);

		field = new PasswordField();
		field.setSelectOnFocus(true);
		return field;
	}

	/**
	 * Получить значение текстового поля
	 *
	 * @return String
	 */
	@Override
	public String getValue() {
		return super.getValue();
	}

	/**
	 * Установка зачения текстового поля
	 *
	 * @param value
	 *            String
	 */
	@Override
	public void setValue(String value) {
		super.setValue(value);
	}

	/**
	 * Returns the field's required minimum length.
	 *
	 * @return field's required minimum length
	 */
	@Override
	public int getMinLength() {
		return super.getMinLength();
	}

	/**
	 * Returns the field's maximum length.
	 *
	 * @return field's maximum length
	 */
	@Override
	public int getMaxLength() {
		return super.getMaxLength();
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		return super.getLocatorByElement(element);
	}

}
