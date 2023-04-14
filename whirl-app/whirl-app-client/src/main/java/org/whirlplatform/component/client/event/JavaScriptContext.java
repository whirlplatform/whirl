package org.whirlplatform.component.client.event;

import java.util.List;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Контекст выполняемого события
 */
@JsType(name = "ContextEvent", namespace = "Whirl")
public class JavaScriptContext {

    private ComponentBuilder source;
    private List<DataValue> parameters;
    private String nextEventCode;

    @JsIgnore
    public JavaScriptContext() {
    }

    @JsIgnore
    public JavaScriptContext(ComponentBuilder source, List<DataValue> parameters) {
        this.source = source;
        this.parameters = parameters;
    }

    @JsIgnore
    public ComponentBuilder getSource() {
        return source;
    }

    /**
     * Получить список параметров контекста
     *
     * @return DataValue[] - массив параметров
     */
    @JsIgnore
    public DataValue[] getParameters() {
        return parameters.toArray(new DataValue[0]);
    }

    /**
     * Вернуть параметр контекста с заданным номером
     *
     * @param index - int номер параметра
     * @return DataValue - переметр
     */
    @JsIgnore
    public DataValue getParameter(int index) {
        return parameters.get(index);
    }

    /**
     * Вернуть параметр контекста с заданным кодом
     *
     * @param code  - код
     * @return DataValue - параметр
     */
    @JsIgnore
    public DataValue getParameter(String code) {
        if (code == null) {
            return null;
        }
        for (DataValue v : parameters) {
            if (code.equals(v.getCode())) {
                return v;
            }
        }
        return null;
    }

    /**
     * Вернуть код подчиненного события
     *
     * @return код события
     */
    public String getNextEvent() {
        return nextEventCode;
    }

    /**
     * Установить код подчиненного события
     *
     * @param eventCode код события
     */
    public void setNextEvent(String eventCode) {
        this.nextEventCode = eventCode;
    }
}
