package org.whirlplatform.editor.client.tree;

import org.whirlplatform.editor.client.presenter.compare.ElementChangeState;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.editor.AbstractElement;

import java.util.List;

public interface ComparableAppTree extends AppTree {
	void setChanges(List<ChangeUnit> changes);

	void clearChanges();

	ElementChangeState getChangeState(AbstractElement element);

	List<AbstractElement> getCheckedSelection();

	boolean isChecked(AbstractElement element);
	
	List<ChangeUnit> getChangeUnits(AbstractElement element);
	
	List<ChangeUnit> getCheckedChangeUnits();
}
