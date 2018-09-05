package org.whirlplatform.integration.db.views;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBCommandExpr;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBView;
import org.whirlplatform.integration.db.TestTableSet;

@SuppressWarnings("serial")
public class VTTestGrid extends DBView {

    public final DBViewColumn DFOBJ;
    public final DBViewColumn LIST_DFNAME;

    public VTTestGrid(DBDatabase db) {
        super("V_T_TEST_GRID", db);
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL);
        LIST_DFNAME = addColumn("LIST_DFNAME", DataType.TEXT);
    }

    @Override
    public DBCommandExpr createCommand() {
        TestTableSet db = (TestTableSet) getDatabase();
        DBCommand command = db.createCommand();
        command.select(db.T_TEST_GRID_LIST.DFOBJ, db.T_TEST_GRID_LIST.LIST_DFNAME);
        return command;
    }

}
