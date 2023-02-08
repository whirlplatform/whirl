package org.whirlplatform.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.ExpImpType;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.expimp.CSVImporter;
import org.whirlplatform.server.expimp.XLSImporter;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.session.SessionManager;

@Singleton
public class ImportServlet extends HttpServlet {

    private static final long serialVersionUID = -6410761304634176145L;

    private Logger _log = LoggerFactory.getLogger(ImportServlet.class);

    private Connector _connector;

    @Inject
    public ImportServlet(Connector connector) {
        super();
        this._connector = connector;
    }

    private Connector connector() {
        return _connector;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String tokenId = req.getParameter(AppConstant.TOKEN_ID);
        String importType = req.getParameter(AppConstant.EXPIMP_TYPE_PARAM);

        ApplicationUser user = null;
        try {
            user = SessionManager
                //.get(req.getSession().getServletContext())
                .get(req.getSession())
                .getUser(
                    new SessionToken(req.getSession().getId(), tokenId));
            String classList = req.getParameter(AppConstant.TABLE_ID);

            ClassMetadata metadata = connector().getClassMetadata(classList,
                null, user);
            if (checkAccess(metadata)) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(req);
                for (FileItem item : items) {
                    if (!item.isFormField() && item.getFieldName() != null
                        && item.getFieldName().equals("file")) {
                        InputStream stream = item.getInputStream();

                        if (ExpImpType.IMPORT_CSV.name().equals(importType)) {
                            CSVImporter importer = new CSVImporter(_connector,
                                user, metadata);
                            importer.importFromStream(stream);
                            if (importer.isImportError()) {
                                throw new CustomException(
                                    "В процессе иморта возникли ошибки. Часть данных не имортирована.");
                            }
                        }

                        if (ExpImpType.IMPORT_XLS.name().equals(importType)) {
                            XLSImporter importer = new XLSImporter(_connector,
                                user, metadata);
                            importer.importFromStream(stream);
                            if (importer.isImportError()) {
                                throw new CustomException(
                                    "В процессе иморта возникли ошибки. Часть данных не имортирована.");
                            }
                        }
                    }
                }
            }

            resp.getWriter().print("OK");
        } catch (CustomException e1) {
            _log.error(e1);
            resp.getWriter().print(e1.getMessage());
        } catch (FileUploadException e) {
            _log.error(e);
            resp.getWriter().print(e.getMessage());
        }
    }

    private boolean checkAccess(ClassMetadata metadata) {
        return metadata.isInsertable();
    }

}
