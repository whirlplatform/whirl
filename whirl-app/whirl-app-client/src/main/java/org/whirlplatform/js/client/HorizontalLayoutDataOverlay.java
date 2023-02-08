package org.whirlplatform.js.client;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

/**
 * Настроечный компонент для определения и работы горизонтальных лайаутов
 */
public abstract class HorizontalLayoutDataOverlay {

    public static HorizontalLayoutData constructor(double width, double height) {
        return new HorizontalLayoutData(width, height);
    }

    public static HorizontalLayoutData constructor(double width, double height,
                                                   int margins) {
        return constructor(width, height, margins, margins, margins, margins);
    }

    public static HorizontalLayoutData constructor(double width, double height,
                                                   int marginTop, int marginRight, int marginBottom,
                                                   int marginLeft) {
        return new HorizontalLayoutData(width, height, new Margins(marginTop,
            marginRight, marginBottom, marginLeft));
    }
}
