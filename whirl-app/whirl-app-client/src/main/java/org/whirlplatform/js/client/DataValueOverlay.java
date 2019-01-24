package org.whirlplatform.js.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExporterUtil;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;

import java.util.Date;

/**
 * Объект может хранить значение одного из следующих типов: STRING, NUMBER,
 * LIST, DATE, BOOLEAN
 */
//@Export("DataValue")
//@ExportPackage("Whirl")
public abstract class DataValueOverlay implements ExportOverlay<DataValueImpl> {

    @ExportConstructor
    public static DataValueImpl constructor(String type) {
        return new DataValueImpl(DataType.valueOf(type.toUpperCase()));
    }

    /**
     * Проверка значения на null
     */
    @ExportInstanceMethod
    public static boolean isNull(DataValueImpl instance) {
        return instance.isNull();
    }

    /**
     * Если значение имеет строковой тип - то вернуть его. Иначе сгенерировать
     * исключение.
     *
     * @throws JavaScriptException
     */
    @ExportInstanceMethod
    public static String getStringValue(DataValueImpl instance) throws JavaScriptException {
        /*
         * добавил тип TEXT, иначе ..getDataValue().getStringValue() не
         * сработает, например, для компонента SimpleHtmlEditorBuilder который
         * устанавливает stringValue, но тип TEXT
         */
        if (instance.getType() == DataType.STRING) {
            return instance.getString();
        } else {
            throw Helper.getAndLogException("getString error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Если значение имеет числовой тип - то вернуть его. Иначе сгенерировать
     * исключение.
     *
     * @throws JavaScriptException
     */
    @ExportInstanceMethod
    public static Double getNumberValue(DataValueImpl instance) throws JavaScriptException {
        if (instance.getType() == DataType.NUMBER) {
            return instance.getDouble();
        } else {
            throw Helper.getAndLogException("getNumber error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Если значение имеет тип boolean - то вернуть его. Иначе сгенерировать
     * исключение.
     *
     * @throws JavaScriptException
     */
    @ExportInstanceMethod
    public static boolean getBooleanValue(DataValueImpl instance) throws JavaScriptException {
        if (instance.getType() == DataType.BOOLEAN) {
            return instance.getBoolean();
        } else {
            throw Helper.getAndLogException("getBoolean error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Вернёт дату либо сгенерирует исключение, если хранимое значение не
     * является датой
     *
     * @throws JavaScriptException
     */
    @ExportInstanceMethod
    public static Date getDateValue(DataValueImpl instance) throws JavaScriptException {
        if (instance.getType() == DataType.DATE) {
            return instance.getDate();
        } else {
            throw Helper.getAndLogException("getDate error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Если значение является элементом списка
     * {@link ListModelDataOverlay ListModelData} ,
     * то вернёт его. Иначе сгенерирует исключение.
     *
     * @throws JavaScriptException
     */
    @ExportInstanceMethod
    public static ListModelData getListValue(DataValueImpl instance) throws JavaScriptException {
        if (instance.getType() == DataType.LIST) {
            return instance.getListModelData();
        } else {
            throw Helper.getAndLogException("getList error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Установить значение. Должно соответстовать типу, указанному в экземпляре
     * текущего объекта. Иначе будет сгенерировано исключение.
     *
     * @param value
     */
    @ExportInstanceMethod
    public static void setValue(DataValueImpl instance, Object value) {
        DataType type = instance.getType();
        // instance.setValue(DataValue.convertValueFromString(value, null,
        // type));
        if (value == null || (value instanceof String && (type == DataType.STRING))
                || (value instanceof Number && type == DataType.NUMBER)
                || (value instanceof Boolean && type == DataType.BOOLEAN)) {
            instance.setValue(value);
        } else if (value instanceof JavaScriptObject && type == DataType.DATE) {
            Date date = ExporterUtil.jsDateToDate((JavaScriptObject) value);
            instance.setValue(date);
        } else if (value instanceof JavaScriptObject && type == DataType.LIST) {
            instance.setValue(ExporterUtil.gwtInstance(value));
        } else {
            throw Helper.getAndLogException("setValue error. Type of value: " + instance.getType().name());
        }
    }

    /**
     * Узнать тип хранимого значения. Это будет одно из (STRING, NUMBER, LIST,
     * DATE, BOOLEAN)
     *
     * @return String
     */
    @ExportInstanceMethod
    public static String getType(DataValueImpl instance) {
        return instance.getType().name();
    }

    @ExportInstanceMethod
    public static void setCode(DataValueImpl instance, String code) {
        instance.setCode(code);
    }

    @ExportInstanceMethod
    public static String getCode(DataValueImpl instance) {
        return instance.getCode();
    }
}
