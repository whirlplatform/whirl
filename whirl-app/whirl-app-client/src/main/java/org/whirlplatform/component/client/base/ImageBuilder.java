package org.whirlplatform.component.client.base;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/** 
 * Простая картинка 
 */
@JsType(name = "Image", namespace = "Whirl")
public class ImageBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers, DoubleClickEvent.HasDoubleClickHandlers {

	private Image image;

	private WidgetComponent wrapper;

	private String url;

	@JsConstructor
	public ImageBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public ImageBuilder() {
		this(Collections.emptyMap());
	}
	

	/**
	 * Получить тип картинки
	 */
	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.ImageType;
	}

	/**
	 * Создание компонента - картинка
	 * @return Component, Созданная картинка
	 */
	protected Component init(Map<String, DataValue> builderProperties) {
		image = new Image();
		wrapper = new WidgetComponent(image);
		return wrapper;
	}

	/**
	 * Установка атрибута для картинки
	 * @param name - String, название атрибута
	 * @param value - String, значение атрибута
	 * @return boolean
	 */
	@JsIgnore
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Url.getCode())) {
			if(value != null){
				String urlVal = value.getString();
				url = urlVal;
				image.setUrl(urlVal);
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	/**
	 * Установка картинки по умолчанию
	 * @param url - String, ссылка
	 */
	public void setDefaultImage(String url) {
		if (this.url == null || url.isEmpty()) {
			image.setUrl(url);
		}
	}

	/**
	 * Получение сущности картинки
	 * @return (C) image
	 */
	@Override
	protected <C> C getRealComponent() {
		return (C) image;
	}

	/**
	 * Добавить обработчика по нажатию кнопки мыши
	 * @param handler - ClickHandler
	 * @return HandlerRegistration
	 */
	@JsIgnore
	@Override
    public HandlerRegistration addClickHandler(ClickEvent.ClickHandler handler) {
		image.addStyleName("xs-link");
		return ensureHandler().addHandler(ClickEvent.getType(), handler);
	}

	/**
	 * Добавить обработчика по двойному нажатию кнопки мыши
	 * @param handler - DoubleClickHandler
	 * @return HandlerRegistration
	 */
	@JsIgnore
	@Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickEvent.DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}
	
	/**
	 * Инициализация обработчиков картинки
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();
		image.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (isEnabled()) {
					fireEvent(new ClickEvent());
				}
			}
		});
		image.addDoubleClickHandler(new com.google.gwt.event.dom.client.DoubleClickHandler() {

			@Override
			public void onDoubleClick(
					com.google.gwt.event.dom.client.DoubleClickEvent event) {
				if (isEnabled()) {
					fireEvent(new DoubleClickEvent());
				}
			}
		});
	}

	/**
	 * Returns component's code.
	 *
	 * @return component's code
	 */
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * Checks if component is in hidden state.
	 *
	 * @return true if component is hidden
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Sets component's hidden state.
	 *
	 * @param hidden true - to hide component, false - to show component
	 */
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Focuses component.
	 */
	@Override
	public void focus() {
		super.focus();
	}

	/**
	 * Checks if component is enabled.
	 *
	 * @return true if component is enabled
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets component's enabled state.
	 *
	 * @param enabled true - to enable component, false - to disable component
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
}
