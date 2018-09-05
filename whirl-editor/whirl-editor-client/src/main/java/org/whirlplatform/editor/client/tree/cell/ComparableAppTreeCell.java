package org.whirlplatform.editor.client.tree.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.data.shared.TreeStore;
import org.whirlplatform.editor.client.presenter.compare.ElementChangeState;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.client.tree.dummy.AbstractDummyElement;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Cell for highlighting differences
 * 
 * @author bedritckiy_mr
 *
 */
public class ComparableAppTreeCell extends AbstractCell<String> {
	final ComparableAppTree tree;
	final TreeStore<AbstractElement> store;

	public ComparableAppTreeCell(final ComparableAppTree tree) {
		this.tree = tree;
		this.store = tree.getStore();
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		AbstractElement element = store.findModelWithKey(context.getKey().toString());
		ElementChangeState changeState = tree.getChangeState(element);
			switch (changeState) {
			case CHANGED:
				sb.appendHtmlConstant("<span style='color:blue'>");
				sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				sb.appendHtmlConstant("</span>");
				break;
			case ADDED:
				sb.appendHtmlConstant("<span style='color:green;text-decoration:underline'>");
				sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				sb.appendHtmlConstant("</span>");
				break;
			case REMOVED:
				sb.appendHtmlConstant("<span style='color:red;text-decoration:line-through'>");
				sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				sb.appendHtmlConstant("</span>");
				break;
			case INHERITED_ADDED:
				sb.appendHtmlConstant("<span style='color:green'>");
				sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				sb.appendHtmlConstant("</span>");
				break;
			case INHERITED_REMOVED:
				sb.appendHtmlConstant("<span style='color:red'>");
				sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				sb.appendHtmlConstant("</span>");
				break;
			case NONE:
				if (!tree.isReference(element) && !(element instanceof AbstractDummyElement)) {
					sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
				} else {
					sb.appendEscaped(value);
				}
				break;
			default:
			}
	}
}
