package org.whirlplatform.server.driver.multibase.fetch.oracle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.DBConnection;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.MetadataFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Map;

public class OracleDynamicMetadataFetcher extends AbstractFetcher implements MetadataFetcher<DynamicTableElement> {
    protected static Logger _log = LoggerFactory.getLogger(OracleDynamicMetadataFetcher.class);

    public OracleDynamicMetadataFetcher(ConnectionWrapper connectionWrapper) {
        super(connectionWrapper);
    }

    @Override
    public ClassMetadata getClassMetadata(DynamicTableElement table, Map<String, DataValue> params) {
        if (table == null) {
            final String message = "The input DynamicTableElement is null";
            _log.warn(message);
            throw new CustomException(message);
        }
        final String classId = table.getId();
        ClassMetadata result = new ClassMetadata(classId);

        try {
            // Можно проаннотировать ClassMetadata и FieldMetadata, и сразу
            // десериализовать объект из json
            String jsonString = getMetadataString(table, params);
            @SuppressWarnings("deprecation")
            JsonParser jp = new JsonFactory().createJsonParser(jsonString);
            jp.nextToken();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jp.getCurrentName();
                jp.nextToken();
                if ("fields".equals(fieldName)) {
                    int idCounter = 0;
                    while (jp.nextToken() == JsonToken.START_OBJECT) {
                        idCounter++;
                        FieldMetadata field = mapper.readValue(jp, FieldMetadata.class);
                        field.setId(String.valueOf(idCounter));
                        if (field.getType() == DataType.LIST) {
                            String tableId = getUser().getApplication()
                                    .findTableIdByCode(field.getClassId());
                            field.setClassId(tableId);
                        }
                        result.addField(field);
                    }
                    continue;
                }
                switch (fieldName) {
                    case "title":
                        result.setTitle(jp.getText());
                        break;
                    case "viewable":
                        result.setViewable(jp.getBooleanValue());
                        break;
                    case "insertable":
                        result.setInsertable(jp.getBooleanValue());
                        break;
                    case "updatable":
                        result.setUpdatable(jp.getBooleanValue());
                        break;
                    case "deletable":
                        result.setDeletable(jp.getBooleanValue());
                        break;
                    case "loadAll":
                        result.setLoadAll(jp.getBooleanValue());
                        break;
                    case "idField":
                        jp.nextToken();
                        result.setIdField(mapper.readValue(jp, FieldMetadata.class));
                    default:
                        break;
                }
            }

            // TODO: Dynamic dataSource Разнести в отдельные обработчики парсинг
            // json и выполнение функции?
        } catch (IOException | SQLException e) {
            _log.error("Error get dynamicTable metadata: " + classId, e);
            throw new CustomException("Error get dynamicTable metadata: " + classId);
        }
        return result;
    }

    private String getMetadataString(DynamicTableElement table, Map<String, DataValue> params)
            throws SQLException, IOException {
        String result = "";
        CallableStatement stmt = null;
        BufferedReader re = null;
        String sql = table.getMetadataFunction();
        if (sql == null || sql.isEmpty()) {
            return null; // Или пустой json?
        }

        NamedParamResolver resolver = new NamedParamResolver(getDriver(), table.getMetadataFunction(),
                Collections.emptyMap());

        try {
            // TODO: Dynamic dataSource Сделать нормальную установку параметров?
            sql = "{? = call " + resolver.getResultSql() + "}";
            stmt = getConnection().prepareCall(sql);
            stmt.setQueryTimeout(DBConnection.STMT_TIMEOUT);
            stmt.registerOutParameter(1, Types.CLOB);

            // Сделать нормальную подстановку параметров!!
            // for (int i = 1; i < StringUtils.countMatches(sql, "?"); i++) {
            // stmt.setObject(i + 1, null);
            // }
            OracleStatementHelper parmsHelper = new OracleStatementHelper(getConnection());
            parmsHelper.setStatementParams(stmt, resolver.getOrderedParamNames(), params);

            stmt.execute();

            Clob clob = (Clob) stmt.getObject(1);
            String str = "";
            if (clob != null) {
                re = new BufferedReader(clob.getCharacterStream());
                while ((str = re.readLine()) != null) {
                    // result += (result.equals("") ? "" : "\n") + str;
                    result += str;
                }
            }
        } catch (SQLException e) {
            // Логируем sql, кидаем exception дальше
            _log.error("Sql command: " + sql);
            throw e;
        } finally {
            DBConnection.closeResources(stmt);
            if (re != null) {
                re.close();
            }
        }
        return result;
    }
}
