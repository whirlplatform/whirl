package org.whirlplatform.server.driver.multibase.fetch.oracle;

import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.driver.multibase.fetch.base.PlainTreeFetcherHelper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.log.impl.ProfileImpl;
import org.whirlplatform.server.log.impl.TableDataMessage;

import java.util.ArrayList;
import java.util.List;

public class OraclePlainTreeFetcher extends OraclePlainDataFetcher implements TreeFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OraclePlainTreeFetcher.class);

    public OraclePlainTreeFetcher(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public List<RowModelData> getTreeData(ClassMetadata metadata, PlainTableElement table,
                                          TreeClassLoadConfig loadConfig) {
        PlainTreeFetcherHelper temp = new PlainTreeFetcherHelper(getConnection(), getDataSourceDriver());
        temp.prepare(metadata, table, loadConfig);
        List<RowModelData> result = new ArrayList<RowModelData>();
        DBCommand selectCmd = createSelectCommand(table, loadConfig, temp);
        TableDataMessage m = new TableDataMessage(getUser(), selectCmd.getSelect());
        try (Profile p = new ProfileImpl(m)) {
            _log.debug("Tree select:\n" + selectCmd.getSelect());
            DBReader selectReader = createAndOpenReader(selectCmd);
            while (selectReader.moveNext()) {
                RowModelData model = createRowModel(selectReader, temp, metadata);

                result.add(model);
            }
            selectReader.close();
            return result;
        }
    }

    private RowModelData createRowModel(DBReader selectReader, PlainTreeFetcherHelper temp, ClassMetadata metadata) {
        RowModelData model = new RowModelDataImpl();
        model.setId(selectReader.getString(temp.topDbPrimaryKey));
        if (temp.dbParentColumn != null) {
            model.set(temp.dbParentColumn.getName(), selectReader.getString(temp.dbParentColumn));
        }
        if (temp.dbLeafExpression != null) {
            model.set("PROPERTY_HAS_CHILDREN", selectReader.getBoolean(temp.dbLeafExpression));
        }
        if (temp.dbStateExpression != null) {
            model.set("STATE_COLUMN", selectReader.getBoolean(temp.dbStateExpression));
        }
        if (temp.dbCheckExpression != null) {
            model.set("CHECK_COLUMN", selectReader.getBoolean(temp.dbCheckExpression));
        }
        if (temp.dbIsSelectExpression != null) {
            model.set("SELECT_COLUMN", selectReader.getBoolean(temp.dbIsSelectExpression));
        }
        if (temp.dbNameExpression != null) {
            if (temp.nameExpressionColumn != null
                    && temp.nameExpressionColumn.getViewFormat() == TableColumnElement.ViewFormat.CSS) {
                setModelStyledValue(new FieldMetadata(temp.dbNameExpression.getName(),
                                DataType.STRING, temp.dbNameExpression.getName()),
                        temp.nameExpressionColumn, selectReader, model);
            } else {
                model.set("NAME_COLUMN", selectReader.getString(temp.dbNameExpression));
            }
        }
        return model;
    }
}
