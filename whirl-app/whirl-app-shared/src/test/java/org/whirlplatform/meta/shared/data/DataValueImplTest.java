package org.whirlplatform.meta.shared.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataValueImplTest {

    private DataValue dataValue;

    @Before
    public void before() {
        dataValue = new DataValueImpl();
    }

    @Test
    public void testInject() {
        Assert.assertNotNull(dataValue);
    }

    @Test
    public void testTypeAndValueSettings() {
        dataValue.setType(DataType.STRING);
        dataValue.setValue("Test");
        String string = dataValue.getString();
        Assert.assertEquals(DataType.STRING, dataValue.getType());
        Assert.assertEquals("Test", string);
        dataValue.setType(DataType.BOOLEAN);
        dataValue.setValue(true);
        Boolean bool = dataValue.getBoolean();
        Assert.assertEquals(DataType.BOOLEAN, dataValue.getType());
        Assert.assertEquals(true, bool);
    }

}
