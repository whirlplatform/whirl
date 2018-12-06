package org.whirlplatform.server.driver.multibase.fetch.oracle;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.exceptions.QueryFailedException;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TableFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTableFetcherHelper;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;
import org.whirlplatform.server.monitor.RunningEvent;

import java.util.ArrayList;
import java.util.List;

public class OraclePlainTableFetcher extends OraclePlainDataFetcher implements TableFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OraclePlainTableFetcher.class);
    
    public OraclePlainTableFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver dataSourceDriver) {
        super(connectionWrapper, dataSourceDriver);
    }

    public LoadData<RowModelData> getTableData(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        PlainTableFetcherHelper temp = new PlainTableFetcherHelper(getConnection(), getDataSourceDriver());
        return getTableData(metadata, table, loadConfig, temp);
    }

    protected <H extends PlainTableFetcherHelper> LoadData<RowModelData> getTableData(ClassMetadata metadata,
                                                                                      PlainTableElement table,
                                                                                      ClassLoadConfig loadConfig,
                                                                                      H temp) {

        temp.prepare(metadata, table, loadConfig);

        List<RowModelData> result = new ArrayList<RowModelData>();

        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);

        final DBReader selectReader = new DBReader();
        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());

        final boolean[] stoppedHolder = new boolean[]{false};
        RunningEvent ev = new RunningEvent(RunningEvent.Type.GRIDREQUEST, table.getCode(), selectCmd.getSelect(),
                getUser().getLogin()) {
            @Override
            public void onStop() {
                //TODO abort
                //                try {
                //                    stoppedHolder[0] = true;
                //                    getConnection().unwrap(OracleConnection.class).abort();
                //                } catch (SQLException e) {
                //                    _log.info("Stop event error", e);
                //                }
            }
        };

        try (Profile p = new ProfileImpl(m, ev)) {
            _log.debug("Table select:\n" + selectCmd.getSelect());

            selectReader.open(selectCmd, getConnection());
            while (selectReader.moveNext()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(selectReader.getString(temp.topDbPrimaryKey));

                for (FieldMetadata f : metadata.getFields()) {
                    if (!f.isView()) {
                        continue;
                    }
                    setModelValue(model, f, selectReader);
                }
                result.add(model);
            }

            LoadData<RowModelData> data = new LoadData<RowModelData>(result);

            DBCommand countCmd = createCountCommand(temp, loadConfig.isAll());

            DBReader countReader = new DBReader();
            countReader.open(countCmd, getConnection());
            if (countReader.moveNext()) {
                data.setRows(countReader.getInt(temp.countColumn));
            }
            countReader.close();
            return data;
        } catch (QueryFailedException e) {
            // Обработка остановки выполнения запроса
            if (stoppedHolder[0]) {
                throw new CustomException(I18NMessage.getMessage(I18NMessage.getRequestLocale()).alert_event_cancelled());
            } else {
                throw e;
            }
        } finally {
            selectReader.close();
        }
    }
}
