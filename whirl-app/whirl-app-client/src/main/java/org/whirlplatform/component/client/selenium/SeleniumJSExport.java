package org.whirlplatform.component.client.selenium;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.util.Util;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.ExportStaticMethod;
import org.timepedia.exporter.client.Exportable;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.base.LoginPanelBuilder;
import org.whirlplatform.component.client.window.WindowBuilder;
import org.whirlplatform.component.client.window.WindowManager;
import org.whirlplatform.component.client.window.dialog.DialogManager;

@Export("Selenium")
@ExportPackage("__Whirl")
public class SeleniumJSExport implements Exportable {

    private static Locator find(Containable containable, Element element) {

        Locator result = null;
        for (ComponentBuilder b : containable.getChildren()) {
            if (b instanceof Containable) {
                result = find((Containable) b, element);
                if (result != null) {
                    break;
                }
            }
            result = b.getLocatorByElement(element);
            if (result != null) {
                break;
            }
        }
        if (result == null) { // в дочерних элементах ничего не нашли.
            // Значит, опрашиваем сам containable(если он - ComponentBuilder ) на наличие в
            // нём искомого элемента
            if (containable instanceof ComponentBuilder)
                result = ((ComponentBuilder) containable).getLocatorByElement(element);

        }
        return result;
    }

    private static Element findEverywhere(Locator locator) {
        if (BuilderManager.getRoot() == null) {
            return null;
        }
        Element result = find(BuilderManager.getRoot(), locator);
        if (result != null) {
            return result;
        }
        for (WindowBuilder w : WindowManager.get().getBuilders()) {
            result = find(w, locator);
            if (result != null) {
                break;
            }
            result = w.getElementByLocator(locator);
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            if (BuilderManager.getRoot() instanceof ComponentBuilder) {
                ComponentBuilder c = (ComponentBuilder) BuilderManager.getRoot();
                result = c.getElementByLocator(locator);
            }
        }
        return result;
    }

    private static Element find(Containable containable, Locator locator) {
        Element result = null;
        for (ComponentBuilder b : containable.getChildren()) {
            if (b instanceof Containable) {
                result = find((Containable) b, locator);
                if (result != null) {
                    break;
                }
            }
            result = b.getElementByLocator(locator);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * Для element-а выше по иерархии DOM нахожу элемент, имеющий атрибут
     * AppConstant.WHIRL_ID_ATTRIBUTE_NAME По этому идентификатору определяю
     * билдер. Вызываю метод билдера getLocatorByElement для уточнения локатора.
     * <p>
     * Проблема в определении whirlId для элементов, добавляемых в DOM динамически,
     * например, выпадающий список. Такие элементы не привязаны к билдерам, поэтому
     * нужно определять билдеры как-то по-другому. А для простых, типа кнопки,
     * текстового поля, такой способ сработает.
     *
     * @param element
     * @return
     */
    @ExportStaticMethod
    public static String getLocatorByElement(Element element) {
        Locator locator = null;
        if (BuilderManager.getRoot() != null) {
            locator = find(BuilderManager.getRoot(), element);
        }
        if (locator == null) {
            for (WindowBuilder w : WindowManager.get().getBuilders()) {
                locator = find(w, element);
                if (locator != null) {
                    break;
                }

            }
        }
        if (locator == null) {
            for (LocatorAware l : DialogManager.getDialogLocators()) {
                locator = l.getLocatorByElement(element);
                if (locator != null) {
                    break;
                }
            }
        }
        if (locator == null) {
            // форма логина
            locator = LoginPanelBuilder.locatorByElement(element);
        }
        if (locator != null) {
            return "whirl:" + locator.toString();
        }
        return null;
    }

    /**
     * Метод проверяет наличие маски whirl:// в локаторе. Берёт из локатора
     * идентификатор. (whirlId) Если маска присутствует, то ищет в локаторе пометку
     * о том что это окно, затем диалог. Если окно и диалог не обнаружены, то
     * опрашивает все зарегистрированные в приложении билдеры. Принадлежит ли им
     * локатор с таким идентификатором? Если некоторый билдер распознает локатор, то
     * спрашиваем у билдера элемент, соответствующий этому локатору. Билдер знает
     * как закодировать и раскодировать элемент и локатор. Т.е. за отношение Элемент
     * <==> Локатор отвечает билдер.
     * <p>
     * Если в билдере специально не реализован такой механизм, то используется
     * базовый механизм: Элемент определяется при помощи xPath относительно
     * корневого элемента выбранного билдера. Этот корневой элемент в DOM-модели
     * имеет атрибут whirlId, по которому и происходит идентификация билдеров.
     *
     * @param strLocator
     * @return
     */
    @ExportStaticMethod
    public static Element getElementByLocator(String strLocator) {

        if (Util.isEmptyString(strLocator) || !strLocator.startsWith("whirl:")) {
            return null;
        }
        strLocator = strLocator.replaceFirst("whirl:", "");
        Locator locator = Locator.parse(strLocator);

        Element element = findEverywhere(locator);

        if (element == null) {
            for (LocatorAware l : DialogManager.getDialogLocators()) {
                element = l.getElementByLocator(locator);
                if (element != null) {
                    break;
                }
            }
        }

        if (element == null) {
            // форма логина
            element = LoginPanelBuilder.elementByLocator(locator);
        }
        return element;
    }

}
