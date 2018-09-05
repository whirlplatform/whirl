package org.whirlplatform.editor.client.view.design;

import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

import java.util.Arrays;
import java.util.List;

public interface Designer {

	void addPropertyChangeListener(PropertyChangeListener listener);

	void addComponentPropertyChangeListener(ComponentPropertyChangeListener listener);

	void addAddComponentListener(AddComponentListener listener);

	void addRemoveComponentListener(RemoveComponentListener listener);

	void addSelectComponentListener(SelectComponentListener listener);

	void addSelectAreaListener(SelectAreaListener listener);

    class PropertyChangeEvent {

		private ComponentElement element;
		private Object data;

		PropertyChangeEvent(ComponentElement element, Object data) {
			this.element = element;
			this.data = data;
		}

		public ComponentElement getElement() {
			return element;
		}

		public Object getData() {
			return data;
		}

	}

    interface PropertyChangeListener {

		void onPropertyChange(PropertyChangeEvent event);

	}

    class ComponentPropertyChangeEvent {

		private ComponentElement element;
		private PropertyType type;
		private PropertyValue value;

		ComponentPropertyChangeEvent(ComponentElement element, PropertyType type, PropertyValue value) {
			this.element = element;
			this.type = type;
			this.value = value;
		}

		public ComponentElement getElement() {
			return element;
		}

		public PropertyType getType() {
			return type;
		}

		public PropertyValue getValue() {
			return value;
		}

	}

    interface ComponentPropertyChangeListener {

		void onComponentPropertyChange(ComponentPropertyChangeEvent event);

	}

    class AddComponentEvent {

		private ComponentElement parentElement;
		private ComponentElement element;

		AddComponentEvent(ComponentElement parentElement, ComponentElement element) {
			this.parentElement = parentElement;
			this.element = element;
		}

		public ComponentElement getParentElement() {
			return parentElement;
		}

		public ComponentElement getElement() {
			return element;
		}

	}

    interface AddComponentListener {

		void onAddComponentListener(AddComponentEvent event);

	}

    class RemoveComponentEvent {

		private ComponentElement parentElement;
		private ComponentElement element;

		RemoveComponentEvent(ComponentElement parentElement, ComponentElement element) {
			this.parentElement = parentElement;
			this.element = element;
		}

		public ComponentElement getParentElement() {
			return parentElement;
		}

		public ComponentElement getElement() {
			return element;
		}

	}

    interface RemoveComponentListener {

		void onRemoveComponentListener(RemoveComponentEvent event);

	}

    class SelectComponentEvent {

		private List<ComponentElement> elements;

		SelectComponentEvent(List<ComponentElement> elements) {
			this.elements = elements;
		}

		SelectComponentEvent(ComponentElement... elements) {
			this(Arrays.asList(elements));
		}

		public List<ComponentElement> getComponentElements() {
			return elements;
		}

	}

    interface SelectComponentListener {

		void onSelectComponentListener(SelectComponentEvent event);

	}

    class SelectAreaEvent {

		private Object data;

		SelectAreaEvent(Object data) {
			this.data = data;
		}

		public Object getData() {
			return data;
		}

	}

    interface SelectAreaListener {

		void onSelectAreaListener(SelectAreaEvent event);

	}
}
