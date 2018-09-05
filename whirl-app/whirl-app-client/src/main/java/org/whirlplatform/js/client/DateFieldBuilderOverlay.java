package org.whirlplatform.js.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.date.DateFieldBuilder;
import org.whirlplatform.meta.shared.data.DataValueImpl;

import java.util.Date;

@Export("DateField")
@ExportPackage("Whirl")
public abstract class DateFieldBuilderOverlay implements
        ExportOverlay<DateFieldBuilder> {

    @ExportInstanceMethod
    @Deprecated
    public static DateFieldBuilder create(DateFieldBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установить код на поле даты
     *
     * @param name - String, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получить код поля даты
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установить активность поля даты
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получить информачию об активности поля даты
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установить скрытость поля даты
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получить информацию о скрытости поля даты
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Установить стиль на поле даты
     *
     * @param styleName - String, название стиля
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Проверка на null значения поля даты
     *
     * @param instance - DateFieldBuilder
     * @return boolean
     */
    @ExportInstanceMethod
    public static boolean isEmpty(DateFieldBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Установка значения поля даты
     *
     * @param value - javascript: Date
     */
    @ExportInstanceMethod
    public static void setValue(DateFieldBuilder instance,
                                JavaScriptObject value) {
        JsDate jsDate = ((JavaScriptObject) value).cast();
        Date date = new Date((long) jsDate.getTime());
        instance.setValue(date);
    }

    /**
     * Получить значение поля даты (вернёт то же значение, что и t.getDataValue().{@link DataValueOverlay#getDateValue(DataValueImpl) getDateValue()} )
     *
     * @return javascript: Date
     */
    @ExportInstanceMethod
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
     * Получить текстовое представление значения поля
     *
     * @return String
     */
    @Export
    public abstract String getText();


    /**
     * Получить значение обёрточного типа {@link DataValueOverlay DataValue}, хранящегося в билдере.
     * А из него уже можно получить javascript Date : dataValue.{@link DataValueOverlay#getDateValue(DataValueImpl) getDateValue()}
     */
//    @ExportInstanceMethod
//    public static DataValue getDataValue(DateFieldBuilder instance) {
//        return instance.getFieldValue();
//    }

    /**
     * Установить значение компонента, используя обёрточный тип {@link DataValueOverlay DataValue}
     */
//    @ExportInstanceMethod
//    public static void setDataValue(DateFieldBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Установка свойства "Обязателен для заполнения" для поля даты
     *
     * @param required - boolean
     */
    @Export
    public abstract void setRequired(boolean required);

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у поля даты
     *
     * @return boolean
     */
    @Export
    public abstract boolean isRequired();

    /**
     * Получение родителя поля даты
     *
     * @param instance - DateFieldBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(DateFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Очистить поле даты
     */
    @Export
    public abstract void clear();

    /**
     * Установить сообщение не валидности поля даты
     *
     * @param
     */
    @Export
    public abstract void markInvalid(String message);

    /**
     * Очистить сообщения невалидности поля даты
     */
    @Export
    public abstract void clearInvalid();

    /**
     * Установить фокус на поле даты
     */
    @Export
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);

    /**
     * Указать текст всплывающей подсказки поля даты
     *
     * @param toolTip
     */
    @ExportInstanceMethod
    public static void setToolTip(DateFieldBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
