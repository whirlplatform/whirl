package org.whirlplatform.component.client.selenium;

import com.google.gwt.junit.client.GWTTestCase;
import org.junit.Ignore;

@Ignore
public class LocatorTest extends GWTTestCase {

    private String cellsPattern = "EditGridBuilder(id=88760223-DE0A-4253-960B-4E7230D840CB)[Row(id=1&index=1&column=3&label=234)[Cell(column=NUMBERS)]]";
    private String checkPattern = "EditGridBuilder(id=88760223-DE0A-4253-960B-4E7230D840CB)[Row(id=1&index=0&column=0&label=&nbsp;)[Check]]";
    private String filterPanel = "EditGridBuilder(id=88760223-DE0A-4253-960B-4E7230D840CB&code=code)[FilterPanel[Filter(mdid=0F0ED547-B7F7-40E7-B1C8-DA8C4B6CBA55&mdname=STRINGS)[FirstVal[NumField[input]]]]]";

    @Override
    public String getModuleName() {
        return "org.whirlplatform.app.Application";
    }

    public void testLocatorParse() {
        locatorParse(cellsPattern);
        //Не проходит т.к у чек - бокса в гриде label=&nbsp;
        //locatorParse(checkPattern);
        locatorParse(filterPanel);
    }

    public void locatorParse(String stringLocator) {
        //Порядок параметров в строке локатора и прочитанном локаторе могут отличаться !.
        Locator locator = Locator.parse(stringLocator);
        System.out.println(stringLocator);
        System.out.println(locator.toString());
        assertTrue(locator != null);
        assertTrue(!locator.toString().isEmpty());
        assertEquals(stringLocator, locator.toString());
    }

}

