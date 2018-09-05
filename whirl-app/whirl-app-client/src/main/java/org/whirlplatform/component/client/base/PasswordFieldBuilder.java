package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class PasswordFieldBuilder extends ValueBaseFieldBuilder {
	
	public PasswordFieldBuilder() {
		super();
	}

	public PasswordFieldBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	
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
