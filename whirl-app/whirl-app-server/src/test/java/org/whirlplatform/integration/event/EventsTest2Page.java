package org.whirlplatform.integration.event;

import java.util.Arrays;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.impl.web.Strings;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;

@Location("app?role=events_test2")
public class EventsTest2Page extends AbstractPage {

    // Selected value labels

    @FindByWhirl("whirl:LabelBuilder(code=combo_sel_id)[Text]")
    private WebElement comboSelected;

    @FindByWhirl("whirl:LabelBuilder(code=multi_sel_id)[Text]")
    private WebElement multiSelected;

    @FindByWhirl("whirl:LabelBuilder(code=tree_sel_id)[Text]")
    private WebElement treeSelected;

    @FindByWhirl("whirl:LabelBuilder(code=grid_sel_id)[Text]")
    private WebElement gridSelected;

    // Transmitted value labels

    @FindByWhirl("whirl:LabelBuilder(code=combo_trans_id)[Text]")
    private WebElement comboTransmitted;

    @FindByWhirl("whirl:LabelBuilder(code=multi_trans_id)[Text]")
    private WebElement multiTransmitted;

    @FindByWhirl("whirl:LabelBuilder(code=tree_trans_id)[Text]")
    private WebElement treeTransmitted;

    @FindByWhirl("whirl:LabelBuilder(code=grid_trans_id)[Text]")
    private WebElement gridTransmitted;

    // JS Buttons

    @FindByWhirl("whirl:ButtonBuilder(code=combo_btn_js_js)")
    private WebElement comboButtonJs;

    @FindByWhirl("whirl:ButtonBuilder(code=multi_btn_js_js)")
    private WebElement multiButtonJs;

    @FindByWhirl("whirl:ButtonBuilder(code=tree_btn_js_js)")
    private WebElement treeButtonJs;

    @FindByWhirl("whirl:ButtonBuilder(code=grid_btn_js_js)")
    private WebElement gridButtonJs;

    // Oracle Buttons

    @FindByWhirl("whirl:ButtonBuilder(code=combo_btn_ora_js)")
    private WebElement comboButtonOracle;

    @FindByWhirl("whirl:ButtonBuilder(code=multi_btn_ora_js)")
    private WebElement multiButtonOracle;

    @FindByWhirl("whirl:ButtonBuilder(code=tree_btn_ora_js)")
    private WebElement treeButtonOracle;

    @FindByWhirl("whirl:ButtonBuilder(code=grid_btn_ora_js)")
    private WebElement gridButtonOracle;

    // Clear Transfer Buttons

    @FindByWhirl("whirl:ButtonBuilder(code=combo_btn_clear)")
    private WebElement comboButtonClear;

    @FindByWhirl("whirl:ButtonBuilder(code=multi_btn_clear)")
    private WebElement multiButtonClear;

    @FindByWhirl("whirl:ButtonBuilder(code=tree_btn_clear)")
    private WebElement treeButtonClear;

    @FindByWhirl("whirl:ButtonBuilder(code=grid_btn_clear)")
    private WebElement gridButtonClear;

    // Triggers and Clearance

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Trigger]")
    private WebElement comboboxTrigger;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Trigger]")
    private WebElement multiboxTrigger;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Clear]")
    private WebElement multiboxClear;

    // Combobox items

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(index=0)]")
    private WebElement comboboxItem0;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000000)]")
    private WebElement comboboxItem1;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000001)]")
    private WebElement comboboxItem2;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000002)]")
    private WebElement comboboxItem3;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000003)]")
    private WebElement comboboxItem4;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000005)]")
    private WebElement comboboxItem5;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000004)]")
    private WebElement comboboxItem6;

    @FindByWhirl("whirl:ComboBoxBuilder(code=combo)[Item(id=1000006)]")
    private WebElement comboboxItem7;

    // MultiComboBox items

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000000)[Check]]")
    private WebElement multiboxItem1;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000001)[Check]]")
    private WebElement multiboxItem2;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000002)[Check]]")
    private WebElement multiboxItem3;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000003)[Check]]")
    private WebElement multiboxItem4;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000005)[Check]]")
    private WebElement multiboxItem5;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000004)[Check]]")
    private WebElement multiboxItem6;

    @FindByWhirl("whirl:MultiComboBoxBuilder(code=multi)[Item(id=1000006)[Check]]")
    private WebElement multiboxItem7;

    // Tree rows

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000000)[Check]]")
    private WebElement treeItem1;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000001)[Check]]")
    private WebElement treeItem2;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000002)[Check]]")
    private WebElement treeItem3;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000003)[Check]]")
    private WebElement treeItem4;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000005)[Check]]")
    private WebElement treeItem5;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000004)[Check]]")
    private WebElement treeItem6;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000006)[Check]]")
    private WebElement treeItem7;

    // Tree joints

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000000)[Joint]]")
    private WebElement treeJoint1;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000003)[Joint]]")
    private WebElement treeJoint2;

    @FindByWhirl("whirl:TreeBuilder(code=tree)[Item(id=1000005)[Joint]]")
    private WebElement treeJoint3;

    // Grid rows

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000000)[Check]]")
    private WebElement gridItem1;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000001)[Check]]")
    private WebElement gridItem2;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000002)[Check]]")
    private WebElement gridItem3;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000003)[Check]]")
    private WebElement gridItem4;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000005)[Check]]")
    private WebElement gridItem5;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000004)[Check]]")
    private WebElement gridItem6;

    @FindByWhirl("whirl:EditGridBuilder(code=grid)[Row(id=1000006)[Check]]")
    private WebElement gridItem7;

    // ComboBox methods

    public String clearComboBoxSelection() {
        return makeComboBoxSelection(comboboxItem0);
    }

    public String makeComboBoxSelection1() {
        return makeComboBoxSelection(comboboxItem1);
    }

    public String makeComboBoxSelection2() {
        return makeComboBoxSelection(comboboxItem7);
    }

    private String makeComboBoxSelection(WebElement item) {
        super.waitForVisibilityAndClick(comboboxTrigger);
        super.waitForVisibilityAndClick(item);
        return comboSelected.getText();
    }

    public String clickComboBoxJs() {
        comboButtonJs.click();
        return comboTransmitted.getText();
    }

    public String clickComboBoxOracle() {
        Graphene.guardAjax(comboButtonOracle).click();
        return comboTransmitted.getText();
    }

    public String clickComboBoxClear() {
        comboButtonClear.click();
        return comboTransmitted.getText();
    }

    // MultiComboBox methods

    public String makeMultiComboBoxSelection1() {
        return makeMultiComboBoxSelection(multiboxItem1);
    }

    public String makeMultiComboBoxSelection2() {
        return makeMultiComboBoxSelection(multiboxItem2, multiboxItem6);
    }

    public String clearMultiComboBoxSelections() {
        multiboxClear.click();
        return multiSelected.getText();
    }

    private String makeMultiComboBoxSelection(WebElement... items) {
        super.waitForVisibilityAndClick(multiboxTrigger);
        super.waitForVisibilityAndClick(items);
        multiboxTrigger.click();
        return sortText(multiSelected);
    }

    public String clickMultiComboBoxJs() {
        Graphene.guardAjax(multiButtonJs).click();
        return sortText(multiTransmitted);
    }

    public String clickMultiComboBoxOracle() {
        Graphene.guardAjax(multiButtonOracle).click();
        return sortText(multiTransmitted);
    }

    public String clickMultiComboBoxClear() {
        multiButtonClear.click();
        return multiTransmitted.getText();
    }

    // Tree methods

    public void expandTree() {
        makeTreeSelection(treeJoint1, treeJoint2, treeJoint3);
    }

    public String makeTreeSelection1() {
        return makeTreeSelection(treeItem1);
    }

    public String makeTreeSelection2() {
        return makeTreeSelection(treeItem2, treeItem6);
    }

    public String clearTreeSelections() {
        makeTreeSelection1();
        makeTreeSelection2();
        return treeSelected.getText();
    }

    private String makeTreeSelection(WebElement... items) {
        super.waitForVisibilityAndClick(items);
        return sortText(treeSelected);
    }

    public String clickTreeJs() {
        Graphene.guardAjax(treeButtonJs).click();
        return sortText(treeTransmitted);
    }

    public String clickTreeOracle() {
        Graphene.guardAjax(treeButtonOracle).click();
        return sortText(treeTransmitted);
    }

    public String clickTreeClear() {
        treeButtonClear.click();
        return treeTransmitted.getText();
    }

    // Grid methods

    public String makeGridSelection1() {
        return makeGridSelection(gridItem1);
    }

    public String makeGridSelection2() {
        return makeGridSelection(gridItem2, gridItem6);
    }

    public String clearGridSelections() {
        makeGridSelection1();
        makeGridSelection2();
        return gridSelected.getText();
    }

    private String makeGridSelection(WebElement... items) {
        super.waitForVisibilityAndClick(items);
        return sortText(gridSelected);
    }

    public String clickGridJs() {
        Graphene.guardAjax(gridButtonJs).click();
        return sortText(gridTransmitted);
    }

    public String clickGridOracle() {
        Graphene.guardAjax(gridButtonOracle).click();
        return sortText(gridTransmitted);
    }

    public String clickGridClear() {
        gridButtonClear.click();
        return gridTransmitted.getText();
    }

    private String sortText(final WebElement label) {
        String[] array = label.getText().split(",");
        Arrays.sort(array);
        String result = Strings.join(Arrays.asList(array), ",");
        return result;
    }
}
