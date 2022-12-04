package org.whirlplatform.component.client.selenium;
//package org.whirlplatform.component.client.selenium;
//
//import com.google.gwt.junit.client.GWTTestCase;
//
///**
// * Проверка механизма работы с локаторами.
// */
///*
// */
//
// /* 
//  * https://gwt-maven-plugin.github.io/gwt-maven-plugin/user-guide/testing.html
//  */
//public class GwtTestLocator extends GWTTestCase {
//    
//    public String getModuleName() { // <span style="color:black;">**(2)**</span>
//        return "org.whirlplatform.app.Application";
//    }
//
//    public void testBackOperation(){
//        
//        Locator loc2 = new Locator();
//        //теперь обратное преобразование
//        String rawLoc2 = loc2.toString();
//        rawLoc2 += "&testParameter=testValue";;
//        //теперь проверяем что можно создать локатор по raw locator
//        try{
//            Locator loc3 = new Locator(rawLoc2);
//            //новый локатор должен содержать тестовый параметр
//            assertTrue(loc3.containsKey("testParameter"));
//        } catch(Exception e){
//            assertTrue("Не удалось создать локатор по форматированной строке", false);
//        }
//        
//    }
//    
//    public void testCreateLocator() {
//        Locator loc1 = new Locator();
//        loc1.setWhirlId("TestWhirlId");
//        String sLoc1 = loc1.toString();
//        //raw-локатор должен содержать такую строку
//        assertTrue(sLoc1.contains("whirl-id=TestWhirlId"));
//        
//        //теперь проверяем что локатор можно "копировать". 
//        Locator loc2 = new Locator(loc1); //
//        assertTrue(loc2.toString().contains("whirl-id=TestWhirlId"));
//    }
//    
//    public void testPopupObjLocator(){
//        Locator loc2 = new Locator(); //
//        loc2.setWhirlId("testWhirlId");
//        //теперь проверим случай диалоговых окон
//        String loc2WhirlId = loc2.getWhirlId();
//        loc2.setWhirlId(Locator.makeDialogIdentifier(loc2WhirlId, "deleteDialogBox"));
//        assertTrue(loc2.hasDialogMark());
//        assertFalse(loc2.hasWindowMark());
//        //именно такой префикс добавляется к идентификатору процедурой makeDialogIdentifier
//        assertTrue(loc2.toString().contains("dlg/"));
//        assertTrue(loc2.toString().contains("dlg/"+loc2WhirlId+"/deleteDialogBox"));
//    }
//    
//    
//    public void testEscape(){
//        String param1Value = "test value 1";
//        String param1ValueEscaped = "test%20whirl%20id";
//        Locator loc1 = new Locator("whirl://whirl-id=test whirl id&param1="+param1Value);
//        assertTrue(loc1.toString().contains(param1ValueEscaped));
//        Locator loc2 = new Locator(loc1);
//        assertFalse(param1ValueEscaped.equals(loc2.get("param1"))); //параметр при извлечении не должен быть закодирован. только при хранении.
//        assertTrue(param1Value.equals(loc2.get("param1"))); //а при извлечении он в нормальном виде
//    }
//    
//}
