package org.whirlplatform.editor.client.tree;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.data.shared.IconProvider;
import org.whirlplatform.editor.client.tree.visitor.AppTreeElementIconSetter;
import org.whirlplatform.editor.client.tree.visitor.VisitableTreeElement;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Provides the icon images for the application tree widget
 */
public class AppTreeIconProvider implements IconProvider<AbstractElement> {
    private AppTreeElementIconVisitContext ctx;
    private AppTreeElementIconSetter visitor;

    public AppTreeIconProvider() {
        ctx = new AppTreeElementIconVisitContext();
        visitor = new AppTreeElementIconSetter();
    }

    @Override
    public ImageResource getIcon(AbstractElement model) {
        if (model instanceof VisitableTreeElement) {
            ((VisitableTreeElement) model).accept(ctx, visitor);
        } else {
            model.accept(ctx, visitor);
        }
        return ctx.getIcon();
    }

    public static class AppTreeElementIconVisitContext implements VisitContext {
        private ImageResource icon;

        public AppTreeElementIconVisitContext() {
        }

        public ImageResource getIcon() {
            return icon;
        }

        public void setIcon(ImageResource icon) {
            this.icon = icon;
        }
    }
}
