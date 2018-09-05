package org.whirlplatform.js.client;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Настроечный компонент для определения и работы горизонтальных лайаутов
 */
@Export("HorizontalLayoutData")
@ExportPackage("Whirl")
public abstract class HorizontalLayoutDataOverlay implements
        ExportOverlay<HorizontalLayoutData> {

    @ExportConstructor
    public static HorizontalLayoutData constructor(double width, double height) {
        return new HorizontalLayoutData(width, height);
    }

    @ExportConstructor
    public static HorizontalLayoutData constructor(double width, double height,
                                                   int margins) {
        return constructor(width, height, margins, margins, margins, margins);
    }

    @ExportConstructor
    public static HorizontalLayoutData constructor(double width, double height,
                                                   int marginTop, int marginRight, int marginBottom, int marginLeft) {
        return new HorizontalLayoutData(width, height, new Margins(marginTop,
                marginRight, marginBottom, marginLeft));
    }
}
