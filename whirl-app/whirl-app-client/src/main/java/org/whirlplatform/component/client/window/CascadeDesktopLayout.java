/**
 * Sencha GXT 3.1.1 - Sencha for GWT Copyright(c) 2007-2014, Sencha, Inc. licensing@sencha.com
 * <p>
 * http://www.sencha.com/products/gxt/license/
 */
package org.whirlplatform.component.client.window;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Window;

public class CascadeDesktopLayout extends LimitedDesktopLayout implements DesktopLayout {

    protected int left;
    protected int top;
    protected int offsetIndex;

    @Override
    public DesktopLayoutType getDesktopLayoutType() {
        return DesktopLayoutType.CASCADE;
    }

    @Override
    public void layoutDesktop(Window requestWindow, RequestType requestType, Element element,
                              Iterable<Window> windows,
                              int containerWidth, int containerHeight) {

        if (requestType == RequestType.LAYOUT) {
            left = 0;
            top = 0;
            offsetIndex = 0;
        }

        super.layoutDesktop(requestWindow, requestType, element, windows, containerWidth,
                containerHeight);
    }

    @Override
    protected void layoutWindow(Window window, int containerWidth, int containerHeight, int width,
                                int height) {

        int offset = window.getHeader().getOffsetHeight();

        window.setPixelSize(width, height);

        if (top + height > containerHeight) {
            left = ++offsetIndex * offset;
            top = 0;
        }

        if (left + width > containerWidth) {
            left = 0;
            top = 0;
            offsetIndex = 0;
        }

        window.setPosition(left, top);

        left += offset;
        top += offset;
    }

}
