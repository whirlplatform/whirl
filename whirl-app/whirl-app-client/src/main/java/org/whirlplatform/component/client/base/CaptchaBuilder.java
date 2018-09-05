package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Validatable;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.Map;

public class CaptchaBuilder extends AbstractFieldBuilder implements Validatable {

	private static final int CAPTCHA_LENGTH = 6;
	public static final String CAPTCHA_KEY = "cid";
	public static final String KEY_CAPTCHA_CORRECT = "captcha_correct";

	private VerticalLayoutContainer container;
	private SimpleContainer space;
	private Image captchaImg;
	private TextButton refreshButton;
	private TextField captchaInput;
	private String key;
	private int imageWidth;
	private int imageHeight;
	private boolean valid;

	public CaptchaBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public CaptchaBuilder() {
		super();
	}

	@Override
	public ComponentType getType() {
		return ComponentType.CaptchaType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		required = false;
		imageWidth = 120;
		imageHeight = 50;
		valid = false;

		container = new VerticalLayoutContainer();
		space = new SimpleContainer();
		addControls();
		return container;
	}

	private void addControls() {
		HorizontalLayoutContainer bottom = new HorizontalLayoutContainer();
		captchaImg = new Image();

		refreshButton = new TextButton();
		refreshButton.setIcon(ApplicationBundle.INSTANCE.refresh());
		refreshButton.setTitle(AppMessage.Util.MESSAGE.refresh());
		refreshButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				captchaClear();
			}
		});

		captchaInput = new TextField();
		captchaInput.setErrorSupport(new SideErrorHandler(captchaInput));
		captchaInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				onPutChar(captchaInput.getText());
			}
		});

		captchaInput.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				onPutChar(captchaInput.getText());
			}
		});

		captchaInput.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				clearInvalid();
			}
		});

		MarginData refreshData = new MarginData(new Margins(0, 5, 0, 5));
		refreshButton.setLayoutData(refreshData);

		HorizontalLayoutData inputData = new HorizontalLayoutData(1, 1);

		container.addResizeHandler(new ResizeHandler() {
			private boolean first = true;

			// Не получится устанавливать размеры при создании компонента,
			// т.к. размеры контейнера еще не известны.
			@Override
			public void onResize(ResizeEvent arg0) {
				if (first) {
					imageHeight = container.getOffsetHeight(true) - 30;
					imageWidth = (int) (120 * (imageHeight / 50.0f));
					updateDimensions();
					captchaClear();
					first = false;
				} else {
					space.setHeight(container.getOffsetHeight(true) - imageHeight - 30);
				}
				container.forceLayout();

			}
		});

		bottom.add(refreshButton);
		bottom.add(captchaInput, inputData);
		container.add(captchaImg);
		container.add(space);
		container.add(bottom);
	}

	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.Width.getCode())) {
			if (value != null && value.getInteger() != null) {
				int width = value.getInteger();
				container.setWidth(width);
				imageWidth = width;
				updateDimensions();
				return true;
			}
		} else if (name.equalsIgnoreCase(PropertyType.Height.getCode())) {
			if (value != null && value.getInteger() != null) {
				int height = value.getInteger();
				container.setHeight(height);
				imageHeight = height - captchaInput.getOffsetHeight(false);
				updateDimensions();
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	@Override
	protected void initHandlers() {
		super.initHandlers();

	}

	private void updateDimensions() {
		// установить размеры картинки
		if (captchaImg != null) {
			captchaImg.setVisibleRect(0, 0, imageWidth, imageHeight);
		}
	}

	private void onPutChar(String value) {
		value = value != null ? value.trim() : "";

		if (value.length() == CAPTCHA_LENGTH) {
			checkCaptcha(value);
		} else {
			markInvalid();
		}
	}

	private String getCaptchaImageURL(boolean randomize) {
		String url = "";

		if (key == null) {
			key = String.valueOf(Math.round((Math.random() * 10000)));
		}
		// отправить на сервер идентификатор капчи и введенное значение
		url = GWT.getHostPageBaseURL() + "captcha.png?" + CAPTCHA_KEY + "=" + key + getDimensionsURLPart() + "&l="
				+ CAPTCHA_LENGTH;
		// Браузер, похоже, кэширует картинки с одинаковыми адресами,
		// предотвратить это
		if (randomize) {
			String randomPart = "&t=" + String.valueOf(Math.round((Math.random() * 100000)));
			url += randomPart;
		}
		return url;
	}

	private void checkCaptcha(String value) {
		container.setData(KEY_CAPTCHA_CORRECT, null);
		// отправить на сервер введенное значение, получить статус проверки
		DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				verify(result);
			}

			public void onFailure(Throwable caught) {
				InfoHelper.throwInfo("check-captcha", caught);
			}
		}).checkCaptchaCode(SessionToken.get(), value, key);
	}

	private void verify(Boolean result) {
		// обработать результат проверки капчи
		if (result) {
			clearInvalid();
		} else {
			markInvalid();
		}
	}

	private void captchaClear() {
		// перерисовать картинку капчи
		captchaInput.setValue("");
		captchaImg.setUrlAndVisibleRect(getCaptchaImageURL(true), 0, 0, imageWidth, imageHeight);
		container.forceLayout();
	}

	/**
	 * Пометить компонент как непрошедший проверку
	 */
	private void markInvalid() {
		captchaInput.forceInvalid(AppMessage.Util.MESSAGE.errorCaptchaVerification());
	}

	/**
	 * Пометить компонент как прошедший проверку
	 */
	public void clearInvalid() {
		captchaInput.clearInvalid();
	}

	protected String getDimensionsURLPart() {
		return "&w=" + imageWidth + "&h=" + imageHeight;
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) container;
	}

	@Override
	public boolean isValid() {
		return isValid(false);
	}

	@Override
	public boolean isValid(boolean invalidate) {
		if (!valid && invalidate) {
			markInvalid();
		}
		return valid;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

}
