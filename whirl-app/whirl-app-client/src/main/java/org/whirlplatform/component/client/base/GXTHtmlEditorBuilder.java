package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

import java.util.Map;

/**
 * @author Vlad Компонент - редактор текста. Использует в себе CKEditor
 */
public class GXTHtmlEditorBuilder extends AbstractFieldBuilder
		implements NativeParameter<String>, Parameter<DataValue> {

	protected HtmlEditor field;

	public GXTHtmlEditorBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public GXTHtmlEditorBuilder() {
		super();
	}

	@Override
	public ComponentType getType() {
		return ComponentType.GXTHtmlEditorType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		required = false;

		// Переопределил методы, т.к. в production режиме возникала ошибка
		field = new HtmlEditor() {

			@Override
			protected void onEnable() {
				if (getElement().selectNode("textarea") != null) {
					super.onEnable();
				} else {
					if (disabledStyle != null) {
						removeStyleName(disabledStyle);
					}
				}
			}

			@Override
			protected void onDisable() {
				if (getElement().selectNode("textarea") != null) {
					super.onDisable();
				} else {
					if (disabledStyle != null) {
						addStyleName(disabledStyle);
					}
				}
			}
		};
		return field;
	}

	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.StringValue.getCode())) {
			if (value != null && value.getString() != null) {
				field.setValue(value.getString());
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Enabled.getCode())) {
			if (value != null && value.getBoolean() != null) {
				field.setEnabled(value.getBoolean());
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	@Override
	public String getValue() {
		return field.getValue();
	}

	@Override
	public void setValue(String value) {
		field.setValue(value);
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) field;
	}

	@Override
	public DataValue getFieldValue() {
		DataValue result = new DataValueImpl(DataType.STRING);
		result.setCode(getCode());
		result.setValue(getValue());
		return result;
	}

	@Override
	public void setFieldValue(DataValue value) {
		if (value.isTypeOf(DataType.STRING)) {
			setValue(value.getString());
		}
	}
}
