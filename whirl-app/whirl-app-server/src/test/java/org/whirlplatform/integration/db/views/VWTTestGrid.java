package org.whirlplatform.integration.db.views;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBCommandExpr;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBView;
import org.whirlplatform.integration.db.TestTableSet;

@SuppressWarnings("serial")
public class VWTTestGrid extends DBView {

    public final DBViewColumn DFOBJ;
    public final DBViewColumn DFSTRING;
    public final DBViewColumn DFNUM;
    public final DBViewColumn DFDATE;
    public final DBViewColumn DFBOOLEAN;
    public final DBViewColumn DFLIST;
    public final DBViewColumn DFLISTDFNAME;

    public VWTTestGrid(DBDatabase db) {
        super("VW_T_TEST_GRID", db);
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL);
        DFSTRING = addColumn("DFSTRING", DataType.TEXT);
        DFNUM = addColumn("DFNUM", DataType.DECIMAL);
        DFDATE = addColumn("DFDATE", DataType.DATETIME);
        DFBOOLEAN = addColumn("DFBOOLEAN", DataType.BOOL);
        DFLIST = addColumn("DFLIST", DataType.DECIMAL);
        DFLISTDFNAME = addColumn("DFLISTDFNAME", DataType.TEXT);
    }

    @Override
    public DBCommandExpr createCommand() {
        TestTableSet db = (TestTableSet) getDatabase();
        DBCommand command = db.createCommand();
        command.select(db.T_TEST_GRID.DFOBJ, db.T_TEST_GRID.DFSTRING, db.T_TEST_GRID.DFNUM, db.T_TEST_GRID.DFDATE,
                db.T_TEST_GRID.DFBOOLEAN, db.T_TEST_GRID.DFLIST);
        command.select(db.T_TEST_GRID_LIST.LIST_DFNAME.as("DFLISTDFNAME"));
        command.where(db.T_TEST_GRID_LIST.DFOBJ.is(db.T_TEST_GRID.DFLIST));
        return command;
    }
}
