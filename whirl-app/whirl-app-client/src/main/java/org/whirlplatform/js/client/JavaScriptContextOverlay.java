package org.whirlplatform.js.client;

import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportStaticMethod;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.JavaScriptContext;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.data.DataValue;


//@Export("JavaScriptContext")
//@ExportPackage("Whirl")
public abstract class JavaScriptContextOverlay implements
        ExportOverlay<JavaScriptContext> {


    @ExportInstanceMethod
    public static ComponentBuilder getSource(JavaScriptContext instance) {
        return instance.getSource();
    }

    /**
     * Получить список параметров контекста
     *
     * @return
     */
    @ExportInstanceMethod
    public static DataValue[] getParameters(JavaScriptContext instance) {
        return instance.getParameters();
    }


    /**
     * Создать структуру для формирования результата, возвращаемого из  JavaScript-событий формы.
     *
     * @return JavaScriptEventResult
     */
    @ExportInstanceMethod
    public static JavaScriptEventResult newResult(JavaScriptContext instance) {
        return instance.newResult();
    }

    /**
     * Некоторым образом обработать context. Фактически этот метод просто возвращает входной параметр и на данный момент бесполезен.
     * Возможно, сохранён для совместимости с ранними версиями кода.
     *
     * @param context
     * @return
     */
    @ExportStaticMethod
    public static Object init(Object context) {
        return JavaScriptContext.init(context);
    }


    /**
     * Вернуть параметр контекста с заданным номером
     *
     * @param index
     * @return
     */
    @ExportInstanceMethod
    public static DataValue getParameter(JavaScriptContext instance, int index) {
        return instance.getParameter(index);
    }

    /**
     * Вернуть параметр контекста с заданным кодом
     *
     * @param code
     * @return
     */
    @ExportInstanceMethod
    public static DataValue getParameterByCode(JavaScriptContext instance,
                                               String code) {
        return instance.getParameter(code);
    }
}
