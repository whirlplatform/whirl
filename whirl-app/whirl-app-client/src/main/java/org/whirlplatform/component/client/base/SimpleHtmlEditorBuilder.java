package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Простой HTML редактор
 */
@JsType(namespace = "Whirl", name = "SimpleHtmlEditor")
public class SimpleHtmlEditorBuilder extends AbstractFieldBuilder
        implements NativeParameter<String>, Parameter<DataValue> {

    protected HtmlEditor field;

    @JsConstructor
    public SimpleHtmlEditorBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public SimpleHtmlEditorBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.SimpleHtmlEditorType;
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

    @JsIgnore
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

    /**
     * Получает значение текстового поля
     *
     * @return String
     */
    @Override
    public String getValue() {
        return field.getValue();
    }

    /**
     * Устанавливает значения текстового поля
     *
     * @param value String
     */
    @Override
    public void setValue(String value) {
        field.setValue(value);
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) field;
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
        if (value.isTypeOf(DataType.STRING)) {
            setValue(value.getString());
        }
    }

    /**
     * Получает маску поля.
     *
     * @return маска поля
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Устанавливает маску поля.
     *
     * @param mask новая маска поля
     */
    @Override
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Очищает статус недействительности для поля.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Очищает значение поля.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
     */
    @Override
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
    }

    /**
     * Проверяет, обязательно ли поле для заполнения.
     *
     * @return true, если обязательно
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Устанавливает обязательность для заполнения поля.
     *
     * @param required true, если поле обязательно для заполнения
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Устанавливает значение только для чтения.
     *
     * @param readOnly true, если поле доступно только для чтения
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @JsIgnore
    @Override
    public Element getElementByLocator(Locator locator) {
        return super.getElementByLocator(locator);
    }
}
