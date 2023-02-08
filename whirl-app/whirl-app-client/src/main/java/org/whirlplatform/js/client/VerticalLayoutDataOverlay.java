package org.whirlplatform.js.client;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public abstract class VerticalLayoutDataOverlay {

    public static VerticalLayoutData constructor(double width, double height) {
        return new VerticalLayoutData(width, height);
    }

    public static VerticalLayoutData constructor(double width, double height,
                                                 int margins) {
        return constructor(width, height, margins, margins, margins, margins);
    }

    public static VerticalLayoutData constructor(double width, double height,
                                                 int marginTop, int marginRight, int marginBottom,
                                                 int marginLeft) {
        return new VerticalLayoutData(width, height, new Margins(marginTop,
            marginRight, marginBottom, marginLeft));
    }

}
