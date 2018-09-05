package org.whirlplatform.component.client.utils;

import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import org.whirlplatform.meta.shared.i18n.AppMessage;

public class ProgressHelper {

	private static AutoProgressMessageBox _instance;

	public static void show(String waitText) {
		if (_instance == null) {
			_instance = new AutoProgressMessageBox(
					AppMessage.Util.MESSAGE.executing());
		}
		_instance.setMessage(waitText);
		_instance.show();
		_instance.auto();
	}

	public static void hide() {
		if (_instance != null) {
			_instance.hide();
		}
	}

}
