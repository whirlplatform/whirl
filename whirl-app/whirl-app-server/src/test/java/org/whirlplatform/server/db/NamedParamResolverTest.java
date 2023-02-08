package org.whirlplatform.server.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.oracle.DBDatabaseDriverOracle;
import org.junit.Assert;
import org.junit.Test;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowValue;

public class NamedParamResolverTest {

    @Test
    public void doubleQuotesOralceTest() {
        DBDatabaseDriver driver = new DBDatabaseDriverOracle();

        // row list value
        Map<String, DataValue> values = new HashMap<>();

        RowListValue value = new RowListValueImpl();
        value.setCode("value_test1");

        RowValue v1 = mock(RowValue.class);
        when(v1.getId()).thenReturn("1");
        when(v1.isChecked()).thenReturn(true);
        value.addRowValue(v1);

        RowValue v2 = mock(RowValue.class);
        when(v2.getId()).thenReturn("2");
        when(v2.isChecked()).thenReturn(false);
        value.addRowValue(v2);

        RowValue v10 = mock(RowValue.class);
        when(v10.getId()).thenReturn("10");
        when(v10.isChecked()).thenReturn(true);
        value.addRowValue(v10);

        values.put("value_test1", value);

        NamedParamResolver resolver = new NamedParamResolver(driver,
            "('':value_test1'')", values);

        Assert.assertEquals("Double quotes", "('1,10')",
            resolver.getResultSql());

        // null
        values = new HashMap<>();
        values.put("value_test2", new RowListValueImpl());
        value.setCode("value_test2");
        resolver = new NamedParamResolver(driver, "('':value_test2'')", values);
        Assert.assertEquals("Null verification", "(null)",
            resolver.getResultSql());
    }

}
