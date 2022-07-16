package org.whirlplatform.server.driver.multibase.fetch.base;

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
}
