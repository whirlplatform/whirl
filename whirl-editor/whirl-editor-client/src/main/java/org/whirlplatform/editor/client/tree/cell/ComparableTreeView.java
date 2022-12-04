package org.whirlplatform.editor.client.tree.cell;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.Tree.Joint;
import com.sencha.gxt.widget.core.client.tree.TreeView;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public abstract class ComparableTreeView extends TreeView<AbstractElement> {
    @Override
    public SafeHtml getTemplate(AbstractElement m, String id, SafeHtml text, ImageResource icon,
                                boolean checkable,
                                CheckState checked, Joint joint, int level,
                                TreeViewRenderMode renderMode) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        tree.getAppearance()
                .renderNode(sb, id, text, tree.getStyle(), icon, isCheckable(m, checkable), checked,
                        joint,
                        level, renderMode);
        return sb.toSafeHtml();
    }

    public abstract boolean isCheckable(AbstractElement element, boolean defaultValue);
}
