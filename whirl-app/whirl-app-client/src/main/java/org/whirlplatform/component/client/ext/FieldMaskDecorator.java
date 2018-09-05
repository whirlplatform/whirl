package org.whirlplatform.component.client.ext;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import org.whirlplatform.meta.shared.AppConstant;

import java.text.ParseException;
import java.util.*;

@SuppressWarnings({ "rawtypes" })
public class FieldMaskDecorator<T> extends AbstractValidator<T> {

	private ValueBaseField<T> field;
	private String mask;
	private List<String> expression = new ArrayList<String>();
	private String baseContent = "";
	private String content = "";
	private String validContent = "";
	private Set<HandlerRegistration> handlerRegistrations = new HashSet<HandlerRegistration>();
	private RegExp compiledRegExp;
	
	private boolean includeMask = false;

	public FieldMaskDecorator(ValueBaseField<T> field, String mask) {
		this.field = field;
		this.mask = mask;
		
		// Если значение уже было назначено, сохраняем..
		String tmpText = null;
		if (field.getText() != null) {
			tmpText = field.getText();
		}

		transformMask();
		initHandlers();
		
		// .. и после установки маски устанавливаем значение
		if (tmpText != null) {
			setValue(tmpText);
		}
		
		if (field.getText().equals(baseContent)) {
			field.setText("");
		}

		String regex = makeRegex(mask);
		if (regex != null) {
			compiledRegExp = RegExp.compile(regex);
		}
	}
	
	public void setIncludeMask(boolean includeMask) {
		this.includeMask = includeMask;
	}

	public String getMask() {
		return mask;
	}
	
	public void remove() {
		Iterator<HandlerRegistration> iter = handlerRegistrations.iterator();
		while (iter.hasNext()) {
			HandlerRegistration r = iter.next();
			r.removeHandler();
			iter.remove();
		}
	}

	private void initHandlers() {
		handlerRegistrations.add(field.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				int cursor = field.getCursorPos();
				int indexToDelete = -1;
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_BACKSPACE
						|| keyCode == KeyCodes.KEY_DELETE) {
					indexToDelete = cursor;
				}
				if (indexToDelete != -1) {
					int index;
					if (keyCode == KeyCodes.KEY_DELETE) {
						index = validIndex(indexToDelete) + 1;
						if (index > validContent.length()) {
							event.preventDefault();
							return;
						}
					} else {
						index = validIndex(indexToDelete);
						if (index > validContent.length()) {
							index = validContent.length();
							cursor = lastValidPos();
						}
						if (index == 0) {
							event.preventDefault();
							return;
						}
					}
					validContent = deleteCharAt(validContent, index);
					format();
					if (keyCode == KeyCodes.KEY_BACKSPACE) {
						cursor = prevValidPos(cursor);
					}
					if (cursor >= 0) {
						field.setCursorPos(cursor);
					}
					event.preventDefault();
				}
			}

		}));
		handlerRegistrations.add(field
				.addKeyPressHandler(new KeyPressHandler() {
					@Override
					public void onKeyPress(KeyPressEvent event) {
						int cursor = field.getCursorPos();
						Integer nativeCode = event.getNativeEvent()
								.getKeyCode();
						if (nativeCode == KeyCodes.KEY_TAB
								|| nativeCode == KeyCodes.KEY_LEFT
								|| nativeCode == KeyCodes.KEY_RIGHT
								|| nativeCode == KeyCodes.KEY_UP
								|| nativeCode == KeyCodes.KEY_DOWN) {
							return;
						}

						int index = validIndex(cursor);
						if (index > validContent.length()) {
							index = validContent.length();
							cursor = lastValidPos();
						}

						if (addCharCode(event.getCharCode(), index)) {
							field.setCursorPos(nextValidPos(cursor) + 1);

						}
						event.preventDefault();
					}
				}));
		handlerRegistrations.add(field.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if ("".equals(field.getText())) {
					setValue(baseContent);
				} else {
					setValue(field.getText());
				}
			}

		}));
		handlerRegistrations.add(field.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (validContent.length() == 0) {
					// Если дату выбрали из списка
					if (field.getText() != null && field instanceof DateField) {
						setValue(field.getText());
					}
					field.setText("");
				}

				// Обход проблемы из-за стирания типов
				T preparedValue = null;
				try {
					preparedValue = field.getPropertyEditor().parse(content);
				} catch (ParseException e) {
//					e.printStackTrace();
				}
				if (preparedValue instanceof String) {
					if (includeMask) {
						field.setValue((T) content);
					} else {
						field.setValue((T) validContent);
						field.setText(content);
					}
				} else {
					field.setValue(preparedValue);
				}
			}
		}));
		handlerRegistrations.add(field.addValueChangeHandler(new ValueChangeHandler<T>() {
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				setValue(field.getText());
			}
		}));
	}

	private int prevValidPos(int cursor) {
		int index = cursor - 1;
		while (0 < index && baseContent.toCharArray()[index] != '_') {
			index = index - 1;
		}
		return index;
	}

	private int nextValidPos(int cursor) {
		int index = cursor;
		while (baseContent.length() > index
				&& baseContent.toCharArray()[index] != '_') {
			index = index + 1;
		}
		if (index == baseContent.length()) {
			index = index - 1;
		}
		return index;
	}

	private int validIndex(int pos) {
		int index = 0;
		for (int i = 0; i < content.toCharArray().length; i++) {
			if (i == pos) {
				break;
			}
			Character ch = content.toCharArray()[i];
			switch (ch) {
			case '/':
			case ',':
			case '*':
			case '.':
			case '-':
			case ':':
			case '(':
			case ')':
			case '{':
			case '}':
			case '=':
			case '+':
			case ' ':
				break;
			default:
				index++;
				break;
			}

		}
		return index;
	}

	private String deleteCharAt(String string, int index) {
		if (string.isEmpty()) {
			return string;
		}
		return string.substring(0, index - 1)
				+ string.substring(index, string.length());
	}

	private String putCharAt(String string, int index, Character ch) {
		if (index < 1) {
			return ch.toString() + string;
		} else if (index == string.length()) {
			return string + ch.toString();
		}
		return string.substring(0, index) + ch.toString()
				+ string.substring(index, string.length());
	}

	private boolean addCharCode(Character ch, int index) {
		String contentToValidate = putCharAt(validContent, index, ch);

		if (contentToValidate.length() <= expression.size()
				&& String.valueOf(ch).matches(
						expression.get(contentToValidate.length() - 1))) {
			validContent = contentToValidate;
			format();
			return true;
		}
		return false;
	}

	private void format() {
		this.content = this.baseContent;

		if (validContent.length() == 0) {
			field.setData(AppConstant.MASK_EMPTY, true);
		} else {
			field.setData(AppConstant.MASK_EMPTY, false);
		}

		for (Integer i = 0; i < this.validContent.length(); i++) {
			this.content = this.content.replaceFirst("[_]",
					this.validContent.substring(i, i + 1));
		}

		field.setText(this.content);
	}

	private void transformMask() {
		for (Integer i = 0; i < this.mask.toCharArray().length; i++) {
			Character ch = this.mask.toCharArray()[i];
			switch (ch) {
			// case '#':
			// this.expression.add("[0-9]{1}");
			// this.content += "_";
			// break;
			case '0':
				this.expression.add("[\\s0-9]{1}");
				this.content += "_";
				break;

			case 'A':
				this.expression.add("[A-ZА-Я]{1}");
				this.content += "_";
				break;

			case 'a':
				this.expression.add("[a-zа-я]{1}");
				this.content += "_";
				break;

			case 'B':
				this.expression.add("[A-Za-zА-Яа-я]{1}");
				this.content += "_";
				break;

			case 'C':
				this.expression.add("[A-Za-z0-9А-Яа-я]{1}");
				this.content += "_";
				break;
			case '[':
			case ']':
			case '\\':
			case '\'':
			case '"':
			case '<':
			case '>':
			case '!':
			case '?':
			case '~':
			case '&':
			case '^':
			case '@':
			case '#':
			case '/':
			case ',':
			case '*':
			case '.':
			case '-':
			case ':':
			case '(':
			case ')':
			case '{':
			case '}':
			case '=':
			case '+':
			case ' ':
				this.content += ch;
				break;
			}
		}

		this.baseContent = this.content;
		field.setText(this.content);
	}

	private void setValue(String value) {
		validContent = "";
		for (int i = 0; i < value.toCharArray().length; i++) {
			Character ch = value.toCharArray()[i];
			switch (ch) {
			case '/':
			case ',':
			case '*':
			case '.':
			case '-':
			case ':':
			case '(':
			case ')':
			case '{':
			case '}':
			case '=':
			case '+':
			case ' ':
			case '_':
				break;
			default:
				validContent += ch;
				break;
			}
		}
		format();
	}

	private int lastValidPos() {
		int index = 0;
		while (index < content.length() && content.toCharArray()[index] != '_') {
			index++;
		}
		return index;
	}

	@Override
	public List validate(Editor editor, Object value) {
		if (editor instanceof ValueBaseField) {
			String text = ((ValueBaseField) editor).getText();
			if (validContent.length() == 0) {
				return null;
			}
			if (!compiledRegExp.test(text)) {
				return createError(editor,
						"Ошибка ввода: строка должна быть в формате " + mask,
						value);
			}
		}
		return null;
	}

	private String makeRegex(String mask) {
		StringBuilder regex = new StringBuilder();
		for (char ch : mask.toCharArray()) {
			switch (ch) {
			case '0':
				regex.append("[\\s0-9]{1}");
				break;

			case 'A':
				regex.append("[A-ZА-Я]{1}");
				break;

			case 'a':
				regex.append("[a-zа-я]{1}");
				break;

			case 'B':
				regex.append("[A-Za-zА-Яа-я]{1}");
				break;

			case 'C':
				regex.append("[A-Za-z0-9А-Яа-я]{1}");
				break;
				
			case '[':
			case '\\':
			case '^':
			case '$':
			case '.':
			case '|':
			case '?':
			case '*':
			case '+':
			case '(':
			case ')':
				regex.append("[\\" + ch + "]{1}");
				break;
				
			case ']':
			case '\'':
			case '"':
			case '<':
			case '>':
			case '!':
			case '~':
			case '&':
			case '@':
			case '#':
			case '/':
			case ',':
			case '-':
			case ':':
			case '{':
			case '}':
			case '=':
			case ' ':
				regex.append("[" + ch + "]{1}");
				break;
			}
		}
		return regex.toString();
	}
}
