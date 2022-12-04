package org.whirlplatform.server.session;

import java.util.HashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.fileupload.FileItem;
import org.whirlplatform.server.servlet.FileServlet;
import org.whirlplatform.server.servlet.FileServlet.FileUpload;

public class SessionListener implements HttpSessionListener {

//    private static Logger _log = Logger.getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        SessionManager.attachSession(event.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SessionManager.get(event.getSession())
                .unregisterSession(event.getSession());
        deleteTemporaryFiles(event.getSession());

        SessionManager.detachSession(event.getSession());
    }

    private void deleteTemporaryFiles(HttpSession session) {
        HashMap<String, FileUpload> map = (HashMap<String, FileUpload>) session
                .getAttribute(FileServlet.SESSION_FILE_MAP);
        if (map != null) {
            for (String key : map.keySet()) {
                FileUpload upload = map.get(key);
                if (upload != null) {
                    FileItem item = upload.getFile();
                    if (item != null) {
                        item.delete();
                    }
                }
            }
        }
    }

}
