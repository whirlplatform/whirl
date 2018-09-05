
package org.whirlplatform.integration.db.tables;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

public class GridTest extends DBTable {

    private static final long serialVersionUID = 1L;

    // define columns
    public final DBTableColumn ID;
    public final DBTableColumn STRINGS;
    public final DBTableColumn NUMBERS;
    public final DBTableColumn FLOAT_NUMS;
    public final DBTableColumn DATES;

    public GridTest(DBDatabase db) {
        super("GRID_TEST", db);

        // create columns
        ID = addColumn("ID", DataType.DECIMAL, 0.0, true, null);
        STRINGS = addColumn("STRINGS", DataType.TEXT, 250.0, false, null);
        NUMBERS = addColumn("NUMBERS", DataType.DECIMAL, 0.0, false, null);
        FLOAT_NUMS = addColumn("FLOAT_NUMS", DataType.DECIMAL, 38.5, false, null);
        DATES = addColumn("DATES", DataType.DATETIME, 7.0, false, null);

        // configure key columns (primary key)
        setPrimaryKey(ID);

        // Optimistic locking column
        /* no locking column specified */
    }
}
