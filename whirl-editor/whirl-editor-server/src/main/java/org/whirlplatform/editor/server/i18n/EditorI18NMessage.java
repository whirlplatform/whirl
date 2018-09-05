package org.whirlplatform.editor.server.i18n;

import com.mattbertolini.hermes.Hermes;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

import java.io.IOException;
import java.util.Locale;

public class EditorI18NMessage {

	private static ThreadLocal<Locale> thread = new ThreadLocal<Locale>();

	public static void setRequestLocale(Locale locale) {
		thread.set(locale);
	}

	public static Locale getRequestLocale() {
		Locale result = thread.get();
		if (result == null) {
			result = Locale.ENGLISH;
		}
		return result;
	}

	public static EditorMessage getMessage(Locale locale) {
		try {
			if (locale != null) {
				return Hermes.get(EditorMessage.class, locale.getLanguage());
			}
			return Hermes.get(EditorMessage.class, null);

		} catch (IOException e) {
			return null;
		}
	}
}
