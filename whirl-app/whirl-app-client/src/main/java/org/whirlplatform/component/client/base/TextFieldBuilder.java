package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.TextField;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.selenium.Locator;
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

    /**
     * Получить значение текстового поля
     *
     * @return String
     */
    @Override
    public String getValue() {
        return super.getValue();
    }

    /**
     * Установка зачения текстового поля
     *
     * @param value String
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    /**
     * Returns the field's required minimum length.
     *
     * @return field's required minimum length
     */
    @Override
    public int getMinLength() {
        return super.getMinLength();
    }

    /**
     * Returns the field's maximum length.
     *
     * @return field's maximum length
     */
    @Override
    public int getMaxLength() {
        return super.getMaxLength();
    }

    /**
     * Gets the field mask.
     *
     * @return the field mask
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Sets the field mask.
     *
     * @param mask the new field mask
     */
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Clears the field value.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        super.clear();
    }

    /**
     * Check if field is valid.
     *
     * @param invalidate true to invalidate field
     * @return true if field is valid
     */
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Sets the required to fill.
     *
     * @param required true, if the field is required to be filled
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Sets the read only.
     *
     * @param readOnly true, if the field is read only
     */
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    /**
     * Returns component's code.
     *
     * @return component's code
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Установка кода компонента
     */
    @JsIgnore
    @Override
    public void setCode(String code) {
        super.setCode(code);
    }

    /**
     * Checks if component is in hidden state.
     *
     * @return true if component is hidden
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Sets component's hidden state.
     *
     * @param hidden true - to hide component, false - to show component
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Добавляет имя CSS
     *
     * @param name - String
     */
    public void addStyleName(String name) {
        super.addStyleName(name);
    }

    /**
     * Установка стиля компонета
     *
     * @param styleName - String, название стиля
     */
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);
    }

    /**
     * Удаляет имя CSS стиля
     *
     * @param name - String
     */
    public void removeStyleName(String name) {
        super.removeStyleName(name);
    }

    /**
     * Focuses component.
     */
    public void focus() {
        super.focus();
    }

    /**
     * Checks if component is enabled.
     *
     * @return true if component is enabled
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Sets component's enabled state.
     *
     * @param enabled true - to enable component, false - to disable component
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void setWidth(int value) {
        super.setWidth(value);
    }

    public void setHeight(int value) {
        super.setHeight(value);
    }

    public String getDomId() {
        return super.getDomId();
    }

    public void setDomId(String domId) {
        super.setDomId(domId);
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        return super.getLocatorByElement(element);
    }

}