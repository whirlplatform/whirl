package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.whirlplatform.editor.client.component.surface.Surface.DefaultTemplate;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceAppearance;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceResources;
import org.whirlplatform.editor.client.component.surface.Surface.SurfaceStyle;

public class DefaultSurfaceAppearance implements SurfaceAppearance {

    private SurfaceResources resource;
    private DefaultTemplate template;

    public DefaultSurfaceAppearance() {
        this(
            GWT.<DefaultSurfaceResources>create(DefaultSurfaceResources.class));
    }

    public DefaultSurfaceAppearance(SurfaceResources resources) {
        this.resource = resources;
        this.resource.style().ensureInjected();

        this.template = GWT.create(DefaultTemplate.class);
    }

    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.append(template.render(resource.style()));
    }

    public interface DefaultSurfaceResources extends SurfaceResources,
        ClientBundle {
        @Source("Surface.gss")
        SurfaceStyle style();
    }

}
