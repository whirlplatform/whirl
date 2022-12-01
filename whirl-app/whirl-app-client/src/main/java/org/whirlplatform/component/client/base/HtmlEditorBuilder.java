package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Clearable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Html-редактор
 */
@JsType(name = "HtmlEditor", namespace = "Whirl")
public class HtmlEditorBuilder extends ComponentBuilder
        implements NativeParameter<String>, Parameter<DataValue>, Clearable {

    protected JavaScriptObject editor;
    private SimpleContainer container;
    private String plugins;

    @JsConstructor
    public HtmlEditorBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public HtmlEditorBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
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
            extraPlugins: plugins,
            uiColor: '#DBE4F2',
            autoGrow_onStartup: true,
            autoGrow_minHeight: 50,
            resize_enabled: false
        });
    }-*/;

    private native String getEditorData(JavaScriptObject editor) /*-{
        return editor.getData();
    }-*/;

    private native void setEditorData(JavaScriptObject editor, String data) /*-{
        editor.setData(data);
    }-*/;


    /**
     * Получение значение поля.
     *
     * @return String
     */
    @Override
    public String getValue() {
        return getEditorData(editor);
    }

    /**
     * Установка значение поля.
     *
     * @param value - String
     */
    @Override
    public void setValue(String value) {
        setEditorData(editor, value);
    }

    /**
     * Получает текст объекта.
     *
     * @return новый текст объекта
     */
    public String getText() {
        return getValue();
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) container;
    }

    /**
     * Очищает значение поля.
     */
    @Override
    public void clear() {
        setEditorData(editor, "");
    }

    @JsIgnore
    @Override
    public DataValue getFieldValue() {
        DataValue result = new DataValueImpl(DataType.STRING);
        result.setCode(getCode());
        result.setValue(getValue());
        return result;
    }

    @JsIgnore
    @Override
    public void setFieldValue(DataValue value) {
        if (value != null && value.isTypeOf(DataType.STRING)) {
            setValue(value.getString());
        }
    }

    /**
     * Возвращает код компонента.
     *
     * @return код компонента
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true, если компонент скрыт
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}