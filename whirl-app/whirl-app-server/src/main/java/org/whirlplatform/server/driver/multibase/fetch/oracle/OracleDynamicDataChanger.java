package org.whirlplatform.server.driver.multibase.fetch.oracle;

import oracle.jdbc.internal.OracleTypes;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataChanger;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class OracleDynamicDataChanger extends AbstractFetcher implements DataChanger<DynamicTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OracleDynamicDataChanger.class);

    public OracleDynamicDataChanger(ConnectionWrapper connection) {
        super(connection);
    }

    @Override
    public RowModelData insert(ClassMetadata metadata, DataModifyConfig config, DynamicTableElement table) {
        RowModelData model = config.getModels().get(0);

        CallableStatement stmt = null;

        try {
            NamedParamResolver resolver = new NamedParamResolver(getConnection().getDatabaseDriver(),
                    table.getInsertFunction(), Collections.emptyMap());
            String sql = "{? = call " + resolver.getResultSql();
            stmt = getConnection().prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            // Подготовка параметров
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            Map<String, DataValue> params = parmsHelper.processParams(config.getParams());
            DataValue insertConf = new DataValueImpl(DataType.STRING, prepareInsertConfig(config));
            params.put("insert_config", insertConf);
            parmsHelper.setStatementParams(stmt, resolver.getOrderedParamNames(), params);
            stmt.execute();
            String result = stmt.getString(1);
            if (StringUtils.isEmpty(result)) {
                throw new CustomException("Dynamic datasource. Insert error: null id");
            } else {
                model.setId(result);
            }
        } catch (SQLException e) {
            _log.error("Insert error", e);
            throw new CustomException("Dynamic datasource. Insert SQLException: " + e.getMessage());
        } catch (IOException e) {
            _log.error("Insert create json error", e);
            throw new CustomException("Dynamic datasource. Insert create json error: " + e.getMessage());
        } finally {
            DBConnection.closeResources(stmt);
        }
        return model;
    }

    @Override
    public RowModelData update(ClassMetadata metadata, DataModifyConfig config, DynamicTableElement table) {
        RowModelData model = config.getModels().get(0);
        CallableStatement stmt = null;
        try {
            NamedParamResolver resolver = new NamedParamResolver(getConnection().getDatabaseDriver(),
                    table.getUpdateFunction(), Collections.emptyMap());
            String sql = "{? = call " + resolver.getResultSql();
            stmt = getConnection().prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            // Подготовка параметров
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            Map<String, DataValue> params = parmsHelper.processParams(config.getParams());
            DataValue insertConf = new DataValueImpl(DataType.STRING, prepareUpdateConfig(config, metadata));
            params.put("update_config", insertConf);
            parmsHelper.setStatementParams(stmt, resolver.getOrderedParamNames(), params);
            stmt.execute();
            String result = stmt.getString(1);
            if (!StringUtils.isEmpty(result)) {
                throw new CustomException("Dynamic datasource. Update error: " + result);
            }
        } catch (SQLException e) {
            _log.error("Update error", e);
            throw new CustomException("Dynamic datasource. Update SQLException: " + e.getMessage());
        } catch (IOException e) {
            _log.error("Update create json error", e);
            throw new CustomException("Dynamic datasource. Update create json error: " + e.getMessage());
        } finally {
            DBConnection.closeResources(stmt);
        }
        return model;
    }

    @Override
    public void delete(ClassMetadata metadata, DataModifyConfig config, DynamicTableElement table) {
        CallableStatement stmt = null;
        try {
            NamedParamResolver resolver = new NamedParamResolver(getConnection().getDatabaseDriver(),
                    table.getDeleteFunction(), Collections.emptyMap());
            String sql = "{? = call " + resolver.getResultSql();
            stmt = getConnection().prepareCall(sql);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            Map<String, DataValue> params = parmsHelper.processParams(config.getParams());
            DataValue insertConf = new DataValueImpl(DataType.STRING, prepareDeleteConfig(config));
            params.put("delete_config", insertConf);
            parmsHelper.setStatementParams(stmt, resolver.getOrderedParamNames(), params);
            stmt.execute();
            String result = stmt.getString(1);
            if (!StringUtils.isEmpty(result)) {
                throw new CustomException("Dynamic datasource. Delete error: " + result);
            }
        } catch (SQLException e) {
            _log.error("Delete error", e);
            throw new CustomException("Dynamic datasource. Delete SQLException: " + e.getMessage());
        } catch (IOException e) {
            _log.error("Delete create json error", e);
            throw new CustomException("Dynamic datasource. Delete create json error: " + e.getMessage());
        } finally {
            DBConnection.closeResources(stmt);
        }
    }

    private String prepareDeleteConfig(DataModifyConfig config) {
        StringBuilder builder = new StringBuilder();
        for (RowModelData m : config.getModels()) {
            builder.append(m.getId()).append(";");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "";
    }

    private String prepareInsertConfig(DataModifyConfig config) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        RowModelData model = config.getModels().get(0);
        for (String p : model.getPropertyNames()) {
            DataValue v = model.getValue(p);
            builder.append(p).append(":").append(v.getType().name()).append(":")
                    .append(getStringPresentation(v.getValue(), v.getType())).append(";");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "";
    }

    private String prepareUpdateConfig(DataModifyConfig config, ClassMetadata metadata)
            throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        RowModelData model = config.getModels().get(0);
        String id = getStringPresentation(model.getId(), metadata.getIdField().getType());
        builder.append(metadata.getIdField().getName()).append(":").append(metadata.getIdField().getType().name())
                .append(":").append(id);
        for (String p : model.getPropertyNames()) {
            DataValue v = model.getValue(p);
            builder.append(";").append(p).append(":").append(v.getType()).append(":")
                    .append(getStringPresentation(v.getValue(), v.getType()));
        }
        return builder.toString();
    }

    private String getStringPresentation(Object obj, DataType type) throws UnsupportedEncodingException {
        if (obj == null) {
            return "";
        }
        String strValue;
        if (type == DataType.STRING) {
            if (StringUtils.isEmpty((String) obj)) {
                strValue = "";
            } else {
                strValue = URLEncoder.encode((String) obj, StandardCharsets.UTF_8.name());
            }
        } else if (type == DataType.BOOLEAN) {
            strValue = (Boolean) obj ? "T" : "F";
        } else if (type == DataType.NUMBER) {
            // Замена на правильный десятичный разделитель производится в базе
            strValue = getConnection().getDatabaseDriver().getValueString(obj,
                    org.apache.empire.data.DataType.FLOAT);
        } else if (type == DataType.DATE) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss.SSS");
            strValue = sdf.format((Date) obj);
        } else if (type == DataType.LIST) {
            ListModelData list = (ListModelData) obj;
            String id = list.getId();
            if (StringUtils.isEmpty(id)) {
                strValue = "";
            } else if (NumberUtils.isNumber(id)) {
                strValue = getConnection().getDatabaseDriver().getValueString(id,
                        org.apache.empire.data.DataType.FLOAT);
            } else {
                strValue = URLEncoder.encode(id, StandardCharsets.UTF_8.name());
            }
        } else {
            strValue = "";
        }
        return strValue;
    }
}
