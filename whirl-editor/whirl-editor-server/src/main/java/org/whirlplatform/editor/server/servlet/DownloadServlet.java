package org.whirlplatform.editor.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.FileElementCategory;
import org.whirlplatform.meta.shared.version.VersionUtil;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

@Singleton
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = -4445862385681846234L;

    private Logger _log = LoggerFactory.getLogger(DownloadServlet.class);
    private MetadataStore metadataStore;

    @Inject
    public DownloadServlet(MetadataStore metadataStore) {
        this.metadataStore = metadataStore;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        onDownload(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        onDownload(req, resp);
    }

    private void onDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String tmpCategory = req.getParameter("category");
        String categoryValue = (tmpCategory != null) ? tmpCategory : req.getParameter("path");
        String fileName = req.getParameter("fileName");
        if (code == null || categoryValue == null || fileName == null) {
            String message = "At least one of the required parameters is empty";
            _log.error(message);
            responseError(resp, message);
            return;
        }
        FileItem fileItem = getFileFromContext(req.getSession(), fileName);
        if (fileItem != null) {
            try (InputStream in = fileItem.getInputStream()) {
                copyInputStreamToResponce(resp, in, fileName);
            } catch (IOException e) {
                responseError(resp, "Impossible to get the input stream from the file " + fileName);
                _log.error(e);
            }
        } else {
            Version version = extractVersion(req);
            FileElementCategory category = FileElementCategory.get(categoryValue);
            try (InputStream in = metadataStore.getApplicationFileInputStream(code, version,
                    category, fileName)) {
                copyInputStreamToResponce(resp, in, fileName);
            } catch (MetadataStoreException e) {
                _log.error(e);
                responseError(resp, e.getMessage());
                return;
            }
        }
    }

    private Version extractVersion(HttpServletRequest req) {
        Version result = null;
        String stringVersion = req.getParameter("version");
        String stringBranch = req.getParameter("branch");
        if (stringVersion != null) {
            result = VersionUtil.createVersion(stringVersion);
        } else if (stringBranch != null) {
            result = VersionUtil.createVersion(stringBranch);
        }
        return result;
    }

    private void copyInputStreamToResponce(HttpServletResponse resp, InputStream in,
                                           String fileName)
            throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        resp.setContentType("application/octet-stream");
        IOUtils.copy(in, out);
        out.flush();
    }

    private void responseError(HttpServletResponse resp, String message) throws IOException {
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();
        writer.append("<error>Error while downloading. " + message + "</error>");
        resp.flushBuffer();
    }

    @SuppressWarnings("unchecked")
    public FileItem getFileFromContext(HttpSession session, String fileName) {
        Map<String, FileItem> files =
                (Map<String, FileItem>) session.getAttribute("whirl-editor-filemap");
        if (files == null) {
            return null;
        }
        FileItem result = null;
        for (FileItem item : files.values()) {
            if (fileName.equals(item.getName())) {
                result = item;
                break;
            }
        }
        return result;
    }
}
