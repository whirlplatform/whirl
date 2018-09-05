package org.whirlplatform.component.client.base;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

/**
 * HTML компонент
 */
public class HtmlBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers, DoubleClickEvent.HasDoubleClickHandlers {

	private HTML html;

	private WidgetComponent wrapper;

	public HtmlBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public HtmlBuilder() {
		super();
	}
	
	/**
	 * Получить тип HtmlBuilder
	 * @return ComponentType
	 */
	@Override
	public ComponentType getType() {
		return ComponentType.HtmlType;
	}

	/**
	 * Создание компонента - HtmlBuilder
	 * @return Component
	 */
	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		html = new HTML();
		wrapper = new WidgetComponent(html);
		wrapper.getElement().applyStyles("fontSize: 12px");

		// Иначе событие при клике в редакторе не обрабатывается
		html.sinkEvents(Event
				.getTypeInt(com.google.gwt.event.dom.client.ClickEvent
						.getType().getName()));
		html.sinkEvents(Event
				.getTypeInt(com.google.gwt.event.dom.client.DragStartEvent
						.getType().getName()));
		html.sinkEvents(Event
				.getTypeInt(com.google.gwt.event.dom.client.DragEndEvent
						.getType().getName()));
		html.sinkEvents(Event
				.getTypeInt(com.google.gwt.event.dom.client.DropEvent.getType()
						.getName()));
		return wrapper;
	}

	/**
	 * Установка атрибута для HtmlBuilder
	 * @param name - String, название атрибута
	 * @param value - String, значение атрибута
	 * @return boolean
	 */
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Html.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				setHTML(value.getString());
			}
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.TextDecoration.getCode())) {
			if (value != null && !Util.isEmptyString(value.getString())) {
				html.getElement().<XElement> cast()
						.applyStyles("textDecoration:" + value.getString());
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
		}
		return super.setProperty(name, value);
	}

	/**
	 * Установка необходимого контента в HtmlBuilder
	 * @param content - String, контент
	 */
	public void setHTML(String content) {
		html.setHTML(content);
	}
	
	/**
	 * Получение контента из HtmlBuilder
	 * @return String
	 */
	public String getHTML() {
		return html.getHTML();
	}

	/**
	 * Получение сущности HtmlBuilder
	 * @return (С) wrapper
	 */
	@Override
	protected <C> C getRealComponent() {
		return (C) wrapper;
	}
	
	/**
	 * Добавить обработчик нажатия на HtmlBuilder
	 * @param handler - ClickHandler
	 * @return HandlerRegistration
	 */
	@Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
		html.addStyleName("xs-link-text");
		return ensureHandler().addHandler(ClickEvent.getType(), handler);
	}

	/**
	 * Добавить обработчик двойного нажатия на HtmlBuilder
	 * @param handler - DoubleClickHandler
	 * @return HandlerRegistration
	 */
	@Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickEvent.DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}
	
	/**
	 * Инициализация обработчиков HtmlBuilder
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();
		html.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (isEnabled()) {
					fireEvent(new ClickEvent());
				}
			}
		});
		html.addDoubleClickHandler(new com.google.gwt.event.dom.client.DoubleClickHandler() {

			@Override
			public void onDoubleClick(
					com.google.gwt.event.dom.client.DoubleClickEvent event) {
				if (isEnabled()) {
					fireEvent(new DoubleClickEvent());
				}
			}
		});
	}
}