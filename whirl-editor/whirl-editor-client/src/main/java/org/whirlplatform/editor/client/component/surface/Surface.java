package org.whirlplatform.editor.client.component.surface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.widget.core.client.Component;

public class Surface extends Component {

    public interface SurfaceAppearance {
        void render(SafeHtmlBuilder sb);
    }

    public interface SurfaceStyle extends CssResource {
        String surface();
    }

    public interface SurfaceResources {
        SurfaceStyle style();
    }

    public interface DefaultTemplate extends XTemplates {
        @XTemplate(source = "Surface.html")
        SafeHtml render(SurfaceStyle style);
    }

    private static Surface instance;

    public static Surface get() {
        if (instance == null) {
            instance = new Surface(new DefaultSurfaceAppearance());
            instance.getElement().getStyle().setZIndex(99999);
        }
        return instance;
    }

    public Surface(SurfaceAppearance appearance) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        appearance.render(sb);

        setElement(XDOM.create(sb.toSafeHtml()));

        setShadow(false);
        hide();
    }

    public void show(Element c) {
        show(c, false);
    }

    public void show(Element c, boolean over) {
        c.insertBefore(getElement(), null);
        XElement el = c.cast();
        getElement().setZIndex(el.getZIndex() + 1);
        show();
        if (over) {
            Rectangle rect = el.getBounds();
            getElement().getStyle().setPosition(Position.ABSOLUTE);
            getElement().setBounds(rect.getX(), rect.getY(), rect.getWidth(),
                    rect.getHeight(), true);
        }
    }

    @Override
    protected void onHide() {
        super.onHide();
        getElement().removeFromParent();
    }

    @Override
    protected void onShow() {
        super.onShow();
        if (!getElement().isConnected()) {
            Document.get().getBody().insertBefore(getElement(), null);
        }
    }

}
