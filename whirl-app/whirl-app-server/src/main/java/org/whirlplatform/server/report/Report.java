package org.whirlplatform.server.report;

public class Report {

    private String reportId;

    private String format;

    private String formId;

    private boolean print;

    private boolean page;

    private String horizontalDPI;

    private String verticalDPI;

    public Report(String reportId) {
        this.reportId = reportId;
    }

    public String getReportId() {
        return reportId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isPage() {
        return page;
    }

    public void setPage(boolean page) {
        this.page = page;
    }

    public String getHorizontalDPI() {
        return horizontalDPI;
    }

    public void setHorizontalDPI(String horizontalDPI) {
        this.horizontalDPI = horizontalDPI;
    }

    public String getVerticalDPI() {
        return verticalDPI;
    }

    public void setVerticalDPI(String verticalDPI) {
        this.verticalDPI = verticalDPI;
    }
}
