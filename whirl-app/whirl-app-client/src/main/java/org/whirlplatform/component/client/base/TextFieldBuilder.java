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
 * Текстовое поле.
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
     * Установка значения текстового поля
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
     * @return максимальная длина возвращаемого поля
     */
    @Override
    public int getMaxLength() {
        return super.getMaxLength();
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
    public void clear() {
        super.clear();
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для не валидного поля
     * @return true если поле доступно
     */
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
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
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
     * Устанавливает кода компонента
     */
    @JsIgnore
    @Override
    public void setCode(String code) {
        super.setCode(code);
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
     * Добавляет имя CSS стиля.
     *
     * @param name - String, имя CSS стиля
     */
    public void addStyleName(String name) {
        super.addStyleName(name);
    }

    /**
     * Устанавливает стиль компонента.
     *
     * @param styleName - String, название стиля
     */
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);
    }

    /**
     * Удаляет имя CSS стиля.
     *
     * @param name - String, название стиля
     */
    public void removeStyleName(String name) {
        super.removeStyleName(name);
    }

    /**
     * Фокусирует компонент.
     */
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true, если компонент включен
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * Устанавливает ширину виджета.
     *
     * @param value - int, новая ширина для установки
     */
    public void setWidth(int value) {
        super.setWidth(value);
    }

    /**
     * Устанавливает высоту виджета.
     *
     * @param value - int, новая высота для установки
     */
    public void setHeight(int value) {
        super.setHeight(value);
    }

    /**
     * Возвращает идентификатор элемента HTML.
     *
     * @return идентификатор элемента
     */
    public String getDomId() {
        return super.getDomId();
    }

    /**
     * Устанавливает идентификатор элемента HTML.
     *
     * @param domId - String, новый идентификатор компонента
     */
    public void setDomId(String domId) {
        super.setDomId(domId);
    }

    @JsIgnore
    @Override
    public Locator getLocatorByElement(Element element) {
        return super.getLocatorByElement(element);
    }

}