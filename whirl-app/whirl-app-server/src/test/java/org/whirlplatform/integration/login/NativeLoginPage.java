package org.whirlplatform.integration.login;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NativeLoginPage {

    @FindBy(id = "login-field")
    private WebElement userName;

    @FindBy(id = "pwd-field")
    private WebElement userPass;

    @FindBy(id = "submit-btn")
    private WebElement submit;

    public void login(String user, String password) {
        Graphene.waitGui().until().element(userName).is().visible();
        userName.click();
        userName.clear();
        userName.sendKeys(user);
        Graphene.waitGui().until().element(userPass).is().visible();
        userPass.click();
        userPass.clear();
        userPass.sendKeys(password);
        Graphene.waitGui().until().element(submit).is().visible();
        Graphene.guardAjax(submit).click();
    }
}
