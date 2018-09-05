package org.whirlplatform.server.i18n;

import com.mattbertolini.hermes.Hermes;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.server.utils.ContextUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class I18NMessage {

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

	public synchronized static AppMessage getMessage(Locale locale) {
		try {
			if (locale != null) {
				return Hermes.get(AppMessage.class, locale.getLanguage());
			}
			return Hermes.get(AppMessage.class, null);

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Получает специфическое для сервера сообщение из контекста
	 * 
	 * @param name
	 *            имя сообщения
	 * @return значение сообщения
	 */
	public static String getSpecifiedMessage(String name, Locale locale) {
        Map<String, String> messages = ContextUtil.lookupAll("Whirl/message/"
				+ locale.getLanguage() + "/", String.class);
		if (messages.containsKey(name)) {
			return messages.get(name);
		}
        messages = ContextUtil.lookupAll("Whirl/message/default/", String.class);
		return messages.get(name);
	}
}
