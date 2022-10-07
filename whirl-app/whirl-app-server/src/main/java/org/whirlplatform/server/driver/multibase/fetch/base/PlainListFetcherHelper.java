package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.expr.column.DBValueExpr;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.utils.TypesUtil;

public class PlainListFetcherHelper extends PlainTableFetcherHelper {

    public DBValueExpr listColumn;

    public PlainListFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig config) {
        super.prepare(metadata, table, config);

        // Previous example which worked with a single column
        //this.listColumn = this.dbTable.addColumn("", DataType.CHAR, 255, false);
        this.listColumn = this.dbDatabase.getValueExpr(config.getLabelColumn());

        String query = config.getQuery();
        if (!StringUtils.isEmpty(query) && !(config instanceof TreeClassLoadConfig)) {
            this.where.add(createContains(this.listColumn, query));
        }

        if (config instanceof TreeClassLoadConfig) {
            TableColumnElement c = table.getColumn(((TreeClassLoadConfig) config).getParentColumn());
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
    }

}
