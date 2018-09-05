/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 * <p>
 * http://www.sencha.com/products/gxt/license/
 */
package org.whirlplatform.component.client.window;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.Window;

public class TileDesktopLayout implements DesktopLayout {

    private static final int MARGIN_BOTTOM = 10;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 10;
    private static final int MARGIN_TOP = 10;
    private static final int SPACING = 10;

    @Override
    public DesktopLayoutType getDesktopLayoutType() {
        return DesktopLayoutType.TILE;
    }

    @Override
    public void layoutDesktop(Window requestWindow, RequestType requestType,
                              Element element, Iterable<Window> windows, int width, int height) {

        int layoutCount = 0;

        for (Window window : windows) {
            if (window.isVisible()) {
                layoutCount++;
            }
        }

        int availableWidth = width - (MARGIN_LEFT + MARGIN_RIGHT);
        int availableHeight = height - (MARGIN_TOP + MARGIN_BOTTOM);

        int rowCount = 1;
        int columnCount = 1;
        int bestFit = Integer.MAX_VALUE;

        for (int testRowCount = 1; testRowCount <= layoutCount; testRowCount++) {
            int testColumnCount = (layoutCount + (testRowCount - 1))
                    / testRowCount;
            int tileWidth = availableWidth / testColumnCount;
            int tileHeight = availableHeight / testRowCount;
            int delta = tileWidth - tileHeight;
            if (delta < 0) {
                delta = -delta;
            }
            if (delta < bestFit) {
                bestFit = delta;
                rowCount = testRowCount;
                columnCount = testColumnCount;
            }
        }

        int horizontalSpacing = (columnCount - 1) * SPACING;
        int verticalSpacing = (rowCount - 1) * SPACING;

        int tileWidth = (availableWidth - horizontalSpacing) / columnCount;
        int tileHeight = (availableHeight - verticalSpacing) / rowCount;

        int layoutIndex = 0;

        for (Window window : windows) {
            if (window.isVisible()) {
                int row = layoutIndex / columnCount;
                int column = layoutIndex % columnCount;
                int top = MARGIN_TOP + (row * tileHeight + row * SPACING);
                int left = MARGIN_LEFT
                        + (column * tileWidth + column * SPACING);
                window.setPixelSize(tileWidth, tileHeight); // must set prior to
                // alignTo
                window.alignTo(element, new AnchorAlignment(Anchor.TOP_LEFT,
                        Anchor.TOP_LEFT), left, top);
                layoutIndex++;
            }
        }
    }

}
