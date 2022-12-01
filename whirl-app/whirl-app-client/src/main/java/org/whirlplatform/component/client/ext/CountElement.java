package org.whirlplatform.component.client.ext;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;

public class CountElement {

    private Element element;
    private DivElement countEl;

    public CountElement(Element el) {
        element = el;

        countEl = Document.get().createDivElement();
        countEl.getStyle().setPosition(Position.ABSOLUTE);
        countEl.getStyle().setDisplay(Display.INLINE);
        countEl.getStyle().setRight(20, Unit.PX);
        countEl.getStyle().setTextAlign(TextAlign.RIGHT);
        countEl.getStyle().setTop(3, Unit.PX);
        countEl.getStyle().setFontSize(8, Unit.PT);
        countEl.getStyle().setBorderWidth(1, Unit.PX);
        countEl.getStyle().setBorderColor("#CAD8F3");
        countEl.getStyle().setBorderStyle(BorderStyle.SOLID);
        countEl.getStyle().setBackgroundColor("#DEE7F8");

        element.getParentElement().appendChild(countEl);
    }

    public void setCount(int count) {
        countEl.setInnerText(String.valueOf(count));
    }

}
