package org.whirlplatform.meta.shared.editor;

import org.junit.Assert;
import org.junit.Test;

public class SQLConditionTest {
    private final String ORIGIN_VALUE = "WHERE A='A'";
    private final String ORIGIN_VALUE_2 = "WHERE A='A'";
    private final String CLONE_VALUE = "WHERE B='B'";

    @Test
    public void cloneTest() {
        SQLCondition origin = new SQLCondition();
        origin.setValue(ORIGIN_VALUE);
        Assert.assertEquals("SQLCondition origin value was not set", ORIGIN_VALUE,
            origin.getValue());
        SQLCondition clone = origin.clone();
        Assert.assertEquals("SQLCondition clone value was not copied", ORIGIN_VALUE,
            clone.getValue());
        clone.setValue(CLONE_VALUE);
        Assert.assertEquals("SQLCondition clone value was not set", CLONE_VALUE, clone.getValue());
        Assert.assertEquals("SQLCondition origin value depends on clone value", ORIGIN_VALUE,
            origin.getValue());
    }

    @Test
    public void equalsTest() {
        SQLCondition origin = new SQLCondition();
        origin.setValue(ORIGIN_VALUE);
        SQLCondition origin2 = new SQLCondition();
        origin2.setValue(ORIGIN_VALUE_2);
        SQLCondition clone = new SQLCondition();
        clone.setValue(CLONE_VALUE);
        Assert.assertEquals("Conditions with the same value do not equal to each other", origin,
            origin2);
        Assert.assertNotEquals("Conditions have the same value", origin, clone);
        Assert.assertNotEquals("Conditions have the same value", origin2, clone);
    }
}
