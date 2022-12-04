package org.whirlplatform.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.driver.Connector;

@Singleton
public class MainController extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 9188970751005986230L;

    private Configuration configuration;
    private Connector connector;

    @Inject
    public MainController(Configuration configuration, Connector connector) {
        super();
        this.configuration = configuration;
        this.connector = connector;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("configuration", configuration);
        req.setAttribute("connector", connector);
        req.getRequestDispatcher("/WEB-INF/jsp/app.jsp").forward(req, resp);
    }

}
