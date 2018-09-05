package org.whirlplatform.server.driver.multibase.fetch.postgresql;

import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractPlainDataChanger;

public class PostgrePlainDataChanger extends AbstractPlainDataChanger {


    public PostgrePlainDataChanger(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    protected String getNextId() {
        java.util.Random rnd = new java.util.Random();
        return String.valueOf(rnd.nextLong());
    }
}
