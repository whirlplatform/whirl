package org.whirlplatform.server.driver.db.table;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBTableColumn;
import org.whirlplatform.server.driver.db.MetadataDatabase;

@SuppressWarnings("checkstyle:all")
public class TableWhirlUsers extends EmpireDBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn ID;
    public final DBTableColumn DELETED;
    public final DBTableColumn LOGIN;
    public final DBTableColumn PASSWORD_HASH;
    public final DBTableColumn NAME;
    public final DBTableColumn EMAIL;
    public final DBTableColumn CREATION_DATE;

    public TableWhirlUsers(MetadataDatabase db) {
        super("WHIRL_USERS", db);

        // create columns
        ID = addColumn("ID", DataType.DECIMAL, 0.0, true, null);
        DELETED = addColumn("DELETED", DataType.BOOL, 1.0, false, null);
        LOGIN = addColumn("LOGIN", DataType.TEXT, 255.0, false, null);
        PASSWORD_HASH = addColumn("PASSWORD_HASH", DataType.TEXT, 255.0, false, null);
        NAME = addColumn("NAME", DataType.TEXT, 255.0, false, null);
        EMAIL = addColumn("EMAIL", DataType.TEXT, 255.0, false, null);
        CREATION_DATE = addColumn("CREATION_DATE", DataType.DATETIME, 7.0, false,
                null);

        // configure key columns (primary key)
        setPrimaryKey(ID);

        addIndex("IDX_WHIRL_USERS_L", false, LOGIN);

        // Optimistic locking column
        /* no locking column specified */
    }
}
