package org.whirlplatform.server.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.empire.db.DBDatabaseDriver;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowValue;
import org.whirlplatform.server.form.TemplateProcessor;

public class NamedParamResolver {

    protected Map<String, List<Integer>> paramMap = new HashMap<String, List<Integer>>();
    protected String resultSql;
    private DBDatabaseDriver driver;
    private Map<String, String> stringParams;
    private List<String> paramList = new ArrayList<String>();

    public NamedParamResolver(DBDatabaseDriver driver, String sql) {
        this(driver, sql, null);
    }

    public NamedParamResolver(DBDatabaseDriver driver, String sql, Map<String, DataValue> params) {
        this.driver = driver;
        this.stringParams = convertParameters(params);
        parse(template(sql, params));
    }

    private Map<String, String> convertParameters(Map<String, DataValue> params) {
        Map<String, String> result = new HashMap<String, String>();
        if (params != null) {
            for (Entry<String, DataValue> e : params.entrySet()) {
                result.put(e.getKey(), escapeParameter(e.getValue()));
            }
        }
        return result;
    }

    private String template(String sql, Map<String, DataValue> params) {
        Map<String, Object> model = new HashMap<String, Object>();
        for (Entry<String, DataValue> e : params.entrySet()) {
            if (e.getValue().getType() == DataType.LIST) {
                ListModelData m = e.getValue().getListModelData();
                model.put(e.getKey(), m != null ? m.getId() : null);
            } else if (e.getValue() instanceof RowListValue &&
                    isRowListEmpty((RowListValue) e.getValue())) {
                // Иначе неправильно работает определение null в wheresql(если
                // checkable,
                // но есть только selected запись, то подставлялся текст "null")
                model.put(e.getKey(), null);
            } else {
                model.put(e.getKey(), e.getValue() == null ? null : e.getValue().getObject());
            }
        }
        return TemplateProcessor.get().replace(sql, model);
    }

    private boolean isRowListEmpty(RowListValue r) {
        if (r == null || r.getRowList().size() == 0) {
            return true;
        } else return r.isCheckable() && r.getRowList().size() == 1 &&
                !r.getRowList().get(0).isChecked();
    }

    private String parse(String sql) {
        String result = "";
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inInClause = false;
        int index = 1;

        for (int i = 0; i < sql.length(); i++) {
            String c = String.valueOf(sql.charAt(i));
            if (inSingleQuote) {
                if ("'".equals(c))
                    inSingleQuote = false;
            } else if (inDoubleQuote) {
                if ("\"".equals(c))
                    inDoubleQuote = false;
            } else if ("'".equals(c)) {
                if (i + 6 < sql.length() && sql.charAt(i + 1) == '\'' && sql.charAt(i + 2) == ':') {
                    int j = i + 3;
                    while (j < sql.length() && Character.isJavaIdentifierPart(sql.charAt(j))
                            && sql.charAt(j + 1) != '\'')
                        j++;
                    if (sql.charAt(j + 1) == '\'' && sql.charAt(j + 2) == '\'') {
                        String name = sql.substring(i + 3, j + 1);
                        i += name.length() + 4;
                        if (!stringParams.isEmpty()) {
                            c = stringParams.get(name);
                            if (c == null) {
                                c = driver.getSQLPhrase(DBDatabaseDriver.SQL_NULL_VALUE);
                            } else {
                                c = "'" + c + "'";
                            }
                        } else {
                            c = "'?'";
                        }
                    } else {
                        inSingleQuote = true;
                    }
                } else {
                    inSingleQuote = true;
                }
            } else if ("\"".equals(c)) {
                inDoubleQuote = true;
            } else if ("(".equals(c)) {
                // определяем in перед (
                int j = i - 1;
                int chars = 0;
                while (chars < 2 && i > 0 && j > -1) {
                    if (Character.isJavaIdentifierPart(sql.charAt(j))) {
                        chars++;
                    }
                    j--;
                }
                String inWord = sql.substring(j + 1, i);
                if ("in".equalsIgnoreCase(inWord.trim()) &&
                        sql.substring(i + 1).trim().startsWith(":")) {
                    inInClause = true;
                }
            } else if (":".equals(c) && i + 1 < sql.length() &&
                    Character.isJavaIdentifierPart(sql.charAt(i + 1))) {
                int j = i + 2;
                while (j < sql.length() && Character.isJavaIdentifierPart(sql.charAt(j)))
                    j++;
                String name = sql.substring(i + 1, j);
                i += name.length();

                if (!stringParams.isEmpty()) {
                    c = stringParams.get(name);
                    if (c == null) {
                        c = driver.getSQLPhrase(DBDatabaseDriver.SQL_NULL_VALUE);
                    }
                    if (stringParams.get(name) != null) {
                        if (!(inInClause && sql.substring(i + 1).trim().startsWith(")"))) {
                            inInClause = false;
                        }
                    }
                } else {
                    c = "?";
                    paramList.add(name);
                }

                List<Integer> indList = paramMap.get(name);
                if (indList == null) {
                    indList = new ArrayList<Integer>();
                    paramMap.put(name, indList);
                }
                indList.add(index);

                index++;
            }
            result += c;
        }
        resultSql = result;
        return result;
    }

    private String escapeParameter(DataValue parameter) {
        if (parameter == null) {
            return null;
        }
        String result;

        // Если параметр - список (например из дерева)
        if (parameter instanceof RowListValue) {
            StringBuilder builder = new StringBuilder();
            for (RowValue row : ((RowListValue) parameter).getRowList()) {
                if ((!row.isChecked() && ((RowListValue) parameter).isCheckable())
                        || (!row.isSelected() && !((RowListValue) parameter).isCheckable())) {
                    continue;
                }
                String id = row.getId();
                if (NumberUtils.isNumber(id)) {
                    id = driver.getValueString(id, org.apache.empire.data.DataType.FLOAT);
                } else {
                    id = driver.getValueString(id, org.apache.empire.data.DataType.TEXT);
                }
                builder.append(id);
                builder.append(",");
            }
            if (builder.length() > 0) {
                result = builder.substring(0, builder.length() - 1);
            } else {
                result = null;
            }
            return result;
        }

        switch (parameter.getType()) {
            case BOOLEAN:
                result = driver.getValueString(parameter.getBoolean(),
                        org.apache.empire.data.DataType.BOOL);
                break;
            case DATE:
                result = driver.getValueString(parameter.getDate(),
                        org.apache.empire.data.DataType.DATETIME);
                break;
            case NUMBER:
                result = driver.getValueString(parameter.getDouble(),
                        org.apache.empire.data.DataType.FLOAT);
                break;
            case STRING:
                result = driver.getValueString(parameter.getString(),
                        org.apache.empire.data.DataType.TEXT);
                break;
            case LIST:
                ListModelData list = parameter.getListModelData();
                if (list == null) {
                    return null;
                }
                String id = list.getId();
                if (NumberUtils.isNumber(id)) {
                    id = driver.getValueString(id, org.apache.empire.data.DataType.FLOAT);
                } else {
                    id = driver.getValueString(id, org.apache.empire.data.DataType.TEXT);
                }
                result = id;
                break;
            default:
                result = driver.getValueString(parameter.getString(),
                        org.apache.empire.data.DataType.UNKNOWN);
                break;
        }
        return result;
    }

    public String getResultSql() {
        return resultSql;
    }

    public List<String> getOrderedParamNames() {
        return paramList;
    }
}
