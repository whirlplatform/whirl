package org.whirlplatform.integration.event;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;

@Location("app?role=events_test")
public class EventsTestPage extends AbstractPage {

    @FindByWhirl("whirl:ButtonBuilder(code=btn_oracle_to_oracle)")
    private WebElement buttonOracleToOracle;

    @FindByWhirl("whirl:ButtonBuilder(code=btn_oracle_to_js)")
    private WebElement buttonOracleToJs;

    @FindByWhirl("whirl:ButtonBuilder(code=btn_js_to_oracle)")
    private WebElement buttonJsToOracle;

    @FindByWhirl("whirl:ButtonBuilder(code=btn_js_to_js)")
    private WebElement buttonJsToJs;

    @FindByWhirl("whirl:TextFieldBuilder(id=-667097117133)[Input]")
    private WebElement textField;

    @FindByWhirl("whirl:NumberFieldBuilder(id=-667097117163)[Input]")
    private WebElement numField;

    @FindByWhirl("whirl:DateFieldBuilder(code=date)[Input]")
    private WebElement dateField;

    @FindByWhirl("whirl:CheckBoxBuilder(id=-667097117143)[Input]")
    private WebElement boolField;

    // oracle results window

    @FindByWhirl("whirl:LabelBuilder(code=text_res)[Text]")
    private WebElement oracleTextResult;

    @FindByWhirl("whirl:LabelBuilder(code=num_res)[Text]")
    private WebElement oracleNumberResult;

    @FindByWhirl("whirl:DateFieldBuilder(code=date_res)[Input]")
    private WebElement oracleDateResult;

    @FindByWhirl("whirl:LabelBuilder(code=flag_res)[Text]")
    private WebElement oracleBooleanResult;

    @FindByWhirl("whirl:ButtonBuilder(code=close_ora)")
    private WebElement oracleCloseButton;

    // js results window

    @FindByWhirl("whirl:LabelBuilder(code=text_js)[Text]")
    private WebElement jsTextResult;

    @FindByWhirl("whirl:LabelBuilder(code=num_js)[Text]")
    private WebElement jsNumberResult;

    @FindByWhirl("whirl:DateFieldBuilder(code=date_js)[Input]")
    private WebElement jsDateResult;

    @FindByWhirl("whirl:LabelBuilder(code=flag_js)[Text]")
    private WebElement jsBooleanResult;

    @FindByWhirl("whirl:ButtonBuilder(code=close_js)")
    private WebElement jsCloseButton;

    public TransmissionParameters transmitOracleToOracle(final TransmissionParameters parameters) {
        fillOutFields(parameters);
        clickOracleToOracle();
        TransmissionParameters result = getOracleResult();
        return result;
    }

    public TransmissionParameters transmitOracleToJs(final TransmissionParameters parameters) {
        fillOutFields(parameters);
        clickOracleToJs();
        TransmissionParameters result = getJsResult();
        return result;
    }

    public TransmissionParameters transmitJsToOracle(final TransmissionParameters parameters) {
        fillOutFields(parameters);
        clickJsToOracle();
        TransmissionParameters result = getOracleResult();
        return result;
    }

    public TransmissionParameters transmitJsToJs(final TransmissionParameters parameters) {
        fillOutFields(parameters);
        clickJsToJs();
        TransmissionParameters result = getJsResult();
        return result;
    }

    public void clickOracleToOracle() {
        Graphene.waitGui().until().element(buttonOracleToOracle).is().visible();
        Graphene.guardAjax(buttonOracleToOracle).click();
    }

    public void clickOracleToJs() {
        Graphene.waitGui().until().element(buttonOracleToJs).is().visible();
        Graphene.guardAjax(buttonOracleToJs).click();
    }

    public void clickJsToOracle() {
        Graphene.waitGui().until().element(buttonJsToOracle).is().visible();
        Graphene.guardAjax(buttonJsToOracle).click();
    }

    public void clickJsToJs() {
        Graphene.waitGui().until().element(buttonJsToJs).is().visible();
        Graphene.guardAjax(buttonJsToJs).click();
    }

    public void fillOutFields(TransmissionParameters parameters) {
        if (parameters.getText() != null) {
            Graphene.waitGui().until().element(textField).is().visible();
            textField.clear();
            textField.sendKeys(parameters.getText());
        }
        if (parameters.getNumber() != null) {
            Graphene.waitGui().until().element(numField).is().visible();
            numField.clear();
            numField.sendKeys(parameters.getNumber());
        }
        if (parameters.getDate() != null) {
            Graphene.waitGui().until().element(dateField).is().visible();
            dateField.clear();
            dateField.sendKeys(parameters.getDate());
        }
        Graphene.waitGui().until().element(boolField).is().visible();
        if (boolField.isSelected()) {
            boolField.click();
        }
        if (parameters.getBool() != null && parameters.getBool() == true) {
            boolField.click();
        }
    }

    public TransmissionParameters getOracleResult() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Graphene.waitGui().until().element(oracleTextResult).is().visible();
        Graphene.waitGui().until().element(oracleNumberResult).is().visible();
        Graphene.waitGui().until().element(oracleDateResult).is().visible();
        Graphene.waitGui().until().element(oracleBooleanResult).is().visible();
        TransmissionParameters result = new TransmissionParameters();
        result.setText(parseStringValue(oracleTextResult.getText()));
        result.setNumber(parseStringValue(oracleNumberResult.getText()));
        result.setDate(parseStringValue(oracleDateResult.getAttribute("value")));
        result.setBool(parseBooleanValue(oracleBooleanResult.getText()));
        Graphene.waitGui().until().element(oracleCloseButton).is().visible();
        oracleCloseButton.click();
        return result;
    }

    public TransmissionParameters getJsResult() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Graphene.waitGui().until().element(jsTextResult).is().visible();
        Graphene.waitGui().until().element(jsNumberResult).is().visible();
        Graphene.waitGui().until().element(jsDateResult).is().visible();
        Graphene.waitGui().until().element(jsBooleanResult).is().visible();
        TransmissionParameters result = new TransmissionParameters();
        result.setText(parseStringValue(jsTextResult.getText()));
        result.setNumber(parseStringValue(jsNumberResult.getText()));
        result.setDate(parseStringValue(jsDateResult.getAttribute("value")));
        result.setBool(parseBooleanValue(jsBooleanResult.getText()));
        Graphene.waitGui().until().element(jsCloseButton).is().visible();
        jsCloseButton.click();
        return result;
    }

    private String parseStringValue(final String value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        if ("null".equals(value) || "n/a".equals(value)) {
            return null;
        }
        return value;
    }

    private Boolean parseBooleanValue(final String value) {
        if ("null".equals(value)) {
            return null;
        }
        return "T".equals(value);
    }
}
