package org.whirlplatform.server.driver.db.table;

import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;

public class EmpireDBTable extends DBTable {

    private static final long serialVersionUID = 1L;

    public EmpireDBTable(String name, DBDatabase db) {
        super(name, db);
    }

}
