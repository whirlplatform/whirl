package org.whirlplatform.meta.shared.editor;

import org.junit.Assert;
import org.junit.Test;

public class BooleanConditionTest {

    @Test
    public void cloneTest() {
        BooleanCondition origin = new BooleanCondition();
        origin.setValue(true);
        Assert.assertTrue("BooleanCondition value was not set", origin.getValue());
        BooleanCondition clone = origin.clone();
        Assert.assertTrue("BooleanCondition clone value was not copied", clone.getValue());
        clone.setValue(false);
        Assert.assertFalse("BooleanCondition clone value was not set", clone.getValue());
        Assert.assertTrue("BooleanCondition origin value depends on clone value",
            origin.getValue());
    }
}
