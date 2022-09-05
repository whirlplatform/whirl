package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
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

}
