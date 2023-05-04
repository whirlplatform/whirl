package org.whirlplatform.server.driver.multibase.fetch.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;


public class BasePlainDataChanger extends AbstractPlainDataChanger {
    private static Logger logger = LoggerFactory.getLogger(BasePlainDataChanger.class);

    public BasePlainDataChanger(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    protected String getNextId() { // таблица, схема, connection
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

        String nextSequenceValue = null;
        for (DBTable tab : db.getTables()) {
            String schema = db.getSchema();
            String tableName = tab.getName();
            String[] params = {schema, schema, tableName, schema};
            try {
                ResultSet resultSet = db.executeQuery(query, params, false, connection);
                while (resultSet.next()) {
                    String seqName = resultSet.getString(1);
                    nextSequenceValue = db.getNextSequenceValue(seqName, connection).toString();
                }
            } catch (SQLException e) {
                logger.error(e.toString());
                throw new CustomException(e.toString());
            }
        }
        if (nextSequenceValue == null) {
            logger.error("Sequence name not found");
            throw new CustomException("Sequence name not found");
        } else {
            return nextSequenceValue;
        }

    }

}