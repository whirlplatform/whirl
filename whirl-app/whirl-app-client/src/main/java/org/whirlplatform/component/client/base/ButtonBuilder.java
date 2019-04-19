package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

/**
 * Button.
 */
@JsType(namespace = "Whirl", name = "Button")
public class ButtonBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers {

	private TextButton button;

	public ButtonBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public ButtonBuilder() {
		super();
	}

	/**
	 * Получить тип кнопки
	 * 
	 * @return ComponentType
	 */
	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.ButtonType;
	}

	/**
	 * Создание компонента - кнопка
	 * 
	 * @return Component, Созданная кнопка
	 */
	protected Component init(Map<String, DataValue> builderProperties) {
		button = new TextButton();
		return button;
	}

	/**
	 * Инициализация обработчиков у кнопки
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();
		button.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				fireEvent(new ClickEvent());
			}

		});
	}

	/**
	 * Установка атрибутов кнопки<br>
	 * Если название = "Html", то присваивается надпись на кнопку
	 * 
	 * @param name
	 *            - String, название атрибута
	 * @param value
	 *            - String, значение атрибута
	 * @return boolean
	 */
	@JsIgnore
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Html.getCode())) {
			if (value != null) {
				setHTML(value.getString());
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	/**
	 * Получение сущности кнопки
	 * 
	 * @return (C) button
	 */
	@Override
	protected <C> C getRealComponent() {
		return (C) button;
	}

	/**
	 * Добавление обработчика нажатие кнопки мыши
	 * 
	 * @param handler
	 *            - ClickHandler
	 * @return HandlerRegistration
	 */
	@JsIgnore
	@Override
	public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
		return ensureHandler().addHandler(ClickEvent.getType(), handler);
	}

	/**
	 * Returns html button title.
	 *
	 * @return title
	 */
	public String getHTML() {
		return button.getHTML();
	}

	/**
	 * Sets html button title.
	 *
	 * @param html title
	 */
	public void setHTML(String html) {
		button.setHTML(html == null ? "" : html);
	}

	@JsIgnore
	@Override
	public Element getElementByLocator(Locator locator) {
		if (locator.getType().equals(getType().getType()) && fitsLocator(locator)) {
			return button.getCell().getFocusElement(button.getElement());
		}
		return null;
	}

}
