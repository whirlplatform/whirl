package org.whirlplatform.integration.grid;

import org.apache.commons.lang3.StringUtils;
import org.whirlplatform.integration.AbstractPage;

public abstract class AbstractGridPart extends AbstractPage {
    private final String PARAMETER_CODE = "code";// TODO ComponentBuilder.LocatorParams.PARAMETER_CODE;
    private final String PARAMETER_ID = "id";// TODO ComponentBuilder.LocatorParams.PARAMETER_ID;
    private final String GRID = "whirl:EditGridBuilder(%s)";

    private String gridParameterName;
    private String gridParameterValue;

    public void setGridId(final String gridId) {
        this.gridParameterName = PARAMETER_ID;
        this.gridParameterValue = gridId;
    }

    public void setGridCode(final String gridCode) {
        this.gridParameterName = PARAMETER_CODE;
        this.gridParameterValue = gridCode;
    }

    private String getGridParameter() {
        checkParameter();
        return String.format("%s=%s", gridParameterName, gridParameterValue);
    }

    public String getGridStringLocator() {
        return String.format(GRID, getGridParameter());
    }

    public String getGridIdentificator() {
        checkParameter();
        return gridParameterValue;
    }

    private void checkParameter() {
        if (StringUtils.isEmpty(gridParameterName) || StringUtils.isEmpty((gridParameterValue))) {
            throw new IllegalArgumentException("Grid parameter was not initialised");
        }
        boolean isId = PARAMETER_ID.equals(gridParameterName);
        boolean isCode = PARAMETER_CODE.equals(gridParameterName);
        if (!isId && !isCode) {
            throw new IllegalArgumentException(String.format("Unknown Grid parameter '%s'", gridParameterName));
        }
    }
}
