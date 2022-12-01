package org.whirlplatform.integration.grid;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;

public class FieldFormPage extends AbstractGridPart {
    private final String FORM = "FieldForm(code=%s)";
    private final String SAVE_BUTTON = "%s[%s[SaveButton]]";
    private final String CANCEL_BUTTON = "%s[%s[CancelButton]]";
    private final String TEXT = "%s[%s[TextFieldBuilder(code=%s)[Input]]]";
    private final String NUMBER = "%s[%s[NumberFieldBuilder(code=%s)[Input]]]";
    private final String DATE = "%s[%s[DateFieldBuilder(code=%s)[Input]]]";
    private final String CHECKBOX = "%s[%s[CheckBoxBuilder(code=%s)[Input]]]";
    private final String COMBO_TRIGGER = "%s[%s[ComboBoxBuilder(code=%s)[Trigger]]]";
    private final String COMBO_ITEM = "%s[%s[ComboBoxBuilder(code=%s)[Item(id=%s)]]]";
    private final String CONFIRM_YES = "whirl:Dialog(id=%s-%s)[YesButton]";
    @Root
    WebElement grid;
    private String formCode;

    public String getFormStringLocator() {
        return String.format(FORM, getFormCode());
    }

    private String getFormCode() {
        if (StringUtils.isEmpty(formCode)) {
            throw new IllegalArgumentException("FieldForm code was not initialised");
        }
        return formCode;
    }

    public void setFormCode(final String formCode) {
        this.formCode = formCode;
    }

    public void clickSaveButton() {
        findButton(SAVE_BUTTON).click();
        WebElement yes = findConfirmationYesButton();
        Graphene.waitGui().until().element(yes).is().visible();
        Graphene.guardAjax(yes).click();
    }

    public void clickCancelButton() {
        findButton(CANCEL_BUTTON).click();
    }

    public void setTextFieldValue(final String code, final String value) {
        setInputFieldValue(TEXT, code, value);
    }

    public void setNumberFieldValue(final String code, final String value) {
        setInputFieldValue(NUMBER, code, value);
    }

    public void setDateFieldValue(final String code, final String value) {
        setInputFieldValue(DATE, code, value);
    }

    private void setInputFieldValue(final String template, final String code, final String value) {
        WebElement field = findField(template, code);
        field.clear();
        field.sendKeys(value);
    }

    public void setCheckBoxValue(final String code, final String value) {
        WebElement field = findField(CHECKBOX, code);
        if (field.isSelected()) {
            field.click();
        }
        if ("T".equals(value)) {
            field.click();
        }
    }

    public void setComboBoxValue(final String code, final String value) {
        WebElement trigger = findField(COMBO_TRIGGER, code);
        trigger.click();
        WebElement item = findItem(COMBO_ITEM, code, value);
        Graphene.waitGui(webDriver).until().element(item).is().visible();
        item.click();
    }

    private WebElement findButton(final String template) {
        final String locator =
                String.format(template, getGridStringLocator(), getFormStringLocator());
        return findElementByWhirl(locator);
    }

    private WebElement findField(final String template, final String fieldCode) {
        final String locator =
                String.format(template, getGridStringLocator(), getFormStringLocator(), fieldCode);
        return findElementByWhirl(locator);
    }

    private WebElement findItem(final String template, final String fieldCode,
                                final String itemId) {
        final String locator =
                String.format(template, getGridStringLocator(), getFormStringLocator(), fieldCode,
                        itemId);
        return findElementByWhirl(locator);
    }

    private WebElement findConfirmationYesButton() {
        final String locator = String.format(CONFIRM_YES, getGridIdentificator(), getFormCode());
        return findElementByWhirl(locator);
    }

    public void waitUntilFormIsVisible() {
        Graphene.waitGui().until().element(findButton(CANCEL_BUTTON)).is().visible();
    }
}
