package org.whirlplatform.editor.client.tree.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.data.shared.TreeStore;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.dummy.AbstractDummyElement;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 *
 */
public class AppTreeMainCell extends AbstractCell<String> {
    final AppTree tree;
    final TreeStore<AbstractElement> store;

    public AppTreeMainCell(final AppTree tree) {
        this.tree = tree;
        this.store = tree.getStore();
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        AbstractElement element = store.findModelWithKey(context.getKey().toString());
        if (!tree.isReference(element) && !(element instanceof AbstractDummyElement)) {
            sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
        } else {
            sb.appendEscaped(value);
        }
    }
}