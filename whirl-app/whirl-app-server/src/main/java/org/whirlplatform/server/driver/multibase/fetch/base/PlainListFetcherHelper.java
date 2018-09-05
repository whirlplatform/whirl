package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBTable;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.utils.TypesUtil;

public class PlainListFetcherHelper extends PlainTableFetcherHelper {
    public DBColumn dbListName;
    public DBColumnExpr dbLeafExpression; // Для TreeComboBox
    public DBColumnExpr dbStateExpression; //

    public PlainListFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig config) {
        this.dbDatabase = createAndOpenDatabase(table.getSchema().getSchemaName());
        this.dbTable = new DBTable(table.getList().getViewName(), this.dbDatabase);
//		this.dbTable.setAlias("a");
        this.dbListName = this.dbTable.addColumn(SrvConstant.COLUMN_LIST_TITLE, DataType.TEXT, 0, false);

        if (table.getIdColumn() != null) {
            TableColumnElement c = table.getIdColumn();
            this.dbPrimaryKey = this.dbTable.addColumn(c.getColumnName(),
                    TypesUtil.toEmpireType(c.getType(),
                            c.getListTable() == null ? null
                                    : getDataSourceDriver().createDataFetcher(c.getListTable())
                                    .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
        }

        String query = config.getQuery();
        if (!StringUtils.isEmpty(query) && !(config instanceof TreeClassLoadConfig)) {
            this.where.add(createContains(this.dbListName, query));
        }

        if (config instanceof TreeClassLoadConfig) {
            TableColumnElement c = table.getColumn(((TreeClassLoadConfig) config).getParentField());
            DBColumn parentColumn = this.dbTable.addColumn(c.getColumnName(),
                    TypesUtil.toEmpireType(c.getType(),
                            c.getListTable() == null ? null
                                    : getDataSourceDriver().createDataFetcher(c.getListTable())
                                    .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());

            RowModelData parent = ((TreeClassLoadConfig) config).getParent();
            if (parent != null) {
                this.where.add(createEquals(parentColumn, parent.getId()));
            } else if (StringUtils.isEmpty(query)) {
                this.where.add(createEmpty(parentColumn));
            }
        }

        // Добавляем остальные столбцы. Нужно для сортировки не по id
        for (TableColumnElement c : table.getColumns()) {
            if (this.dbTable.getColumn(c.getColumnName()) != null) {
                continue;
            }
            this.dbTable.addColumn(c.getColumnName(),
                    TypesUtil.toEmpireType(c.getType(),
                            c.getListTable() == null ? null
                                    : getDataSourceDriver().createDataFetcher(c.getListTable())
                                    .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
        }

        this.where.addAll(getWhereRestrictions(this.dbDatabase, metadata, config, table));
    }

    /*
     * Getters and setters
     */

    public DBColumn getDbListName() {
        return dbListName;
    }

    public void setDbListName(DBColumn dbListName) {
        this.dbListName = dbListName;
    }

    public DBColumnExpr getDbLeafExpression() {
        return dbLeafExpression;
    }

    public void setDbLeafExpression(DBColumnExpr dbLeafExpression) {
        this.dbLeafExpression = dbLeafExpression;
    }

    public DBColumnExpr getDbStateExpression() {
        return dbStateExpression;
    }

    public void setDbStateExpression(DBColumnExpr dbStateExpression) {
        this.dbStateExpression = dbStateExpression;
    }
}
