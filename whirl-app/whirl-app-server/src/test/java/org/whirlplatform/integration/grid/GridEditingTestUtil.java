package org.whirlplatform.integration.grid;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.whirlplatform.integration.db.DBInitUtil;
import org.whirlplatform.integration.db.GridDataExtractor;

public class GridEditingTestUtil {
    public static final Integer ID_1 = 2000011;
    public static final Integer ID_2 = 2000012;
    public static final Integer COPY_OF_ID_1 = 2000013;
    public static final Integer COPY_OF_ID_2 = 2000014;
    private static final String TEST_DATA_TABLE = "unicore.vw_t_test_grid";
    private static final String TEST_LIST_TABLE = "unicore.t_test_grid_list";
    private static final String TEST_LIST_VALUE_COLUMN = "LIST_DFNAME";

    private static final GridDataExtractor extractor = new GridDataExtractor(DBInitUtil.CONFIG);

    public static GridTestRowModel createFirstRow() {
        final GridTestRowModel result = new GridTestRowModel();
        result.setId(ID_1);
        result.addValue("DFOBJ", String.valueOf(ID_1));
        result.addValue("DFSTRING", "aaaaaaaa");
        result.addValue("DFNUM", "1212,2");
        result.addValue("DFDATE", "10.11.2012 10:11:12");
        result.addValue("DFBOOLEAN", "T");
        result.addValue("DFLIST", "1000000");
        result.addValue("DFLISTDFNAME", readDatabaseListValue(1000000));
        return result;
    }

    public static GridTestRowModel createFirstRowCopy() {
        final GridTestRowModel result = createFirstRow();
        result.addValue("DFOBJ", String.valueOf(COPY_OF_ID_1));
        return result;
    }

    public static GridTestRowModel createFirstUpdatedRow() {
        final GridTestRowModel result = createFirstRow();
        result.addValue("DFSTRING", "zzzzzzzz");
        result.addValue("DFNUM", "45");
        result.addValue("DFDATE", "12.11.2010 12:11:10");
        result.addValue("DFBOOLEAN", "F");
        result.addValue("DFLIST", "1000001");
        result.addValue("DFLISTDFNAME", readDatabaseListValue(1000001));
        return result;
    }

    public static GridTestRowModel createSecondRow() {
        final GridTestRowModel result = new GridTestRowModel();
        result.setId(ID_2);
        result.addValue("DFOBJ", String.valueOf(ID_2));
        result.addValue("DFSTRING", "bbbbbbbb");
        result.addValue("DFNUM", "123");
        result.addValue("DFDATE", "02.03.2004 05:06:07");
        result.addValue("DFBOOLEAN", "F");
        result.addValue("DFLIST", "1000002");
        result.addValue("DFLISTDFNAME", readDatabaseListValue(1000002));
        return result;
    }

    public static GridTestRowModel createSecondRowCopy() {
        final GridTestRowModel result = createSecondRow();
        result.addValue("DFOBJ", String.valueOf(COPY_OF_ID_2));
        return result;
    }

    public static GridTestRowModel createSecondUpdatedRow() {
        final GridTestRowModel result = createSecondRow();
        result.addValue("DFSTRING", "yyyyyyyyy");
        result.addValue("DFNUM", "123,3");
        result.addValue("DFDATE", "07.06.2005 04:03:02");
        result.addValue("DFBOOLEAN", "T");
        result.addValue("DFLIST", "1000003");
        result.addValue("DFLISTDFNAME", readDatabaseListValue(1000003));
        return result;
    }

    public static void deleteTestDatabaseRows() {
        final String queryTemplate =
                "delete from %s where dfobj=%s or dfobj=%s or dfobj=%s or dfobj=%s";
        final String query = String.format(queryTemplate, TEST_DATA_TABLE, ID_1, ID_2, COPY_OF_ID_1,
                COPY_OF_ID_2);
        extractor.execute(query);
    }

    public static Map<Integer, GridTestRowModel> readAllDatabaseRows() {
        final String queryTemplate = "select * from %s";
        final String query = String.format(queryTemplate, TEST_DATA_TABLE);
        return extractor.extractDbData(query);
    }

    public static Map<Integer, GridTestRowModel> readTestDatabaseRows() {
        final String queryTemplate =
                "select * from %s where dfobj=%s or dfobj=%s or dfobj=%s or dfobj=%s";
        final String query = String.format(queryTemplate, TEST_DATA_TABLE, ID_1, ID_2, COPY_OF_ID_1,
                COPY_OF_ID_2);
        return extractor.extractDbData(query);
    }

    public static GridTestRowModel readDatabaseRow(int dfobj) {
        if (dfobj <= 0) {
            throw new IllegalArgumentException("Wrong Dfobj value");
        }
        final String queryTemplate = "select * from %s where dfobj=%s";
        final String query = String.format(queryTemplate, TEST_DATA_TABLE, String.valueOf(dfobj));
        Map<Integer, GridTestRowModel> map = extractor.extractDbData(query);
        return (map.size() > 0) ? map.get(dfobj) : null;
    }

    public static void updateListDfobjValue(final GridTestRowModel row) {
        final String gridFirstListValue = row.getColumnValue("DFLISTDFNAME");
        row.addValue("DFLIST", findDatabaseListDfobj(gridFirstListValue));
    }

    public static String readDatabaseListValue(int dfobj) {
        if (dfobj <= 0) {
            throw new IllegalArgumentException("Wrong List Dfobj value");
        }
        final String queryTemplate = "select * from %s where dfobj=%s";
        final String query = String.format(queryTemplate, TEST_LIST_TABLE, String.valueOf(dfobj));
        Map<Integer, GridTestRowModel> map = extractor.extractDbData(query);
        return (map.size() > 0) ? map.get(dfobj).getColumnValue(TEST_LIST_VALUE_COLUMN) : null;
    }

    public static String findDatabaseListDfobj(final String list_dfname) {
        if (StringUtils.isEmpty(list_dfname)) {
            throw new IllegalArgumentException("The argument 'list_dfname' is empty");
        }
        final String queryTemplate = "select * from %s where list_dfname='%s'";
        final String query = String.format(queryTemplate, TEST_LIST_TABLE, list_dfname);
        Map<Integer, GridTestRowModel> map = extractor.extractDbData(query);
        for (Map.Entry<Integer, GridTestRowModel> entry : map.entrySet()) {
            return String.valueOf(entry.getKey());
        }
        return null;
    }

    @Test
    public void testListValue() {
        final String actual1 = readDatabaseListValue(1000000);
        assertEquals("Тип1", actual1);
        final String actual2 = readDatabaseListValue(1000001);
        assertEquals("Тип2", actual2);
        final String actual3 = readDatabaseListValue(1000002);
        assertEquals("Тип3", actual3);
        final String actual4 = readDatabaseListValue(1000003);
        assertEquals("Тип4", actual4);
    }

    @Test
    public void testListDfobj() {
        final String actual1 = findDatabaseListDfobj("Тип1");
        assertEquals("1000000", actual1);
        final String actual2 = findDatabaseListDfobj("Тип2");
        assertEquals("1000001", actual2);
        final String actual3 = findDatabaseListDfobj("Тип3");
        assertEquals("1000002", actual3);
        final String actual4 = findDatabaseListDfobj("Тип4");
        assertEquals("1000003", actual4);
    }
}
