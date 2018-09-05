package org.whirlplatform.component.client.base;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
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
 * Простая картинка 
 */
public class ImageBuilder extends ComponentBuilder implements ClickEvent.HasClickHandlers, DoubleClickEvent.HasDoubleClickHandlers {

	private Image image;

	private WidgetComponent wrapper;

	private String url;

	public ImageBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public ImageBuilder() {
		super();
	}
	

	/**
	 * Получить тип картинки
	 */
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
}
