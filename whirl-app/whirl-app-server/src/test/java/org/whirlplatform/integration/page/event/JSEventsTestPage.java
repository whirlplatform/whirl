package org.whirlplatform.integration.page.event;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.whirlplatform.integration.AbstractPage;
import org.whirlplatform.integration.graphene.FindByWhirl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Location("app?role=arquillian_event_test")
public class JSEventsTestPage extends AbstractPage {

    @FindByWhirl("whirl:TextFieldBuilder(code=tf_component)[Input]")
    private WebElement textFieldComponent;
    @FindByWhirl("whirl:DateFieldBuilder(code=df_component)[Input]")
    private WebElement dateFieldComponent;
    @FindByWhirl("whirl:NumberFieldBuilder(code=nf_component)[Input]")
    private WebElement numberFieldComponent;
    @FindByWhirl("whirl:CheckBoxBuilder(code=chb_component)[Input]")
    private WebElement checkBoxComponent;

    @FindByWhirl("whirl:ButtonBuilder(code=btn_messages_clearer)")
    private WebElement buttonMessagesClearer;

    @FindByWhirl("whirl:ButtonBuilder(code=btn_click_event_emitter)")
    private WebElement buttonClickEventEmitter;

    @JavaScript("window.JsTest")
    @Dependency(sources = "org/whirlplatform/integration/page/event/js-events-routines.js")
    public interface JsTest {
        String executeEvent(String eventCode, Object data, String type);

        String getMultiComboBoxValues(String multiComboBoxCode);
    }

    @JavaScript
    private JsTest jsTest;

    /**
     * Ожидаю готовность сразу всех элементов.
     */
    public void waitWhenComponentsReady() {
        Graphene.waitGui().pollingEvery(2, TimeUnit.SECONDS)
                .until(input -> !numberFieldComponent.getAttribute("value").equals("") && checkBoxComponent.isSelected()
                        && !dateFieldComponent.getAttribute("value").equals("")
                        && !textFieldComponent.getAttribute("value").equals(""));
    }

    public void executeEvent(String eventCode, Object data, String type) {
        // убеждаюсь что поле, куда попадёт вывод после выполнения события, отображено
        // на странице
        jsTest.executeEvent(eventCode, data, type);
    }

    public boolean getBooleanFieldValue() {
        return checkBoxComponent.isSelected();
    }

    public String getNumberFieldValue() {
        return numberFieldComponent.getAttribute("value");
    }

    public String getDateFieldValue() {
        return dateFieldComponent.getAttribute("value");
    }

    public String getTextFieldValue() {
        return textFieldComponent.getAttribute("value");
    }

    /**
     * при выполнении события создаётся компонент с
     * id=arquillian_whirl_js_event_lock, при завершении этот компонент удаляется
     * из dom. поэтому здесь я проверяю его наличие.
     */
    public void waitForEventComplete() {
        // я не уверен что эта техника работает так как ожидается, т.к. иногда ждёт
        // больше 2-х секунд. А иногда - проходит быстро.
        Graphene.waitGui().pollingEvery(2, TimeUnit.SECONDS).until(input -> {
            try {
                webDriver.findElement(By.id("arquillian_whirl_js_event_lock"));
                return false;
            } catch (NoSuchElementException nsee) {
                return true;
            }
        });
    }

    public List<String> getMultiComboBoxValues() {

        waitForEventComplete();
        String mcbValues = jsTest.getMultiComboBoxValues("mcb_component");
        // нужно выяснить, в каком формате прилетают параметры и преобразовать в список
        // строк
        List<String> values = new ArrayList<String>();
        try {
            JSONArray jsonarray = new JSONArray(mcbValues);
            for (int i = 0; i < jsonarray.length(); i++) {
                Object o = jsonarray.get(i);
                values.add(String.valueOf(o));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return values;
    }

    public static void main(String[] args) throws JSONException {
        String test = "[\"some value 1\",\"another value2\",\"and other value 3\"]";
        JSONArray jsonarray = new JSONArray(test);

        for (int i = 0; i < jsonarray.length(); i++) {
            Object o = jsonarray.get(i);
        }

    }
}
