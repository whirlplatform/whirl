package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.EventParameterImpl;
import org.whirlplatform.meta.shared.data.ParameterType;


//@Export("EventParameter")
//@ExportPackage("Whirl")
public abstract class EventParameterOverlay implements
		ExportOverlay<EventParameterImpl> {

	private EventParameterOverlay() {
	}

	@ExportConstructor
	public static EventParameterImpl constructor(String type) {
		return new EventParameterImpl(ParameterType.valueOf(type.toUpperCase()));
	}

   /**
    * Получить тип параметра события. Одно из строковых значений: COMPONENT, COMPONENTCODE, STORAGE, DATAVALUE
    * @return String
    */
	@ExportInstanceMethod
	public static String getType(EventParameterImpl instance) {
		return instance.getType().name();
	}

	/**
	 * Получить код компонента, выступающего параметром события. Вообще код правильнее получать через builder компонента. 
	 * @return 
	 */
	@Export
	public abstract String getComponentCode();

	/**
	 * Установить код компонента параметра события
	 * @param comp
	 */
	@Export
	public abstract void setComponentCode(String comp);

	/**
	 * Получить объект DataValue, хранящийся в параметре
	 * @return
	 */
	@Export
	public abstract DataValue getData();

	/**
	 * Установить значение параметра DataValue события. Событие должно иметь тип DATAVALUE 
     * @param {@link DataValueOverlay DataValue}
	 */
	@Export
	public abstract void setData(DataValue data);

	/**
	 * Назначить код параметра события
	 * @return
	 */
	@Export
	public abstract String getCode();

	/**
	 * Назначить параметру события код code
	 */
	@Export
	public abstract void setCode(String code);
}
