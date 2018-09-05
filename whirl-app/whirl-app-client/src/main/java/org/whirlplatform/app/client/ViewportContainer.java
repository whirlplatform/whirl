package org.whirlplatform.app.client;

import com.sencha.gxt.widget.core.client.container.Viewport;
import org.timepedia.exporter.client.Export;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;

public class ViewportContainer implements Containable {

	private Viewport viewport;
	private ComponentBuilder topComponent;

	public ViewportContainer(Viewport viewport) {
		this.viewport = viewport;
	}

	@Override
	public void addChild(ComponentBuilder child) {
		viewport.setWidget(child.getComponent());
		topComponent = child;
	}

	@Override
	public void removeChild(ComponentBuilder child) {
		if (viewport.remove(child.getComponent())) {
			topComponent = null;
		}

	}

	@Override
	public void clearContainer() {
		viewport.clear();
		topComponent = null;
	}

	@Override
	public void forceLayout() {
		viewport.forceLayout();
	}

	@Override
	public ComponentBuilder[] getChildren() {
		if (topComponent != null) {
			ComponentBuilder[] result = { topComponent };
			return result;
		} else {
			return new ComponentBuilder[0];
		}
	}

	@Export
	public int getChildrenCount() {
		if (topComponent == null) {
			return 0;
		}
		return 1;
	}
	
}
