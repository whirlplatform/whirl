package org.whirlplatform.component.client.utils;

import com.google.gwt.core.client.JavaScriptException;

public class LogHelper {

    /**
     * Получение обработчика ошибки
     *
     * @return JavaScriptException
     */
    public static JavaScriptException getAndLogException(String message) {
        consoleError(message);
        return new JavaScriptException(message);
    }

    /**
     * Создание сообщение об ошибке
     *
     * @param message - String
     */
    private static native void consoleError(String message) /*-{
		if (window.console) {
			window.console.error("JS Error: " + message);
		}
	}-*/;

}
