package org.whirlplatform.integration;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.whirlplatform.selenium.ByWhirl;

public abstract class AbstractPage {

    @Drone
    protected WebDriver webDriver;

    public void waitForPageLoading() {
        (new WebDriverWait(webDriver, 15)).until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState;").equals("complete"));
    }

    protected void waitForVisibility(WebElement... elements) {
        for (WebElement e : elements) {
            Graphene.waitGui(webDriver).until().element(e).is().visible();
        }
    }

    protected void waitForVisibilityAndClick(WebElement... elements) {
        for (WebElement element : elements) {
            Graphene.waitGui(webDriver).until().element(element).is().visible();
            element.click();
        }
    }

    protected WebElement findElementByWhirl(final String locator) {
        final By by = new ByWhirl(locator);
        WebElement result = webDriver.findElement(by);
        return result;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}
