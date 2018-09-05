package org.whirlplatform.selenium;

import org.openqa.selenium.*;

import java.io.Serializable;
import java.util.List;

public class ByWhirl extends By implements Serializable {
    private static final long serialVersionUID = -8495280851093917046L;

    private String locator;

    public ByWhirl(String locator) {
        this.locator = locator;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        throw new UnsupportedOperationException("Method findElement not supported by ByWhirl locator.");
    }

    @Override
    public WebElement findElement(SearchContext context) {
        if (context instanceof JavascriptExecutor) {
            JavascriptExecutor executor = (JavascriptExecutor) context;
            try {
                WebElement element = (WebElement) executor
                        .executeScript("return __Whirl.Selenium.getElementByLocator(arguments[0]);", locator);
                if (element != null) {
                    return element;
                }
            } catch (WebDriverException e) {
                if ((Boolean) (executor.executeScript("return (typeof __Whirl == 'undefined');"))) {
                    throw new NoSuchElementException("Not Whirl page.");
                }
            }
        }
        throw new NoSuchElementException("Element by whirl locator " + locator + " not found.");
    }

}
