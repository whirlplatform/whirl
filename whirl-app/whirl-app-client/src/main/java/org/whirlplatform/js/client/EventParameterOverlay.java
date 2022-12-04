package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.EventParameterImpl;
import org.whirlplatform.meta.shared.data.ParameterType;

public abstract class EventParameterOverlay {

    private EventParameterOverlay() {
    }

    public static EventParameterImpl constructor(String type) {
        return new EventParameterImpl(ParameterType.valueOf(type.toUpperCase()));
    }

    /**
     * Получить тип параметра события. Одно из строковых значений: COMPONENT, COMPONENTCODE,
     * STORAGE, DATAVALUE
     *
     * @return String
     */
    public static String getType(EventParameterImpl instance) {
        return instance.getType().name();
    }

    /**
     * Получить код компонента, выступающего параметром события. Вообще код правильнее получать
     * через builder компонента.
     *
     * @return
     */
    public abstract String getComponentCode();

    /**
     * Установить код компонента параметра события
     *
     * @param comp
     */
    public abstract void setComponentCode(String comp);

    /**
     * Получить объект DataValue, хранящийся в параметре
     *
     * @return
     */
    public abstract DataValue getData();

    /**
     * Установить значение параметра DataValue события. Событие должно иметь тип DATAVALUE
     *
     * @param {@link DataValueOverlay DataValue}
     */
    public abstract void setData(DataValue data);

    /**
     * Назначить код параметра события
     *
     * @return
     */
    public abstract String getCode();

    /**
     * Назначить параметру события код code
     */
    public abstract void setCode(String code);
}
