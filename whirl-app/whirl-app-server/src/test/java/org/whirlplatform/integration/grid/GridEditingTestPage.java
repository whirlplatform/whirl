package org.whirlplatform.integration.grid;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;
import org.whirlplatform.selenium.ByWhirl;

import java.util.ArrayList;
import java.util.List;

@Location("main?role=it_grid_edit")
public class GridEditingTestPage extends AbstractPage {
    private final String GRID_CODE = "grid";
    private final String GRID_LIST_CODE = "grid_list";
    private final String GRID_LIST_CELL_LOCATOR = "whirl:EditGrid(code=%s)[Row(index=%d)[Cell(column=%s)]]";

    @FindByWhirl("whirl:EditGrid(code=" + GRID_CODE + ")")
    protected WebElement grid;

    @FindByWhirl("whirl:EditGrid(code=" + GRID_CODE + ")")
    private GridRowPageFragment gridRow;

    @FindByWhirl("whirl:EditGrid(code=" + GRID_LIST_CODE + ")")
    private GridRowPageFragment gridListRow;

    @FindByWhirl("whirl:EditGrid(code=" + GRID_LIST_CODE + ")")
    private FieldFormPage form;

    @FindByWhirl("whirl:EditGrid(code=" + GRID_CODE + ")[ToolBar]")
    private EditGridToolBarPageFragment toolbar;

    public void init() {
        gridRow.setGridCode(GRID_CODE);
        gridListRow.setGridCode(GRID_LIST_CODE);
        toolbar.setGridCode(GRID_CODE);
        form.setGridCode(GRID_CODE);
    }

    public void selectRowById(int id) {
        gridRow.setRowId(id);
        gridRow.clickCheck();
    }

    public void updateRowInline(GridTestRowModel row) {
        gridRow.setRowId(row.getId());
        gridRow.waitUntilGridIsVisible();
        // TODO не работает обновление текстового поля по нажатию на Enter, надо
        // смотреть глубИны
        // gridRow.setTextValue("DFSTRING", row.getColumnValue("DFSTRING"));
        gridRow.setNumberValue("DFNUM", row.getColumnValue("DFNUM"));
        gridRow.setDateValue("DFDATE", row.getColumnValue("DFDATE"));
        gridRow.setCheckBoxValue("DFBOOLEAN", row.getColumnValue("DFBOOLEAN"));
        gridRow.selectComboBoxValue("DFLIST", row.getColumnValue("DFLIST"));
        gridRow.clickCheck();
    }

    public void insertRowUsingForm(final GridTestRowModel rowModel) {
        form.setFormCode("insert");
        toolbar.clickAddButton();
        form.waitUntilFormIsVisible();
        form.setNumberFieldValue("DFOBJ", rowModel.getColumnValue("DFOBJ"));
        form.setTextFieldValue("DFSTRING", rowModel.getColumnValue("DFSTRING"));
        form.setNumberFieldValue("DFNUM", rowModel.getColumnValue("DFNUM"));
        form.setDateFieldValue("DFDATE", rowModel.getColumnValue("DFDATE"));
        form.setCheckBoxValue("DFBOOLEAN", rowModel.getColumnValue("DFBOOLEAN"));
        form.setComboBoxValue("DFLIST", rowModel.getColumnValue("DFLIST"));
        form.clickSaveButton();
    }

    public void updateRowsUsingForm(GridTestRowModel... rows) {
        for (GridTestRowModel row : rows) {
            selectRowById(row.getId());
        }
        toolbar.clickEditButton();
        List<String> formCodes = new ArrayList<>();
        for (GridTestRowModel row : rows) {
            formCodes.add(String.format("update-%d", row.getId()));
        }
        for (int i = rows.length - 1; i >= 0; i--) {
            form.setFormCode(formCodes.get(i));
            GridTestRowModel row = rows[i];
            form.waitUntilFormIsVisible();
            form.setTextFieldValue("DFSTRING", row.getColumnValue("DFSTRING"));
            form.setNumberFieldValue("DFNUM", row.getColumnValue("DFNUM"));
            form.setDateFieldValue("DFDATE", row.getColumnValue("DFDATE"));
            form.setCheckBoxValue("DFBOOLEAN", row.getColumnValue("DFBOOLEAN"));
            form.setComboBoxValue("DFLIST", row.getColumnValue("DFLIST"));
            form.clickSaveButton();
        }
        for (GridTestRowModel row : rows) {
            selectRowById(row.getId());
        }
    }

    public void copyRowsUsingForm(GridTestRowModel... rows) {
        List<Integer> deselect = new ArrayList<>();
        for (GridTestRowModel row : rows) {
            selectRowById(row.getId());
            deselect.add(row.getId());
        }
        toolbar.clickCopyButton();
        List<String> formCodes = new ArrayList<>();
        for (GridTestRowModel row : rows) {
            formCodes.add(String.format("copy-%d", row.getId()));
        }
        for (int i = rows.length - 1; i >= 0; i--) {
            form.setFormCode(formCodes.get(i));
            GridTestRowModel row = rows[i];
            form.waitUntilFormIsVisible();
            form.setNumberFieldValue("DFOBJ", row.getColumnValue("DFOBJ"));
            form.setTextFieldValue("DFSTRING", row.getColumnValue("DFSTRING"));
            form.setNumberFieldValue("DFNUM", row.getColumnValue("DFNUM"));
            form.setDateFieldValue("DFDATE", row.getColumnValue("DFDATE"));
            form.setCheckBoxValue("DFBOOLEAN", row.getColumnValue("DFBOOLEAN"));
            form.setComboBoxValue("DFLIST", row.getColumnValue("DFLIST"));
            form.clickSaveButton();
            row.setId(Integer.parseInt(row.getColumnValue("DFOBJ")));
        }
        for (Integer rowId : deselect) {
            selectRowById(rowId);
        }
    }

    public void deleteGridRows(int... rowIds) {
        for (int rowId : rowIds) {
            selectRowById(rowId);
        }
        toolbar.clickDeleteButton();
        final String confirm = String.format("whirl:Dialog(id=%s-delete)[YesButton]", GRID_CODE);
        By by = new ByWhirl(confirm);
        Graphene.waitGui().until().element(by).is().visible();
        WebElement yes = webDriver.findElement(by);
        Graphene.guardAjax(yes).click();
    }

    public GridTestRowModel readGridRow(int id) {
        GridTestRowModel result = null;
        gridRow.setRowId(id);
        gridRow.waitUntilGridIsVisible();
        try {
            result = new GridTestRowModel();
            result.setId(id);
            result.addValue("DFOBJ", gridRow.getTextValue("DFOBJ"));
            result.addValue("DFSTRING", gridRow.getTextValue("DFSTRING"));
            result.addValue("DFNUM", gridRow.getTextValue("DFNUM"));
            result.addValue("DFDATE", gridRow.getTextValue("DFDATE"));
            result.addValue("DFBOOLEAN", gridRow.getCheckBoxValue("DFBOOLEAN"));
            final String dflistValue = gridRow.getTextValue("DFLIST");
            result.addValue("DFLISTDFNAME", dflistValue);
            result.addValue("DFLIST", findListDfobj(GRID_LIST_CODE, "LIST_DFNAME", dflistValue));
        } catch (NoSuchElementException e) {
            return null;
        }
        return result;
    }

    private String findListDfobj(final String gridCode, final String columnName, final String columnValue) {
        int rowIndex = 0;
        String result = "";
        try {
            while (true) {
                final String locator = String.format(GRID_LIST_CELL_LOCATOR, gridCode, rowIndex, columnName);
                final WebElement cell = webDriver.findElement(new ByWhirl(locator));
                if (columnValue.equals(cell.getText())) {
                    final String dfobjLocator = String.format(GRID_LIST_CELL_LOCATOR, gridCode, rowIndex, "DFOBJ");
                    final WebElement dfobjCell = webDriver.findElement(new ByWhirl(dfobjLocator));
                    result = dfobjCell.getText();
                    break;
                }
                rowIndex++;
            }
        } catch (NoSuchElementException e) {
        }
        return result;
    }
}
