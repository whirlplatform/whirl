package org.whirlplatform.component.client.event;

import com.google.gwt.core.client.JavaScriptObject;
import org.timepedia.exporter.client.ExporterUtil;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.List;

public class JavaScriptContext {
	private ComponentBuilder source;
	private List<DataValue> parameters;

	public JavaScriptContext() {
	}

	public JavaScriptContext(ComponentBuilder source, List<DataValue> parameters) {
		this.source = source;
		this.parameters = parameters;
	}

	public ComponentBuilder getSource() {
		return source;
	}

	public DataValue[] getParameters() {
		return parameters.toArray(new DataValue[0]);
	}

	public DataValue getParameter(int index) {
		return parameters.get(index);
	}

	public DataValue getParameter(String code) {
		if (code == null) {
			return null;
		}
		for (DataValue v : parameters) {
			if (code.equals(v.getCode())) {
				return v;
			}
		}
		return null;
	}

	public JavaScriptEventResult newResult() {
		return new JavaScriptEventResult();
	}

	// @SuppressWarnings("unchecked")
	// public static JavaScriptContext init(String context) {
	// Map<String, Object> root = XJSONConverter.decodeSimple(context);
	//
	// JavaScriptContext result = new JavaScriptContext();
	// String componentId = (String) root.get("source");
	// result.source = BuilderManager.findBuilder(componentId, false);
	// result.parameters = new ArrayList<DataValue>();
	//
	// for (Map<String, String> item : (List<Map<String, String>>) root
	// .get("parameters")) {
	// DataType type = DataType.valueOf(item.get("type"));
	// DataValue value = new DataValue(type);
	// value.setCode(item.get("code"));
	// value.setValue(DataValue.convertValueFromString(item.get("value"),
	// item.get("listTitle"), type));
	// result.parameters.add(value);
	// }
	//
	// return result;
	// }

	public static Object init(Object context) {
		return context;
	}

	public JavaScriptObject asObject() {
		return ExporterUtil.wrap(this);
	}
}
