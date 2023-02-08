package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.resources.client.ClientBundle;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public interface RedBorderSurfaceResources extends SurfaceResources,
    ClientBundle {

    @Source("RedBorderSurface.gss")
    RedBorderSurfaceStyle style();

    interface RedBorderSurfaceStyle extends SurfaceStyle {
        @Override
        String surface();
    }
}
