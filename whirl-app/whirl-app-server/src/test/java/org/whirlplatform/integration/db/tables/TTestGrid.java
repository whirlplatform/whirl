
package org.whirlplatform.integration.db.tables;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

public class TTestGrid extends DBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn DFOBJ;
    public final DBTableColumn DFSTRING;
    public final DBTableColumn DFNUM;
    public final DBTableColumn DFDATE;
    public final DBTableColumn DFBOOLEAN;
    public final DBTableColumn DFLIST;

    public TTestGrid(DBDatabase db) {
        super("T_TEST_GRID", db);

        // create columns
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL, 0.0, true, null);
        DFSTRING = addColumn("DFSTRING", DataType.TEXT, 255.0, false, null);
        DFNUM = addColumn("DFNUM", DataType.DECIMAL, 0.0, false, null);
        DFDATE = addColumn("DFDATE", DataType.DATETIME, 7.0, false, null);
        DFBOOLEAN = addColumn("DFBOOLEAN", DataType.BOOL, 1.0, false, null);
        DFLIST = addColumn("DFLIST", DataType.DECIMAL, 0.0, false, null);

        // configure key columns (primary key)
        setPrimaryKey(DFOBJ);

        // Optimistic locking column
        /*no locking column specified*/
    }
}
