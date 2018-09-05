package org.whirlplatform.server.driver.multibase.fetch.oracle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import oracle.jdbc.internal.OracleTypes;
import org.apache.commons.lang.StringUtils;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.ListFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
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

public class OracleDynamicListFetcher extends OracleDynamicDataFetcher implements ListFetcher<DynamicTableElement> {
    private static Logger _log = LoggerFactory.getLogger(OracleDynamicListFetcher.class);

    public OracleDynamicListFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    public LoadData<ListModelData> getListData(ClassMetadata metadata, DynamicTableElement table,
                                               ClassLoadConfig loadConfig) {
        LoadData<ListModelData> result = null;
        List<ListModelData> data = new ArrayList<ListModelData>();

        if (loadConfig.isReloadMetadata()) {
            // metadata = getClassMetadata(metadata.getClassId(),
            // loadConfig.getParameters(), user);
            metadata = getDataSourceDriver().createMetadataFetcher(table).getClassMetadata(table,
                    loadConfig.getParameters());
        }

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
                    prepareListClassLoadConfig(loadConfig, metadata, table));
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
                ListModelData model = new ListModelDataImpl();
                model.setId(rs.getString(metadata.getIdField().getName()));
                model.setLabel(rs.getString("list_dfname"));
                for (FieldMetadata f : metadata.getFields()) {
                    setModelValue(model, f, rs);
                }
                data.add(model);
            }
        } catch (SQLException | IOException e) {
            _log.error("Get list data exception", e);
            e.printStackTrace();
            throw new CustomException("Dynamic datasource. GetListException: " + e.getMessage());
        } finally {
            DBConnection.closeResources(stmt);
        }

        result = new LoadData<ListModelData>(data);
        result.setRows(rowCount);
        return result;
    }

    private String prepareListClassLoadConfig(ClassLoadConfig config, ClassMetadata metadata,
                                              DynamicTableElement table) {
        StringBuilder builder = new StringBuilder();

        StringBuilder fieldsString = new StringBuilder();
        fieldsString.append(metadata.getIdField().getName());
        fieldsString.append(",").append("list_dfname");

        builder.append("fields:").append(fieldsString.toString());
        OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
        String whereSql = resolveValue(config.getWhereSql(), parmsHelper.processParams(config.getParameters()));
        try {
            whereSql = URLEncoder.encode(whereSql, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";whereSql:").append(whereSql);
        String rights = getRightsString(table, getUser());
        try {
            rights = URLEncoder.encode(rights, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        builder.append(";rightsFilter:").append(rights);
        builder.append(";query:").append(config.getQuery());

        if (config.getTableField() != null) {
            FieldMetadata field = config.getTableField();
            try {
                try (StringWriter out = new StringWriter(); @SuppressWarnings("deprecation")
                JsonGenerator generator = new JsonFactory().createJsonGenerator(out)) {
                    generator.writeStartObject();
                    generator.writeStringField("name", field.getName());
                    generator.writeStringField("label", field.getLabel());
                    generator.writeBooleanField("edit", field.isEdit());
                    generator.writeBooleanField("view", field.isView());
                    generator.writeBooleanField("hidden", field.isHidden());
                    generator.writeNumberField("length", field.getLength());
                    generator.writeStringField("type", field.getType().name());
                    if (!StringUtils.isEmpty(field.getClassId())) {
                        // AbstractTableElement t =
                        // findTableElement(field.getClassId(),
                        // user.getApplication());
                        AbstractTableElement t = getApplication().findTableElementById(field.getClassId());
                        generator.writeStringField("classId", t.getCode());
                    }
                    generator.writeBooleanField("required", field.isRequired());
                    generator.writeStringField("regEx", field.getRegEx());
                    generator.writeStringField("regExError", field.getRegExError());
                    generator.writeBooleanField("filter", field.isFilter());
                    generator.writeBooleanField("password", field.isPassword());
                    generator.writeStringField("viewFormat", field.getViewFormat());
                    if (field.getListViewType() != null) {
                        generator.writeStringField("listViewType", field.getListViewType().name());
                    }
                    generator.writeStringField("dataFormat", field.getDataFormat());

                    generator.writeEndObject();
                    generator.flush();
                    out.flush();

                    builder.append(";tableField:")
                            .append(URLEncoder.encode(out.toString(), StandardCharsets.UTF_8.name()));
                }
            } catch (IOException e) {
                _log.error("Field metadata serialization error", e);
            }
        }
        return builder.toString();
    }
}
