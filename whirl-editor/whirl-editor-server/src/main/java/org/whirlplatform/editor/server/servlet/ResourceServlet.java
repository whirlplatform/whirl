package org.whirlplatform.editor.server.servlet;

import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.FileElementCategory;
import org.whirlplatform.meta.shared.version.VersionUtil;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@SuppressWarnings("serial")
public class ResourceServlet extends HttpServlet {
    private Logger _log = LoggerFactory.getLogger(ResourceServlet.class);
    private MetadataStore metadataStore;

    @Inject
    public ResourceServlet(MetadataStore metadataStore) {
        this.metadataStore = metadataStore;
    }

    // private String getPlatformContextPath() {
    // int start = (getServletContext().getContextPath().startsWith("/")) ? 1 :
    // 0;
    // String context = getServletContext().getContextPath().substring(start);
    // context = StringUtils.substringBeforeLast(context, "-editor");
    // context.replace("/", "-");
    // return context;
    // }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        try {
            if ("upload".equals(action)) {
                onUpload(req, resp);
                responseOk(resp);
            } else if ("download".equals(action)) {
                onDownload(req, resp);
            } else {
                _log.error("Unrecognized command");
                responseError(resp);
            }
        } catch (FileUploadException e) {
            _log.error(e);
            responseError(resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getParameter("action");
        if ("download".equals(action)) {
            onDownload(req, resp);
        }
    }

    private ServletFileUpload createServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setFileCleaningTracker(new FileCleaningTracker());
        ServletFileUpload upload = new ServletFileUpload(factory);
        return upload;
    }

    @SuppressWarnings("unchecked")
    public void saveFileInContext(HttpSession session, String fileId, FileItem file) {
        Map<String, FileItem> files = (Map<String, FileItem>) session.getAttribute("whirl-editor-filemap");
        if (files == null) {
            files = new HashMap<String, FileItem>();
            session.setAttribute("whirl-editor-filemap", files);
        }
        files.put(fileId, file);
    }

    private void onUpload(HttpServletRequest req, HttpServletResponse resp) throws FileUploadException {
        if (ServletFileUpload.isMultipartContent(req)) {
            ServletFileUpload upload = createServletFileUpload();
            List<FileItem> files = upload.parseRequest(req);
            for (FileItem f : files) {
                if (!f.isFormField()) {
                    String fileId = f.getFieldName();
                    if (fileId.startsWith("file")) {
                        fileId = fileId.substring(4);
                        saveFileInContext(req.getSession(), fileId, f);
                    }
                }
            }
        }
    }

    private void onDownload(HttpServletRequest req, HttpServletResponse resp) {
        String appCode = req.getParameter("code");
        String type = req.getParameter("path");
        String fileName = req.getParameter("fileName");
        if (appCode == null || type == null || fileName == null) {
            return;
        }
        FileItem fileItem = getFileFromContext(req.getSession(), fileName);
        if (fileItem != null) {
            try (InputStream in = fileItem.getInputStream()) {
                copyInputStreamToResponce(resp, in, fileName);
            } catch (IOException e) {
                _log.error(e);
            }
        } else {
            FileElementCategory category = FileElementCategory.get(type);
            Version version = extractVersion(req);
            try (InputStream in = metadataStore.getApplicationFileInputStream(appCode, version, category, fileName)) {
                copyInputStreamToResponce(resp, in, fileName);
            } catch (MetadataStoreException | IOException e) {
                _log.error(e);
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

    private void copyInputStreamToResponce(HttpServletResponse resp, InputStream in, String fileName)
            throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        resp.setContentType("application/octet-stream");
        IOUtils.copy(in, out);
        out.flush();
    }

    private void responseOk(HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();
        writer.append("<result>OK</result>");
        resp.flushBuffer();
    }

    private void responseError(HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF8");
        resp.setContentType("text/xml");
        PrintWriter writer = resp.getWriter();
        writer.append("<error>Resource servlet error. See server log for details.</error>");
        resp.flushBuffer();
    }

    @SuppressWarnings("unchecked")
    public FileItem getFileFromContext(HttpSession session, String fileName) {
        Map<String, FileItem> files = (Map<String, FileItem>) session.getAttribute("whirl-editor-filemap");
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
