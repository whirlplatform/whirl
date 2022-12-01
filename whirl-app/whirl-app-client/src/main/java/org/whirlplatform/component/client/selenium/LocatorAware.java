package org.whirlplatform.component.client.selenium;

import com.google.gwt.dom.client.Element;

/**
 * Интерфейс характеризует для компонента наличие локатора и методов, необходимых для реализации его
 * работы.
 */
public interface LocatorAware {

    Locator getLocatorByElement(Element element);

    void fillLocatorDefaults(Locator locator, Element element);

    Element getElementByLocator(Locator locator);
}
