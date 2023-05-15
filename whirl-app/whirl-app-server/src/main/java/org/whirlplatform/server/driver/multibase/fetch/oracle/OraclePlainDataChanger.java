package org.whirlplatform.server.driver.multibase.fetch.oracle;

import java.sql.Connection;
import org.apache.empire.db.DBDatabase;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataChanger;

public class OraclePlainDataChanger extends AbstractPlainDataChanger {

    public OraclePlainDataChanger(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    protected String getNextId() {
        @SuppressWarnings("serial")
        DBDatabase db = new DBDatabase() {
        };
        db.open(getDriver(), getConnection());
        Object result = db.getNextSequenceValue("SOBJ", getConnection());
        if (result == null) {
            java.util.Random rnd = new java.util.Random();
            result = String.valueOf(rnd.nextLong());
        }
        return String.valueOf(result);
    }

    @Override
    protected String getNextIdInSequence(DBDatabase db, Connection connection) {
        return null;
    }
}
