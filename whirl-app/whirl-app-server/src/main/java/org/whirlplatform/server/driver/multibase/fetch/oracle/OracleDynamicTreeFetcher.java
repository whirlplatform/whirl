package org.whirlplatform.server.driver.multibase.fetch.oracle;

import oracle.jdbc.internal.OracleTypes;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.TreeFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

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

public class OracleDynamicTreeFetcher extends OracleDynamicDataFetcher implements TreeFetcher<DynamicTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OracleDynamicTreeFetcher.class);

    public OracleDynamicTreeFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    public List<RowModelData> getTreeData(ClassMetadata metadata, DynamicTableElement table,
                                          TreeClassLoadConfig loadConfig) {

        List<RowModelData> result = new ArrayList<RowModelData>();
        ResultSet rs = null;
        @SuppressWarnings("unused")
        int rowCount = 0;

        NamedParamResolver resolver = new NamedParamResolver(getDriver(), table.getDataFunction(),
                Collections.emptyMap());
        String sql = "{? = call " + resolver.getResultSql();

        try (CallableStatement stmt = getConnection().prepareCall(sql)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);

            // Подготовка параметров
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            Map<String, DataValue> params = parmsHelper.processParams(loadConfig.getParameters());
            DataValue dataConf = new DataValueImpl(DataType.STRING,
                    prepareTreeClassLoadConfig(loadConfig, metadata, table, getUser()));
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
                result.add(model);
            }
        } catch (SQLException | IOException e) {
            _log.error("Get tree data error", e);
            e.printStackTrace();
            throw new CustomException("Dynamic datasource. Get tree data exception: " + e.getMessage());
        }
        return result;
    }

    private String prepareTreeClassLoadConfig(TreeClassLoadConfig config, ClassMetadata metadata,
                                              DynamicTableElement table, ApplicationUser user) {
        StringBuilder builder = new StringBuilder();

        // Формирование строки с названиями полей (для дерева только id поле
        // нужно?)
        StringBuilder fieldsString = new StringBuilder();
        fieldsString.append(metadata.getIdField().getName());
        // for (FieldMetadata f : metadata.getFields()) {
        // fieldsString.append(",").append(f.getName());
        // }
        OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
        builder.append("fields:").append(fieldsString.toString());
        String whereSql = resolveValue(config.getWhereSql(), parmsHelper.processParams(config.getParameters()));
        try {
            whereSql = URLEncoder.encode(whereSql, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";whereSql:").append(whereSql);
        String rights = getRightsString(table, user);
        try {
            rights = URLEncoder.encode(rights, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";rightsFilter:").append(rights);
        builder.append(";parentField:").append(config.getParentField());
        builder.append(";nameExpression:").append(config.getNameExpression());
        builder.append(";leafExpression:").append(config.getLeafExpression());
        builder.append(";checkExpression:").append(config.getCheckExpression());
        builder.append(";parentId:").append(config.getParent().getId());

        return builder.toString();
    }
}
