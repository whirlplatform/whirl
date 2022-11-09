package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
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
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.List;

public class BasePlainTableFetcher extends BasePlainDataFetcher implements TableFetcher<PlainTableElement> {
    @SuppressWarnings("unused")
    private static Logger _log = LoggerFactory.getLogger(BasePlainTableFetcher.class);
    //private static Logger _log = LogManager.getLogger(BasePlainTableFetcher.class.getName());

    public BasePlainTableFetcher(ConnectionWrapper connection, DataSourceDriver fetcher) {
        super(connection, fetcher);
    }

    @Override
    public LoadData<RowModelData> getTableData(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        return getTableData(metadata, table, loadConfig, temp);
    }

    protected <H extends PlainTableFetcherHelper> LoadData<RowModelData> getTableData(ClassMetadata metadata,
                                                                                      PlainTableElement table,
                                                                                      ClassLoadConfig loadConfig,
                                                                                      H temp) {
        List<RowModelData> result = new ArrayList<RowModelData>();
        temp.prepare(metadata, table, loadConfig);

        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);
        _log.info("Hello LOGS! ::: " + selectCmd.getSelect());

        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {

            DBReader selectReader = createAndOpenReader(selectCmd);

            while (selectReader.moveNext()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(selectReader.getString(temp.dbPrimaryKey));

                for (FieldMetadata f : metadata.getFields()) {
                    if (!f.isView()) {
                        continue;
                    }
                    setModelValue(model, f, selectReader);
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
