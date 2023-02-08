package org.whirlplatform.server.cache;

import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Фильтр доступа к приложению
 */
@Singleton
public class CacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Cache-Control",
            "max-age=0,no-cache,no-store,must-revalidate,proxy-revalidate,s-maxage=0");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "No-cache");
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig arg0) {

    }

}
