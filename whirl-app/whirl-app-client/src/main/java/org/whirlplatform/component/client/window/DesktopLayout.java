/**
 * Sencha GXT 3.1.1 - Sencha for GWT Copyright(c) 2007-2014, Sencha, Inc. licensing@sencha.com
 * <p>
 * http://www.sencha.com/products/gxt/license/
 */

package org.whirlplatform.component.client.window;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Window;

public interface DesktopLayout {

    int PREFERRED_WIDTH = 400;
    int PREFERRED_HEIGHT = 400;
    int PREFERRED_MAX_WIDTH_PCT = 75;
    int PREFERRED_MAX_HEIGHT_PCT = 75;

    DesktopLayoutType getDesktopLayoutType();

    /**
     * Requests a layout of the desktop as indicated by the specified values.
     *
     * @param requestWindow the window that was responsible for the request, or
     *                      null if the request is not window specific
     * @param requestType   the type of layout request
     * @param element       the desktop element to be used for positioning
     * @param windows       a list of all windows on the desktop
     * @param width         the desktop width
     * @param height        the desktop height
     */
    void layoutDesktop(Window requestWindow, RequestType requestType, Element element,
                       Iterable<Window> windows,
                       int width, int height);

    enum RequestType {
        OPEN, HIDE, SHOW, LAYOUT
    }

}
