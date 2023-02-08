package org.whirlplatform.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Сервлет отображающий версию платформы.
 */
public class VersionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String version;

    public VersionServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        if (version == null) {
            version = getClass().getPackage().getImplementationVersion();
            if (version == null) {
                try (InputStream is = getServletContext().getResourceAsStream(
                    "/META-INF/MANIFEST.MF")) {
                    Properties prop = new Properties();
                    prop.load(is);
                    version = prop.getProperty("Implementation-Version");
                }
            }
        }
        if (version != null) {
            response.getWriter().write(version);
            response.flushBuffer();
        }
    }

}
