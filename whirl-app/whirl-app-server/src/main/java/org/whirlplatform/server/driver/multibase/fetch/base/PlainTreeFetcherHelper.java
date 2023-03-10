package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBColumn;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.utils.TypesUtil;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

public class PlainTreeFetcherHelper extends PlainTableFetcherHelper {
    public PlainTreeFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig config) {
        super.prepare(metadata, table, config);

        this.labelExpression = dbDatabase.getValueExpr(config.getLabelExpression(), DataType.UNKNOWN)
                        .as(LABEL_EXPRESSION_NAME);

        //this.parentExpression...

        String query = config.getQuery();

        TableColumnElement c = table.getColumn(((TreeClassLoadConfig) config).getParentExpression());

        // добавить проверку на наличие кол
//            if(c.isNotNull()) {
//
//            } else {
//                table.getColumn(config).
//            }

        DBColumn parentColumn;
        if (this.dbTable.getColumn(c.getColumnName()).getName().equals(c.getColumnName())) {
            parentColumn = this.dbTable.getColumn(c.getColumnName());
        } else {
            parentColumn = this.dbTable.addColumn(c.getColumnName(),
                    TypesUtil.toEmpireType(c.getType(),
                            c.getListTable() == null ? null
                                    : getDataSourceDriver().createDataFetcher(c.getListTable())
                                    .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
        }

        ListModelData parent = ((TreeClassLoadConfig) config).getParent();
        if (parent != null) {
            this.where.add(createEquals(parentColumn, parent.getId()));
        } else if (StringUtils.isEmpty(query)) {
            this.where.add(createEmpty(parentColumn));
        }
    }
}
