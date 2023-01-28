package org.whirlplatform.editor.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

@Singleton
public class ImportServlet extends HttpServlet {
    private static final long serialVersionUID = -8556318429926656200L;
    private static final int MEMORY_THRESHOLD = 65536;
    private static final String FILES_ATTR_NAME = "whirl-editor-filemap";
    private Logger _log = LoggerFactory.getLogger(ImportServlet.class);
    private MetadataStore metadataStore;

    @Inject
    public ImportServlet(MetadataStore metadataStore) {
        this.metadataStore = metadataStore;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doImport(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doImport(req, resp);
    }

    private void doImport(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (!item.isFormField() && item.getFieldName() != null
                        && item.getFieldName().equals("file")) {
                    InputStream stream = item.getInputStream();
                    ApplicationElement application =
                            metadataStore.deserialize(IOUtils.toString(stream, "UTF-8"));
                    req.getSession().setAttribute("APPLICATION", application);
                    saveFilesIntoTheContext(req.getSession(), application.getJavaFiles());
                    saveFilesIntoTheContext(req.getSession(), application.getJavaScriptFiles());
                    saveFilesIntoTheContext(req.getSession(), application.getCssFiles());
                    saveFilesIntoTheContext(req.getSession(), application.getImageFiles());
                    saveFileIntoContext(req.getSession(), application.getStaticFile());
                }
            }
            resp.getWriter().print("OK");
        } catch (IOException | FileUploadException | MetadataStoreException e) {
            _log.error(e);
            resp.getWriter().print(e.getMessage());
        }
    }

    private void saveFilesIntoTheContext(HttpSession session, Collection<FileElement> files)
            throws IOException {
        for (final FileElement file : files) {
            saveFileIntoContext(session, file);
        }
    }

    @SuppressWarnings("unchecked")
    private void saveFileIntoContext(HttpSession session, FileElement file) throws IOException {
        if (file == null || file.getInputStream() == null) {
            return;
        }
        Map<String, FileItem> files = (Map<String, FileItem>) session.getAttribute(FILES_ATTR_NAME);
        if (files == null) {
            files = new HashMap<String, FileItem>();
            session.setAttribute(FILES_ATTR_NAME, files);
        }
        FileItem fileItem = createFileItem(file);
        files.put(file.getId(), fileItem);
    }

    private FileItem createFileItem(final FileElement fileElement) throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        FileItem result = null;
        try (InputStream is = (InputStream) fileElement.getInputStream()) {
            result = factory.createItem(null, null, false, fileElement.getFileName());
            java.io.OutputStream output = result.getOutputStream();
            IOUtils.copy(is, output);
            output.flush();
            output.close();
        }
        return result;
    }
}
