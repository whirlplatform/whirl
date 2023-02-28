package org.whirlplatform.server.driver.multibase.fetch.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.expr.compare.DBCompareColExpr;
import org.apache.empire.db.expr.compare.DBCompareExpr;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FilterValue;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RightElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.SQLCondition;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.utils.TypesUtil;

public class PlainTableFetcherHelper extends AbstractMultiFetcher {
    public Map<FieldMetadata, TableColumnElement> tableColumns = new HashMap<>();
    public boolean ready = false;
    public DBDatabase dbDatabase;
    public DBTable dbTable;
    public DBColumn dbPrimaryKey;
    public DBTable topDbTable;
    public DBColumn topDbPrimaryKey;
    public List<DBCompareExpr> where = new ArrayList<DBCompareExpr>();
    public DBColumnExpr countColumn;

    public PlainTableFetcherHelper(ConnectionWrapper connectionWrapper,
                                   DataSourceDriver datasourceDriver) {
        super(connectionWrapper, datasourceDriver);
    }

    public void prepare(ClassMetadata metadata, PlainTableElement table,
                        ClassLoadConfig loadConfig) {
        prepare(metadata, table, loadConfig, false);
    }

    protected void prepare(ClassMetadata metadata, PlainTableElement table,
                           ClassLoadConfig loadConfig, boolean tree) {
        this.dbDatabase = createAndOpenDatabase(table.getSchema().getSchemaName());

        String viewName =
            table.getView() != null && !StringUtils.isEmpty(table.getView().getViewName())
                ? table.getView().getViewName() :
                table.getTableName();

        this.dbTable = new DBTable(viewName, this.dbDatabase, "t");

        if (table.getIdColumn() != null) {
            // добавляем колонку первичного ключа если ее еще нет
            TableColumnElement c = table.getIdColumn();
            this.dbPrimaryKey = this.dbTable.getColumn(c.getColumnName());
            if (this.dbPrimaryKey == null) {
                this.dbPrimaryKey = this.dbTable.addColumn(c.getColumnName(),
                    TypesUtil.toEmpireType(c.getType(),
                        c.getListTable() == null ? null
                            : getDataSourceDriver().createDataFetcher(c.getListTable())
                            .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
            }
        } else {
            System.out.println("");
        }

        // Добавление стилизованных колонок в дереве по новому алгоритму
        if (tree) {
            TableColumnElement nameColumn =
                table.getColumn(((TreeClassLoadConfig) loadConfig).getLabelExpression());
            if (nameColumn.getConfigColumn() != null) {
                addConfigColumn(this.dbTable, nameColumn);
            }
        }

        for (FieldMetadata f : metadata.getFields()) {
            TableColumnElement c = table.getColumn(f.getName());

            if (c == table.getIdColumn() || (c.isHidden() && c == table.getDeleteColumn())) {
                continue;
            }

            this.tableColumns.put(f, c);
            DataType type = TypesUtil.toEmpireType(c.getType(), c.getListTable() == null ? null
                : getDataSourceDriver().createDataFetcher(c.getListTable())
                .getIdColumnType(table));
            // TODO: Dynamic DataSource Получение нужного fetcher'а, и из него получение metadata?

            if (!StringUtils.isEmpty(c.getConfigColumn())) {
                // Новый алгоритм
                addConfigColumn(dbTable, c);
            }

            // Если поле - функция, добавляем её на этапе формирования запроса
            if (StringUtils.isEmpty(c.getFunction())) {
                this.dbTable.addColumn(c.getColumnName(), type,
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
            }
        }

        try {
            this.topDbTable = (DBTable) this.dbTable.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // this.topDbTable.setAlias("t");
        if (this.dbPrimaryKey != null) {
            this.topDbPrimaryKey = this.topDbTable.getColumn(this.dbPrimaryKey.getName());
        }

        // собираем фильтры
        for (FilterValue filter : loadConfig.getFilters()) {
            DBColumnExpr col;

            // TODO: add functionality for file column
            if (filter.getMetadata().getType()
                == org.whirlplatform.meta.shared.data.DataType.FILE) {
                col = this.dbTable.getColumn(filter.getMetadata().getLabelExpression());
            } else {
                col = this.dbTable.getColumn(filter.getMetadata().getName());
            }
            DBCompareExpr expr = null;
            Object firstValue;
            if (filter.getMetadata().getType() == org.whirlplatform.meta.shared.data.DataType.LIST
                && filter.getFirstValue() != null) {
                firstValue = ((ListModelData) filter.getFirstValue()).getId();
            } else {
                firstValue = filter.getFirstValue();
            }
            switch (filter.getType()) {
                case NO_FILTER:
                    break;
                case EQUALS:
                    expr = createEquals(col, firstValue);
                    break;
                case CONTAINS:
                    expr = createContains(col, firstValue);
                    break;
                case NOT_CONTAINS:
                    expr = createNotContains(col, firstValue);
                    break;
                case EMPTY:
                    expr = createEmpty(col);
                    break;
                case NOT_EMPTY:
                    expr = createNotEmpty(col);
                    break;
                case START_WITH:
                    expr = createStartWith(col, firstValue);
                    break;
                case END_WITH:
                    expr = createEndWith(col, firstValue);
                    break;
                case LOWER:
                    expr = createLower(col, firstValue);
                    break;
                case GREATER:
                    expr = createGreater(col, firstValue);
                    break;
                case BETWEEN:
                    expr = createBetween(col, firstValue, filter.getSecondValue());
                    break;
                case REVERSE:
                    expr = createReverse(col, firstValue);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type");
            }
            if (expr != null) {
                this.where.add(expr);
            }
        }

        where.addAll(getWhereRestrictions(this.dbDatabase, metadata, loadConfig, table));
    }

    /**
     * Добавляется колонка с конфигом(пока используется только для стилей)
     */
    protected void addConfigColumn(DBTable tbl, TableColumnElement column) {
        tbl.addColumn(column.getConfigColumn(), DataType.TEXT, 0, false);
    }

    // Или склеивать все в одну строку и возвращать один DBCompareExpr?
    protected List<DBCompareExpr> getWhereRestrictions(DBDatabase db, ClassMetadata metadata,
                                                       ClassLoadConfig config,
                                                       PlainTableElement table) {
        List<DBCompareExpr> result = new ArrayList<DBCompareExpr>();

        // Добавление ограничений
        // String restriction = metadata.getRestriction();
        // if (restriction != null && !restriction.isEmpty()) {
        // String whereSql = resolveValue(driver, restriction,
        // processParams(config.getParameters()));
        // DBColumnExpr expr = db.getValueExpr(whereSql, DataType.UNKNOWN);
        // result.add(new DBCompareColExpr(expr, DBCmpType.NONE, " "));
        // }

        // Добавление whereSql
        if (config.getWhereSql() != null && !config.getWhereSql().isEmpty()) {
            String whereSql =
                resolveValue(config.getWhereSql(), processParams(config.getParameters()));
            DBColumnExpr expr = db.getValueExpr(whereSql, DataType.UNKNOWN);
            result.add(new DBCompareColExpr(expr, DBCmpType.NONE, " "));
        }

        RightCollectionElement rightCollection = getUser().getApplication().getTableRights(table);
        // Добавление ограничений по правам приложения
        for (RightElement right : rightCollection.getApplicationRights()) {
            if (RightType.RESTRICT == right.getType()
                && right.getCondition() instanceof SQLCondition) {
                String value = (String) right.getCondition().getValue();
                if (value != null && !value.isEmpty()) {
                    String resolvedValue =
                        resolveValue(value, processParams(config.getParameters()));
                    DBColumnExpr expr =
                        db.getValueExpr("(" + resolvedValue + ")", DataType.UNKNOWN);
                    result.add(new DBCompareColExpr(expr, DBCmpType.NONE, " "));
                }
            }
        }

        // Добавление ограничений по правам пользователя
        for (String groupName : getUser().getGroups()) {
            GroupElement group = getApplication().getGroup(groupName);
            for (RightElement right : rightCollection.getGroupRights(group)) {
                if (RightType.RESTRICT == right.getType()
                    && right.getCondition() instanceof SQLCondition) {
                    String value = (String) right.getCondition().getValue();
                    if (value != null && !value.isEmpty()) {
                        String resolvedValue =
                            resolveValue(value, processParams(config.getParameters()));
                        DBColumnExpr expr =
                            db.getValueExpr("(" + resolvedValue + ")", DataType.UNKNOWN);
                        result.add(new DBCompareColExpr(expr, DBCmpType.NONE, " "));
                    }
                }
            }
        }
        return result;
    }

    protected Map<String, DataValue> processParams(Map<String, DataValue> paramMap) {
        ApplicationUser user = getUser();
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

        // for (Entry<String, DataValue> entry : paramMap.entrySet()) {
        // data = new DataValue(entry.getValue().getType());
        // data.setCode(entry.getValue().getCode().toUpperCase());
        // data.setValue(entry.getValue().getValue());
        // result.put(entry.getKey().toUpperCase(), data);
        // }

        for (Entry<String, DataValue> entry : paramMap.entrySet()) {
            entry.getValue().setCode(entry.getValue().getCode());
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    protected DBCompareExpr createEquals(DBColumnExpr column, Object value) {
        return column.is(value);
    }

    protected DBCompareExpr createContains(DBColumnExpr column, Object value) {
        Object v;
        if (value instanceof String) {
            v = ((String) value).toUpperCase();
        } else {
            v = value;
        }
        return column.upper().like("%" + v + "%");
    }

    /**
     * Добавляет апострофы в выражение "LIKE".
     * Использовать вместе c labelExpression, если тип данных "UNKNOWN",
     * т.к. empire-db не добавляет апострофы автоматически.
     */
    protected DBCompareExpr createContainsForCombobox(DBColumnExpr column, Object value) {
        Object v;
        if (value instanceof String) {
            v = ((String) value).toUpperCase();
        } else {
            v = value;
        }
        return column.upper().like("'%" + v + "%'");
    }

    private DBCompareExpr createNotContains(DBColumnExpr column, Object value) {
        Object v;
        if (value instanceof String) {
            v = ((String) value).toUpperCase();
        } else {
            v = value;
        }
        return column.upper().notLike("%" + v + "%");
    }

    protected DBCompareExpr createEmpty(DBColumnExpr column) {
        return column.cmp(DBCmpType.NULL, null);
    }

    private DBCompareExpr createNotEmpty(DBColumnExpr column) {
        return column.cmp(DBCmpType.NOTNULL, null);
    }

    private DBCompareExpr createStartWith(DBColumnExpr column, Object value) {
        return column.like(value + "%");
    }

    private DBCompareExpr createEndWith(DBColumnExpr column, Object value) {
        return column.like("%" + value);
    }

    private DBCompareExpr createLower(DBColumnExpr column, Object value) {
        return column.isLessOrEqual(value);
    }

    private DBCompareExpr createGreater(DBColumnExpr column, Object value) {
        return column.isMoreOrEqual(value);
    }

    private DBCompareExpr createBetween(DBColumnExpr column, Object valueFirst,
                                        Object valueSecond) {
        return column.isBetween(valueFirst, valueSecond);
    }

    private DBCompareExpr createReverse(DBColumnExpr column, Object value) {
        Object v;
        if (value instanceof String) {
            v = "%" + value;
        } else {
            v = value;
        }
        return createContains(column.reverse(),
                this.dbDatabase.getValueExpr(v, DataType.TEXT).reverse());
    }

    /*
     * Getters and setters
     */

    public Map<FieldMetadata, TableColumnElement> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(Map<FieldMetadata, TableColumnElement> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public DBDatabase getDbDatabase() {
        return dbDatabase;
    }

    public void setDbDatabase(DBDatabase dbDatabase) {
        this.dbDatabase = dbDatabase;
    }

    public DBTable getDbTable() {
        return dbTable;
    }

    public void setDbTable(DBTable dbTable) {
        this.dbTable = dbTable;
    }

    public DBColumn getDbPrimaryKey() {
        return dbPrimaryKey;
    }

    public void setDbPrimaryKey(DBColumn dbPrimaryKey) {
        this.dbPrimaryKey = dbPrimaryKey;
    }

    public DBTable getTopDbTable() {
        return topDbTable;
    }

    public void setTopDbTable(DBTable topDbTable) {
        this.topDbTable = topDbTable;
    }

    public DBColumn getTopDbPrimaryKey() {
        return topDbPrimaryKey;
    }

    public void setTopDbPrimaryKey(DBColumn topDbPrimaryKey) {
        this.topDbPrimaryKey = topDbPrimaryKey;
    }

    public List<DBCompareExpr> getWhere() {
        return where;
    }

    public void setWhere(List<DBCompareExpr> where) {
        this.where = where;
    }

    public DBColumnExpr getCountColumn() {
        return countColumn;
    }

    public void setCountColumn(DBColumnExpr countColumn) {
        this.countColumn = countColumn;
    }
}
