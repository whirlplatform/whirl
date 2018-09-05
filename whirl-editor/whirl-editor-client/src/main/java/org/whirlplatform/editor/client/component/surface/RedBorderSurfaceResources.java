package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.resources.client.ClientBundle;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public interface RedBorderSurfaceResources extends SurfaceResources,
        ClientBundle {

    interface RedBorderSurfaceStyle extends SurfaceStyle {
        @Override
        String surface();
    }

    @Source("RedBorderSurface.gss")
    RedBorderSurfaceStyle style();
}
