package org.whirlplatform.server.i18n;

import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Singleton
public class I18NFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        Enumeration<Locale> e = req.getLocales();
        if (e.hasMoreElements()) {
            I18NMessage.setRequestLocale(e.nextElement());
        } else {
            I18NMessage.setRequestLocale(req.getLocale());
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) {
    }

}
