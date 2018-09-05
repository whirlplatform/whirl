package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TableFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostgrePlainTableFetcher extends PostgrePlainDataFetcher implements TableFetcher<PlainTableElement> {
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(PostgrePlainTableFetcher.class);

    public PostgrePlainTableFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public LoadData<RowModelData> getTableData(ClassMetadata metadata, PlainTableElement table,
                                               ClassLoadConfig loadConfig) {
        @SuppressWarnings("unused")
        Date start = new Date();
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);

        List<RowModelData> result = new ArrayList<RowModelData>();

        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);

        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {

            DBReader selectReader = createAndOpenReader(selectCmd);

            while (selectReader.moveNext()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(selectReader.getString(temp.dbPrimaryKey));

                for (FieldMetadata f : metadata.getFields()) {
                    setModelValue(model, f, selectReader, temp);
                }
                result.add(model);
            }
            selectReader.close();

            LoadData<RowModelData> data = new LoadData<RowModelData>(result);

            DBCommand countCmd = createCountCommand(temp, loadConfig.isAll());

            DBReader countReader = createAndOpenReader(countCmd);

            if (countReader.moveNext()) {
                data.setRows(countReader.getInt(temp.countColumn));
            }
            countReader.close();

            return data;
        }
    }

}
