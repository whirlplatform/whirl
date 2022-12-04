package org.whirlplatform.component.client;

import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

public class StringParameter implements NativeParameter<String>, Parameter<DataValue>, HasCode {

    private String code;
    private String value;

    public StringParameter(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public StringParameter(String value) {
        this(null, value);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
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
        // if (DataType.STRING.equals(value.getType())) {
        if (value.isTypeOf(DataType.STRING)) {
            setValue(value.getString());
            setCode(value.getCode());
        }
    }
}
