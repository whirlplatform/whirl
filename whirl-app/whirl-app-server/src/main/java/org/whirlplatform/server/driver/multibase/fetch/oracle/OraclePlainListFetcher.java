package org.whirlplatform.server.driver.multibase.fetch.oracle;

import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.ListFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainListFetcherHelper;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.util.List;

public class OraclePlainListFetcher
        extends org.whirlplatform.server.driver.multibase.fetch.oracle.OraclePlainTableFetcher
        implements ListFetcher<PlainTableElement> {

    private static Logger logger = LoggerFactory.getLogger(OraclePlainListFetcher.class);

    public OraclePlainListFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    public LoadData<ListModelData> getListData(ClassMetadata metadata, PlainTableElement table,
                                               ClassLoadConfig loadConfig) {

        PlainListFetcherHelper temp = new PlainListFetcherHelper(getConnection(), getDataSourceDriver());
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

        LoadData<ListModelData> resultData = new LoadData<>(result);
        resultData.setRows(data.getRows());
        return resultData;
    }

}
