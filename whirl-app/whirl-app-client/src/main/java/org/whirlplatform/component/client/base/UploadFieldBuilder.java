package org.whirlplatform.component.client.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.Prepareable;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.NativeParameter;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.Collections;
import java.util.Map;

/**
 * Поле загрузки файла.
 */
@JsType(name = "UploadField", namespace = "Whirl")
public class UploadFieldBuilder extends AbstractFieldBuilder implements
		Prepareable, NativeParameter<String>, Parameter<DataValue> {

	private FileUploadField upload;

	private int fileId = Random.nextInt();

	private FormPanel form;

	private boolean ready = false;

	private Command readyCommand;

	private boolean saveFilename = false;

	private String fileTmpValue;

	private AdapterField<String> adapter;

	@JsConstructor
	public UploadFieldBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public UploadFieldBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.UploadFieldType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		required = false;
		
		fileId = Random.nextInt();
		ready = false;
		saveFilename = false;
		
		upload = new FileUploadField();
		upload.setName(AppConstant.TABLE);
		form = new FormPanel();
		form.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		form.setEncoding(FormPanel.Encoding.MULTIPART);
		form.setMethod(FormPanel.Method.POST);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults() != null
						&& event.getResults().contains("OK")) {
					ready = true;
					readyCommand.execute();
				} else {
					InfoHelper.error("file-upload", AppMessage.Util.MESSAGE.error(),
							AppMessage.Util.MESSAGE.file_notUploaded() + ": "
									+ upload.getValue());
				}
			}
		});

		form.setWidget(upload);
		adapter = new AdapterField<String>(form) {

			@Override
			public String getValue() {
				if (upload.getValue() != null) {
					return upload.getValue();
				}
				return fileTmpValue;
			}

			@Override
			public void setValue(String value) {
				fileTmpValue = value;
				upload.getElement().child("input[type=\"text\"]")
						.setPropertyString("value", value);
			}
		};
		return adapter;
	}

	@JsIgnore
	@Override
	public Component create() {
		Component comp = super.create();
		form.setAction(GWT.getHostPageBaseURL() + "file?"
				+ AppConstant.GETTYPE + "=" + AppConstant.FORM_UPLOAD+ "&"
				+ AppConstant.TABLE + "=" + fileId + "&"
				+ AppConstant.SAVE_FILE_NAME + "="
				+ Boolean.valueOf(saveFilename) + "&" + AppConstant.TOKEN_ID
				+ "=" + SessionToken.get().getTokenId());
		return comp;
	}

	@JsIgnore
	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.SaveFileName.getCode())) {
			if (value != null && value.getBoolean() != null) {
				saveFilename = value.getBoolean();
			}
			return true;
		}
		return super.setProperty(name, value);
	}
	
	public void submit() {
		form.submit();
	}

	/**
	 * Получить значение текстового поля
	 *
	 * @return String
	 */
	@Override
	public String getValue() {
		return fileId + "&" + Boolean.valueOf(saveFilename);
	}

	@JsIgnore
	public FileValue getFileValue() {
		FileValue file = new FileValue();
		file.setName(adapter.getValue());
		file.setTempId(String.valueOf(fileId));
		return file;
	}

	@JsIgnore
	public void setFileValue(FileValue value) {
		adapter.setValue(value.getName());
		isValid(true);
	}

	/**
	 * Установка значения текстового поля
	 *
	 * @param value String
	 */
	@Override
	public void setValue(String value) {
		// do nothing
	}

	@JsIgnore
	@Override
	public void prepair(Command complete) {
		readyCommand = complete;
		ready = false;
		submit();
	}

	/**
	 * Проверяет готовность компонента к выполнению события.
	 *
	 * @return true, если готов
	 */
	@Override
	public boolean isReady() {
		return ready;
	}

	/**
	 * Очищает значение поля.
	 */
	@Override
	public void clear() {
		upload.clear();
		ValueChangeEvent.fire(upload, null);
	}
	
	@Override
	protected <C> C getRealComponent() {
		return (C) upload;
	}

	/**
	 * Устанавливает значение только для чтения.
	 *
	 * @param readOnly true, если поле доступно только для чтения
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		upload.setReadOnly(readOnly);
	}

	/**
	 * Очищает статус недействительности для поля.
	 */
	@Override
	public void clearInvalid() {
		upload.clearInvalid();
		adapter.clearInvalid();
	}

	/**
	 * Проверяет, является ли поле валидным.
	 *
	 * @param invalidate true для не валидного поля
	 * @return true если поле доступно
	 */
	@Override
	public boolean isValid(boolean invalidate) {
		if (!upload.isValid(false)) {
			return false;
		}
		if (required) {
			String value = upload.getValue();
			if (value == null || value.isEmpty()) {
				if (invalidate) {
					adapter.markInvalid(AppMessage.Util.MESSAGE.requiredField());
				}
				return false;
			}
		}
		if (invalidate) {
			clearInvalid();
		}
		return true;
	}

	@JsIgnore
	@Override
	public DataValue getFieldValue() {
		DataValue result = new DataValueImpl(DataType.FILE);
		result.setCode(getCode());
		result.setValue(getFileValue());
		return result;
	}

	@JsIgnore
	@Override
	public void setFieldValue(DataValue value) {
		// do nothing
	}
	
	@Override
	protected void initHandlers() {
		super.initHandlers();
		upload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				isValid(true);
                fireEvent(new org.whirlplatform.component.client.event.ChangeEvent());
			}
		});
	}

	/**
	 * Получает имя файла.
	 *
	 * @return String - имя файла
	 */
	public String getFileName() {
		return adapter.getValue();
	}

	/**
	 * Устанавливает включенное состояние компонента.
	 *
	 * @param enabled true - для включения компонента, false - для отключения компонента
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		upload.setEnabled(enabled);
	}

	/**
	 * Получает маску поля.
	 *
	 * @return маска поля
	 */
	@Override
	public String getFieldMask() {
		return super.getFieldMask();
	}

	/**
	 * Устанавливает маску поля.
	 *
	 * @param mask новая маска поля
	 */
	@Override
	public void setFieldMask(String mask) {
		super.setFieldMask(mask);
	}

	/**
	 * Устанавливает статус недействительности для поля с заданным текстом.
	 *
	 * @param msg сообщение
	 */
	@Override
	public void markInvalid(String msg) {
		super.markInvalid(msg);
	}

	/**
	 * Проверяет, обязательно ли поле для заполнения.
	 *
	 * @return true, если обязательно
	 */
	@Override
	public boolean isRequired() {
		return super.isRequired();
	}

	/**
	 * Устанавливает обязательность для заполнения поля.
	 *
	 * @param required true, если поле обязательно для заполнения
	 */
	@Override
	public void setRequired(boolean required) {
		super.setRequired(required);
	}


}
