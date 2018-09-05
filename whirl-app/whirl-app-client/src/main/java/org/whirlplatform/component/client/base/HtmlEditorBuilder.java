package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.whirlplatform.component.client.Clearable;
import org.whirlplatform.component.client.ComponentBuilder;
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
public class HtmlEditorBuilder extends ComponentBuilder
		implements NativeParameter<String>, Parameter<DataValue>, Clearable {

	protected JavaScriptObject editor;
	private SimpleContainer container;
	private String plugins;

	public HtmlEditorBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public HtmlEditorBuilder() {
		super();
	}

	@Override
	public ComponentType getType() {
		return ComponentType.HtmlEditorType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		plugins = "divarea";
		DataValue frEditing = builderProperties.get(PropertyType.FrameEditing.getCode());
		if (frEditing != null && frEditing.getBoolean()) {
			plugins = "autogrow";
		}

		container = new SimpleContainer();
		Element editorElement = DOM.createDiv();
		container.getElement().appendChild(editorElement);
		editor = getEditor(editorElement, plugins);
		return container;
	}

	private native JavaScriptObject getEditor(Object element, String plugins) /*-{
		return $wnd.CKEDITOR.replace(element, {
			extraPlugins : plugins,
			uiColor : '#DBE4F2',
			autoGrow_onStartup : true,
			autoGrow_minHeight : 50,
			resize_enabled : false
		});
	}-*/;

	private native String getEditorData(JavaScriptObject editor) /*-{
		return editor.getData();
	}-*/;

	private native void setEditorData(JavaScriptObject editor, String data) /*-{
		editor.setData(data);
	}-*/;

	@Override
	public String getValue() {
		return getEditorData(editor);
	}

	@Override
	public void setValue(String value) {
		setEditorData(editor, value);
	}

	public String getText() {
		return getValue();
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) container;
	}

	@Override
	public void clear() {
		setEditorData(editor, "");
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
		if (value != null && value.isTypeOf(DataType.STRING)) {
			setValue(value.getString());
		}
	}
}