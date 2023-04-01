package org.whirlplatform.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.empire.commons.StringUtils;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.ExpImpType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.session.SessionManager;
import org.whirlplatform.server.utils.TranslitUtil;

/**
 *
 */
@Singleton
public class ExportServlet extends HttpServlet {

    private static final long serialVersionUID = 5116792332869665489L;

    private Logger _log = LoggerFactory.getLogger(ExportServlet.class);

    private Connector _connector;

    @Inject
    public ExportServlet(Connector connector) {
        super();
        this._connector = connector;
    }

    private Connector connector() {
        return _connector;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        doExport(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        doExport(req, resp);
    }

    private void doExport(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String tokenId = request.getParameter(AppConstant.TOKEN_ID);
        ApplicationUser user = null;
        try {
            //user = SessionManager.get(request.getSession().getServletContext())
            user = SessionManager.get(request.getSession())
                .getUser(
                    new SessionToken(request.getSession().getId(),
                        tokenId));
        } catch (CustomException e) {
            _log.error(e);
            response.sendError(500);
        }

        String classList = request.getParameter(AppConstant.TABLE_ID);
        ExpImpType exportType = ExpImpType.valueOf(request
            .getParameter(AppConstant.EXPIMP_TYPE_PARAM));
        boolean isAllRecords = Boolean.parseBoolean(request
            .getParameter(AppConstant.EXPORT_ALLREC_PARAM));
        String whereSql = request.getParameter(PropertyType.WhereSql.getCode());

        boolean xlsx = Boolean.parseBoolean(request
            .getParameter(AppConstant.EXPORT_XLSX_PARAM));
        boolean columnHeader = Boolean.parseBoolean(request
            .getParameter(AppConstant.EXPORT_COLUMNS_PARAM));
        String paramId = request.getParameter("parameters_id");

        ClassLoadConfig loadConfig = null;
        if (paramId != null) {
            HttpSession session = request.getSession();
            loadConfig = (ClassLoadConfig) session.getAttribute(paramId);
            session.removeAttribute(paramId);
        } else {
            loadConfig = new ClassLoadConfig();
        }

        _log.info("SERVLET EXPORT: [pfuser, pfrole, classList, isAllRecords] = ["
            + user.getId()
            + ", "
            + user.getApplication().getId()
            + ", "
            + classList
            + ", "
            + isAllRecords
            + "] "
            + exportType.toString());

        if (!StringUtils.isEmpty(whereSql)) {
            // TODO расшифровать whereSql =
            // Encryptor.get(getServletContext()).decrypt(whereSql);
        }
        ClassMetadata metadata;
        try {
            metadata = connector().getClassMetadata(classList, new ArrayList<>(loadConfig.getParameters().values()),
                user);
            if (loadConfig != null) {
                loadConfig.setAll(isAllRecords);
            }
            if (exportType == ExpImpType.EXPORT_XLS) {
                String extension = null;
                if (xlsx) {
                    extension = ".xlsx";
                } else {
                    extension = ".xls";
                }
                response.setContentType("application/ms-excel;");
                response.setHeader(
                    "Content-Disposition",
                    "attachment;filename=\""
                        + getExportFileName(metadata, user) + extension
                        + "\"");
                connector().exportXLS(metadata, columnHeader,
                    xlsx, loadConfig, response.getOutputStream(), user);
            } else if (exportType == ExpImpType.EXPORT_CSV) {
                response.setContentType("text/csv;");
                response.setHeader(
                    "Content-Disposition",
                    "attachment;filename=\""
                        + getExportFileName(metadata, user) + ".csv"
                        + "\"");
                connector().exportCSV(metadata, columnHeader,
                    loadConfig, response.getOutputStream(), user);
            }
        } catch (CustomException e) {
            _log.error(e);
            response.sendError(500);
        }
    }

    private String getExportFileName(ClassMetadata metadata,
                                     ApplicationUser user) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd_HH-mm-ss");
        String tmp = "file-";

        String tableName = metadata.getTitle();
        if (!StringUtils.isEmpty(tableName)) {
            tmp = TranslitUtil.toTranslit(tableName);
            tmp = tmp.replaceAll("[/]", "_");
            tmp = tmp.replaceAll(" ", "_");
            tmp = tmp.replaceAll("/", "_");
            tmp = tmp.replaceAll("\"", "");
            tmp = tmp.replaceAll(":", "");
            tmp = tmp.replaceAll("'", "");
        }

        String result = tmp + "_" + format.format(new Date());
        return result;
    }

}