package org.whirlplatform.integration.grid;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;
import org.whirlplatform.selenium.ByWhirl;

import java.util.LinkedHashMap;
import java.util.List;

@Location("uniapp?role=edc")
public class GridTestPage extends AbstractPage {

    private final String gridLocator = "whirl:EditGridBuilder(id=88760223-DE0A-4253-960B-4E7230D840CB)";
    @FindByWhirl(gridLocator)
    protected WebElement grid;

    private String indexBasedRowLocator = "[Row(index=%d)[Cell(column=%s)]]";
    private String triggerLocator = "[Paginator[Selector[Trigger]]]";
    private String selectorItemLocator = "[Paginator[Selector[Item(value=%d)]]]";
    private String gridPageLocatorPattern = "[Paginator[PageButton(num=%d)]]";

    private String conditionTrigger = "[FilterPanel[Filter(mdname=%s)[Condition[ComboBox[Trigger]]]]]";
    private String conditionItem = "[FilterPanel[Filter(mdname=%s)[Condition[ComboBox[Item(value=%s)]]]]]";
    private String firstValueTextField = "[FilterPanel[Filter(mdname=%s)[FirstVal[TextField[Input]]]]]";
    private String secondValueTextField = "[FilterPanel[Filter(mdname=%s)[SecondVal[TextField[Input]]]]]";

    private String sortFromPanelByLabel = "[SortPanel[FromPanel[SortVal(label=%s)]]]";
    private String sortFromPanelByIndex = "[SortPanel[FromPanel[SortVal(index=%d)]]]";
    private String sortToPanelByLabel = "[SortPanel[ToPanel[SortVal(label=%s)]]]";
    private String sortToPanelByIndex = "[SortPanel[ToPanel[SortVal(index=%d)]]]";
    private String sortToPanelImg = "[SortPanel[ToPanel[SortVal(label=%s)[Img]]]]";
    private String sortButtonsPattern = "[SortPanel[%s]]";

    public enum SortPanelButtons {
        RIGHT("Right"), LEFT("Left"), ALL_RIGHT("AllRight"), ALL_LEFT("AllLeft"), DOWN("Down"), UP("Up"), SAVE(
                "SaveButton"), CLOSE("CloseButton");

        private final String text;

        SortPanelButtons(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private List<String> columns;

    public void configure(List<String> colNames) {
        this.columns = colNames;
    }

    @Override
    public void waitForPageLoading() {
        super.waitForPageLoading();
        waitForVisibility(grid);
    }

    /**
     * Парсит основную часть грида. Формирует колекцию объектов описывающих ряд
     * грида.
     *
     * @return
     */
    public LinkedHashMap<Integer, GridTestRowModel> parseGrid() {
        waitForVisibility(grid);
        LinkedHashMap<Integer, GridTestRowModel> rows = new LinkedHashMap<>();
        int currentRowInd = 0;
        String rowLocator = gridLocator + String.format(indexBasedRowLocator, currentRowInd, columns.get(0));
        WebElement row = webDriver.findElement(new ByWhirl(rowLocator));
        try {
            // Цикл прерывается невозможностью найти элемент.
            while (true) {
                // Итерация по ячейкам ряда и создание TestRowModel.
                GridTestRowModel rowModel = parseRow(currentRowInd);
                if (rowModel.getId() == null) {
                    rowModel.setId(currentRowInd);
                }
                rows.put(rowModel.getId(), rowModel);
                currentRowInd++;
                rowLocator = gridLocator + String.format(indexBasedRowLocator, currentRowInd, columns.get(0));
                row = webDriver.findElement(new ByWhirl(rowLocator));
            }
        } catch (NoSuchElementException e) {
            // Не найден элемент по созданному локатору.
        }
        return rows;
    }

    private GridTestRowModel parseRow(int rowIndex) {
        GridTestRowModel rowModel = new GridTestRowModel();
        try {
            for (String colName : columns) {
                String currentColLocator = gridLocator + String.format(indexBasedRowLocator, rowIndex, colName);
                WebElement cell = webDriver.findElement(new ByWhirl(currentColLocator));
                if ("ID".equals(colName) || "DFOBJ".equals(colName)) {
                    rowModel.setId(Integer.valueOf(cell.getText()));
                }
                rowModel.addValue(colName, cell.getText());
            }
        } catch (NoSuchElementException e) {
        }
        return rowModel;
    }

    public void setSelectorValue(int value) {
        WebElement trigger = webDriver.findElement(new ByWhirl(gridLocator + triggerLocator));
        Graphene.waitGui(webDriver).until().element(trigger).is().visible();
        trigger.click();
        String itemLocator = gridLocator + String.format(selectorItemLocator, value);
        WebElement item = webDriver.findElement(new ByWhirl(itemLocator));
        Graphene.waitGui(webDriver).until().element(item).is().visible();
        Graphene.guardAjax(item).click();
    }

    public void changeGridPage(int page) {
        String pageButtonLocator = gridLocator + String.format(gridPageLocatorPattern, page);
        clickPaginatorButton(pageButtonLocator);
    }

    private void clickPaginatorButton(String locator) {
        WebElement paginButton = webDriver.findElement(new ByWhirl(locator));
        Graphene.waitGui(webDriver).until().element(paginButton).is().visible();
        Graphene.guardAjax(paginButton).click();
    }

    public void goToNextPage() {
        String nextButtonLocator = gridLocator + "[Paginator[NextButton]]";
        clickPaginatorButton(nextButtonLocator);
    }

    public void goToLastButton() {
        String lastButtonLocator = gridLocator + "[Paginator[LastButton]]";
        clickPaginatorButton(lastButtonLocator);
    }

    public void goToPrevPage() {
        String prevButtonLocator = gridLocator + "[Paginator[PrevButton]]";
        clickPaginatorButton(prevButtonLocator);
    }

    public void goToFirstPage() {
        String firstButtonLocator = gridLocator + "[Paginator[FirstButton]]";
        clickPaginatorButton(firstButtonLocator);
    }

    public void setPageNum(int num) {
        String numFieldLocator = gridLocator + "[Paginator[NumField]]";
        WebElement numField = webDriver.findElement(new ByWhirl(numFieldLocator));
        Graphene.waitGui(webDriver).until().element(numField).is().visible();
        numField.click();
        numField.sendKeys(Keys.BACK_SPACE);
        numField.sendKeys(String.valueOf(num));
        Graphene.guardAjax(numField).sendKeys(Keys.ENTER);
    }

    private void openFilterPanel() {
        String searchButtonLoc = gridLocator + "[ToolBar[SearchButton]]";
        WebElement searchButton = webDriver.findElement(new ByWhirl(searchButtonLoc));
        Graphene.waitGui(webDriver).until().element(searchButton).is().visible();
        searchButton.click();
        String filterPanelLoc = gridLocator + "[FilterPanel]";
        WebElement filterPanel = webDriver.findElement(new ByWhirl(filterPanelLoc));
        Graphene.waitGui(webDriver).until().element(filterPanel).is().visible();
    }

    public void setFilter(String filterName, String condition, String firstVal, String secondVal) {
        // TODO: Расширить для работы с комбо - боксами и т.д!
        openFilterPanel();
        String triggerLocator = gridLocator + String.format(conditionTrigger, filterName);
        WebElement trigger = webDriver.findElement(new ByWhirl(triggerLocator));
        trigger.click();
        String itemLocator = gridLocator + String.format(conditionItem, filterName, condition);
        WebElement item = webDriver.findElement(new ByWhirl(itemLocator));
        item.click();
        String firstValLocator = gridLocator + String.format(firstValueTextField, filterName);
        WebElement firstValInput = webDriver.findElement(new ByWhirl(firstValLocator));
        firstValInput.clear();
        firstValInput.sendKeys(firstVal);
        if (secondVal != null) {
            String secondValLocator = gridLocator + String.format(secondValueTextField, filterName);
            WebElement secondValInput = webDriver.findElement(new ByWhirl(secondValLocator));
            secondValInput.clear();
            secondValInput.sendKeys(secondVal);
        }
        String searchButtonLoc = gridLocator + "[FilterPanel[SearchButton]]";
        WebElement searchButton = webDriver.findElement(new ByWhirl(searchButtonLoc));
        Graphene.guardAjax(searchButton).click();
        String closeButtonLoc = gridLocator + "[FilterPanel[CloseButton]]";
        WebElement closeButton = webDriver.findElement(new ByWhirl(closeButtonLoc));
        closeButton.click();
    }

    public void setFilter(String filterName, String condition, String firstVal) {
        setFilter(filterName, condition, firstVal, null);
    }

    public void clearFilter() {
        openFilterPanel();
        String clearButtonLoc = gridLocator + "[FilterPanel[ClearButton]]";
        WebElement clearButton = webDriver.findElement(new ByWhirl(clearButtonLoc));
        clearButton.click();
        String closeButtonLoc = gridLocator + "[FilterPanel[CloseButton]]";
        WebElement closeButton = webDriver.findElement(new ByWhirl(closeButtonLoc));
        closeButton.click();
    }

    public void openSortPanel() {
        String sortButtonLoc = gridLocator + "[ToolBar[SortButton]]";
        WebElement sortButton = webDriver.findElement(new ByWhirl(sortButtonLoc));
        Graphene.waitGui(webDriver).until().element(sortButton).is().visible();
        sortButton.click();
        String sortPanelLoc = gridLocator + "[SortPanel]";
        WebElement sortPanel = webDriver.findElement(new ByWhirl(sortPanelLoc));
        Graphene.waitGui(webDriver).until().element(sortPanel).is().visible();
    }

    public void closeSortPanel() {
        String closeButtonLoc = gridLocator + "[SortPanel[SaveButton]]";
        WebElement closeButton = webDriver.findElement(new ByWhirl(closeButtonLoc));
        Graphene.waitGui(webDriver).until().element(closeButton).is().visible();
        closeButton.click();
    }

    public enum SortPanelSide {
        LEFT, RIGHT
    }

    public int getFromPanelElementsCount(SortPanelSide side) {
        String panelLocator;
        if (SortPanelSide.LEFT.equals(side)) {
            panelLocator = sortFromPanelByIndex;
        } else {
            panelLocator = sortToPanelByIndex;
        }
        int count = 0;
        try {
            while (true) {
                String elementLoc = gridLocator + String.format(panelLocator, count);
                WebElement el = webDriver.findElement(new ByWhirl(elementLoc));
                count++;
            }
        } catch (NoSuchElementException e) {
            // Невозможно найти элемент по созданному локатору.
        }
        return count;
    }

    public void clickBarButton(SortPanelButtons buttonName) {
        String buttonLocator = gridLocator + String.format(sortButtonsPattern, buttonName.text);
        WebElement button = webDriver.findElement(new ByWhirl(buttonLocator));
        Graphene.waitGui(webDriver).until().element(button).is().visible();
        button.click();
    }

    public void selectFromPanelItem(String label) {
        String itemLocator = gridLocator + String.format(sortFromPanelByLabel, label);
        WebElement item = webDriver.findElement(new ByWhirl(itemLocator));
        Graphene.waitGui(webDriver).until().element(item).is().visible();
        item.click();
    }

    public void selectToPanelItem(String label) {
        String itemLocator = gridLocator + String.format(sortToPanelByLabel, label);
        WebElement item = webDriver.findElement(new ByWhirl(itemLocator));
        Graphene.waitGui(webDriver).until().element(item).is().visible();
        item.click();
    }

    public void changeSort(String label) {
        String itemLocator = gridLocator + String.format(sortToPanelImg, label);
        WebElement item = webDriver.findElement(new ByWhirl(itemLocator));
        Graphene.waitGui(webDriver).until().element(item).is().visible();
        item.click();
        String saveButtonLocator = gridLocator + String.format(sortButtonsPattern, SortPanelButtons.SAVE);
        WebElement saveButton = webDriver.findElement(new ByWhirl(saveButtonLocator));
        Graphene.guardAjax(saveButton).click();
        String closeButtonLocator = gridLocator + String.format(sortButtonsPattern, SortPanelButtons.SAVE);
        WebElement closeButton = webDriver.findElement(new ByWhirl(closeButtonLocator));
        closeButton.click();
    }

}
