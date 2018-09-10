package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

/**
 * Текстовая строка вывода, надпись
 *
 */
public class LabelBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers, DoubleClickEvent.HasDoubleClickHandlers {

	private String bgColor;
	protected boolean hasEvent;

	private Label field;
	private WidgetComponent wrapper;

	public LabelBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public LabelBuilder() {
		super();
	}

	/**
	 * Получить тип текста
	 */
	@Override
	public ComponentType getType() {
		return ComponentType.LabelType;
	}

	/**
	 * Создание компонента - текст
	 * 
	 * @return Component, Созданный текст
	 */
	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		field = new Label();
		wrapper = new WidgetComponent(field);
		wrapper.getElement().applyStyles("fontSize: 12px");

		// Иначе событие при клике в редакторе не обрабатывается
		field.sinkEvents(Event.getTypeInt(com.google.gwt.event.dom.client.ClickEvent.getType().getName()));
		field.sinkEvents(Event.getTypeInt(com.google.gwt.event.dom.client.DragStartEvent.getType().getName()));
		field.sinkEvents(Event.getTypeInt(com.google.gwt.event.dom.client.DragEndEvent.getType().getName()));
		field.sinkEvents(Event.getTypeInt(com.google.gwt.event.dom.client.DropEvent.getType().getName()));
		return wrapper;
	}

	/**
	 * Инициализация обработчиков текста
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();
		wrapper.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent e) {
				if (e.isAttached()) {
					updateBgColor((WidgetComponent) e.getSource());
				}
			}
		});
		field.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (isEnabled()) {
					fireEvent(new ClickEvent());
				}
			}
		});
		field.addDoubleClickHandler(new com.google.gwt.event.dom.client.DoubleClickHandler() {

			@Override
			public void onDoubleClick(com.google.gwt.event.dom.client.DoubleClickEvent event) {
				if (isEnabled()) {
					fireEvent(new DoubleClickEvent());
				}
			}
		});
	}

	/**
	 * Обновление фона под текстом
	 * 
	 * @param c
	 *            - Component
	 */
	private void updateBgColor(Component c) {
		if (c.getParent() != null)
			if (bgColor != null)
				c.getElement().getParentElement().getStyle().setBackgroundColor(bgColor);
	}

	/**
	 * Установка атрибута для текста
	 * 
	 * @param name
	 *            - String, название атрибута
	 * @param value
	 *            - String, значение атрибута
	 * @return boolean
	 */
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Html.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				setHtml(value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.FontFamily.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				wrapper.getElement().applyStyles("fontFamily:" + value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.FontSize.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				wrapper.getElement().applyStyles("fontSize:" + value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.FontStyle.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				wrapper.getElement().applyStyles("fontStyle:" + value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.FontWeight.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				wrapper.getElement().applyStyles("fontWeight:" + value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Color.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				wrapper.getElement().applyStyles("color:" + value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.BackgroundColor.getCode())) {
			if (value != null) {
				bgColor = value.getString();
				updateBgColor(wrapper);
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.TextDecoration.getCode())) {
			if (value != null && value.getBoolean() != null) {
				Boolean textDecor = value.getBoolean();
				if (textDecor) {
					wrapper.getElement().applyStyles("textDecoration:" + textDecor);
				}
			}
			return true;
		}
		return super.setProperty(name, value);
	}

	/**
	 * Установка текста
	 * 
	 * @param value
	 *            - String, текст
	 */
	public void setHtml(String value) {
		field.setText(value == null ? "" : value);
	}

	/**
	 * Получение текста
	 * 
	 * @return String
	 */
	public String getHtml() {
		return field.getText();
	}

	/**
	 * Почение сущности текста
	 * 
	 * @return (C) wrapper
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected <C> C getRealComponent() {
		return (C) wrapper;
	}

	/**
	 * Добавление обработчика двойного нажатия левой кнопкой мыши
	 * 
	 * @param handler
	 *            - DoubleClickHandler
	 * @return HandlerRegistration
	 */
	@Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickEvent.DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}

	/**
	 * Добавление обработчика нажатия левой кнопкой мыши
	 * 
	 * @param handler
	 *            - ClickHandler
	 * @return HandlerRegistration
	 */
	@Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
		field.addStyleName("xs-link-text");
		return addHandler(handler, ClickEvent.getType());
	}

	// TODO Selenium

	private static class LocatorParams {
		private static String TYPE_TEXT = "Text";
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		Locator result = super.getLocatorByElement(element);
		if (result != null) {
			if (field.getElement().isOrHasChild(element)) {
				result.setPart(new Locator(LabelBuilder.LocatorParams.TYPE_TEXT));
			}
		}
		return result;
	}

	@Override
	public Element getElementByLocator(Locator locator) {
		if (!super.fitsLocator(locator)) {
			return null;
		}
		Element element = null;
		Locator part = locator.getPart();
		if (part != null) {
			if (LocatorParams.TYPE_TEXT.equals(part.getType())) {
				element = field.getElement();
			}
		}
		return element;
	}
}
