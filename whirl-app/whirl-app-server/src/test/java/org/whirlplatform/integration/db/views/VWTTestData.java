package org.whirlplatform.integration.db.views;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBCommandExpr;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBView;
import org.whirlplatform.integration.db.TestTableSet;

@SuppressWarnings("serial")
public class VWTTestData extends DBView {

    public final DBViewColumn DFOBJ;
    public final DBViewColumn DFNUMBER;
    public final DBViewColumn LIST_DFNAME;
    public final DBViewColumn DFPARENT;

    public VWTTestData(DBDatabase db) {
        super("VW_T_TEST_DATA", db);
        DFOBJ = addColumn("DFOBJ", DataType.DECIMAL);
        DFNUMBER = addColumn("DFNUMBER", DataType.DECIMAL);
        LIST_DFNAME = addColumn("LIST_DFNAME", DataType.TEXT);
        DFPARENT = addColumn("DFPARENT", DataType.DECIMAL);
    }

    @Override
    public DBCommandExpr createCommand() {
        TestTableSet db = (TestTableSet) getDatabase();
        DBCommand command = db.createCommand();
        command.select(db.T_TEST_DATA.DFOBJ, db.T_TEST_DATA.DFNUMBER, db.T_TEST_DATA.DFSTRING.as("LIST_DFNAME"),
                db.T_TEST_DATA.DFPARENT);
        command.orderBy(LIST_DFNAME);
        return command;

    }

}
