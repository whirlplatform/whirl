package org.whirlplatform.editor.client.view.widget;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.validator.ValidatorUtil;

public class WidgetUtil {
	public static final int MAX_WINDOW_HEIGHT = 600;
	public static final int MAX_WINDOW_WIDTH = 1024;
	
	public static BoxLayoutData flexLayout() {
		final BoxLayoutData result = new BoxLayoutData(new Margins(0, 0, 0, 0));
		result.setFlex(1);
		return result;
	}

	public static BoxLayoutData noStretchLayout() {
		final BoxLayoutData result = new BoxLayoutData(new Margins(0, 0, 0, 0));
		return result;
	}

	public static BoxLayoutData flexLayout(int top, int right, int bottom, int left) {
		final BoxLayoutData result = new BoxLayoutData(new Margins(top, right, bottom, left));
		result.setFlex(1);
		return result;
	}

	public static BoxLayoutData noStretchLayout(int top, int right, int bottom, int left) {
		final BoxLayoutData result = new BoxLayoutData(new Margins(top, right, bottom, left));
		return result;
	}

	public static TextField createTextField(boolean allowBlank) {
		TextField result = new TextField();
		if (!allowBlank) {
			result.addValidator(ValidatorUtil.createEmptyStringValidator());
		}
		return result;
	}

	public static Radio createRadio(final String label, boolean val) {
		Radio radio = new Radio();
		radio.setBoxLabel(label);
		radio.setValue(val);
		return radio;
	}
}
