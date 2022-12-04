package org.whirlplatform.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.server.metadata.container.ContainerException;
import org.whirlplatform.server.metadata.container.MetadataContainer;
import org.whirlplatform.server.utils.ApplicationReference;

@Singleton
public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 8505156265144432492L;

    private MetadataContainer metadataContainer;

    @Inject
    public ResourceServlet(MetadataContainer metadataContainer) {
        this.metadataContainer = metadataContainer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        String type = req.getParameter("action");
        try {
            if ("download".equals(type)) {
                onDownload(req, resp);
            }
        } catch (IOException | ContainerException e) {
            throw new ServletException(e);
        }
    }

    private void onDownload(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ContainerException {
        String type = req.getParameter("path");
        String appCode = req.getParameter("code");
        String branch = req.getParameter("branch");
        String version = req.getParameter("version");
        final String fileName = req.getParameter("fileName");

        AtomicReference<ApplicationReference> reference = metadataContainer
                .getApplication(appCode,
                        Version.parseBranchVersionOrNull(branch, version));
        if (reference != null) {
            ApplicationElement application = reference.get().getApplication();
            if (fileName != null && "javascript".equals(type)
                    || "css".equals(type) || "image".equals(type)) {
                FileElement file = null;
                if ("javascript".equals(type)) {
                    file = findFileElement(application.getJavaScriptFiles(),
                            branch, version, fileName);
                } else if ("css".equals(type)) {
                    file = findFileElement(application.getCssFiles(), branch,
                            version, fileName);
                } else if ("image".equals(type)) {
                    file = findFileElement(application.getImageFiles(), branch,
                            version, fileName);
                }
                if (file != null) {
                    try (InputStream in = (InputStream) file.getInputStream()) {
                        if (in != null) {
                            IOUtils.copy(in, resp.getOutputStream());
                            resp.flushBuffer();
                            return;
                        }
                    }
                }
            }

        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private FileElement findFileElement(Collection<FileElement> collection,
                                        String branch, String version, final String fileName) {
        return CollectionUtils.find(collection, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                FileElement file = (FileElement) object;
                return fileName.equals(file.getFileName());
            }
        });

    }
}
