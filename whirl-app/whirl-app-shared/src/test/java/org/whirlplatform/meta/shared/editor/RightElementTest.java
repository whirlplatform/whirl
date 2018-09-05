package org.whirlplatform.meta.shared.editor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RightElementTest {
    private final String ORIGIN_CONDITION_VALUE = "WHERE A='A'";
    private final String CLONE_CONDITION_VALUE = "WHERE B='B'";
    private final RightType ORIGIN_TYPE = RightType.ADD;
    private final RightType CLONE_TYPE = RightType.EXECUTE;
    SQLCondition originCondition;
    SQLCondition cloneCondition;

    @Before
    public void initializeConditions() {
        originCondition = new SQLCondition();
        originCondition.setValue(ORIGIN_CONDITION_VALUE);
        cloneCondition = new SQLCondition();
        cloneCondition.setValue(CLONE_CONDITION_VALUE);
    }

    @Test
    public void creatingConditionsTest() {
        Assert.assertNotNull("Original condition was not initialized", originCondition);
        Assert.assertNotNull("Clone condition was not initialized", cloneCondition);
    }

    @Test
    public void setValuesTest() {
        RightElement origin = new RightElement();
        origin.setType(ORIGIN_TYPE);
        origin.setCondition(originCondition);
        Assert.assertEquals("RightElement type was not set", ORIGIN_TYPE, origin.getType());
        Assert.assertEquals("RightElement condition was not set", ORIGIN_CONDITION_VALUE,
                origin.getCondition().getValue());
    }

    @Test
    public void cloneTest() {
        RightElement origin = new RightElement(ORIGIN_TYPE);
        origin.setCondition(originCondition);
        RightElement clone = origin.clone();
        Assert.assertEquals("Clone condition value was not copied", origin.getCondition().getValue(),
                clone.getCondition().getValue());
        Assert.assertEquals("Clone type value was not copied", origin.getType(), clone.getType());
        Assert.assertEquals("Clone is not equal to the origin", origin, clone);
        clone.setType(CLONE_TYPE);
        clone.setCondition(cloneCondition);
        Assert.assertEquals("Origin type depends on clone type", ORIGIN_TYPE, origin.getType());
        Assert.assertEquals("Origin condition depends on clone type", ORIGIN_CONDITION_VALUE,
                origin.getCondition().getValue());
        Assert.assertNotEquals("Clone is equal to the origin", origin, clone);
    }
}
