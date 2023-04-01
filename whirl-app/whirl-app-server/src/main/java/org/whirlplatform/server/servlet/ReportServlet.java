package org.whirlplatform.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.FormWriter;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Message;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.ReportMessage;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.report.CSVReportWriter;
import org.whirlplatform.server.report.HTMLReportWriter;
import org.whirlplatform.server.report.Report;
import org.whirlplatform.server.report.XLSReportWriter;
import org.whirlplatform.server.report.XLSXReportWriter;
import org.whirlplatform.server.session.SessionManager;

@Singleton
public class ReportServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 8744571622846658251L;

    private static Logger _log = LoggerFactory.getLogger(ReportServlet.class);

    private Connector _connector;

    private ConnectionProvider connectionProvider;

    @Inject
    public ReportServlet(Connector connector, ConnectionProvider connectionProvider) {
        super();
        this._connector = connector;
        this.connectionProvider = connectionProvider;
    }

    private Connector connector() {
        return _connector;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        String tokenId = request.getParameter(AppConstant.TOKEN_ID);
        ApplicationUser user = null;
        try {
            // user =
            // SessionManager.get(request.getSession().getServletContext())
            user = SessionManager.get(request.getSession())
                .getUser(new SessionToken(request.getSession().getId(), tokenId));
        } catch (CustomException e1) {
            // skipped
        }
        response.reset();

        String rpt = request.getParameter("rpt");
        String format = request.getParameter("format");
        int horizontalDpi = NumberUtils.toInt(request.getParameter("dpih"), 0);
        int verticalDpi = NumberUtils.toInt(request.getParameter("dpiv"), 0);

        try {
            Report report = loadReport(rpt, user);
            report.setFormat(format);
            if ("auto".equals(report.getHorizontalDPI())) {
                report.setHorizontalDPI(String.valueOf(horizontalDpi));
            }
            if ("auto".equals(report.getVerticalDPI())) {
                report.setVerticalDPI(String.valueOf(verticalDpi));
            }

            List<DataValue> params = loadParameters(report, user, request.getSession());

            FormElementWrapper form =
                connector().getFormRepresent(report.getFormId(), params, user);
            if (form == null) {
                throw new CustomException("Wrong form id");
            }
            if (AppConstant.REPORT_FORMAT_XLSX.equalsIgnoreCase(format)) {
                writeXLSX(report, form, user, response, params);
            } else if (AppConstant.REPORT_FORMAT_XLS.equalsIgnoreCase(format)) {
                writeXLS(report, form, user, response, params);
            } else if (AppConstant.REPORT_FORMAT_HTML.equalsIgnoreCase(format)) {
                writeHTML(report, form, user, response, params);
            } else if (AppConstant.REPORT_FORMAT_CSV.equalsIgnoreCase(format)) {
                writeCSV(report, form, user, response, params);
            }
        } catch (CustomException e) {
            writeError(e, response);
        } catch (SQLException e) {
            writeError(e, response);
        } catch (ConnectException e) {
            writeError(e, response);
        }
    }

    private void writeXLSX(Report report, FormElementWrapper form, ApplicationUser user,
                           HttpServletResponse response,
                           List<DataValue> params)
        throws IOException, SQLException, ConnectException {
        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + form.getName() + ".xlsx\"");
        // + getReportName(report, user) + ".xlsx\"");
        response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");

        Message m = new ReportMessage(user, report.getReportId(), form.getName(), params);
        try (Profile p = new ProfileImpl(m);
             FormWriter writer = new XLSXReportWriter(connectionProvider, report, form, params,
                 user)) {
            writer.write(response.getOutputStream());
        }
    }

    private void writeXLS(Report report, FormElementWrapper form, ApplicationUser user,
                          HttpServletResponse response,
                          List<DataValue> params)
        throws IOException, SQLException, ConnectException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + form.getName() + ".xls\"");
        // + getReportName(report, user) + ".xls\"");
        response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");

        Message m = new ReportMessage(user, report.getReportId(), form.getName(), params);
        try (Profile p = new ProfileImpl(m);
             FormWriter writer = new XLSReportWriter(connectionProvider, report, form, params,
                 user)) {
            writer.write(response.getOutputStream());
        }
    }

    private void writeHTML(Report report, FormElementWrapper form, ApplicationUser user,
                           HttpServletResponse response,
                           List<DataValue> params)
        throws IOException, SQLException, ConnectException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");

        Message m = new ReportMessage(user, report.getReportId(), form.getName(), params);
        try (Profile p = new ProfileImpl(m);
             HTMLReportWriter writer = new HTMLReportWriter(connectionProvider, form, params,
                 user)) {
            writer.setPrint(report.isPrint());
            writer.write(response.getOutputStream());
        }
    }

    private void writeCSV(Report report, FormElementWrapper form, ApplicationUser user,
                          HttpServletResponse response,
                          List<DataValue> params)
        throws IOException, SQLException, ConnectException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + form.getName() + ".csv\"");
        response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");

        Message m = new ReportMessage(user, report.getReportId(), form.getName(), params);
        try (Profile p = new ProfileImpl(m);
             CSVReportWriter writer = new CSVReportWriter(connectionProvider, form, params, user)) {
            writer.write(response.getOutputStream());
        }
    }

    private Report loadReport(String rpt, ApplicationUser user) {
        Report report = new Report(rpt);

        Map<PropertyType, PropertyValue> props = connector().getReportProperties(rpt, false, user);

        LocaleElement locale =
            new LocaleElement(user.getLocale().getLanguage(), user.getLocale().getCountry());

        PropertyValue propPrint = props.get(PropertyType.Print);
        if (propPrint != null) {
            report.setPage(propPrint.getValue(locale).getBoolean());
        }
        PropertyValue propPageNum = props.get(PropertyType.PageNum);
        if (propPageNum != null) {
            report.setPrint(propPrint.getValue(locale).getBoolean());
        }
        PropertyValue propFormId = props.get(PropertyType.FormId);
        if (propFormId != null) {
            report.setFormId(propFormId.getValue(locale).getString());
        }

        return report;
    }

    @SuppressWarnings("unchecked")
    private List<DataValue> loadParameters(Report report, ApplicationUser user,
                                           HttpSession session) {
        List<DataValue> result = new ArrayList<DataValue>();

        // Если сохраненные значения есть в контексте - достаем из контекста
        // Если нет, достаем из базы
        Map<String, DataValue> innerValues = (Map<String, DataValue>) session
            .getAttribute("report_id_" + report.getReportId());
        if (innerValues != null) {
            for (Entry<String, DataValue> entry : innerValues.entrySet()) {
                DataValue value = entry.getValue();
                value.setCode(entry.getKey());
                result.add(value);
            }
        }
        // TODO: Убедиться что параметры всегда будут в сессии
        /*
         * else { Map<FieldMetadata, DataValue> values = connector().getReportFields(
         * report.getReportId(), user); for (Entry<FieldMetadata, DataValue> entry :
         * values.entrySet()) { DataValue value = entry.getValue();
         * value.setCode(entry.getKey().getName()); result.add(value); } }
         */

        DataValue data = new DataValueImpl(DataType.STRING);
        data.setCode(AppConstant.WHIRL_REPORT_FORMAT);
        data.setValue(report.getFormat());
        result.add(data);
        return result;
    }

    private void writeError(Exception e, HttpServletResponse response) throws IOException {
        _log.error(e);
        e.printStackTrace();
        if (response != null) {
            response.setContentType("text/html");
            response.setCharacterEncoding(SrvConstant.ENCODING);
        }
        PrintStream ps = new PrintStream(response.getOutputStream());
        ps.print(e.getMessage());
        ps.flush();
        ps.close();
    }

}
