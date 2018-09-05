package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDesigner extends FlowLayoutContainer implements Designer {

	private Set<PropertyChangeListener> propertyChangeListeners = new HashSet<>();
	private Set<ComponentPropertyChangeListener> componentPropertyChangeListeners = new HashSet<>();
	private Set<AddComponentListener> addComponentListeners = new HashSet<>();
	private Set<RemoveComponentListener> removeComponentListeners = new HashSet<>();
	private Set<SelectComponentListener> selectComponentListeners = new HashSet<>();
	private Set<SelectAreaListener> selectAreaListeners = new HashSet<>();

	protected LocaleElement defaultLocale;
	protected ComponentElement rootElement;

	public AbstractDesigner(LocaleElement defaultLocale, ComponentElement element) {
		this.defaultLocale = defaultLocale;
		this.rootElement = element;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		propertyChangeListeners.add(listener);
	}

	protected void firePropertyChangeEvent(PropertyChangeEvent event) {
		for (PropertyChangeListener l : propertyChangeListeners) {
			l.onPropertyChange(event);
		}
	}

	@Override
	public void addComponentPropertyChangeListener(ComponentPropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		componentPropertyChangeListeners.add(listener);
	}

	protected void fireComponentPropertyChangeEvent(ComponentPropertyChangeEvent event) {
		for (ComponentPropertyChangeListener l : componentPropertyChangeListeners) {
			l.onComponentPropertyChange(event);
		}
	}

	@Override
	public void addAddComponentListener(AddComponentListener listener) {
		if (listener == null) {
			return;
		}
		addComponentListeners.add(listener);
	}

	protected void fireAddComponentEvent(AddComponentEvent event) {
		for (AddComponentListener l : addComponentListeners) {
			l.onAddComponentListener(event);
		}
	}

	@Override
	public void addRemoveComponentListener(RemoveComponentListener listener) {
		if (listener == null) {
			return;
		}
		removeComponentListeners.add(listener);
	}

	protected void fireRemoveComponentEvent(RemoveComponentEvent event) {
		for (RemoveComponentListener l : removeComponentListeners) {
			l.onRemoveComponentListener(event);
		}
	}

	@Override
	public void addSelectComponentListener(SelectComponentListener listener) {
		if (listener == null) {
			return;
		}
		selectComponentListeners.add(listener);
	}

	protected void fireSelectComponentEvent(SelectComponentEvent event) {
		for (SelectComponentListener l : selectComponentListeners) {
			l.onSelectComponentListener(event);
		}
	}

	@Override
	public void addSelectAreaListener(SelectAreaListener listener) {
		if (listener == null) {
			return;
		}
		selectAreaListeners.add(listener);
	}

	protected void fireSelectAreaEvent(SelectAreaEvent event) {
		for (SelectAreaListener l : selectAreaListeners) {
			l.onSelectAreaListener(event);
		}
	}
}
