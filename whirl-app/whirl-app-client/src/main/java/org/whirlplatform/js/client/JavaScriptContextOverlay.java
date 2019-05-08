package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.JavaScriptContext;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.data.DataValue;

public abstract class JavaScriptContextOverlay {


    public static ComponentBuilder getSource(JavaScriptContext instance) {
        return instance.getSource();
    }

    /**
     * Получить список параметров контекста
     *
     * @return
     */
    public static DataValue[] getParameters(JavaScriptContext instance) {
        return instance.getParameters();
    }


    /**
     * Создать структуру для формирования результата, возвращаемого из  JavaScript-событий формы.
     *
     * @return JavaScriptEventResult
     */
    public static JavaScriptEventResult newResult(JavaScriptContext instance) {
        return instance.newResult();
    }


    /**
     * Вернуть параметр контекста с заданным номером
     *
     * @param index
     * @return
     */
    public static DataValue getParameter(JavaScriptContext instance, int index) {
        return instance.getParameter(index);
    }

    /**
     * Вернуть параметр контекста с заданным кодом
     *
     * @param code
     * @return
     */
    public static DataValue getParameterByCode(JavaScriptContext instance,
                                               String code) {
        return instance.getParameter(code);
    }
}
