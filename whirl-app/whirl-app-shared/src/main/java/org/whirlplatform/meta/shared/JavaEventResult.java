package org.whirlplatform.meta.shared;

import com.google.gwt.user.client.Command;
import org.whirlplatform.meta.shared.data.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class JavaEventResult implements EventResult, Serializable {

	private String nextEventCode;
	private EventMetadata nextEvent;

	private String title;
	private String message;
	private String messageType;
	private String rawValue;

	private transient Command command;

	private TreeMap<Integer, EventParameter> parameters = new TreeMap<Integer, EventParameter>();

	public JavaEventResult() {
	}

	public JavaEventResult(String nextEventCode) {
		this.nextEventCode = nextEventCode;
	}

	@Override
	public void setNextEvent(EventMetadata event) {
		this.nextEvent = event;
	}

	@Override
	public EventMetadata getNextEvent() {
		return nextEvent;
	}

	@Override
	public boolean hasNextEvent() {
		return nextEvent != null;
	}

	@Override
	public void setNextEventCode(String nextEventCode) {
		this.nextEventCode = nextEventCode;
	}

	@Override
	public String getNextEventCode() {
		return nextEventCode;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	private void commandExecute() {
		if (isReady()) {
			command.execute();
		}

	}

	@Override
	public boolean isReady() {
		return nextEvent != null;
	}

	public void addParameter(EventParameter parameter) {
		Integer key;
		if (parameters.isEmpty()) {
			key = 0;
		} else {
			key = parameters.lastKey() + 1;
		}
		parameters.put(key, parameter);
	}

	public void setParameter(Integer index, EventParameter parameter) {
		parameters.put(index, parameter);
	}

	@Override
	public Map<Integer, EventParameter> getParametersMap() {
		return parameters;
	}

	// TODO перенести в DataFetcher
	@SuppressWarnings("unchecked")
	public static JavaEventResult parseEventResult(Map<String, Object> root) {
		Map<String, Object> map = (Map<String, Object>) root.get("result");

		String nextEventCode = (String) map.get("nextEvent");
		JavaEventResult eventResult = new JavaEventResult(nextEventCode);
		eventResult.setMessage((String) map.get("message"));
		eventResult.setTitle((String) map.get("title"));

		eventResult.setMessageType((String) map.get("messageType"));

		for (Map<String, String> p : (List<Map<String, String>>) map
				.get("parameters")) {

			Integer index = Integer.valueOf(p.get("index"));
			DataType type = DataType.valueOf(p.get("type"));
			String code = p.get("code");
			String component = p.get("component");
			String value = p.get("value");
			String title = p.get("title");

			ParameterType paramType;
			if (component != null && !component.isEmpty()) {
				paramType = ParameterType.COMPONENTCODE;
			} else {
				paramType = ParameterType.DATAVALUE;
			}

			EventParameter parameter = new EventParameterImpl(paramType);
			if (paramType == ParameterType.COMPONENTCODE) {
				parameter.setComponentCode(component);
			} else {
				DataValue fieldValue = new DataValueImpl(type);
				fieldValue.setCode(code);
				fieldValue.setValue(DataValueImpl.convertValueFromString(value,
						title, type));
				parameter.setData(fieldValue);
				parameter.setCode(code);
			}

			eventResult.setParameter(index, parameter);
		}

		return eventResult;
	}

	@Override
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	@Override
	public String getRawValue() {
		return rawValue;
	}

	@Override
	public void setRawValue(String value) {
		this.rawValue = value;
	}
}
