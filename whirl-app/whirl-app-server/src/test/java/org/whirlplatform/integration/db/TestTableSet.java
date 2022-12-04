package org.whirlplatform.integration.db;

import org.apache.empire.db.DBDatabase;
import org.whirlplatform.integration.db.tables.GridTest;
import org.whirlplatform.integration.db.tables.TTestData;
import org.whirlplatform.integration.db.tables.TTestGrid;
import org.whirlplatform.integration.db.tables.TTestGridList;
import org.whirlplatform.integration.db.views.VTTestData;
import org.whirlplatform.integration.db.views.VTTestGrid;
import org.whirlplatform.integration.db.views.VWTTestData;
import org.whirlplatform.integration.db.views.VWTTestGrid;

@SuppressWarnings("serial")
public class TestTableSet extends DBDatabase {

    private static TestTableSet instance;

    public final GridTest GRID_TEST = new GridTest(this);
    public final TTestData T_TEST_DATA = new TTestData(this);
    public final TTestGrid T_TEST_GRID = new TTestGrid(this);
    public final TTestGridList T_TEST_GRID_LIST = new TTestGridList(this);

    public final VWTTestGrid VW_T_TEST_GRID = new VWTTestGrid(this);
    public final VWTTestData VW_T_TEST_DATA = new VWTTestData(this);
    public final VTTestGrid V_T_TEST_GRID = new VTTestGrid(this);
    public final VTTestData V_T_TEST_DATA = new VTTestData(this);


    private TestTableSet() {
    }

    public static TestTableSet get() {
        if (instance == null) {
            instance = new TestTableSet();
        }
        return instance;
    }

}
