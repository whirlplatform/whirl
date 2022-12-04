package org.whirlplatform.editor.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.whirlplatform.editor.server.EditorConnector;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

@Singleton
@SuppressWarnings("serial")
public class FileServlet extends HttpServlet {

    EditorConnector connector;
    private Logger _log = LoggerFactory.getLogger(FileServlet.class);

    @Inject
    public FileServlet(EditorConnector connector) {
        this.connector = connector;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String getType = req.getParameter(AppConstant.GETTYPE);
        //TODO do nothing
    }
}
