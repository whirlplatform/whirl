package org.whirlplatform.component.client.ext;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.widget.core.client.form.TextArea;

public class XTextArea extends TextArea {

    private int fieldWidth;
    private int fieldHeight;
    private boolean isfocused;

    public XTextArea(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
    }

    public boolean isFocused() {
        return isfocused;
    }

    @Override
    protected void onFocus(Event event) {
        isfocused = true;
        super.onFocus(event);
    }

    @Override
    protected void onBlur(Event event) {
        isfocused = false;
        super.onBlur(event);
    }

    @Override
    protected void onShow() {
        setWidth(fieldWidth);
        setHeight(fieldHeight);
        focus();
        super.onShow();
    }

    public void setSelectionText(String value) {
        if (GXT.isIE()) {
            setSelectionTextIE(getElement(), value);
        } else {
            setSelectionText(getElement(), value);
        }
    }

    private native void setSelectionText(Element elem, String strValue)/*-{
        if (elem.selectionStart || elem.selectionStart == '0') {
            var startPos = elem.selectionStart;
            var endPos = elem.selectionEnd;
            restoreTop = elem.scrollTop;
            elem.value = elem.value.substring(0, startPos) + strValue
                    + elem.value.substring(endPos, elem.value.length);
            elem.selectionStart = startPos + strValue.length;
            elem.selectionEnd = startPos + strValue.length;
            if (restoreTop > 0) {
                elem.scrollTop = restoreTop;
            }
        } else {
            elem.value += strValue;
        }
    }-*/;

    private native void setSelectionTextIE(Element elem, String strValue)/*-{
        if (elem.document.selection) {
            elem.focus();
            sel = elem.document.selection.createRange();
            sel.text = strValue;
            sel.select();
        } else {
            elem.value += strValue;
        }

    }-*/;
}
