package org.whirlplatform.editor.client.component;

import com.sencha.gxt.data.shared.Converter;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class PropertyValueFieldConverter implements
		Converter<PropertyValue, String> {

	private PropertyValueField field;

	public PropertyValueFieldConverter(PropertyValueField field) {
		this.field = field;
	}

	@Override
	public PropertyValue convertFieldValue(String object) {
		field.setValue(field.getCurrentKey(), object);
		return field.getPropertyValue();
	}

	@Override
	public String convertModelValue(PropertyValue object) {
		field.clear();
		field.setPropertyValue(object);
		field.redrawCell();
		return field.getValue(field.getCurrentKey());
	}

}
