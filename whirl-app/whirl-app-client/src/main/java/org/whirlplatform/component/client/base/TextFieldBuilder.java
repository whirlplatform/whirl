package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

/**
 * Поле ввода - текстовое
 * 
 * @author semenov_pa
 */
public class TextFieldBuilder extends ValueBaseFieldBuilder implements NativeParameter<String>, Parameter<DataValue> {

	public TextFieldBuilder() {
		super();
	}

	public TextFieldBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	
	/**
	 * Получить тип текстового поля
	 */
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