package org.whirlplatform.editor.client.dnd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.Component;

public class HorizontalInsert extends Component {

    private static HorizontalInsert instance;
    private HorizontalInsertAppearance appearance;

    HorizontalInsert() {
        this.appearance = GWT.create(DefaultHorizontalInsertAppearance.class);

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        appearance.render(sb);

        setElement(XDOM.create(sb.toSafeHtml()));

        setShadow(false);
        hide();
    }

    public static HorizontalInsert get() {
        if (instance == null) {
            instance = new HorizontalInsert();
        }
        return instance;
    }

    public void show(Element c) {
        c.insertBefore(getElement(), null);
        show();
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

    public interface HorizontalInsertAppearance {
        void render(SafeHtmlBuilder sb);
    }

    public static class DefaultHorizontalInsertAppearance implements
        HorizontalInsertAppearance {

        private HorizontalInsertStyle style;
        private Template template;

        public DefaultHorizontalInsertAppearance() {
            this(GWT
                .create(HorizontalInsertResources.class));
        }

        public DefaultHorizontalInsertAppearance(
            HorizontalInsertResources resources) {
            this.style = resources.style();
            this.style.ensureInjected();

            this.template = GWT.create(Template.class);
        }

        @Override
        public void render(SafeHtmlBuilder sb) {
            sb.append(template.render(style));
        }

        public interface HorizontalInsertResources extends ClientBundle {

            ImageResource top();

            @ImageOptions(repeatStyle = RepeatStyle.Vertical)
            ImageResource center();

            ImageResource bottom();

            @Source("HorizontalInsert.gss")
            HorizontalInsertStyle style();

        }

        public interface HorizontalInsertStyle extends CssResource {

            String bar();

            String top();

            String center();

            String bottom();

        }

        public interface Template extends XTemplates {
            @XTemplate(source = "HorizontalInsert.html")
            SafeHtml render(HorizontalInsertStyle style);
        }

    }

}
