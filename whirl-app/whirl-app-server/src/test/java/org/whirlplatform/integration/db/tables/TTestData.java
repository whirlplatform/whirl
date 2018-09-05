
package org.whirlplatform.integration.db.tables;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

public class TTestData extends DBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn DFOBJ;
    public final DBTableColumn DFDELETE;
    public final DBTableColumn DFNUMBER;
    public final DBTableColumn DFBOOLEAN;
    public final DBTableColumn DFDATE;
    public final DBTableColumn DFSTRING;
    public final DBTableColumn DFPARENT;

    public TTestData(DBDatabase db) {
        super("T_TEST_DATA", db);

        // create columns
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL, 0.0, true, null);
        DFDELETE = addColumn("DFDELETE", DataType.BOOL, 1.0, false, null);
        DFNUMBER = addColumn("DFNUMBER", DataType.DECIMAL, 0.0, false, null);
        DFBOOLEAN = addColumn("DFBOOLEAN", DataType.BOOL, 1.0, true, null);
        DFDATE = addColumn("DFDATE", DataType.DATETIME, 7.0, false, null);
        DFSTRING = addColumn("DFSTRING", DataType.TEXT, 255.0, false, null);
        DFPARENT = addColumn("DFPARENT", DataType.DECIMAL, 0.0, false, null);

        // configure key columns (primary key)
        setPrimaryKey(DFOBJ);

        // Optimistic locking column
        /*no locking column specified*/
    }
}
