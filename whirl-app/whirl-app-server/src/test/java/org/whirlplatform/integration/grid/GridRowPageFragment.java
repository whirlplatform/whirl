package org.whirlplatform.integration.grid;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.whirlplatform.selenium.ByWhirl;

public class GridRowPageFragment extends AbstractGridPart {
    private final String PARAMETER_INDEX = "index"; //TODO EditGridBuilder.LocatorParams.PARAMETER_INDEX;
    private final String PARAMETER_ID = "id"; //TODO EditGridBuilder.LocatorParams.PARAMETER_ID;

    private final String EDITING_NUMBER = "%s[Row(%s)[CellEditing[NumberFieldBuilder(code=%s)[Input]]]]";
    private final String EDITING_TEXT = "%s[Row(%s)[CellEditing[TextFieldBuilder(code=%s)[Input]]]]";
    private final String EDITING_DATE = "%s[Row(%s)[CellEditing[DateFieldBuilder(code=%s)[Input]]]]";
    private final String EDITING_COMBO = "%s[Row(%s)[CellEditing[ComboBoxBuilder(code=%s)[Input]]]]";
    private final String COMBO_TRIGGER = "%s[Row(%s)[CellEditing[ComboBoxBuilder(code=%s)[Trigger]]]]";
    private final String COMBO_ITEM = "%s[Row(%s)[CellEditing[ComboBoxBuilder(code=%s)[Item(id=%s)]]]]";
    private final String ROW_CELL = "%s[Row(%s)[Cell(column=%s)]]";
    private final String ROW_CHECK = "%s[Row(%s)[Check]]";

    @Root
    WebElement grid;

    private String rowParameterName;
    private String rowParameterValue;

    public GridRowPageFragment setRowId(final String rowId) {
        setRowParameter(PARAMETER_ID, rowId);
        return this;
    }

    public GridRowPageFragment setRowId(int rowId) {
        return setRowId(String.valueOf(rowId));
    }

    public GridRowPageFragment setRowIndex(final String rowIndex) {
        setRowParameter(PARAMETER_INDEX, rowIndex);
        return this;
    }

    public GridRowPageFragment setRowIndex(int rowIndex) {
        return setRowIndex(String.valueOf(rowIndex));
    }

    public void clickCheck() {
        final String locator = String.format(ROW_CHECK, getGridStringLocator(), getRowParameter());
        final By by = new ByWhirl(locator);
        Graphene.waitGui().until().element(by).is().visible();
        WebElement checkCell = findElementByWhirl(locator);
        checkCell.click();
    }

    public String getTextValue(final String columnCode) {
        WebElement cell = findCell(columnCode);
        return cell.getText();
    }

    public String getCheckBoxValue(final String columnCode) {
        WebElement checkbox = findCheckBox(columnCode);
        return (checkbox.isSelected()) ? "T" : "F";
    }

    public void setCheckBoxValue(final String columnCode, final String value) {
        WebElement checkbox = findCheckBox(columnCode);
        if (checkbox != null) {
            if (checkbox.isSelected()) {
                checkbox.click();
            }
            if ("T".equals(value)) {
                checkbox.click();
            }
        } else {
            throw new IllegalArgumentException(String.format("The column '%s' is not a checkbox", columnCode));
        }
    }

    public void setNumberValue(final String columnCode, final String value) {
        setInlineFieldValue(EDITING_NUMBER, columnCode, value);
    }

    public void setTextValue(final String columnCode, final String value) {
        setInlineFieldValue(EDITING_TEXT, columnCode, value);
    }

    public void setDateValue(final String columnCode, final String value) {
        setInlineFieldValue(EDITING_DATE, columnCode, value);
    }

    public void setComboBoxValue(final String columnCode, final String value) {
        setInlineFieldValue(EDITING_COMBO, columnCode, value);
    }

    public void selectComboBoxValue(final String columnCode, final String itemId) {
        if (StringUtils.isEmpty(itemId)) {
            throw new IllegalArgumentException("The item id is null");
        }
        doubleClick(columnCode);
        WebElement trigger = findInlineField(COMBO_TRIGGER, columnCode);
        Graphene.waitGui().until().element(trigger).is().visible();
        trigger.click();
        String itemLocator = String.format(COMBO_ITEM, getGridStringLocator(), getRowParameter(), columnCode, itemId);
        Graphene.waitGui().until().element(new ByWhirl(itemLocator)).is().visible();
        WebElement item = findElementByWhirl(itemLocator);
        item.click();
    }

    private void setInlineFieldValue(final String template, final String columnCode, final String value) {
        doubleClick(columnCode);
        WebElement field = findInlineField(template, columnCode);
        Graphene.waitGui().until().element(field).is().visible();
        field.clear();
        field.sendKeys(value);
        field.sendKeys(Keys.ENTER);
    }

    private void doubleClick(final String columnCode) {
        WebElement cell = findCell(columnCode);
        Actions builder = new Actions(webDriver);
        builder.doubleClick(cell).build().perform();
    }

    private String getRowParameter() {
        if (StringUtils.isEmpty(rowParameterName) || StringUtils.isEmpty(rowParameterValue)) {
            throw new IllegalArgumentException("The row parameter was not initialised");
        }
        boolean isId = PARAMETER_ID.equals(rowParameterName);
        boolean isIndex = PARAMETER_INDEX.equals(rowParameterName);
        if (!isId && !isIndex) {
            throw new IllegalArgumentException(String.format("Unknown row parameter: '%s'", rowParameterName));
        }
        return String.format("%s=%s", rowParameterName, rowParameterValue);
    }

    private void setRowParameter(final String rowParameterName, final String rowParameterValue) {
        if (StringUtils.isEmpty(rowParameterValue)) {
            throw new IllegalArgumentException("The row parameter value is empty");
        }
        this.rowParameterName = rowParameterName;
        this.rowParameterValue = rowParameterValue;
    }

    private WebElement findCell(final String columnCode) {
        if (StringUtils.isEmpty(columnCode)) {
            throw new IllegalArgumentException("The column code is empty");
        }
        final String locator = String.format(ROW_CELL, getGridStringLocator(), getRowParameter(), columnCode);
        WebElement result = findElementByWhirl(locator);
        return result;
    }

    private WebElement findCheckBox(final String columnCode) {
        WebElement cell = findCell(columnCode);
        WebElement result = cell.findElement(By.tagName("input"));
        return result;
    }

    private WebElement findInlineField(final String template, final String columnCode) {
        if (StringUtils.isEmpty(columnCode)) {
            throw new IllegalArgumentException("The column code of the field is empty");
        }
        final String locator = String.format(template, getGridStringLocator(), getRowParameter(), columnCode);
        WebElement result = findElementByWhirl(locator);
        return result;
    }

    public void waitUntilGridIsVisible() {
        Graphene.waitGui().until().element(grid).is().visible();
    }
}
