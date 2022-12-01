package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Поле пароля.
 */
@JsType(namespace = "Whirl", name = "PasswordField")
public class PasswordFieldBuilder extends ValueBaseFieldBuilder {

    @JsIgnore
    public PasswordFieldBuilder() {
        this(Collections.emptyMap());
    }

    @JsConstructor
    public PasswordFieldBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
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

    /**
     * Получает значение текстового поля
     *
     * @return String
     */
    @Override
    public String getValue() {
        return super.getValue();
    }

    /**
     * Устанавливает значения текстового поля
     *
     * @param value String
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    /**
     * Возвращает требуемую минимальную длину поля.
     *
     * @return требуемая минимальная длина поля
     */
    @Override
    public int getMinLength() {
        return super.getMinLength();
    }

    /**
     * Возвращает максимальную длину поля.
     *
     * @return максимальная длина поля
     */
    @Override
    public int getMaxLength() {
        return super.getMaxLength();
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        return super.getLocatorByElement(element);
    }

}
