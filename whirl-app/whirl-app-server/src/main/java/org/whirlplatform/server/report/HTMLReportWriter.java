package org.whirlplatform.server.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.ReportDataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.form.CellElementWrapper;
import org.whirlplatform.server.form.ColumnElementWrapper;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.form.FormWriter;
import org.whirlplatform.server.form.RowElementWrapper;
import org.whirlplatform.server.login.ApplicationUser;

public class HTMLReportWriter extends FormWriter {

    private XMLStreamWriter xmlStream;

    private int rows = -1;

    private boolean print = false;

    public HTMLReportWriter(ConnectionProvider connectionProvider, FormElementWrapper form,
                            Collection<DataValue> startParams, ApplicationUser user) {
        super(connectionProvider, form, startParams, user);
    }

    @Override
    public void write(OutputStream stream) throws IOException, SQLException, ConnectException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("escapeCharacters", false);
        try {
            xmlStream =
                    outputFactory.createXMLStreamWriter(new OutputStreamWriter(stream, "UTF-8"));

            writeHeader();
            prepareForm();

            build();

            writeFooter();

            xmlStream.flush();
            xmlStream.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void writeHeader() throws XMLStreamException {
        xmlStream.writeStartDocument();
        xmlStream.writeStartElement("html");
        xmlStream.writeDefaultNamespace("http://www.w3.org/1999/xhtml");

        xmlStream.writeStartElement("head");

        xmlStream.writeStartElement("meta");
        xmlStream.writeAttribute("http-equiv", "Content-Type");
        xmlStream.writeAttribute("content", "text/html; charset=utf-8");
        xmlStream.writeEndElement();

        xmlStream.writeStartElement("title");
        xmlStream.writeCharacters("Отчет");
        xmlStream.writeEndElement();

        xmlStream.writeEndElement(); // head

        xmlStream.writeStartElement("body");
        if (print) {
            writePrint();
        }

        xmlStream.writeStartElement("div");
        xmlStream.writeAttribute("id", form.getId());

        xmlStream.writeStartElement("table");
        writeTableStyle();

        writeColGroup();
        xmlStream.writeStartElement("tbody");
    }

    private void writePrint() throws XMLStreamException {
        xmlStream.writeStartElement("table");
        xmlStream.writeAttribute("id", "button-table");
        xmlStream.writeAttribute("width", "100%");

        xmlStream.writeStartElement("tr");
        xmlStream.writeStartElement("td");

        xmlStream.writeStartElement("a");
        xmlStream.writeAttribute("href", "#");
        xmlStream.writeAttribute("style", "color: #033351");

        String click = "document.getElementById('button-table').style.visibility='hidden';"
                + "var prtContent = document.getElementById('" + form.getId() + "');"
                + "var WinPrint = window.open('', '', 'left=-100, top=-100, width=1, height=1, "
                + "toolbar=0,scrollbars=0,status=0');"
                + "WinPrint.document.write(prtContent.innerHTML);" + "WinPrint.document.close();"
                + "WinPrint.focus();" + "WinPrint.print();" + "WinPrint.close();"
                + "document.getElementById('button-table').style.visibility='visible';";
        xmlStream.writeAttribute("onclick", click);

        xmlStream.writeStartElement("img");
        xmlStream.writeAttribute("style", "border: 0px none");
        xmlStream.writeAttribute("src", "image/base/ico_print.gif");
        xmlStream.writeEndElement();

        xmlStream.writeStartElement("span");
        xmlStream.writeAttribute("style", "padding: 3px 5px");
        xmlStream.writeEndElement();

        xmlStream.writeCharacters("Печать");
        xmlStream.writeEndElement(); // a

        xmlStream.writeEndElement(); // td
        xmlStream.writeEndElement(); // tr
        xmlStream.writeEndElement(); // table
        // <table id="button-table" width="100%">
        // <tr>
        // <td width="100%" align="right">
        // <xsl:element name="a">
        // <xsl:attribute name="href">#</xsl:attribute>
        // <xsl:attribute name="style">color: #033351</xsl:attribute>
        // <xsl:attribute name="onclick">
        // <![CDATA[
        // document.getElementById('button-table').style.visibility='hidden';
        // var prtContent = document.getElementById(']]><xsl:value-of
        // select="FORM_OPTIONS/@DFOBJ" /><![CDATA[');
        // var WinPrint =
        // window.open('','','left=-100,top=-100,width=1,height=1,toolbar=0,scrollbars=0,status=0');
        // WinPrint.document.write(prtContent.innerHTML);
        // WinPrint.document.close();
        // WinPrint.focus();
        // WinPrint.print();
        // WinPrint.close();
        // document.getElementById('button-table').style.visibility='visible';]]>
        // </xsl:attribute>
        // <img style="border: 0px none" src="image/base/ico_print.gif" />
        // <span style="padding: 3px 5px" />
        // Печать
        // </xsl:element>
        // </td>
        // </tr>
        // </table>
    }

    private void writeTableStyle() throws XMLStreamException {
        String style;
        if (form.isGrid()) {
            style =
                    "border-left: 1px solid; border-top: 1px solid;    "
                        + "border-collapse: collapse; border-spacing: 0; table-layout: fixed";
        } else {
            style = "border: 0px none " + form.getGridColor()
                    + "; border-collapse: collapse; border-spacing: 0; table-layout: fixed;";
        }
        xmlStream.writeAttribute("style", style);
    }

    private void writeColGroup() throws XMLStreamException {
        xmlStream.writeStartElement("colgroup");
        for (ColumnElementWrapper column : form.getFinalColMap().values()) {
            xmlStream.writeStartElement("col");
            if (column.getWidth() > 1) {
                xmlStream.writeAttribute("width", String.valueOf(column.getWidth()));
            }
            xmlStream.writeEndElement();
        }
        xmlStream.writeEndElement();
    }

    private void writeFooter() throws XMLStreamException {
        xmlStream.writeEndElement(); // tbody
        xmlStream.writeEndElement(); // table
        xmlStream.writeEndElement(); // div
        xmlStream.writeEndElement(); // body
        xmlStream.writeEndElement(); // html
        xmlStream.writeEndDocument();
    }

    @Override
    public void close() throws IOException {
        try {
            xmlStream.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected int nextRow() {
        rows = rows + 1;
        return rows;
    }

    @Override
    protected void writeRow(RowElementWrapper row) throws IOException {
        try {
            xmlStream.writeStartElement("tr");
            if (row.getHeight() > 0) {
                xmlStream.writeAttribute("height", String.valueOf(row.getHeight()));
            }
            for (CellElementWrapper cell : row.getCells()) {
                writeCell(cell);
            }
            xmlStream.writeEndElement(); // tr
            xmlStream.flush();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void writeCell(CellElementWrapper cell) throws XMLStreamException {
        xmlStream.writeStartElement("td");
        xmlStream.writeAttribute("colspan", String.valueOf(cell.getColSpan()));
        xmlStream.writeAttribute("rowspan", String.valueOf(cell.getRowSpan()));
        // write style
        writeStyle(cell);
        writeComponent(cell.getComponent());
        xmlStream.writeEndElement(); // td
    }

    private void writeStyle(CellElementWrapper cell) throws XMLStreamException {
        StringBuilder style = new StringBuilder();
        String gridColor = form.getGridColor();
        if (gridColor == null || gridColor.isEmpty()) {
            gridColor = "#000000";
        }
        if (cell.getBackgroundColor() != null) {
            style.append("background-color: " + cell.getBackgroundColor() + "; ");
        }
        if (form.isGrid()) {
            style.append("    border-right: 1px solid; border-bottom: 1px solid; border-color: "
                    + gridColor + "; ");
        }
        if (cell.getBorderBottom() > 0) {
            style.append(
                    "border-bottom: " + cell.getBorderBottom() + "px solid " + gridColor + "; ");
        }
        if (cell.getBorderTop() > 0) {
            style.append("border-top: " + cell.getBorderTop() + "px solid " + gridColor + "; ");
        }
        if (cell.getBorderLeft() > 0) {
            style.append("border-left: " + cell.getBorderLeft() + "px solid " + gridColor + "; ");
        }
        if (cell.getBorderRight() > 0) {
            style.append("border-right: " + cell.getBorderRight() + "px solid " + gridColor + "; ");
        }
        if (cell.getComponent() != null) {
            ComponentModel component = cell.getComponent();
            if (cell.getComponent().getType() == ComponentType.LabelType
                    || cell.getComponent().getType() == ComponentType.HtmlType) {
                if (component.getValue(PropertyType.Color.getCode()) != null) {
                    style.append(
                            "color: " + component.getValue(PropertyType.Color.getCode()) + "; ");
                }
                if (component.getValue(PropertyType.FontWeight.getCode()) != null) {
                    style.append("font-weight: "
                            + component.getValue(PropertyType.FontWeight.getCode()) + "; ");
                }
                if (component.getValue(PropertyType.FontSize.getCode()) != null) {
                    style.append(
                            "font-size: " + component.getValue(PropertyType.FontSize.getCode())
                                    + "; ");
                }
                if (component.getValue(PropertyType.FontStyle.getCode()) != null) {
                    style.append(
                            "font-style: " + component.getValue(PropertyType.FontStyle.getCode())
                                    + "; ");
                }
                if (component.getValue(PropertyType.FontFamily.getCode()) != null) {
                    style.append("font-family: "
                            + component.getValue(PropertyType.FontFamily.getCode()) + "; ");
                }
            }
        }
        xmlStream.writeAttribute("style", style.toString());
    }

    private void writeComponent(ComponentModel component) throws XMLStreamException {
        // start component
        if (component == null) {
            return;
        }
        if (component.hasValues()) {
            writeComponentProperties(component);
        }
        // end component
    }

    private void writeComponentProperties(ComponentModel component) throws XMLStreamException {
        if (ComponentType.LabelType == component.getType()
                || ComponentType.HtmlType == component.getType()) {
            String valueStr = !component.containsValue(PropertyType.Html.getCode()) ? null
                    : component.getValue(PropertyType.Html.getCode()).getString();
            String type = !component.containsValue(PropertyType.ReportDataType.getCode()) ? null
                    : component.getValue(PropertyType.ReportDataType.getCode()).getString();
            String fmt = !component.containsValue(PropertyType.ReportDataFormat.getCode()) ? null
                    : component.getValue(PropertyType.ReportDataFormat.getCode()).getString();

            if (valueStr == null || valueStr.isEmpty()) {
                // xmlStream.writeCharacters("");
                return;
            }

            if (fmt == null || fmt.isEmpty()) {
                xmlStream.writeCharacters(valueStr);
                return;
            }

            if (ReportDataType.NUMBER.name().equals(type)) {
                try {
                    DecimalFormat nf = new DecimalFormat(fmt);
                    xmlStream.writeCharacters(
                            nf.format(Double.parseDouble(valueStr.replace(",", "."))));

                } catch (NumberFormatException e) {
                    // xmlStream.writeCharacters("");
                }
            } else if (ReportDataType.DATE.name().equals(type)) {
                DateFormat dfIn = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONGEST);
                try {
                    SimpleDateFormat dfOut = new SimpleDateFormat(fmt);
                    xmlStream.writeCharacters(dfOut.format(dfIn.parse(valueStr)));
                } catch (ParseException e) {
                    // xmlStream.writeCharacters("");
                }
            } else {
                xmlStream.writeCharacters(valueStr);
            }
            // xmlStream.writeCharacters(component.getValue(PropertyType.Html
            // .getCode()));
        }
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

}
