package org.whirlplatform.integration.login;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;

@Location("uniapp?role=login-success")
public class LoginSuccessPage extends AbstractPage {

    @FindByWhirl("whirl:LabelBuilder(code=success)[Text]")
    private WebElement success;

    public boolean isSuccess() {
        waitPage();
        return "Login success".equals(success.getText());
    }

    private void waitPage() {
        waitForVisibility(success);
    }

}
