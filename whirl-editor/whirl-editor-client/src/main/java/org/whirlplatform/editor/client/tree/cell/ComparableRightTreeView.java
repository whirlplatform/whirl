package org.whirlplatform.editor.client.tree.cell;

import org.whirlplatform.editor.client.presenter.compare.ElementChangeState;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public class ComparableRightTreeView extends ComparableTreeView {

	@Override
	public boolean isCheckable(AbstractElement element, boolean defaultValue) {
		if (tree instanceof ComparableAppTree) {
			ElementChangeState state = ((ComparableAppTree) tree).getChangeState(element);
			return (state == ElementChangeState.ADDED || state == ElementChangeState.CHANGED);
		}
		return defaultValue;
	}
}
