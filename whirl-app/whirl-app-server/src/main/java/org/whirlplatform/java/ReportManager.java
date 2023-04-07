package org.whirlplatform.java;

import com.google.gwt.core.shared.GwtIncompatible;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.FormWriter;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.report.CSVReportWriter;
import org.whirlplatform.server.report.HTMLReportWriter;
import org.whirlplatform.server.report.Report;
import org.whirlplatform.server.report.XLSReportWriter;
import org.whirlplatform.server.report.XLSXReportWriter;

@GwtIncompatible
public class ReportManager {

    // Параметры отчета
    private Integer horizontalDpi;

    private Integer verticalDpi;

    private ApplicationUser user;

    private Connector connector;

    private ConnectionProvider connectionProvider;

    @javax.inject.Inject
    protected ReportManager(ApplicationUser user, Connector connector,
                            ConnectionProvider connectionProvider) {
        this.user = user;
        this.connector = connector;
        this.connectionProvider = connectionProvider;
    }

    public void writeReportXLS(String code, List<DataValue> params, OutputStream out)
        throws Exception {
        writeReport(code, true, AppConstant.REPORT_FORMAT_XLS, params, out);
    }

    public void writeReportXLSX(String code, List<DataValue> params, OutputStream out)
        throws Exception {
        writeReport(code, true, AppConstant.REPORT_FORMAT_XLSX, params, out);
    }

    public void writeReportHTML(String code, List<DataValue> params, OutputStream out)
        throws Exception {
        writeReport(code, true, AppConstant.REPORT_FORMAT_HTML, params, out);
    }

    public void writeReportCSV(String code, List<DataValue> params, OutputStream out)
        throws Exception {
        writeReport(code, true, AppConstant.REPORT_FORMAT_CSV, params, out);
    }

    public void setHorizontalDpi(int horizontalDpi) {
        this.horizontalDpi = horizontalDpi;
    }

    public void setVerticalDpi(int verticalDpi) {
        this.verticalDpi = verticalDpi;
    }

    // private Collection<org.whirlplatform.meta.shared.DataValueImpl>
    // convertParams(Collection<DataValue> params) {
    // Collection<org.whirlplatform.meta.shared.DataValueImpl> values = new
    // ArrayList<org.whirlplatform.meta.shared.DataValueImpl>();
    // for (DataValue v : params) {
    // values.add(v.asInternal());
    // }
    // return values;
    // }

    public void writeReport(String codeOrId, boolean isCode, String format, List<DataValue> params,
                            OutputStream out) throws Exception {

        Report report = loadReport(codeOrId, isCode, user);

        report.setFormat(format);
        if (horizontalDpi != null && "auto".equals(report.getHorizontalDPI())) {
            report.setHorizontalDPI(String.valueOf(horizontalDpi));
        }
        if (verticalDpi != null && "auto".equals(report.getVerticalDPI())) {
            report.setVerticalDPI(String.valueOf(verticalDpi));
        }

        FormElementWrapper form = connector.getFormRepresent(report.getFormId(), params, user);

        // Collection<org.whirlplatform.meta.shared.DataValue> values = new
        // ArrayList<org.whirlplatform.meta.shared.DataValue>();
        // for (DataValue v : params) {
        // values.add(v.asInternal());
        // }

        try (FormWriter writer = createWriter(format, report, form, params)) {
            if (writer != null) {
                writer.write(out);
                out.flush();
            } else {
                throw new Exception("Writer creation error");
            }
        }

    }

    private FormWriter createWriter(String format, Report report, FormElementWrapper form,
                                    List<DataValue> params) {
        if (AppConstant.REPORT_FORMAT_XLSX.equalsIgnoreCase(format)) {
            return new XLSXReportWriter(connectionProvider, report, form, params, user);
        } else if (AppConstant.REPORT_FORMAT_XLS.equalsIgnoreCase(format)) {
            return new XLSReportWriter(connectionProvider, report, form, params, user);
        } else if (AppConstant.REPORT_FORMAT_HTML.equalsIgnoreCase(format)) {
            return new HTMLReportWriter(connectionProvider, form, params, user);
        } else if (AppConstant.REPORT_FORMAT_CSV.equalsIgnoreCase(format)) {
            return new CSVReportWriter(connectionProvider, form, params, user);
        }
        throw new UnsupportedOperationException("Roport format not supported: " + format);
    }

    private Report loadReport(String codeOrId, boolean isCode, ApplicationUser user) {

        Map<PropertyType, PropertyValue> props =
            connector.getReportProperties(codeOrId, isCode, user);

        if (props == null) {
            throw new CustomException("Report is not found");
        }

        Report report = new Report(isCode ? "" : codeOrId);

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
}
