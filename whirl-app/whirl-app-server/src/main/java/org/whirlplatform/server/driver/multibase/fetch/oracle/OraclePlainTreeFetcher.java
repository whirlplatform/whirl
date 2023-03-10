package org.whirlplatform.server.driver.multibase.fetch.oracle;

import java.util.ArrayList;
import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainListFetcherHelper;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTreeFetcherHelper;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class OraclePlainTreeFetcher extends OraclePlainTableFetcher
    implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OraclePlainTreeFetcher.class);

    public OraclePlainTreeFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public List<ListModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                               TreeClassLoadConfig loadConfig) {

        PlainTreeFetcherHelper temp =
                new PlainTreeFetcherHelper(getConnection(), getDataSourceDriver());
        LoadData<RowModelData> data = getTableData(metadata, table, loadConfig, temp);

        List<ListModelData> result = data.getData().stream().map(v -> {
            ListModelData l = ListModelData.fromRowModelData(v);
            l.setLabel(v.get(table.getLabelExpression().getColumnName()));
            return l;
        }).collect(java.util.stream.Collectors.toList());

        // Добавление пустой записи, если надо
        if (table.isEmptyRow()) {
            ListModelData empty = new ListModelDataImpl();
            empty.setId(null);
            empty.setLabel(I18NMessage.getMessage(I18NMessage.getRequestLocale()).noData());
            result.add(0, empty);
        }

        List<ListModelData> resultData = new ArrayList<>(result);
        //resultData.setRows(data.getRows());
        return resultData;
    }
}
