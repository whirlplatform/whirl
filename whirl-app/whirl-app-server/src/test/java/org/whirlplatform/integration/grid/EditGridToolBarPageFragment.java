package org.whirlplatform.integration.grid;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.whirlplatform.selenium.ByWhirl;

public class EditGridToolBarPageFragment extends AbstractGridPart {
    private final String ADD_BUTTON = "%s[ToolBar[AddButton]]";
    private final String EDIT_BUTTON = "%s[ToolBar[EditButton]]";
    private final String COPY_BUTTON = "%s[ToolBar[CopyButton]]";
    private final String DELETE_BUTTON = "%s[ToolBar[DeleteButton]]";
    private final String VIEW_BUTTON = "%s[ToolBar[ViewButton]]";
    private final String SEARCH_BUTTON = "%s[ToolBar[SearchButton]]";
    private final String SORT_BUTTON = "%s[ToolBar[SortButton]]";
    private final String REFRESH_BUTTON = "%s[ToolBar[RefreshButton]]";

    @Root
    WebElement gridToolbar;

    public void clickAddButton() {
        findButton(ADD_BUTTON).click();
    }

    public void clickEditButton() {
        findButton(EDIT_BUTTON).click();
    }

    public void clickDeleteButton() {
        findButton(DELETE_BUTTON).click();
    }

    public void clickViewButton() {
        findButton(VIEW_BUTTON).click();
    }

    public void clickSearchButton() {
        findButton(SEARCH_BUTTON).click();
    }

    public void clickCopyButton() {
        findButton(COPY_BUTTON).click();
    }

    public void clickSortButton() {
        findButton(SORT_BUTTON).click();
    }

    public void clickRefreshButton() {
        findButton(REFRESH_BUTTON).click();
    }

    private WebElement findButton(final String template) {
        waitUntilIsVisible();
        By locator = new ByWhirl(String.format(template, getGridStringLocator()));
        WebElement result = webDriver.findElement(locator);
        return result;
    }

    public void waitUntilIsVisible() {
        Graphene.waitGui().until().element(gridToolbar).is().visible();
    }
}
