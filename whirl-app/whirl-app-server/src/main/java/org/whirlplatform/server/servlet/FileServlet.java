package org.whirlplatform.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.IOUtils;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.DataModifyConfig.DataModifyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.session.SessionManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Singleton
public class FileServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 7432737718917945930L;

    private Logger _log = LoggerFactory.getLogger(FileServlet.class);

    private String tokenId;

    public static final String SESSION_FILE_MAP = "SESSION_FILE_MAP";

    private Connector _connector;

    @Inject
    public FileServlet(Connector connector) {
        super();
        this._connector = connector;
    }

    private Connector connector() {
        return _connector;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        executer(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        executer(req, resp);
    }

    private void executer(HttpServletRequest req, HttpServletResponse resp) {
        tokenId = req.getParameter(AppConstant.TOKEN_ID);
        ApplicationUser user = null;
        try {
            user = SessionManager
                    // .get(req.getSession().getServletContext())
                    .get(req.getSession())
                    .getUser(
                            new SessionToken(req.getSession().getId(), tokenId));
        } catch (CustomException e1) {
        }

        String gettype = req.getParameter(AppConstant.GETTYPE);
        String field = req.getParameter(AppConstant.FIELD);
        String fileId = req.getParameter(AppConstant.TABLE);
        String classList = req.getParameter(AppConstant.TABLE_ID);
        String tableCode = req.getParameter(AppConstant.TABOLE_CODE);
        if (classList == null && tableCode != null) {
            for (AbstractTableElement t : user.getApplication()
                    .getAvailableTables()) {
                if (tableCode.equals(t.getCode())) {
                    classList = t.getId();
                    break;
                }
            }
        }
        String id = req.getParameter(AppConstant.ID);
        String contentType = req.getParameter(AppConstant.CONTENT_TYPE);
        Boolean attachment = req.getParameter(AppConstant.ATTACHMENT) == null ? true
                : Boolean.valueOf(req.getParameter(AppConstant.ATTACHMENT));

        _log.info("SERVLET FILE: [gettype, field, classList, dfobj] = ["
                + gettype + ", " + field + ", " + classList + ", " + id + "]");
        if (gettype.equalsIgnoreCase(AppConstant.TABLE)) {
            try (OutputStream out = resp.getOutputStream()) {
                FileValue file = connector().downloadFileFromTable(classList,
                        field, id, user);

                resp.reset();
                // resp.setContentLength(((InputStream)
                // file.getInputStream()).available()); // ?
                if (attachment) {
                    resp.setHeader(
                            "Content-Disposition",
                            "attachment; filename=\""
                                    + encodeFileName(file.getName()) + "\";"
                                    + "filename*=\""
                                    + encodeFileName(file.getName()) + "\"");
                }

                String type = URLConnection.guessContentTypeFromName(file
                        .getName());
                if (type == null && contentType == null) {
                    type = "application/octet-stream";
                } else if (contentType != null) {
                    type = contentType;
                }
                resp.setContentType(type + "; charset=UTF-8");

                IOUtils.copy((InputStream) file.getInputStream(), out);
                out.flush();

            } catch (Exception e) {
                _log.error(e, e);
            }
        } else if (gettype.equalsIgnoreCase(AppConstant.UPLOAD)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // не удаляем файлы
            factory.setFileCleaningTracker(null);

            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List<FileItem> items = upload.parseRequest(req);
                for (FileItem item : items) {
                    if (!item.isFormField() && item.getFieldName() != null
                            && item.getSize() > 0) {
                        FileValue fileValue = new FileValue();
                        fileValue.setName(item.getName());
                        fileValue.setInputStream(item.getInputStream());

                        ClassMetadata metadata = new ClassMetadata(classList);
                        FieldMetadata fieldMeta = new FieldMetadata(field,
                                DataType.FILE, "");
                        RowModelData model = new RowModelDataImpl();
                        model.set(field, fileValue);
                        DataModifyConfig config = new DataModifyConfig(
                                DataModifyType.UPDATE, Arrays.asList(model),
                                null);
                        metadata.addField(fieldMeta);
                        connector().update(metadata, config, user);
                    }
                    item.delete();
                }
            } catch (Exception e) {
                _log.error(e);
            }
        } else if (gettype.equalsIgnoreCase(AppConstant.FORM_UPLOAD)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // не удаляем файлы
            factory.setFileCleaningTracker(null);

            ServletFileUpload upload = new ServletFileUpload(factory);

            boolean saveName = Boolean.valueOf(req
                    .getParameter(AppConstant.SAVE_FILE_NAME));
            List<FileItem> items;
            try {
                boolean fileLoaded = false;
                items = upload.parseRequest(req);
                for (FileItem item : items) {
                    if (!item.isFormField()
                            && AppConstant.TABLE.equals(item.getFieldName())) {
                        putToFileMap(req, fileId, item, saveName);
                        fileLoaded = true;
                        _log.info("File info: "
                                + "FileName="
                                + item.getName()
                                + "; FieldName="
                                + item.getFieldName()
                                + "; Content-type: "
                                + item.getContentType()
                                + "; Size: "
                                + String.valueOf(item.getSize())
                                + "Path: "
                                + ((DiskFileItem) item).getStoreLocation()
                                .getAbsolutePath());
                    }
                }
                if (!fileLoaded) {
                    putToFileMap(req, fileId, null, saveName);
                }
                resp.getWriter().print("OK");
            } catch (FileUploadException e) {
                _log.error(e);
                try {
                    resp.getWriter().println("ERROR");
                } catch (IOException ex) {
                }
            } catch (IOException e) {
                _log.error(e);
            }
        }
    }

    private void putToFileMap(HttpServletRequest req, String key,
                              FileItem file, boolean saveName) {
        HttpSession session = req.getSession();
        HashMap<String, FileUpload> map = (HashMap<String, FileUpload>) session
                .getAttribute(SESSION_FILE_MAP);
        if (map == null) {
            map = new HashMap<String, FileUpload>();
            session.setAttribute(SESSION_FILE_MAP, map);
        }
        map.put(key, new FileUpload(file, saveName));
    }

    public static String encodeFileName(String fileName) throws Exception {
        fileName = fileName.replaceAll(" ", "_");
        fileName = "UTF-8''" + URLEncoder.encode(fileName, "UTF8");
        return fileName;
    }

    public class FileUpload {

        private FileItem file;

        private boolean saveName;

        public FileUpload(FileItem file, boolean saveName) {
            this.file = file;
            this.saveName = saveName;
        }

        public FileItem getFile() {
            return file;
        }

        public boolean isSaveName() {
            return saveName;
        }

    }
}
