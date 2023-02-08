package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.resources.client.ClientBundle;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public interface BlueDottedSurfaceResources extends SurfaceResources,
    ClientBundle {

    @Source("BlueDottedSurface.gss")
    BlueDottedSurfaceStyle style();

    interface BlueDottedSurfaceStyle extends SurfaceStyle {
        @Override
        String surface();
    }
}
