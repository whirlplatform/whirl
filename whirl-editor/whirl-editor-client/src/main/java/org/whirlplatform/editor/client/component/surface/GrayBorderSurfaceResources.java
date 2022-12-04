package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.resources.client.ClientBundle;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public interface GrayBorderSurfaceResources extends SurfaceResources,
        ClientBundle {

    @Source("GrayBorderSurface.gss")
    GrayBorderSurfaceStyle style();

    interface GrayBorderSurfaceStyle extends SurfaceStyle {
        @Override
        String surface();
    }
}
