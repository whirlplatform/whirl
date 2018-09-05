package org.whirlplatform.server.driver.multibase.fetch.oracle;

import oracle.jdbc.internal.OracleTypes;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TableFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OracleDynamicTableFetcher extends OracleDynamicDataFetcher implements TableFetcher<DynamicTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OracleDynamicTableFetcher.class);

    public OracleDynamicTableFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    public LoadData<RowModelData> getTableData(ClassMetadata metadata, DynamicTableElement table,
                                               ClassLoadConfig loadConfig) {
        LoadData<RowModelData> result = null;
        List<RowModelData> data = new ArrayList<RowModelData>();

        CallableStatement stmt = null;
        ResultSet rs = null;
        int rowCount = 0;

        NamedParamResolver resolver = new NamedParamResolver(getDriver(), table.getDataFunction(),
                Collections.emptyMap());
        String sql = "{? = call " + resolver.getResultSql();

        try {
            stmt = getConnection().prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            // Подготовка параметров
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            Map<String, DataValue> params = parmsHelper.processParams(loadConfig.getParameters());
            DataValue dataConf = new DataValueImpl(DataType.STRING,
                    prepareClassLoadConfig(loadConfig, metadata, table));
            params.put("data_config", dataConf);
            DataValue dataCount = new DataValueImpl(DataType.NUMBER, null);
            params.put("data_count", dataCount);
            parmsHelper.setStatementParams(stmt, resolver.getOrderedParamNames(), params);

            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);

            for (int i = 0; i < resolver.getOrderedParamNames().size(); i++) {
                if ("data_count".equalsIgnoreCase(resolver.getOrderedParamNames().get(i))) {
                    rowCount = stmt.getInt(i + 2);
                }
            }

            while (rs.next()) {
                RowModelData model = new RowModelDataImpl();
                model.setId(rs.getString(metadata.getIdField().getName()));
                for (FieldMetadata f : metadata.getFields()) {
                    setModelValue(model, f, rs);
                }
                data.add(model);
            }
        } catch (SQLException | IOException e) {
            _log.error("Get table data exception", e);
            e.printStackTrace();
            throw new CustomException("Dynamic datasource. Get table data exception: " + e.getMessage());
        } finally {
            DBConnection.closeResources(stmt);
        }

        result = new LoadData<RowModelData>(data);
        result.setRows(rowCount);
        return result;
    }

    private String prepareClassLoadConfig(ClassLoadConfig config, ClassMetadata metadata, DynamicTableElement table) {
        StringBuilder builder = new StringBuilder();

        // Формирование строки с названиями полей
        StringBuilder fieldsString = new StringBuilder();
        fieldsString.append(metadata.getIdField().getName());
        for (FieldMetadata f : metadata.getFields()) {
            fieldsString.append(",").append(f.getName());
        }
        builder.append("fields:").append(fieldsString.toString());
        builder.append(";page:").append(config.getPageNum());
        builder.append(";rowsPerPage:").append(config.getRowsPerPage());
        builder.append(";loadAll:").append(config.isAll() ? 'T' : 'F');
        OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
        String whereSql = resolveValue(config.getWhereSql(), parmsHelper.processParams(config.getParameters()));
        try {
            whereSql = URLEncoder.encode(whereSql, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";whereSql:").append(whereSql);
        String filter = getFiltersString(config);
        try {
            filter = URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";filters:").append(filter);
        String rights = getRightsString(table, getUser());
        try {
            rights = URLEncoder.encode(rights, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";rightsFilter:").append(rights);
        builder.append(";sorts:").append(getSortsString(config, metadata));

        return builder.toString();
    }
}
