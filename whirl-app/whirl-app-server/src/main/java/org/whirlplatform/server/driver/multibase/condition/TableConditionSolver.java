package org.whirlplatform.server.driver.multibase.condition;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.derby.DBDatabaseDriverDerby;
import org.apache.empire.db.oracle.DBDatabaseDriverOracle;
import org.apache.empire.exceptions.EmpireException;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RightElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.SQLCondition;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

@SuppressWarnings("serial")
public class TableConditionSolver extends AbstractConditionSolver {

    private static Logger _log = LoggerFactory.getLogger(TableConditionSolver.class);

    private AbstractTableElement table;
    private ApplicationElement application;
    private ApplicationUser user;
    private ConnectionWrapper connection;
    private Map<String, DataValue> params;
    private boolean view = false;
    private boolean insert = false;
    private boolean update = false;
    private boolean delete = false;
    private boolean ready = false;
    private String restrictions;

    // переменные необходимые в процессе расчета
    private RightType currentType;
    private boolean currentTable = false;
    private boolean currentColumn = false;
    private TableColumnElement nowColumn;
    private Set<SQLCondition> sqlViewConditions = new HashSet<>();
    private Set<SQLCondition> sqlInsertConditions = new HashSet<>();
    private Set<SQLCondition> sqlUpdateConditions = new HashSet<SQLCondition>();
    private Set<SQLCondition> sqlDeleteConditions = new HashSet<SQLCondition>();
    // private Set<SQLCondition> sqlRestrictConditions = new
    // HashSet<SQLCondition>();
    private Multimap<TableColumnElement, SQLCondition> sqlColumnViewConditions =
        HashMultimap.create();
    private Multimap<TableColumnElement, SQLCondition> sqlColumnEditConditions =
        HashMultimap.create();
    private Map<TableColumnElement, Boolean> columnView =
        new HashMap<TableColumnElement, Boolean>();
    private Map<TableColumnElement, Boolean> columnEdit =
        new HashMap<TableColumnElement, Boolean>();

    private Set<String> sqlColumnTableInsert = new HashSet<String>();
    private Set<String> sqlColumnTableUpdate = new HashSet<String>();
    private Set<String> sqlColumnTableDelete = new HashSet<String>();
    private Multimap<TableColumnElement, String> sqlColumnColumnView = HashMultimap.create();
    private Multimap<TableColumnElement, String> sqlColumnColumnEdit = HashMultimap.create();

    public TableConditionSolver(AbstractTableElement table, ApplicationElement application,
                                Map<String, DataValue> params, ApplicationUser user,
                                ConnectionWrapper connection) {
        this.table = table;
        this.application = application;
        this.connection = connection;
        this.user = user;
        this.params = params;
    }

    public boolean needNext() {
        return !allowed;
    }

    @Override
    public void solve(BooleanCondition condition) {
        allowed = allowed || condition.getValue();
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void solve(SQLCondition condition) {
        if (currentTable) {
            switch (currentType) {
                case VIEW:
                    sqlInsertConditions.add(condition);
                    break;
                case ADD:
                    sqlInsertConditions.add(condition);
                    break;
                case EDIT:
                    sqlUpdateConditions.add(condition);
                    break;
                case DELETE:
                    sqlDeleteConditions.add(condition);
                    break;
                // case RESTRICT:
                // sqlRestrictConditions.add(condition);
                // break;
                default:
                    break;
            }
        } else if (currentColumn && nowColumn != null) {
            switch (currentType) {
                case EDIT:
                    sqlColumnEditConditions.put(nowColumn, condition);
                    break;
                case VIEW:
                    sqlColumnViewConditions.put(nowColumn, condition);
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isViewable() {
        allowed();
        return view;
    }

    public boolean isInsertable() {
        allowed();
        return insert;
    }

    public boolean isUpdateable() {
        allowed();
        return update;
    }

    public boolean isDeletable() {
        allowed();
        return delete;
    }

    public boolean isEditable(TableColumnElement column) {
        allowed();
        if (!columnEdit.containsKey(column)) {
            return false;
        }
        return columnEdit.get(column);
    }

    public boolean isViewable(TableColumnElement column) {
        allowed();
        if (!columnView.containsKey(column)) {
            return false;
        }
        return columnView.get(column);
    }

    public String getRestrictions() {
        return restrictions;
    }

    @Override
    public boolean allowed() {
        if (ready) {
            return false;
        }
        // собираем все права
        // сначала собираем все простые

        // по таблице на просмотр
        checkTableViewBooleanPrepareOther();

        // по таблице на вставку
        checkTableInsertBooleanPrepareOther();

        // по таблице на редактирование
        checkTableUpdateBooleanPrepareOther();

        // по таблице на удаление
        checkTableDeleteBooleanPrepareOther();

        // по таблице ограничения
        // checkTableRestrictPrepareOther();

        // по колонкам на просмотр
        checkColumnViewBooleanPrepareOther();

        // по колонкам на редактирование
        checkColumnEditBooleanPrepareOther();

        // теперь по тем где доступа еще нет начинаем вычислять
        try {
            checkSqlConditions();
            // addRestrictions();
        } catch (SQLException | EmpireException e) {
            _log.warn("Unable to check table SQL conditions: "
                + /* table.getTableName() */table.getName(), e); // TODO:
            // DynamicDataSource
        }

        ready = true;
        return false;
    }

    private Collection<RightElement> filter(Collection<RightElement> all, RightType type) {
        Set<RightElement> result = new HashSet<RightElement>();
        for (RightElement r : all) {
            if (type == r.getType()) {
                result.add(r);
            }
        }
        return result;
    }

    private void checkTableViewBooleanPrepareOther() {
        currentTable = true;
        currentType = RightType.VIEW;
        Set<RightElement> rights = new HashSet<RightElement>();
        RightCollectionElement right = application.getTableRights(table);
        for (String g : user.getGroups()) {
            rights.addAll(filter(right.getGroupRights(application.getGroup(g)), currentType));
        }
        // если нет прав на группы, то берем права по приложению
        if (rights.isEmpty()) {
            rights.addAll(filter(right.getApplicationRights(), currentType));
        }
        checkAllowed(rights, currentType);
        view = allowed;
        allowed = false;
        currentType = null;
        currentTable = false;
    }

    private void checkTableInsertBooleanPrepareOther() {
        currentTable = true;
        currentType = RightType.ADD;
        Set<RightElement> rights = new HashSet<RightElement>();
        RightCollectionElement right = application.getTableRights(table);
        for (String g : user.getGroups()) {
            rights.addAll(filter(right.getGroupRights(application.getGroup(g)), currentType));
        }
        // если нет прав на группы, то берем права по приложению
        if (rights.isEmpty()) {
            rights.addAll(filter(right.getApplicationRights(), currentType));
        }
        checkAllowed(rights, currentType);
        insert = allowed;
        allowed = false;
        currentType = null;
        currentTable = false;
    }

    private void checkTableUpdateBooleanPrepareOther() {
        currentTable = true;
        currentType = RightType.EDIT;
        Set<RightElement> rights = new HashSet<RightElement>();
        RightCollectionElement right = application.getTableRights(table);
        for (String g : user.getGroups()) {
            rights.addAll(filter(right.getGroupRights(application.getGroup(g)), currentType));
        }
        // если нет прав на группы, то берем права по приложению
        if (rights.isEmpty()) {
            rights.addAll(filter(right.getApplicationRights(), currentType));
        }
        checkAllowed(rights, currentType);
        update = allowed;
        allowed = false;
        currentType = null;
        currentTable = false;
    }

    private void checkTableDeleteBooleanPrepareOther() {
        currentTable = true;
        currentType = RightType.DELETE;
        Set<RightElement> rights = new HashSet<RightElement>();
        RightCollectionElement right = application.getTableRights(table);
        for (String g : user.getGroups()) {
            rights.addAll(filter(right.getGroupRights(application.getGroup(g)), currentType));
        }
        // если нет прав на группы, то берем права по приложению
        if (rights.isEmpty()) {
            rights.addAll(filter(right.getApplicationRights(), currentType));
        }
        checkAllowed(rights, currentType);
        delete = allowed;
        allowed = false;
        currentType = null;
        currentTable = false;
    }

    // private void checkTableRestrictPrepareOther() {
    // currentTable = true;
    // currentType = RightType.RESTRICT;
    // Set<RightElement> rights = new HashSet<RightElement>();
    // RightCollectionElement right = application.getTableRights(table);
    // for (String g : user.getGroups()) {
    // rights.addAll(filter(right.getGroupRights(application.getGroup(g)),
    // currentType));
    // }
    // // если нет прав на группы, то берем права по приложению
    // if (rights.isEmpty()) {
    // rights.addAll(filter(right.getApplicationRights(), currentType));
    // }
    // checkAllowed(rights, currentType);
    // delete = allowed;
    // allowed = false;
    // currentType = null;
    // currentTable = false;
    // }

    private void checkColumnViewBooleanPrepareOther() {
        if (table instanceof PlainTableElement) {

            currentColumn = true;
            currentType = RightType.VIEW;

            for (TableColumnElement c : ((PlainTableElement) table).getColumns()) {
                nowColumn = c;
                Set<RightElement> rights = new HashSet<RightElement>();
                RightCollectionElement right = application.getTableColumnRights(c);
                for (String g : user.getGroups()) {
                    rights.addAll(
                        filter(right.getGroupRights(application.getGroup(g)), currentType));
                }
                // если нет прав на группы, то берем права по приложению
                if (rights.isEmpty()) {
                    rights.addAll(filter(right.getApplicationRights(), currentType));
                }
                checkAllowed(rights, currentType);

                setColumnViewAllowance(c, allowed);
                nowColumn = null;
                allowed = false;
            }
            currentType = null;
            currentColumn = false;
        }
    }

    private void checkColumnEditBooleanPrepareOther() {
        if (table instanceof PlainTableElement) {
            currentColumn = true;
            currentType = RightType.EDIT;

            for (TableColumnElement c : ((PlainTableElement) table).getColumns()) {
                nowColumn = c;
                Set<RightElement> rights = new HashSet<RightElement>();
                RightCollectionElement right = application.getTableColumnRights(c);
                for (String g : user.getGroups()) {
                    rights.addAll(
                        filter(right.getGroupRights(application.getGroup(g)), currentType));
                }
                // если нет прав на группы, то берем права по приложению
                if (rights.isEmpty()) {
                    rights.addAll(filter(right.getApplicationRights(), currentType));
                }
                checkAllowed(rights, currentType);

                setColumnEditAllowance(c, allowed);
                nowColumn = null;
                allowed = false;
            }
            currentType = null;
            currentColumn = false;
        }
    }

    private void setColumnViewAllowance(TableColumnElement column, boolean allowed) {
        setColumnAllowance(column, allowed, columnView);
    }

    private void setColumnEditAllowance(TableColumnElement column, boolean allowed) {
        setColumnAllowance(column, allowed, columnEdit);
    }

    private void setColumnAllowance(TableColumnElement column, boolean allowed,
                                    Map<TableColumnElement, Boolean> map) {
        Boolean v = map.get(column);
        if (v == null) {
            v = allowed;
        } else {
            v = v || allowed;
        }
        map.put(column, v);
    }

    private void checkSqlConditions() throws SQLException {
        if (sqlInsertConditions.isEmpty() && sqlUpdateConditions.isEmpty()
            && sqlDeleteConditions.isEmpty()
            && sqlColumnViewConditions.isEmpty() && sqlColumnEditConditions.isEmpty()) {
            return;
        }

        // TODO: Dynamic DataSource Что делать не с таблицами?
        if (!(table instanceof PlainTableElement)) {
            return;
        }

        List<String> subColumns = new ArrayList<String>();
        List<String> subQueries = new ArrayList<String>();

        // запросы для вычисления доступов по таблице
        if (!insert) {
            sqlColumnTableInsert.addAll(
                createConditionSubQueries("TI_", ((PlainTableElement) table).getTableName(),
                    sqlInsertConditions, subColumns, subQueries, false));
        }
        if (!update) {
            sqlColumnTableUpdate.addAll(
                createConditionSubQueries("TU_", ((PlainTableElement) table).getTableName(),
                    sqlUpdateConditions, subColumns, subQueries, false));
        }
        if (!delete) {
            sqlColumnTableDelete.addAll(
                createConditionSubQueries("TD_", ((PlainTableElement) table).getTableName(),
                    sqlDeleteConditions, subColumns, subQueries, false));
        }

        // запросы для вычисления доступов по колонкам
        for (TableColumnElement c : sqlColumnViewConditions.keySet()) {
            // if (!isViewable(c)) {
            if (!columnView.get(c)) {
                sqlColumnColumnView.putAll(c, createConditionSubQueries("CV_", c.getColumnName(),
                    sqlColumnViewConditions.get(c), subColumns, subQueries, true));
            }
        }
        for (TableColumnElement c : sqlColumnEditConditions.keySet()) {
            if (!columnEdit.get(c)) {
                sqlColumnColumnEdit.putAll(c, createConditionSubQueries("CE_", c.getColumnName(),
                    sqlColumnEditConditions.get(c), subColumns, subQueries, true));
            }
        }

        // если нет ни одного условия
        if (subQueries.size() == 0) {
            return;
        }

        String query = makeQuery(subQueries);
        _log.info(query);

        try (ResultSet rs = connection.getDatabaseDriver()
            .executeQuery(query, null, false, connection)) {
            if (rs.next()) {
                // достаем вычисленные значения
                // таблица
                for (String dbCol : sqlColumnTableInsert) {
                    insert = insert || getSubQueryValue(rs, subColumns.indexOf(dbCol) + 1);
                }
                for (String dbCol : sqlColumnTableUpdate) {
                    update = update || getSubQueryValue(rs, subColumns.indexOf(dbCol) + 1);
                }
                for (String dbCol : sqlColumnTableDelete) {
                    delete = delete || getSubQueryValue(rs, subColumns.indexOf(dbCol) + 1);
                }

                // колонки
                for (TableColumnElement col : sqlColumnColumnView.keySet()) {
                    for (String dbCol : sqlColumnColumnView.get(col)) {
                        setColumnViewAllowance(col,
                            getSubQueryValue(rs, subColumns.indexOf(dbCol) + 1));
                    }
                }
                for (TableColumnElement col : sqlColumnColumnEdit.keySet()) {
                    for (String dbCol : sqlColumnColumnEdit.get(col)) {
                        setColumnEditAllowance(col,
                            getSubQueryValue(rs, subColumns.indexOf(dbCol) + 1));
                    }
                }
            }
        }
    }

    private Collection<String> createConditionSubQueries(String columnPrefix, String objectName,
                                                         Collection<SQLCondition> list,
                                                         List<String> subColumns,
                                                         List<String> subQueries, boolean column) {
        Set<String> result = new HashSet<String>();
        DBDatabaseDriver driver = connection.getDatabaseDriver();
        int index = 0;
        for (SQLCondition condition : list) {
            String columnName = columnPrefix + objectName + index;
            String resolvedValue = resolveValue(driver, condition.getValue(), objectName, column);
            String q = "(" + resolvedValue + ")"
                + driver.getSQLPhrase(DBDatabaseDriver.SQL_RENAME_COLUMN) + columnName;
            subColumns.add(columnName);
            subQueries.add(q);
            result.add(columnName);
            index++;
        }
        return result;
    }

    private String makeQuery(Collection<String> subQueries) {
        DBDatabaseDriver driver = connection.getDatabaseDriver();

        StringBuilder result = new StringBuilder();
        result.append("select ");

        for (String q : subQueries) {
            result.append(q + " ,");
        }
        result.deleteCharAt(result.length() - 1);

        if (driver instanceof DBDatabaseDriverOracle) {
            result.append("from DUAL");
        } else if (driver instanceof DBDatabaseDriverDerby) {
            result.append("from SYSIBM.SYSDUMMY1");
        }
        return result.toString();
    }

    private boolean getSubQueryValue(ResultSet rs, int colIndex) throws SQLException {
        DBDatabaseDriver driver = connection.getDatabaseDriver();
        Boolean result = (Boolean) driver.getResultValue(rs, colIndex, DataType.BOOL);
        if (result == null) {
            return false;
        }
        return result;
    }
    //
    // private void addRestrictions() {
    // if (sqlRestrictConditions.isEmpty()) {
    // return;
    // }
    //// sqlColumnTableUpdate.addAll(createConditionSubQueries("TR_",
    //// sqlRestrictConditions, subColumns, subQueries));
    //
    // StringBuilder builder = new StringBuilder();
    // for (SQLCondition condition : sqlRestrictConditions) {
    // builder.append("(");
    // builder.append(condition.getValue());
    // builder.append(") AND ");
    // }
    // if (builder.length() > 0) {
    // restrictions = builder.substring(0, builder.length() - 5);
    // }
    // }

    protected String resolveValue(DBDatabaseDriver driver, String value, String objectName,
                                  boolean column) {
        if (value == null) {
            return null;
        }
        NamedParamResolver resolver =
            new NamedParamResolver(driver, value, processParams(params, objectName, column));
        return resolver.getResultSql();
    }

    protected Map<String, DataValue> processParams(Map<String, DataValue> paramMap,
                                                   String objectName, boolean column) {
        ApplicationUser user = connection.getUser();
        Map<String, DataValue> result = new HashMap<String, DataValue>();

        DataValue data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
        data.setCode("PFUSER");
        data.setValue(user.getId());
        result.put(data.getCode(), data);

        data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
        data.setCode("PFIP");
        data.setValue(user.getIp());
        result.put(data.getCode(), data);

        data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
        data.setCode("PFROLE");
        data.setValue(user.getApplication().getId());
        result.put(data.getCode(), data);

        if (column) {
            data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
            data.setCode("COLUMN_NAME");
            data.setValue(objectName);
            result.put(data.getCode(), data);
        } else {
            data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
            data.setCode("TABLE_NAME");
            data.setValue(objectName);
            result.put(data.getCode(), data);
        }

        if (paramMap == null) {
            return result;
        }
        for (Entry<String, DataValue> entry : paramMap.entrySet()) {
            entry.getValue().setCode(entry.getValue().getCode());
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
