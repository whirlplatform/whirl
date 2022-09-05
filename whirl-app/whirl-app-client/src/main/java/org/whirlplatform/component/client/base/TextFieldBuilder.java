package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.TextField;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/**
 * Text field.
 */
@JsType(namespace = "Whirl", name = "TextField")
public class TextFieldBuilder extends ValueBaseFieldBuilder implements NativeParameter<String>, Parameter<DataValue> {

	@JsIgnore
	public TextFieldBuilder() {
		this(Collections.emptyMap());
	}

	@JsConstructor
	public TextFieldBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	
	/**
	 * Получить тип текстового поля
	 */
	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.TextFieldType;
	}

	/**
	 * Создание компонента - текстовое поле
	 * 
	 * @return Component
	 */
	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		super.init(builderProperties);

		field = new TextField();
		field.setSelectOnFocus(true);
		return field;
	}

}