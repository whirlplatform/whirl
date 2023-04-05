package org.whirlplatform.server.driver.multibase.fetch.base;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

import java.util.stream.Collectors;
import org.apache.empire.commons.StringUtils;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBColumnExpr;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

public class PlainListFetcherHelper extends PlainTableFetcherHelper {

    public PlainListFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig config) {
        super.prepare(metadata, table, config);

        String labelExpression = resolveValue(config.getLabelExpression(),
            config.getParameters().values().stream().collect(Collectors.toList()));
        this.labelExpression = dbDatabase.getValueExpr(labelExpression, DataType.UNKNOWN)
                .as(LABEL_EXPRESSION_NAME);

        String query = config.getQuery();
        if (!StringUtils.isEmpty(query) && !(config instanceof TreeClassLoadConfig)) {
            this.where.add(createContainsForCombobox(labelExpression, query));
        }
    }
}
