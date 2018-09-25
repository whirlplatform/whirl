package org.whirlplatform.integration.login;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.page.Location;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;

@Location(value = "app")
public class LoginPage extends AbstractPage {

    @FindByWhirl("whirl:LoginPanelBuilder[LoginField]")
    private GrapheneElement loginField;

    @FindByWhirl("whirl:LoginPanelBuilder[PasswordField]")
    private GrapheneElement pwdField;

    @FindByWhirl("whirl:LoginPanelBuilder[SubmitButton]")
    private GrapheneElement submit;

    public void login(String user, String password) {
        waitPage();

        loginField.sendKeys(user);
        pwdField.sendKeys(password);
        Graphene.guardAjax(submit).click();
    }

    private void waitPage() {
        waitForVisibility(loginField, pwdField, submit);
    }

}
