
package org.whirlplatform.integration.db.tables;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

public class TTestGridList extends DBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn DFOBJ;
    public final DBTableColumn LIST_DFNAME;

    public TTestGridList(DBDatabase db) {
        super("T_TEST_GRID_LIST", db);

        // create columns
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL, 0.0, true, null);
        LIST_DFNAME = addColumn("LIST_DFNAME", DataType.TEXT, 255.0, true, null);

        // configure key columns (primary key)
        setPrimaryKey(DFOBJ);

        // Optimistic locking column
        /*no locking column specified*/
    }
}
