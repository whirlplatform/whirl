package org.whirlplatform.component.client.window;

import com.sencha.gxt.widget.core.client.Component;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

public class TaskBarBuilder extends ComponentBuilder {

	private TaskBar taskBar;

	public TaskBarBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public TaskBarBuilder() {
		super();
	}
	
	
	@Override
	public ComponentType getType() {
		return ComponentType.TaskBarType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		taskBar = new TaskBar();
		return taskBar;
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) taskBar;
	}

}
