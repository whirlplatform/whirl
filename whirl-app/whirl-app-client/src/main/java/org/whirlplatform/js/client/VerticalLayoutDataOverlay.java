package org.whirlplatform.js.client;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

@Export("VerticalLayoutData")
@ExportPackage("Whirl")
public abstract class VerticalLayoutDataOverlay implements
        ExportOverlay<VerticalLayoutData> {

    @ExportConstructor
    public static VerticalLayoutData constructor(double width, double height) {
        return new VerticalLayoutData(width, height);
    }

    @ExportConstructor
    public static VerticalLayoutData constructor(double width, double height,
                                                 int margins) {
        return constructor(width, height, margins, margins, margins, margins);
    }

    @ExportConstructor
    public static VerticalLayoutData constructor(double width, double height,
                                                 int marginTop, int marginRight, int marginBottom, int marginLeft) {
        return new VerticalLayoutData(width, height, new Margins(marginTop,
                marginRight, marginBottom, marginLeft));
    }

}
