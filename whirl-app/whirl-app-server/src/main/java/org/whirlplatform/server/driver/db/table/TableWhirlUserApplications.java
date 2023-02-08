package org.whirlplatform.server.driver.db.table;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBTableColumn;
import org.whirlplatform.server.driver.db.MetadataDatabase;

@SuppressWarnings("checkstyle:all")
public class TableWhirlUserApplications extends EmpireDBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn ID;
    public final DBTableColumn DELETED;
    public final DBTableColumn R_WHIRL_USERS;
    public final DBTableColumn APPLICATION_CODE;

    public TableWhirlUserApplications(MetadataDatabase db) {
        super("WHIRL_USER_APPS", db);

        // create columns
        ID = addColumn("ID", DataType.DECIMAL, 0.0, true, null);
        DELETED = addColumn("DELETED", DataType.BOOL, 1.0, false, null);
        R_WHIRL_USERS = addColumn("R_WHIRL_USERS", DataType.DECIMAL, 0.0, false, null);
        APPLICATION_CODE = addColumn("APPLICATION_CODE", DataType.TEXT, 255.0, false, null);

        // configure key columns (primary key)
        setPrimaryKey(ID);

        addIndex("IDX_WHIRL_USER_APPS_RWU", false, R_WHIRL_USERS);

        // Optimistic locking column
        /*no locking column specified*/
    }
}
