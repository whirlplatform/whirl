package org.whirlplatform.js.client;

import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.component.client.event.EventCallbackResult;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Результат, который получает функция success callback
 * {@see Events Примеры}
 */
//@Export("EventCallbackResult")
//@ExportPackage("Whirl")
public abstract class EventCallbackResultOverlay implements
        ExportOverlay<EventCallbackResult> {

    public abstract DataValue[] getParameters();

    public abstract DataValue getParameter(int index);

    public abstract Object getRawValue();

    public abstract int getParametersCount();
}
