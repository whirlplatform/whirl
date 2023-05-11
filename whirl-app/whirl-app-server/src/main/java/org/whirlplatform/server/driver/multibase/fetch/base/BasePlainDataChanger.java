package org.whirlplatform.server.driver.multibase.fetch.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;


public class BasePlainDataChanger extends AbstractPlainDataChanger {


    public BasePlainDataChanger(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    protected String getNextId() {
        java.util.Random rnd = new java.util.Random();
        return String.valueOf(rnd.nextLong());
    }

    @Override
    protected String getNextIdInSequence(DBDatabase db, Connection connection) {
        String query = "SELECT sl.sequence_name "
                + "from "
                + "(SELECT sequence_schema, sequence_name "
                + "FROM information_schema.sequences "
                + "WHERE SEQUENCE_SCHEMA = ?) as sl "
                + "inner join (SELECT  t.column_name, t.column_default "
                + "FROM INFORMATION_SCHEMA.COLUMNS t "
                + "WHERE t.table_schema = ? "
                + "AND t.table_name = ? "
                + "AND t.column_default is not null) as st  "
                + "on st.column_default = 'nextval('''||?||'.'||sl.sequence_name||'''::regclass)'";

        String schema = db.getSchema();
        for (DBTable tab : db.getTables()) {
            String tableName = tab.getName();
            String[] params = {schema, schema, tableName, schema};
            try {
                ResultSet resultSet = db.executeQuery(query, params, false, connection);
                while (resultSet.next()) {
                    String seqName = resultSet.getString(1);
                    return db.getNextSequenceValue(seqName, connection).toString();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return getNextId();
    }

}