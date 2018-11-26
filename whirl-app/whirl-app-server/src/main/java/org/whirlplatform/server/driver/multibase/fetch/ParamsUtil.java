package org.whirlplatform.server.driver.multibase.fetch;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.server.driver.multibase.Getter;
import org.whirlplatform.server.driver.multibase.fetch.oracle.OraclePlainDataFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParamsUtil {

    private static Logger _log = LoggerFactory.getLogger(OraclePlainDataFetcher.class);

    private static String asString(RowListValue value) {
        StringBuilder result = new StringBuilder();
        for (RowValue v : value.getRowList()) {
            result.append(v.getId());
            result.append(";" + Getter.toDBString(v.isSelected()));
            result.append(";" + Getter.toDBString(v.isChecked()));
            result.append(";" + Getter.toDBString(v.isExpanded()));
            result.append(":");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static List<Object> namedFunctionParams(List<DataValue> params, ApplicationUser user) {
        List<Object> result = new ArrayList<Object>();
        Document xml = DocumentHelper.createDocument();
        Element root = xml.addElement("function-input");
        Element parameters = root.addElement("parameters");
        for (DataValue v : params) {
            Element param = parameters.addElement("parameter");

            param.addAttribute("type", v.getType().name());
            param.addAttribute("code", v.getCode());

            if (v instanceof RowListValue) {
                result.add(asString((RowListValue) v));
                param.setText(asString((RowListValue) v));
                param.attributeValue("rowlist", "true");
            } else if (v.getType() == DataType.LIST) {
                ListModelData list = v.getListModelData();
                if (list != null) {
                    param.setText(list.getId());
                }
            } else if (v.getType() == DataType.FILE) {
                FileValue file = v.getFileValue();
                result.add(file.getInputStream());
            } else if (v.getType() == DataType.STRING) {
                param.addCDATA(v.getString());
            } else if (v.getType() == DataType.DATE) {
                Date dv = v.getDate();
                if (dv != null) {
                    DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    format.setTimeZone(user.getTimeZone());
                    String srtDate = format.format(dv);
                    param.setText(srtDate);
                } else {
                    param.setText(Getter.toDBString(v.getObject()));
                }
            } else {
                if (v.getType() == null) {
                    _log.warn("Parameter " + v.getCode() + " not found for event. Null value passed.");
                }
                param.setText(Getter.toDBString(v.getObject()));
            }
        }
        result.add(xml.asXML());
        return result;
    }

    public static List<Object> listFunctionParams(List<DataValue> params, ApplicationUser user) {
        List<Object> result = new ArrayList<Object>();
        for (DataValue v : params) {
            if (v.getObject() == null) {
                result.add(null);
            } else if (v instanceof RowListValue) {
                result.add(asString((RowListValue) v));
            } else if (v.getType() == DataType.LIST) {
                ListModelData list = v.getListModelData();
                if (list != null) {
                    result.add(list.getId());
                } else {
                    result.add(null);
                }
            } else if (v.getType() == DataType.FILE) {
                FileValue file = v.getFileValue();
                result.add(file.getInputStream());
            } else if (v.getType() == DataType.DATE) {
                result.add(v);
            } else {
                if (v.getType() == null) {
                    _log.warn("Parameter " + v.getCode() + " has null type. Value " + v.getObject() + " passed.");
                }
                result.add(v.getObject());
            }
        }
        return result;
    }
}
