package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Результат, который получает функция success callback
 * {@see Events Примеры}
 */
public abstract class EventCallbackResultOverlay {

    public abstract DataValue[] getParameters();

    public abstract DataValue getParameter(int index);

    public abstract Object getRawValue();

    public abstract int getParametersCount();
}
