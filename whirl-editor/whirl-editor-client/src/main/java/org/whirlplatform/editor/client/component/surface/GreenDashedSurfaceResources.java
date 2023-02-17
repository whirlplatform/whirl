package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.resources.client.ClientBundle;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public interface GreenDashedSurfaceResources extends SurfaceResources,
    ClientBundle {

    @Source("GreenDashedSurface.gss")
    GrayBorderSurfaceStyle style();

    interface GrayBorderSurfaceStyle extends SurfaceStyle {
        @Override
        String surface();
    }
}
