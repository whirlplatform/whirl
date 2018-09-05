package org.whirlplatform.test;

import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class TestHelper {

	public static ApplicationElement emptyApplication() {
		ApplicationElement application = new ApplicationElement();
		application.setDefaultLocale(new LocaleElement("ru", null));
		return application;
	}

}
