package org.whirlplatform.editor.server.i18n;

import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

@Singleton
public class EditorI18NFilter implements Filter {

	private static Locale localeFilter = new Locale("");

	public Locale getLocale(){
		return localeFilter;
	}

	public static void setLocale(Locale l){
		localeFilter = l;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (localeFilter.getLanguage().equals(new Locale("").getLanguage())){
			Enumeration<Locale> e = req.getLocales();
			if (e.hasMoreElements()) {
				EditorI18NMessage.setRequestLocale(e.nextElement());
			} else {
				EditorI18NMessage.setRequestLocale(req.getLocale());
			}
		} else {
			EditorI18NMessage.setRequestLocale(localeFilter);
		}
		chain.doFilter(req, resp);
	}

	@Override
    public void init(FilterConfig config) {
	}

}
