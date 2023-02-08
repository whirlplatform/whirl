package org.whirlplatform.js.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import java.util.Date;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.date.DateFieldBuilder;
import org.whirlplatform.meta.shared.data.DataValueImpl;

public abstract class DateFieldBuilderOverlay {

    @Deprecated
    public static DateFieldBuilder create(DateFieldBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Проверка на null значения поля даты
     *
     * @param instance - DateFieldBuilder
     * @return boolean
     */
    public static boolean isEmpty(DateFieldBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения поля даты
     *
     * @param value - javascript: Date
     */
    public static void setValue(DateFieldBuilder instance,
                                JavaScriptObject value) {
        JsDate jsDate = value.cast();
        Date date = new Date((long) jsDate.getTime());
        instance.setValue(date);
    }

    /**
     * Получить значение поля даты (вернёт то же значение, что и
     * t.getDataValue().{@link DataValueOverlay#getDateValue(DataValueImpl) getDateValue()} )
     *
     * @return javascript: Date
     */
    public static JavaScriptObject getValue(DateFieldBuilder instance) {
        Date date = instance.getValue();
        JsDate jsDate = null;
        if (date != null) {
            jsDate = JsDate.create(date.getYear() + 1900, date.getMonth(),
                date.getDate(), date.getHours(), date.getMinutes(),
                date.getSeconds(), (int) (date.getTime() % 1000));
        }
        return jsDate;
    }

    /**
     * Получение родителя поля даты
     *
     * @param instance - DateFieldBuilder
     * @return ComponentBuilder
     */
    public static ComponentBuilder getParent(DateFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Указать текст всплывающей подсказки поля даты
     *
     * @param toolTip
     */
    public static void setToolTip(DateFieldBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }

    /**
     * Возвращает идентификатор элемента в DOM документа.
     */
    public abstract String getDomId();

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    /**
     * Получить код поля даты
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установить код на поле даты
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получить информачию об активности поля даты
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установить активность поля даты
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информацию о скрытости поля даты
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установить скрытость поля даты
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);


    /**
     * Получить значение обёрточного типа {@link DataValueOverlay DataValue}, хранящегося в билдере.
     * А из него уже можно получить javascript Date :
     * dataValue.{@link DataValueOverlay#getDateValue(DataValueImpl) getDateValue()}
     */
    //    public static DataValue getDataValue(DateFieldBuilder instance) {
    //        return instance.getFieldValue();
    //    }

    /**
     * Установить значение компонента, используя обёрточный тип {@link DataValueOverlay DataValue}
     */
    //    public static void setDataValue(DateFieldBuilder instance, DataValue value) {
    //        instance.setFieldValue(value);
    //    }

    /**
     * Установить стиль на поле даты
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Получить текстовое представление значения поля
     *
     * @return String
     */
    public abstract String getText();

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у поля даты
     *
     * @return boolean
     */
    public abstract boolean isRequired();

    /**
     * Установка свойства "Обязателен для заполнения" для поля даты
     *
     * @param required - boolean
     */
    public abstract void setRequired(boolean required);

    /**
     * Очистить поле даты
     */
    public abstract void clear();

    /**
     * Установить сообщение не валидности поля даты
     *
     * @param message String
     */
    public abstract void markInvalid(String message);

    /**
     * Очистить сообщения невалидности поля даты
     */
    public abstract void clearInvalid();

    /**
     * Установить фокус на поле даты
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);
}
